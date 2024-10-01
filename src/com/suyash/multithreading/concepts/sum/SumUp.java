package com.suyash.multithreading.concepts.sum;

public class SumUp {

    long startRange;
    long endRange;
    long counter = 0;
    static long MAX_NUM = Integer.MAX_VALUE;


    public SumUp(long startRange, long endRange) {
        this.startRange = startRange;
        this.endRange = endRange;
    }

    public void add() {
        for (long i = startRange; i <= endRange; i++) {
            counter += i;
        }
    }

    static public void oneThread() {
        long start = System.currentTimeMillis();
        SumUp s = new SumUp(1, MAX_NUM );
        s.add();
        long end = System.currentTimeMillis();
        System.out.println("Single thread final count = " + s.counter + " took " + (end - start));
    }

    static public void twoThreads() throws InterruptedException {
        long start = System.currentTimeMillis();
        SumUp s1 = new SumUp(1, MAX_NUM / 2);
        SumUp s2 = new SumUp(1 + (MAX_NUM / 2), MAX_NUM);
        Thread t1 = new Thread(() -> {
            s1.add();
        });
        Thread t2 = new Thread(() -> {
            s2.add();
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        long finalCount = s1.counter + s2.counter;
        long end = System.currentTimeMillis();
        System.out.println("Two threads final count = " + finalCount + " took " + (end - start));
    }

    public static void runTest() throws InterruptedException {
        oneThread();
        twoThreads();
    }
}
