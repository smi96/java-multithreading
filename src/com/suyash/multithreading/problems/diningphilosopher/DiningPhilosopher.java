package com.suyash.multithreading.problems.diningphilosopher;

import com.suyash.multithreading.problems.uberride.UberRide;

import java.util.Random;
import java.util.concurrent.Semaphore;

class Demo {
    public static void main(String[] args) throws InterruptedException {
        DiningPhilosopher.runTest();
    }
}

public class DiningPhilosopher {
    private static Random random = new Random(System.currentTimeMillis());
    // Five semaphore represent the five forks.
    private Semaphore[] forks = new Semaphore[5];
    private Semaphore maxDiners = new Semaphore(4);


    public DiningPhilosopher() {
        forks[0] = new Semaphore(1);
        forks[1] = new Semaphore(1);
        forks[2] = new Semaphore(1);
        forks[3] = new Semaphore(1);
        forks[4] = new Semaphore(1);
    }

    public void lifecycleOfPhilosopher(int id) throws InterruptedException {
        while (true) {
            contemplate();
            eat(id);
        }
    }

    void contemplate() throws InterruptedException {
        Thread.sleep(random.nextInt(500));
    }

    void eat(int id) throws InterruptedException {
        maxDiners.acquire();
        forks[id].acquire();
        forks[(id + 4) % 5].acquire();
        System.out.println("Philosopher " + id + " is eating");
        forks[id].release();
        forks[(id + 4) % 5].release();
        maxDiners.release();
    }

    static void startPhilosoper(DiningPhilosopher dp, int id) {
        try {
            dp.lifecycleOfPhilosopher(id);
        } catch (InterruptedException ie) {
        }
    }

    public static void runTest() throws InterruptedException {
        final DiningPhilosopher dp = new DiningPhilosopher();
        Thread p1 = new Thread(new Runnable() {
            public void run() {
                startPhilosoper(dp, 0);
            }
        });
        Thread p2 = new Thread(new Runnable() {
            public void run() {
                startPhilosoper(dp, 1);
            }
        });
        Thread p3 = new Thread(new Runnable() {
            public void run() {
                startPhilosoper(dp, 2);
            }
        });
        Thread p4 = new Thread(new Runnable() {
            public void run() {
                startPhilosoper(dp, 3);
            }
        });
        Thread p5 = new Thread(new Runnable() {
            public void run() {
                startPhilosoper(dp, 4);
            }
        });
        p1.start();
        p2.start();
        p3.start();
        p4.start();
        p5.start();
        p1.join();
        p2.join();
        p3.join();
        p4.join();
        p5.join();
    }
}
