package com.qg.exclusiveplug.web;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class TwinsLock implements Lock {

    private final Sync sync = new Sync(2);

    private static final class Sync extends AbstractQueuedSynchronizer {
        Sync(int count) {
            if (count < 0) {
                throw new IllegalArgumentException("error");
            }
            super.setState(count);
        }

        @Override
        protected int tryAcquireShared(int count) {
            for (; ; ) {
                int currentCount = super.getState();
                int newCount = currentCount - count;
                if (newCount < 0 || compareAndSetState(currentCount, newCount)) {
                    return newCount;
                }
            }
        }

        @Override
        protected boolean tryReleaseShared(int count) {
            for (; ; ) {
                int currentCount = super.getState();
                int newCount = currentCount + count;
                if (compareAndSetState(currentCount, newCount)) {
                    return true;
                }
            }
        }
    }

    @Override
    public void lock() {
        sync.acquireShared(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        sync.releaseShared(1);
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        Lock lock = new TwinsLock();

        for (int i = 0; i < 10; i++) {
            new Thread(new TestLock(lock)).start();
        }
//        Thread.sleep(10000);
//        URL url = new URL("https://dldir1.qq.com/weixin/Windows/WeChatSetup.exe");

        /*BufferedReader os = new BufferedReader(new InputStreamReader(url.openStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("E://1.exe"))));
        while (os.read() > 0) {
            bw.write(os.readLine());
        }*/
    }
}

class TestLock implements Runnable {

    private final Lock lock;

    TestLock(Lock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName());
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
