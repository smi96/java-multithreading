package com.suyash.multithreading.problems.printorder;

class Demo {
    public static void main(String[] args) throws InterruptedException {
        PrintOrder.runTest();
    }
}

public class PrintOrder {

    int count = 1;
    void printFirst() {
        synchronized (this) {
            System.out.println("First printed");
            count++;
            this.notifyAll();
        }
    }

    void printSecond() throws InterruptedException {
        synchronized (this) {
            while(count != 2) {
                this.wait();
            }
            System.out.println("Second printed");
            count++;
            this.notifyAll();
        }
    }

    void printThird() throws InterruptedException {
        synchronized (this) {
            while(count != 3) {
                this.wait();
            }
            System.out.println("Third printed");
            count++;
            this.notifyAll();
        }
    }

    public static void runTest() throws InterruptedException {
        PrintOrder po = new PrintOrder();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    po.printThird();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                po.printFirst();
            }
        });

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    po.printSecond();
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


