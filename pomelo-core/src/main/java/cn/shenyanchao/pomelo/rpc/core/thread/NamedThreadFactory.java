package cn.shenyanchao.pomelo.rpc.core.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 带名字的线程池工厂类,便于排查问题
 *
 * @author shenyanchao
 */
public class NamedThreadFactory implements ThreadFactory {

    static final AtomicInteger poolNumber = new AtomicInteger(0);

    final AtomicInteger threadNumber = new AtomicInteger(0);

    final ThreadGroup group;
    final String namePrefix;
    final boolean isDaemon;

    public NamedThreadFactory() {
        this("pomelo");
    }

    public NamedThreadFactory(String name) {
        this(name, false);
    }

    public NamedThreadFactory(String prefix, boolean daemon) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread()
                .getThreadGroup();
        namePrefix = prefix + "-" + poolNumber.getAndIncrement() + "-thread-";
        isDaemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix
                + threadNumber.getAndIncrement(), 0);
        t.setDaemon(isDaemon);
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }

}
