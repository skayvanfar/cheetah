package concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class BackgroundExecutor {
    private static final ExecutorService daemonExecutor = Executors.newCachedThreadPool(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true); // Mark thread as daemon
            return t;
        }
    });

    public static ExecutorService getExecutor() {
        return daemonExecutor;
    }
}
