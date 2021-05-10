package com.cy.onepush.datapackager.domain;

import com.cy.onepush.common.exception.IllegalParamsException;
import com.cy.onepush.common.framework.domain.AbstractAggregateRoot;
import com.cy.onepush.common.publishlanguage.datapackager.DataPackagerId;
import com.cy.onepush.common.publishlanguage.datastructure.DataStructureId;
import com.cy.onepush.common.publishlanguage.executioncontext.ExecutionContextId;
import com.cy.onepush.common.publishlanguage.version.Version;
import com.cy.onepush.common.utils.ByteUnit;
import com.cy.onepush.datastructure.domain.DataStructure;
import com.cy.onepush.datastructure.domain.DataStructureFactory;
import com.cy.onepush.datastructure.domain.DataType;
import com.cy.onepush.dataview.domain.DataView;
import com.cy.onepush.debugtool.domain.DebugTool;
import com.cy.onepush.execution.context.domain.ExecutionContext;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;

import java.util.*;

@Slf4j
@Getter
public class DataPackager extends AbstractAggregateRoot<String> {

    /**
     * 名称
     */
    private final String name;
    /**
     * 绑定视图映射
     */
    private final Map<String, DataView> dataViews;
    /**
     * 执行脚本
     */
    private final ExecutionScript executionScript;
    /**
     * script execution engine
     */
    private ScriptExecutionEngine scriptExecutionEngine;
    /**
     * script execution limiter
     */
    private ScriptExecutionLimiter scriptExecutionLimiter;
    /**
     * 请求数据结构
     */
    private final DataStructure requestDataStructure;
    /**
     * 响应数据结构
     */
    private final DataStructure responseDataStructure;
    /**
     * 版本
     */
    private final Version version;

    public DataPackager(DataPackagerId id, String name,
                        String scriptType, String scriptContent) {
        this(id, name, scriptType, scriptContent, Version.initVersion());
    }

    public DataPackager(DataPackagerId id, String name,
                        String scriptType, String scriptContent,
                        Version version) {
        super(id);
        this.name = name;
        this.dataViews = new HashMap<>(8);
        this.executionScript = ExecutionScript.of(scriptType, scriptContent);

        final String requestTargetCode = String.format("PRQ_%s", id.getId());
        this.requestDataStructure = new DataStructure(DataStructureId.of(requestTargetCode));
        this.requestDataStructure.setDataType(DataType.ITEM);
        this.requestDataStructure.setField("params");
        this.requestDataStructure.setName("params");

        final String responseTargetCode = String.format("PRP_%s", id.getId());
        this.responseDataStructure = new DataStructure(DataStructureId.of(responseTargetCode));
        this.responseDataStructure.setDataType(DataType.ANY);
        this.responseDataStructure.setField("data");
        this.responseDataStructure.setName("data");

        this.version = version;
    }

    @Override
    public DataPackagerId getId() {
        return DataPackagerId.of(super.getId().getId());
    }

    public DataPackagerIdWithVersion getIdWithVersion() {
        return DataPackagerIdWithVersion.of(getId(), getVersion());
    }

    public Version getDataPackagerVersion() {
        return version;
    }

    /**
     * debug
     */
    public void debug(DebugTool debugTool, Map<String, Object> executionParams) {
        // create the debug context id
        final ExecutionContextId debugContextId = ExecutionContextId.of(UUID.randomUUID().toString());

        // create debug context
        final ExecutionContext debugContext = new ExecutionContext(debugContextId, true, debugTool);
        debugContext.enterContext();
        debugContext.setParams(MapUtils.emptyIfNull(executionParams));

        try {
            // execute the data packager
            this.execute(debugContext);
        } finally {
            debugContext.exitContext();
        }
    }

