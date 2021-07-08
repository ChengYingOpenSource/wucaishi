package com.cy.onepush.common.utils;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class NamedParameterUtils {

    /**
     * Set of characters that qualify as comment or quotes starting characters.
     */
    private static final String[] START_SKIP = new String[]{"'", "\"", "--", "/*"};

    /**
     * Set of characters that at are the corresponding comment or quotes ending characters.
     */
    private static final String[] STOP_SKIP = new String[]{"'", "\"", "\n", "*/"};

    /**
     * Set of characters that qualify as parameter separators,
     * indicating that a parameter name in an SQL String has ended.
     */
    private static final String PARAMETER_SEPARATORS = "\"':&,;()|=+-*%/\\<>^.";

    /**
     * An index with separator flags per character code.
     * Technically only needed between 34 and 124 at this point.
     */
    private static final boolean[] separatorIndex = new boolean[128];

    static {
        for (char c : PARAMETER_SEPARATORS.toCharArray()) {
            separatorIndex[c] = true;
        }
    }

    public static String replace(String template, Map<String, Object> params) {
        final List<ParameterHolder> parameterHolders = parseTemplate(template);
        return substituteNamedParameters(template, parameterHolders, params);
    }

    private static List<ParameterHolder> parseTemplate(String template) {
        Set<String> namedParameters = new HashSet<>();
        StringBuilder templateToUse = new StringBuilder(template);

        char[] statement = template.toCharArray();
        int namedParameterCount = 0;
        int unnamedParameterCount = 0;
        int totalParameterCount = 0;
        List<ParameterHolder> parameterList = new ArrayList<>();

        int escapes = 0;
        int i = 0;
        while (i < statement.length) {
            int skipToPosition = i;
            while (i < statement.length) {
                skipToPosition = skipCommentsAndQuotes(statement, i);
                if (i == skipToPosition) {
                    break;
                } else {
                    i = skipToPosition;
                }
            }
            if (i >= statement.length) {
                break;
            }
            char c = statement[i];
            if (c == ':' || c == '&') {
                int j = i + 1;
                if (c == ':' && j < statement.length && statement[j] == ':') {
                    // Postgres-style "::" casting operator should be skipped
                    i = i + 2;
                    continue;
                }
                String parameter = null;
                if (c == ':' && j < statement.length && statement[j] == '{') {
                    // :{x} style parameter
                    while (statement[j] != '}') {
                        j++;
                        if (j >= statement.length) {
                            throw new InvalidDataAccessApiUsageException("Non-terminated named parameter declaration " +
                                "at position " + i + " in statement: " + template);
                        }
                        if (statement[j] == ':' || statement[j] == '{') {
                            throw new InvalidDataAccessApiUsageException("Parameter name contains invalid character '" +
                                statement[j] + "' at position " + i + " in statement: " + template);
                        }
                    }
                    if (j - i > 2) {
                        parameter = template.substring(i + 2, j);
                        namedParameterCount = addNewNamedParameter(namedParameters, namedParameterCount, parameter);
                        totalParameterCount = addNamedParameter(
                            parameterList, totalParameterCount, escapes, i, j + 1, parameter);
                    }
                    j++;
                } else {
                    while (j < statement.length && !isParameterSeparator(statement[j])) {
                        j++;
                    }
                    if (j - i > 1) {
                        parameter = template.substring(i + 1, j);
                        namedParameterCount = addNewNamedParameter(namedParameters, namedParameterCount, parameter);
                        totalParameterCount = addNamedParameter(
                            parameterList, totalParameterCount, escapes, i, j, parameter);
                    }
                }
                i = j - 1;
            } else {
                if (c == '\\') {
                    int j = i + 1;
                    if (j < statement.length && statement[j] == ':') {
                        // escaped ":" should be skipped
                        templateToUse.deleteCharAt(i - escapes);
                        escapes++;
                        i = i + 2;
                        continue;
                    }
                }
                if (c == '?') {
                    int j = i + 1;
                    if (j < statement.length && (statement[j] == '?' || statement[j] == '|' || statement[j] == '&')) {
                        // Postgres-style "??", "?|", "?&" operator should be skipped
                        i = i + 2;
                        continue;
                    }
                    unnamedParameterCount++;
                    totalParameterCount++;
                }
            }
            i++;
        }

        return parameterList;
    }

    private static String substituteNamedParameters(String template, @Nullable List<ParameterHolder> parameterHolders, Map<String, Object> paramSource) {
        if (CollectionUtils.isEmpty(parameterHolders)) {
            return template;
        }
        StringBuilder actualStr = new StringBuilder(template.length());
        int lastIndex = 0;
        for (int i = 0; i < parameterHolders.size(); i++) {
            ParameterHolder parameterHolder = parameterHolders.get(i);
            int startIndex = parameterHolder.getStartIndex();
            int endIndex = parameterHolder.getEndIndex();
            actualStr.append(template, lastIndex, startIndex);
            if (paramSource != null && paramSource.containsKey(parameterHolder.getParameterName())) {
                Object value = paramSource.get(parameterHolder.getParameterName());
                if (value instanceof Iterable) {
                    Iterator<?> entryIter = ((Iterable<?>) value).iterator();
                    int k = 0;
                    while (entryIter.hasNext()) {
                        if (k > 0) {
                            actualStr.append(", ");
                        }
                        k++;
                        Object entryItem = entryIter.next();
                        if (entryItem instanceof Object[]) {
                            Object[] expressionList = (Object[]) entryItem;
                            actualStr.append('(');
                            for (int m = 0; m < expressionList.length; m++) {
                                if (m > 0) {
                                    actualStr.append(", ");
                                }

                                final Object item = expressionList[m];
                                actualStr.append(ObjectUtils.defaultIfNull(item, ""));
                            }
                            actualStr.append(')');
                        } else {
                            actualStr.append(ObjectUtils.defaultIfNull(entryItem, ""));
                        }
                    }
                } else {
                    actualStr.append(ObjectUtils.defaultIfNull(value, ""));
                }
            } else {
                actualStr.append(' ');
            }
            lastIndex = endIndex;
        }
        actualStr.append(template, lastIndex, template.length());
        return actualStr.toString();
    }

    /**
     * Skip over comments and quoted names present in an SQL statement.
     *
     * @param statement character array containing SQL statement
     * @param position  current position of statement
     * @return next position to process after any comments or quotes are skipped
     */
    private static int skipCommentsAndQuotes(char[] statement, int position) {
        for (int i = 0; i < START_SKIP.length; i++) {
            if (statement[position] == START_SKIP[i].charAt(0)) {
                boolean match = true;
                for (int j = 1; j < START_SKIP[i].length(); j++) {
                    if (statement[position + j] != START_SKIP[i].charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    int offset = START_SKIP[i].length();
                    for (int m = position + offset; m < statement.length; m++) {
                        if (statement[m] == STOP_SKIP[i].charAt(0)) {
                            boolean endMatch = true;
                            int endPos = m;
                            for (int n = 1; n < STOP_SKIP[i].length(); n++) {
                                if (m + n >= statement.length) {
                                    // last comment not closed properly
                                    return statement.length;
                                }
                                if (statement[m + n] != STOP_SKIP[i].charAt(n)) {
                                    endMatch = false;
                                    break;
                                }
                                endPos = m + n;
                            }
                            if (endMatch) {
                                // found character sequence ending comment or quote
                                return endPos + 1;
                            }
                        }
                    }
                    // character sequence ending comment or quote not found
                    return statement.length;
                }
            }
        }
        return position;
    }

    /**
     * Determine whether a parameter name ends at the current position,
     * that is, whether the given character qualifies as a separator.
     */
    private static boolean isParameterSeparator(char c) {
        return (c < 128 && separatorIndex[c]) || Character.isWhitespace(c);
    }

    private static int addNewNamedParameter(Set<String> namedParameters, int namedParameterCount, String parameter) {
        if (!namedParameters.contains(parameter)) {
            namedParameters.add(parameter);
            namedParameterCount++;
        }
        return namedParameterCount;
    }

    private static int addNamedParameter(
        List<ParameterHolder> parameterList, int totalParameterCount, int escapes, int i, int j, String parameter) {

        parameterList.add(new ParameterHolder(parameter, i - escapes, j - escapes));
        totalParameterCount++;
        return totalParameterCount;
    }

    private static class Template {
    }

    private static class ParameterHolder {

        private final String parameterName;

        private final int startIndex;

        private final int endIndex;

        public ParameterHolder(String parameterName, int startIndex, int endIndex) {
            this.parameterName = parameterName;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        public String getParameterName() {
            return this.parameterName;
        }

        public int getStartIndex() {
            return this.startIndex;
        }

        public int getEndIndex() {
            return this.endIndex;
        }
    }

}
