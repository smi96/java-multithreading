package com.suyash.multithreading.problems.printzeroevenodd;

import java.util.concurrent.Semaphore;

class Demo {
    public static void main(String[] args) throws InterruptedException {
        PrintZeroEvenOdd.runTest();
    }
}

public class PrintZeroEvenOdd {
    private final int n;
    Semaphore zeroSem, evenSem, oddSem;
    public PrintZeroEvenOdd(int n) {
        this.n = n;
        zeroSem = new Semaphore(1);
        evenSem = new Semaphore(0);
        oddSem = new Semaphore(0);
    }

    public void PrintZero() throws InterruptedException {
        for (int i = 0; i < n; i++) {
            zeroSem.acquire();
            System.out.print("0");
            (i%2==0 ? oddSem : evenSem).release();
        }
    }

    public void PrintOdd() throws InterruptedException {
        for (int i = 1; i <= n; i += 2) {
            oddSem.acquire();
            System.out.print(i);
            zeroSem.release();
        }
    }

    public void PrintEven() throws InterruptedException {
        for (int i = 2; i <= n; i += 2) {
            evenSem.acquire();
            System.out.print(i);
            zeroSem.release();
        }
    }

    public static void runTest() throws InterruptedException {
        PrintZeroEvenOdd pzeo = new PrintZeroEvenOdd(10);
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pzeo.PrintZero();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pzeo.PrintEven();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pzeo.PrintOdd();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();
    }
}
