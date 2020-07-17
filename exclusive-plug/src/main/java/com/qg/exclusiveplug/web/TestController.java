package com.qg.exclusiveplug.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author WilderGao
 * time 2018-09-25 16:28
 * motto : everything is no in vain
 * description
 */
@RestController
public class TestController {
    @RequestMapping("/hello")
    public String hello() {
        return "test success!";
    }

    private static volatile int integer = 0;

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        for (int i = 0; i < 100; i++) {
            new Thread(new LockTest(lock)).start();
        }
    }

    static class LockTest implements Runnable {

        private Lock lock;

        public LockTest(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            try {
                lock.lock();
                System.out.println(integer++);
                lock.lock();
            } finally {
                lock.unlock();
                lock.unlock();
            }
        }
    }

}
