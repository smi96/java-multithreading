package com.suyash.multithreading.problems.uberride;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

class Demo {
    public static void main(String[] args) throws InterruptedException {
        UberRide.runTest();
    }
}

public class UberRide {
    private int republicans = 0;
    private int democrats = 0;
    private Semaphore demsWaiting = new Semaphore(0);
    private Semaphore repubsWaiting = new Semaphore(0);

    CyclicBarrier barrier = new CyclicBarrier(4);
    ReentrantLock lock = new ReentrantLock();

    void seatDemocrat() throws InterruptedException, BrokenBarrierException {
        boolean rideLeader = false;
        lock.lock();
        democrats++;
        if (democrats == 4) {
            demsWaiting.release(3);
            democrats-=4;
            rideLeader = true;
        } else if (democrats == 2 && republicans >= 2) {
            demsWaiting.release(2);
            repubsWaiting.release(2);
            rideLeader = true;
            democrats -= 2;
            republicans -= 2;
        } else {
            lock.unlock();
            demsWaiting.acquire();
        }

        seated();
        barrier.await();
        if (rideLeader == true) {
            drive();
            lock.unlock();
        }
    }

    void seatRepublican() throws InterruptedException, BrokenBarrierException {
        boolean rideLeader = false;
        lock.lock();
        republicans++;
        if (republicans == 4) {
            repubsWaiting.release(3);
            republicans-=4;
            rideLeader = true;
        } else if (republicans == 2 && democrats >= 2) {
            demsWaiting.release(2);
            repubsWaiting.release(2);
            rideLeader = true;
            democrats -= 2;
            republicans -= 2;
        } else {
            lock.unlock();
            repubsWaiting.acquire();
        }

        seated();
        barrier.await();
        if (rideLeader == true) {
            drive();
            lock.unlock();
        }
    }

    void seated() {
        System.out.println(Thread.currentThread().getName() + " seated");
        System.out.flush();
    }

    void drive() {
        System.out.println("Uber Ride on Its wayyyy... with ride leader " + Thread.currentThread().getName());
        System.out.flush();
    }

    public static void runTest() throws InterruptedException {
        final UberRide uberRide = new UberRide();
        Set<Thread> allThreads = new HashSet<Thread>();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        uberRide.seatDemocrat();
                    } catch (BrokenBarrierException e) {
                        System.out.println("We have a problem");
                    } catch (InterruptedException e) {
                        System.out.println("WE have a problem");
                    }
                }
            });
            thread.setName("Democrat_" + (i + 1));
            allThreads.add(thread);
            Thread.sleep(50);
        }

        for (int i = 0; i < 14; i++) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        uberRide.seatRepublican();
                    } catch (InterruptedException ie) {
                        System.out.println("We have a problem");
                    } catch (BrokenBarrierException bbe) {
                        System.out.println("We have a problem");
                    }
                }
            });
            thread.setName("Republican_" + (i + 1));
            allThreads.add(thread);
            Thread.sleep(20);
        }
        for (Thread t : allThreads) {
            t.start();
        }
        for (Thread t : allThreads) {
            t.join();
        }
    }
}
