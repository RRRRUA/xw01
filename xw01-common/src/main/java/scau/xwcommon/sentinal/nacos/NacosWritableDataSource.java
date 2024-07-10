package scau.xwcommon.sentinal.nacos;

import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.WritableDataSource;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lwc
 */
public class NacosWritableDataSource<T> extends CustomNacosDataSource implements WritableDataSource<T> {

    private final Lock lock = new ReentrantLock(true);

    private final Converter<T, String> configEncoder;

    public NacosWritableDataSource(String serverAddr, String groupId, String dataId, Converter<T, String> configEncoder) {
        super(serverAddr, groupId, dataId, configEncoder);
        this.configEncoder = configEncoder;
    }

    @Override
    public void write(T value) throws Exception {
        lock.lock();
        try {
            String convertResult = configEncoder.convert(value);
            configService.publishConfig(dataId, groupId, convertResult);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void close() {
        super.close();
    }
}
