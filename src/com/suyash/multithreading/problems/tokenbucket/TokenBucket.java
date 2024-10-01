package com.suyash.multithreading.problems.tokenbucket;

import java.util.HashSet;
import java.util.Set;

class Demonstration {
    public static void main( String args[] ) throws InterruptedException {
        TokenBucket.runTest();
    }
}

public class TokenBucket {
    private int MAX_TOKENS;
    private long lastRequestTimeStamp = System.currentTimeMillis();
    long possibleTokens = 0;

    public TokenBucket(int token) {
        MAX_TOKENS = token;
    }

    synchronized void getToken() throws InterruptedException {
        possibleTokens += (System.currentTimeMillis() - lastRequestTimeStamp) / 1000;
        if (possibleTokens > MAX_TOKENS) {
            possibleTokens = MAX_TOKENS;
        }
        if (possibleTokens == 0) {
            Thread.sleep(1000);
        }
        else {
            possibleTokens--;
        }
        lastRequestTimeStamp = System.currentTimeMillis();
        System.out.println("Granting " + Thread.currentThread().getName() + " token at " + System.currentTimeMillis());
    }

     public static void runTest() throws InterruptedException {
         Set<Thread> allThreads = new HashSet<Thread>();
         final TokenBucket tokenBucketFilter = new TokenBucket(5);
         // Sleep for 10 seconds.
         Thread.sleep(10000);
        // Generate 12 threads requesting tokens almost all at once.
         for (int i = 0; i < 12; i++) {
             Thread thread = new Thread(new Runnable() {
                 public void run() {
                     try {
                         tokenBucketFilter.getToken();
                     } catch (InterruptedException ie) {
                         System.out.println("We have a problem");
                     }
                 }
             });
             thread.setName("Thread_" + (i + 1));
             allThreads.add(thread);
         }
         for (Thread t : allThreads) {
             t.start();
         }
         for (Thread t : allThreads) {
             t.join();
         }
     }
}

class DaemonThreadTokenBucket {
    private int MAX_TOKENS;
    long possibleTokens = 0;

    public DaemonThreadTokenBucket(int token) {
        MAX_TOKENS = token;
        Thread dt = new Thread(() -> {
            daemonThread();
        });
        dt.setDaemon(true);
        dt.start();
    }

    void getToken() throws InterruptedException {
        synchronized (this) {
            while (possibleTokens == 0) {
                this.wait();
            }
            possibleTokens--;
        }
        System.out.println("Granting " + Thread.currentThread().getName() + " token at " + System.currentTimeMillis() / 1000);
    }

    private void daemonThread() {
        while(true) {
            synchronized (this) {
                if (possibleTokens < MAX_TOKENS) {
                    possibleTokens++;
                }
                this.notify();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {

            }
        }
    }
}

class DemonstrationDaemonThread {
    public static void main( String args[] ) throws InterruptedException {
        Set<Thread> allThreads = new HashSet<Thread>();
        final DaemonThreadTokenBucket tokenBucketFilter = new DaemonThreadTokenBucket(5);
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        tokenBucketFilter.getToken();
                    } catch (InterruptedException ie) {
                        System.out.println("We have a problem");
                    }
                }
            });
            thread.setName("Thread_" + (i + 1));
            allThreads.add(thread);
        }
        for (Thread t : allThreads) {
            t.start();
        }
        for (Thread t : allThreads) {
            t.join();
        }
    }
}