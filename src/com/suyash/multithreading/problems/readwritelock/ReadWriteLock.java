package com.suyash.multithreading.problems.readwritelock;

class Demonstration {
    public static void main(String args[]) throws Exception {
        final ReadWriteLock rwl = new ReadWriteLock();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Attempting to acquire write lock in t1: " + System.currentTimeMillis());
                    rwl.acquireWriteLock();
                    System.out.println("write lock acquired t1: " + +System.currentTimeMillis());
                    // Simulates write lock being held indefinitely
                    for (; ; ) {
                        Thread.sleep(500);
                    }
                } catch (InterruptedException ignored) {
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Attempting to acquire write lock in t2: " + System.currentTimeMillis());
                    rwl.acquireWriteLock();
                    System.out.println("write lock acquired t2: " + +System.currentTimeMillis());
                    // Simulates write lock being held indefinitely
                    for (; ; ) {
                        Thread.sleep(500);
                    }
                } catch (InterruptedException ignored) {
                }
            }
        });

        Thread tReader1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    rwl.acquireReadLock();
                    System.out.println("Read lock acquired: " + System.currentTimeMillis());
                } catch (InterruptedException ignored) {
                }
            }
        });
        Thread tReader2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Read lock about to release: " + System.currentTimeMillis());
                try {
                    rwl.releaseReadLock();
                } catch (InterruptedException ignored) {

                }
                System.out.println("Read lock released: " + System.currentTimeMillis());
            }
        });

        tReader1.start();
        t1.start();
        Thread.sleep(3000);
        tReader2.start();
        Thread.sleep(1000);
        t2.start();
        tReader1.join();
        tReader2.join();
        t2.join();
    }
}

public class ReadWriteLock {
    boolean isWriteLocked = false;
    int readers = 0;

    public synchronized void acquireReadLock() throws InterruptedException {
        while (isWriteLocked) {
            wait();
        }
        readers++;
    }

    public synchronized void releaseReadLock() throws InterruptedException {
        readers--;
        notify();
    }

    public synchronized void acquireWriteLock() throws InterruptedException {
        while ( isWriteLocked || readers != 0) {
            wait();
        }
        isWriteLocked = true;
    }

    public synchronized void releaseWriteLock() {
        isWriteLocked = false;
        notify();
    }
}
