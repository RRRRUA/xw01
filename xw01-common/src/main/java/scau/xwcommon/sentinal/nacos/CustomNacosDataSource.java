package scau.xwcommon.sentinal.nacos;

import com.alibaba.csp.sentinel.concurrent.NamedThreadFactory;
import com.alibaba.csp.sentinel.datasource.AbstractDataSource;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;

import java.util.Properties;
import java.util.concurrent.*;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;

/**
 * @author lwc
 */
public class CustomNacosDataSource<T> extends AbstractDataSource<String, T> {
    private static final int DEFAULT_TIMEOUT = 3000;
    private final ExecutorService pool;
    private final Listener configListener;
    protected final String groupId;
    protected final String dataId;
    private final Properties properties;
    protected ConfigService configService;

    public CustomNacosDataSource(String serverAddr, String groupId, String dataId, Converter<String, T> parser) {
        this(buildProperties(serverAddr), groupId, dataId, parser);
    }

    public CustomNacosDataSource(final Properties properties, final String groupId, final String dataId, Converter<String, T> parser) {
        super(parser);
        this.pool = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue(1), new NamedThreadFactory("sentinel-nacos-ds-update"), new DiscardOldestPolicy());
        this.configService = null;
        if (!StringUtil.isBlank(groupId) && !StringUtil.isBlank(dataId)) {
            AssertUtil.notNull(properties, "Nacos properties must not be null, you could put some keys from PropertyKeyConst");
            this.groupId = groupId;
            this.dataId = dataId;
            this.properties = properties;
            this.configListener = new Listener() {
                @Override
                public Executor getExecutor() {
                    return CustomNacosDataSource.this.pool;
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    RecordLog.info(String.format("[CustomNacosDataSource] New property value received for (properties: %s) (dataId: %s, groupId: %s): %s", properties, dataId, groupId, configInfo), new Object[0]);
                    T newValue = CustomNacosDataSource.this.parser.convert(configInfo);
                    CustomNacosDataSource.this.getProperty().updateValue(newValue);
                }
            };
            this.initNacosListener();
            this.loadInitialConfig();
        } else {
            throw new IllegalArgumentException(String.format("Bad argument: groupId=[%s], dataId=[%s]", groupId, dataId));
        }
    }

    private void loadInitialConfig() {
        try {
            T newValue = this.loadConfig();
            if (newValue == null) {
                RecordLog.warn("[CustomNacosDataSource] WARN: initial config is null, you may have to check your data source", new Object[0]);
            }

            this.getProperty().updateValue(newValue);
        } catch (Exception var2) {
            RecordLog.warn("[CustomNacosDataSource] Error when loading initial config", var2);
        }

    }

    private void initNacosListener() {
        try {
            this.configService = NacosFactory.createConfigService(this.properties);
            this.configService.addListener(this.dataId, this.groupId, this.configListener);
        } catch (Exception var2) {
            RecordLog.warn("[CustomNacosDataSource] Error occurred when initializing Nacos data source", var2);
            var2.printStackTrace();
        }

    }

    @Override
    public String readSource() throws Exception {
        if (this.configService == null) {
            throw new IllegalStateException("Nacos config service has not been initialized or error occurred");
        } else {
            return this.configService.getConfig(this.dataId, this.groupId, 3000L);
        }
    }

    @Override
    public void close() {
        if (this.configService != null) {
            this.configService.removeListener(this.dataId, this.groupId, this.configListener);
        }

        this.pool.shutdownNow();
    }

    private static Properties buildProperties(String serverAddr) {
        Properties properties = new Properties();
        properties.setProperty("serverAddr", serverAddr);
        return properties;
    }
}
