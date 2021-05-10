package com.cy.onepush.common.publishlanguage.version;

import com.cy.onepush.common.exception.IllegalParamsException;
import com.cy.onepush.common.framework.publishedlanguage.AggregateId;
import org.apache.commons.lang3.StringUtils;

public class Version extends AggregateId<String> implements Comparable<Version> {

    private static final String VERSION_SPLIT = ".";
    private static final Version INIT_VERSION = new Version(0, 0, 1);

    public static Version initVersion() {
        return INIT_VERSION;
    }

    public static Version of(int x, int y, int z) {
        return new Version(x, y, z);
    }

    public static Version of(String rawVersion) {
        final String[] split = StringUtils.split(rawVersion, VERSION_SPLIT);
        if (split == null || split.length != 3) {
            throw new IllegalParamsException("failed parse version due to bad version %s", rawVersion);
        }

        try {
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);
            int z = Integer.parseInt(split[2]);
            return Version.of(x, y, z);
        } catch (NumberFormatException e) {
            throw new IllegalParamsException("failed parse version due to bad version %s", rawVersion);
        }
    }

    private final int x;
    private final int y;
    private final int z;

    private Version(int x, int y, int z) {
        super(String.format("%s.%s.%s", x, y, z));

        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Version version = (Version) o;

        if (x != version.x) return false;
        if (y != version.y) return false;
        return z == version.z;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }

    @Override
    public int compareTo(Version o) {
        boolean less = this.x < o.x || this.y < o.y || this.z < o.z;
        if (less) {
            return -1;
        }
        boolean equal = this.equals(o);
        if (equal) {
            return 0;
        }

        return 1;
    }
}
