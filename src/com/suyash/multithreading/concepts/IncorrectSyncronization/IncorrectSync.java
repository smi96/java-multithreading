package com.suyash.multithreading.concepts.IncorrectSyncronization;

class Demonstration{
    public static void main(String[] args) throws InterruptedException {
        IncorrectSync.runExample();
    }
}

public class IncorrectSync {
    Boolean flag = true;
    public void example() throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                synchronized (flag) {
                    try {
                        while (flag) {
                            System.out.println("First thread about to sleep");
                            Thread.sleep(5000);
                            System.out.println("Woke up and about to invoke wait()");
                            flag.wait();
                        }
                    } catch (InterruptedException ie) {
                    }
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                flag = false;
                System.out.println("Boolean Assignment Done");
            }
        });

        t1.start();
        Thread.sleep(1000);
        t2.start();
        t1.join();
        t2.join();
    }

    public static void runExample() throws InterruptedException {
        IncorrectSync incorrectSynchronization = new IncorrectSync();
        incorrectSynchronization.example();
    }
}
