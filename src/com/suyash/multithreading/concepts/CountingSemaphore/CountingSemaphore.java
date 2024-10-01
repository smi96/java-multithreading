package com.suyash.multithreading.concepts.CountingSemaphore;

class Demo {
    public static void main(String[] args) throws InterruptedException {
        final CountingSemaphore cs = new CountingSemaphore(1);
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 5; i++) {
                        cs.acquire();
                        System.out.println("Ping " + i);
                    }
                } catch(InterruptedException e) {

                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    try {
                        cs.release();
                        System.out.println("Pong " + i);
                    } catch (InterruptedException ie) {
                    }
                }
            }
        });
        t2.start();
        t1.start();
        t1.join();
        t2.join();
    }
}


public class CountingSemaphore {

    int usedPermit = 0;
    int maxCount;

    public CountingSemaphore(int maxCount) {
        this.maxCount = maxCount;
    }

    public synchronized void acquire() throws InterruptedException {
        while (usedPermit == maxCount) {
            wait();
        }
        usedPermit++;
        notify();
    }

    public synchronized void release() throws InterruptedException {
        while (usedPermit == 0) {
            wait();
        }
        usedPermit--;
        notify();
    }
}
