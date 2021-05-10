package com.cy.onepush.dataview.domain;

import com.cy.onepush.common.exception.AbstractAppRuntimeException;
import com.cy.onepush.common.exception.IllegalParamsException;
import com.cy.onepush.common.framework.domain.AbstractAggregateRoot;
import com.cy.onepush.common.publishlanguage.datastructure.DataStructureId;
import com.cy.onepush.common.publishlanguage.dataview.DataViewId;
import com.cy.onepush.common.publishlanguage.executioncontext.ExecutionContextId;
import com.cy.onepush.common.publishlanguage.version.Version;
import com.cy.onepush.datasource.domain.DataSource;
import com.cy.onepush.datastructure.domain.DataStructure;
import com.cy.onepush.datastructure.domain.DataStructureFactory;
import com.cy.onepush.datastructure.domain.DataType;
import com.cy.onepush.debugtool.domain.DebugTool;
import com.cy.onepush.execution.context.domain.ExecutionContext;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.util.*;

@Slf4j
@EqualsAndHashCode(callSuper = true)
public class DataView extends AbstractAggregateRoot<String> implements Cloneable {

    /**
     * data view name
     */
    private final String name;
    /**
     * the datasource bound with this data view
     */
    private DataSource dataSource;
    /**
     * data view params
     */
    private final Map<String, Object> params;
    /**
     * the request data structure
     */
    private final DataStructure requestDataStructure;
    /**
     * the response data structure
     */
    private final DataStructure responseDataStructure;
    /**
     * the version
     */
    private final Version version;

    public DataView(DataViewId id, String name) {
        this(id, name, Version.initVersion());
    }

    public DataView(DataViewId id, String name, Version version) {
        super(id);
        this.name = name;
        this.params = new HashMap<>(16);

        final String requestTargetCode = String.format("VRQ_%s", id.getId());
        this.requestDataStructure = new DataStructure(DataStructureId.of(requestTargetCode));
        this.requestDataStructure.setDataType(DataType.ITEM);
        this.requestDataStructure.setField("params");
        this.requestDataStructure.setName("params");

        final String responseTargetCode = String.format("VRP_%s", id.getId());
        this.responseDataStructure = new DataStructure(DataStructureId.of(responseTargetCode));
        this.responseDataStructure.setDataType(DataType.ANY);
        this.responseDataStructure.setField("data");
        this.responseDataStructure.setName("data");

        this.version = version;
    }

    @Override
    public DataViewId getId() {
        return DataViewId.of(super.getId().getId());
    }

    public DataViewIdWithVersion getIdWithVersion() {
        return DataViewIdWithVersion.of(getId(), getDataViewVersion());
    }

    public String getName() {
        return this.name;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public DataStructure getRequestDataStructure() {
        return requestDataStructure;
    }

    public DataStructure getResponseDataStructure() {
        return responseDataStructure;
    }

    public final void debug(DebugTool debugTool, Map<String, Object> runtimeParams) {
        // create the debug context id
        final ExecutionContextId debugContextId = ExecutionContextId.of(UUID.randomUUID().toString());

        // create debug context
        final ExecutionContext debugContext = new ExecutionContext(debugContextId, true, debugTool);
        debugContext.enterContext();
        debugContext.setParams(MapUtils.emptyIfNull(runtimeParams));

        try {
            // execute the data packager
            this.execute(debugContext);
        } finally {
            debugContext.exitContext();
        }
    }

    public final boolean init() {
        if (!_checkParams()) {
            log.error("the data view {} params checked failed", getId().getId());
            return false;
        }

        if (_init()) {
            log.error("the data view {} failed to init", getId().getId());
            return false;
        }

        return true;
    }

    protected boolean _checkParams() {
        return false;
    }

    protected boolean _init() {
        return false;
    }

    public Map<String, Object> getParams() {
        return ImmutableMap.copyOf(params);
    }

    public void setParams(Map<String, Object> params) {
        this.params.clear();
        if (params != null) {
            this.params.putAll(params);
        }
    }

    public final void execute(ExecutionContext context) {
        final DebugTool debugTool = context.getDebugTool();

        final Map<String, Object> params = context.getParams();

        // enter the data node
        context.enterDataNode(params);

        try {

            // request params validation
            final Object requestParams;
            try {
                requestParams = requestDataStructure.resolve(params);
            } catch (IllegalArgumentException e) {
                // failed
                log.warn("failed to parse request params {}", params);
                throw new IllegalParamsException("the data view execution params {} illegal", e, params);
            }
            if (context.isDebug()) {
                debugTool.printLog("request data view with params %s", requestParams);
            }

            // execute
            final Object result;
            try {
                result = this._execute(dataSource, ((Map<String, Object>) requestParams).get("params"));
            } catch (Throwable t) {
                if (context.isDebug()) {
                    debugTool.printLog("failed to execute data view due to exception %s", t);
                }
                throw t;
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

            if (context.isDebug()) {
                debugTool.printLog("request data view with result: \r\n ======== result ======= \r\n %s \r\n ======== result end ======= \r\n \r\n", resolved);
            }

            context.setResult(resolved);

        } finally {
            // leave the current node
            context.leaveNode();
        }
    }

    /**
     * the actual executor to execute the data view
     *
     * @param dataSource the datasource
     * @param params     the params passed
     * @return result
     */
    protected Object _execute(DataSource dataSource, Object params) {
        return null;
    }

    public boolean bindDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return true;
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

    public DataView createNewVersion(String newVersion) {
        final Constructor<? extends DataView> constructor;
        try {
            constructor = ReflectionUtils.accessibleConstructor(this.getClass(), DataViewId.class, String.class, Version.class);
        } catch (NoSuchMethodException e) {
            // ignore
            throw new AbstractAppRuntimeException("failed due to create new data view version", e);
        }

        final DataView dataView = BeanUtils.instantiateClass(constructor, getId(), getName(), Version.of(newVersion));
        dataView.bindDataSource(dataSource);
        dataView.addAllRequestDataStructure(requestDataStructure.getDataStructures());
        dataView.addAllResponseDataStructure(responseDataStructure.getDataStructures());
        return dataView;
    }

    public Version getDataViewVersion() {
        return version;
    }

}
