package scau.xwcommon.sentinal.nacos;

import com.alibaba.csp.sentinel.command.handler.ModifyParamFlowRulesCommandHandler;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.WritableDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.csp.sentinel.transport.util.WritableDataSourceRegistry;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.List;

/**
 * @author lwc
 */
public class NacosDataSourceInit implements InitFunc {

    private static final String SERVER_ADDR = "localhost:8848";
    private static final String GROUP_ID = "RENREN_CLOUD_GROUP";
    private static final String FLOW_DATA_ID = "sentinel-flow-rule.json";
    private static final String AUTHORITY_DATA_ID = "sentinel-authority-rule.json";
    private static final String DEGRADE_DATA_ID = "sentinel-degrade-rule.json";
    private static final String PARAM_DATA_ID = "sentinel-param-flow-rule.json";
    private static final String SYSTEM_DATA_ID = "sentinel-system-rule.json";

    @Override
    public void init() {
        System.out.println("NacosDataSourceInit 运行");

        //流控规则
        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new CustomNacosDataSource<>(SERVER_ADDR, GROUP_ID, FLOW_DATA_ID, source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
        }));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());

        WritableDataSource<List<FlowRule>> writableFlowRuleDataSource = new NacosWritableDataSource<>(SERVER_ADDR, GROUP_ID, FLOW_DATA_ID, JSON::toJSONString);
        WritableDataSourceRegistry.registerFlowDataSource(writableFlowRuleDataSource);

        //降级规则
        ReadableDataSource<String, List<DegradeRule>> degradeRuleDataSource = new CustomNacosDataSource<>(SERVER_ADDR, GROUP_ID, DEGRADE_DATA_ID, source -> JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {
        }));
        DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());

        WritableDataSource<List<DegradeRule>> writableDegradeRuleDataSource = new NacosWritableDataSource<>(SERVER_ADDR, GROUP_ID, DEGRADE_DATA_ID, JSON::toJSONString);
        WritableDataSourceRegistry.registerDegradeDataSource(writableDegradeRuleDataSource);

        //系统规则
        ReadableDataSource<String, List<SystemRule>> systemRuleDataSource = new CustomNacosDataSource<>(SERVER_ADDR, GROUP_ID, SYSTEM_DATA_ID, source -> JSON.parseObject(source, new TypeReference<List<SystemRule>>() {
        }));
        SystemRuleManager.register2Property(systemRuleDataSource.getProperty());

        WritableDataSource<List<SystemRule>> writableSystemRuleDataSource = new NacosWritableDataSource<>(SERVER_ADDR, GROUP_ID, SYSTEM_DATA_ID, JSON::toJSONString);
        WritableDataSourceRegistry.registerSystemDataSource(writableSystemRuleDataSource);

        //授权规则
        ReadableDataSource<String, List<AuthorityRule>> authorityRuleDataSource = new CustomNacosDataSource<>(SERVER_ADDR, GROUP_ID, AUTHORITY_DATA_ID, source -> JSON.parseObject(source, new TypeReference<List<AuthorityRule>>() {
        }));
        AuthorityRuleManager.register2Property(authorityRuleDataSource.getProperty());

        WritableDataSource<List<AuthorityRule>> writableAuthorityRuleDataSource = new NacosWritableDataSource<>(SERVER_ADDR, GROUP_ID, FLOW_DATA_ID, JSON::toJSONString);
        WritableDataSourceRegistry.registerAuthorityDataSource(writableAuthorityRuleDataSource);

        //热点参数规则
        ReadableDataSource<String, List<ParamFlowRule>> paramFlowRuleDataSource = new CustomNacosDataSource<>(SERVER_ADDR, GROUP_ID, PARAM_DATA_ID, source -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {
        }));
        ParamFlowRuleManager.register2Property(paramFlowRuleDataSource.getProperty());

        WritableDataSource<List<ParamFlowRule>> writableParamFlowRuleDataSource = new NacosWritableDataSource<>(SERVER_ADDR, GROUP_ID, PARAM_DATA_ID, JSON::toJSONString);
        ModifyParamFlowRulesCommandHandler.setWritableDataSource(writableParamFlowRuleDataSource);

    }
}