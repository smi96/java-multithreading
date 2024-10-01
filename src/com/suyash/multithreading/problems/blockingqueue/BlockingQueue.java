package com.suyash.multithreading.problems.blockingqueue;

class Demo {
    public static void main(String[] args) throws InterruptedException {
        final BlockingQueue<Integer> queue = new BlockingQueue<>(5);
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 50; i++) {
                        queue.enqueue(i);
                        System.out.println("enqueued " + i);
                    }
                } catch (InterruptedException ie) {
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 25; i++) {
                        System.out.println("Thread 2 dequeued: " + queue.dequeue());
                    }
                } catch (InterruptedException ie) {
                }
            }
        });

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 25; i++) {
                        System.out.println("Thread 3 dequeued: " + queue.dequeue());
                    }
                } catch (InterruptedException ie) {
                }
            }
        });

        t1.start();
        Thread.sleep(4000);
        t2.start();

        t2.join();
        t3.start();
        t1.join();
        t3.join();

    }
}

public class BlockingQueue<T> {
    T[] array;
    Object lock = new Object();
    int size = 0;
    int capacity;
    int head = 0; int tail = 0;

    @SuppressWarnings("unchecked")
    public BlockingQueue(int capacity) {
        array = (T[]) new Object[capacity];
        this.capacity = capacity;
    }

    public void enqueue(T item) throws InterruptedException {
        synchronized (lock) {
            while (size == capacity) {
                lock.wait();
            }
            if (tail == capacity) {
                tail = 0;
            }
            array[tail] = item;
            size++;
            tail++;
            lock.notifyAll();
        }
    }

    public T dequeue() throws InterruptedException {
        T item = null;
        synchronized (lock) {
            while (size == 0) {
                lock.wait();
            }
            if (head == capacity) {
                head = 0;
            }
            item = array[head];
            array[head] = null;
            size--;
            head++;
            lock.notifyAll();
        }
        return item;
    }
}

class BlockingQueueWithMutex {

}
