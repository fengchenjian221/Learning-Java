package boot.utils;

import java.util.concurrent.CountDownLatch;

public class AsyncSyncData {
    private static final int THREAD_COUNT = 5;
    private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(THREAD_COUNT);
    private static final Object LOCK = new Object();

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int index = i;
            new Thread(() -> {
                //异步任务
                asyncTask(index);
                //等待所有异步任务结束
                try {
                    COUNT_DOWN_LATCH.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //同步任务
                synchronized (LOCK) {
                    syncTask(index);
                }
            }).start();
        }
    }

    private static void asyncTask(int index) {
        System.out.println("异步任务 " + index + " 开始");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("异步任务 " + index + " 结束");
        COUNT_DOWN_LATCH.countDown();
    }

    private static void syncTask(int index) {
        System.out.println("同步任务 " + index + " 开始");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("同步任务 " + index + " 结束");
    }
}