    @SuppressWarnings("unchecked")
    public void execute(ExecutionContext context) {
        final DebugTool debugTool = context.getDebugTool();

        final Map<String, Object> params = context.getParams();

        // enter data node
        context.enterDataNode(params);

        try {

            // request params validation
            final Object requestParams;
            try {
                requestParams = requestDataStructure.resolve(params);
            } catch (IllegalArgumentException e) {
                // failed
                log.warn("failed to parse request params {}", params);
                throw new IllegalParamsException("the data packager execution params {} illegal", e, params);
            }
            if (context.isDebug()) {
                debugTool.printLog("request data packager with params %s", requestParams);
            }

            // execute
            final Object result;
            try {
                result = scriptExecutionEngine.execute(context, dataViews, executionScript,
                    (Map<String, Object>) ((Map<String, Object>) requestParams).get("params"),
                    createBySystem().toArray(new LimitRule[]{}));
            } catch (Throwable t) {
                if (context.isDebug()) {
                    debugTool.printLog("failed to execute data packager due to exception %s", t.getMessage());
                }
                return;
            }

            // wrap response
            final Object resolved;
            try {
                resolved = responseDataStructure.resolve(result);
            } catch (IllegalArgumentException e) {
                // failed
                if (context.isDebug()) {
                    debugTool.printError("failed to parse response result due to bad response structure %s", result);
                }
                return;
            }
            context.setResult(resolved);

            if (context.isDebug()) {
                debugTool.printLog("request data packager with result: \r\n ======== result ======= \r\n %s \r\n ======== result end ======= \r\n \r\n", resolved);
            }

        } finally {
            // leave the current node
            context.leaveNode();
        }
    }

    public boolean bindDataView(String alias, DataView dataView) {
        dataViews.put(alias, dataView);
        return true;
    }

    public void bindScriptExecutionEngine(ScriptExecutionEngine scriptExecutionEngine) {
        this.scriptExecutionEngine = scriptExecutionEngine;
    }

    public boolean bindRequestDataStructure(List<Map<String, Object>> raw) {
        final String requestTargetCode = requestDataStructure.getId().getId();
        final List<DataStructure> dataStructures = DataStructureFactory.createFromMap(requestTargetCode, raw)
            .orElse(Lists.newArrayListWithCapacity(0));

        this.requestDataStructure.setDataStructures(dataStructures);
        return true;
    }

    public boolean addRequestDataStructure(DataStructure raw) {
        this.requestDataStructure.getDataStructures().add(raw);
        return true;
    }

    public boolean addAllRequestDataStructure(Collection<DataStructure> collection) {
        this.requestDataStructure.getDataStructures().addAll(collection);
        return true;
    }

    public boolean bindResponseDataStructure(List<Map<String, Object>> raw) {
        final String responseTargetCode = responseDataStructure.getId().getId();
        final List<DataStructure> dataStructures = DataStructureFactory.createFromMap(responseTargetCode, raw)
            .orElse(Lists.newArrayListWithCapacity(0));

        this.responseDataStructure.setDataStructures(dataStructures);
        return true;
    }

    public boolean addResponseDataStructure(DataStructure raw) {
        this.responseDataStructure.getDataStructures().add(raw);
        return true;
    }

    public boolean addAllResponseDataStructure(Collection<DataStructure> collection) {
        this.responseDataStructure.getDataStructures().addAll(collection);
        return true;
    }

    public DataPackager createNewVersion(String newVersion) {
        final DataPackager newDataPackager = new DataPackager(getId(), name, getExecutionScript().getType(), getExecutionScript().getContent(), Version.of(newVersion));
        newDataPackager.dataViews.putAll(dataViews);
        newDataPackager.addAllResponseDataStructure(getRequestDataStructure().getDataStructures());
        newDataPackager.addAllResponseDataStructure(getResponseDataStructure().getDataStructures());
        newDataPackager.scriptExecutionEngine = scriptExecutionEngine;
        return newDataPackager;
    }

    private List<LimitRule> createBySystem() {
        return ImmutableList.of(
            // not exceed 1mb
            LimitRule.of("first", "currentMemoryUsage < 1048576")
        );
    }

}
