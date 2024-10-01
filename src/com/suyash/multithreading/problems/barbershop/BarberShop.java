package com.suyash.multithreading.problems.barbershop;

import java.util.HashSet;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

class Demo {
    public static void main(String[] args) throws InterruptedException {
        BarberShop.runTests();
    }
}

public class BarberShop {
    final int CHAIRS = 3;
    int waitingCustomers = 0;
    int hairCutsGiven = 0;
    ReentrantLock lock = new ReentrantLock();
    Semaphore waitForCustomerToEnter = new Semaphore(0);
    Semaphore waitForBarberToGetReady = new Semaphore(0);
    Semaphore waitForCustomerToLeave = new Semaphore(0);
    Semaphore waitForBarberToCutHair = new Semaphore(0);

    void customerWalksIn() throws InterruptedException {
        lock.lock();
        if (waitingCustomers == CHAIRS) {
            System.out.println("Customer walk out, all chairs occupied");
            lock.unlock();
            return;
        }
        waitingCustomers++;
        lock.unlock();
        waitForCustomerToEnter.release();
        waitForBarberToGetReady.acquire();
        lock.lock();
        waitingCustomers--;
        lock.unlock();
        waitForBarberToCutHair.acquire();
        waitForCustomerToLeave.release();
    }

    void barber() throws InterruptedException {
        while(true) {
            waitForCustomerToEnter.acquire();
            waitForBarberToGetReady.release();
            hairCutsGiven++;
            System.out.println("Barber cutting hair..." + hairCutsGiven);
            Thread.sleep(50);
            waitForBarberToCutHair.release();
            waitForCustomerToLeave.acquire();
        }
    }

    public static void runTests() throws InterruptedException {
        HashSet<Thread> set = new HashSet<Thread>();
        final BarberShop barberShopProblem = new BarberShop();
        Thread barberThread = new Thread(new Runnable() {
            public void run() {
                try {
                    barberShopProblem.barber();
                } catch (InterruptedException ie) {
                }
            }
        });
        barberThread.start();

        for (int i=0;i<10;i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        barberShopProblem.customerWalksIn();
                    } catch (InterruptedException e) {

                    }
                }
            });
            set.add(t);
        }
        for (Thread t : set) {
            t.start();
        }
        for (Thread t : set) {
            t.join();
        }
        set.clear();
        Thread.sleep(800);
        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        barberShopProblem.customerWalksIn();
                    } catch (InterruptedException ie) {
                    }
                }
            });
            set.add(t);
        }
        for (Thread t : set) {
            t.start();
        }
        barberThread.join();
    }
}