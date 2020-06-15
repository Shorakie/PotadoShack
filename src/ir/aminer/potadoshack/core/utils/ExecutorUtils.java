package ir.aminer.potadoshack.core.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorUtils {
    /**
     * Creates an executor service with a fixed pool size, that will time
     * out after a certain period of inactivity.
     * <p>
     * SO link @link https://stackoverflow.com/questions/13883293/turning-an-executorservice-to-daemon-in-java/29453160#29453160
     *
     * @param poolSize      The core- and maximum pool size
     * @param keepAliveTime The keep alive time
     * @param timeUnit      The time unit
     * @return The executor service
     */
    public static ExecutorService createFixedTimeoutExecutorService(
            int poolSize, long keepAliveTime, TimeUnit timeUnit) {
        ThreadPoolExecutor e =
                new ThreadPoolExecutor(poolSize, poolSize,
                        keepAliveTime, timeUnit, new LinkedBlockingQueue<Runnable>());
        e.allowCoreThreadTimeOut(true);
        return e;
    }
}
