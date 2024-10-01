package com.suyash.multithreading.concepts.CorrectSemaphoreUsage;

import java.util.concurrent.Semaphore;

class Demo {
    public static void main(String[] args) throws Exception {
        CorrectSemaphoreUsage.example();
    }
}

public class CorrectSemaphoreUsage {
    public static void example () throws InterruptedException {
        Semaphore semaphore = new Semaphore(1);

        Thread badThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        semaphore.acquire();
                        try {
                            throw new RuntimeException("");
                        } catch (Exception e) {
                            return;
                        } finally {
                            System.out.println("Bad Thread releasing Sema");
                            semaphore.release();
                        }
                    } catch (InterruptedException e) {

                    }
                }
            }
        });
        badThread.start();
        Thread.sleep(10000);


        Thread goodThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Waiting for signal");
                try {
                    semaphore.acquire();
                } catch (Exception e) {

                }
            }
        });
        goodThread.start();
        badThread.join();
        goodThread.join();
        System.out.println("Exiting");
    }
}
