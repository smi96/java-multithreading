package com.suyash.multithreading.problems.deferredcallback;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Demonstration {
    public static void main( String args[] ) throws InterruptedException {
        //DeferredCallback.runTestTenCallbacks();
        DeferredCallback.lateThenEarlyCallbacks();
    }
}

public class DeferredCallback {
    private static Random random = new Random(System.currentTimeMillis());

    PriorityQueue<Callback> q = new PriorityQueue<>(new Comparator<Callback>() {
        @Override
        public int compare(Callback o1, Callback o2) {
            return (int)(o1.executeAt - o2.executeAt);
        }
    });

    ReentrantLock lock = new ReentrantLock();

    Condition newCallbackArrived = lock.newCondition();

    private long findSleepDuration() {
        long currentTime = System.currentTimeMillis();
        assert q.peek() != null;
        return q.peek().executeAt - currentTime;
    }

    public void start() throws InterruptedException {
        long sleepFor = 0;
        while(true) {
            lock.lock();

            while (q.size() == 0) {
                newCallbackArrived.await();
            }

            while (q.size()!=0) {
                sleepFor = findSleepDuration();
                if(sleepFor <=0)
                    break;
                newCallbackArrived.await(sleepFor, TimeUnit.MILLISECONDS);
            }

            Callback cb = q.poll();
            System.out.println("Executed at " + System.currentTimeMillis() / 1000 + " required at " + cb.executeAt + " :message : " + cb.message);
            lock.unlock();
        }
    }

    public void registerCallback(Callback callback) {
        lock.lock();
        q.add(callback);
        newCallbackArrived.signal();
        lock.unlock();
    }

    static class Callback {
        long executeAt;
        String message;
        public Callback(long executeAt, String message) {
            this.executeAt = System.currentTimeMillis() + executeAt * 1000;
            this.message = message;
        }
    }

    public static void runTestTenCallbacks() throws InterruptedException {
        Set<Thread> allThreads = new HashSet<Thread>();
        final DeferredCallback deferredCallback = new DeferredCallback();
        Thread service = new Thread(new Runnable() {
            public void run() {
                try {
                    deferredCallback.start();
                } catch (InterruptedException ie) {
                }
            }
        });
        service.start();

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Callback cb = new Callback(1, "Hello this is " + Thread.currentThread().getName());
                    deferredCallback.registerCallback(cb);
                }
            });
            thread.setName("Thread_" + (i + 1));
            thread.start();
            allThreads.add(thread);
            Thread.sleep(1000);
        }

        for (Thread t : allThreads) {
            t.join();
        }
    }

    public static void lateThenEarlyCallbacks() throws InterruptedException {
        Set<Thread> allThreads = new HashSet<Thread>();
        final DeferredCallback deferredCallback = new DeferredCallback();
        Thread service = new Thread(new Runnable() {
            public void run() {
                try {
                    deferredCallback.start();
                } catch (InterruptedException ie) {
                }
            }
        });
        service.start();

        Thread lateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Callback cb = new Callback(8, "THis callback submitted first");
                deferredCallback.registerCallback(cb);
            }
        });
        lateThread.start();

        Thread.sleep(3000);

        Thread earlyThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Callback cb = new Callback(1, "THis callback submitted second");
                deferredCallback.registerCallback(cb);
            }
        });

        earlyThread.start();
        lateThread.join();
        earlyThread.join();
    }
}
