package com.yong.executors;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class MyExecutors {

    public BlockingDeque<Runnable> runnables;
    public volatile Boolean isRun = true;

    public MyExecutors(int dequeSize, int threadCount) {
        runnables = new LinkedBlockingDeque<Runnable>(dequeSize);
        for (int i = 0; i < threadCount; i++) {
            WorkThread workThread = new WorkThread();
            workThread.start();
        }
    }

    public void execute(Runnable runnable) {
        runnables.offer(runnable);
    }

    class WorkThread extends Thread {
        @Override
        public void run() {
            while (isRun || runnables.size() > 0) {
                Runnable runnable = runnables.poll();
                if (runnable != null) {
                    runnable.run();
                }
            }
        }
    }

    public static void main(String[] args) {
        MyExecutors myExecutors = new MyExecutors(10, 2);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            myExecutors.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + ":--" + finalI);
                }
            });
        }
        myExecutors.isRun = false;
    }
}
