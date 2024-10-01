package com.suyash.multithreading.concepts.UnsafeThread;

import java.util.Random;

public class DemoUnsafeThread {
    static Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) throws InterruptedException {
        ThreadUnsafeCounter badCounter = new ThreadUnsafeCounter();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    badCounter.increment();
                    DemoUnsafeThread.sleepRandomlyForLessThan10Secs();
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    badCounter.decrement();
                    DemoUnsafeThread.sleepRandomlyForLessThan10Secs();
                }
            }
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        badCounter.printFinalCounterValue();
    }

    public static void sleepRandomlyForLessThan10Secs() {
        try {
            Thread.sleep(random.nextInt(10));
        } catch (InterruptedException ie) {
        }
    }
}

class ThreadUnsafeCounter {
    int count = 0;

    public void increment() {
        count++;
    }

    public void decrement() {
        count--;
    }

    void printFinalCounterValue() {
        System.out.println("Counter Is " + count);
    }
}
