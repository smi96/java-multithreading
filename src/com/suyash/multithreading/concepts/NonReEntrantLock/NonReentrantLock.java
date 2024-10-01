package com.suyash.multithreading.concepts.NonReEntrantLock;

class Demonstration {
    public static void main(String[] args) throws Exception {
        NonReentrantLock nrLock = new NonReentrantLock();
        nrLock.lock();
        System.out.println("Lock acquired");
        System.out.println("Trying to acquire second lock");
        nrLock.unlock();
        System.out.println("Acquired second lock");
        nrLock.lock();
        System.out.println("Trying to acquire third lock");

    }
}

public class NonReentrantLock {
    boolean isLocked;
    public NonReentrantLock() {
        isLocked = false;
    }

    public synchronized void lock() throws InterruptedException {
        while (isLocked) {
            wait();
        }
        isLocked = true;
    }
    public synchronized void unlock() {
        isLocked = false;
        notify();
    }
}
