package ir.aminer.potadoshack.core.utils;

import ir.aminer.potadoshack.client.User;
import ir.aminer.potadoshack.client.page.PageHandler;
import ir.aminer.potadoshack.core.error.Error;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Common {
    public static String hmacSha256(String key, String raw) {
        Mac hmac = null;
        try {
            hmac = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException ex) {
            return "";
        }
        try {
            hmac.init(new SecretKeySpec(key.getBytes(), "HmacSHA256"));
        } catch (InvalidKeyException e) {
            System.err.println("Key is invalid.");
            return null;
        }

        return Base64.getUrlEncoder().encodeToString(hmac.doFinal(raw.getBytes()));
    }

    public static Consumer<Error> getAuthorityErrorChecker() {
        return error -> {
            if (error.equals(Error.UNAUTHORIZED_TOKEN) || error.equals(Error.INVALID_TOKEN)) {
                User.getPreferenceFile().delete();
                if (!User.getPreferenceFile().exists())
                    PageHandler.getInstance().activePage("sign_in");
            }
        };
    }

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
