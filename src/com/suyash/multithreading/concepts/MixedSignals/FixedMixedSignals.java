package com.suyash.multithreading.concepts.MixedSignals;

import java.util.concurrent.Semaphore;

class Demo {
    public static void main(String[] args) throws Exception {
        FixedMixedSignals.example();;
    }
}

public class FixedMixedSignals {
    public static void example() throws Exception {
        final Semaphore semaphore = new Semaphore(2);

        Thread signaller = new Thread(new Runnable() {
            @Override
            public void run() {
                semaphore.release();
                System.out.println("Signal Sent");
            }
        });

        Thread receiver = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    semaphore.acquire();
                    System.out.println("Signal received");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        signaller.start();
        signaller.join();
        Thread.sleep(1000);
        receiver.start();
        receiver.join();
        System.out.println("Exiting");
    }
}
