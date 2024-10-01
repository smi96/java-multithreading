package com.suyash.multithreading.problems.unisexbathroom;

class Demo {
    public static void main(String[] args) throws InterruptedException {
        UnisexBathroom.runTest();
    }
}

public class UnisexBathroom {
    static String MEN = "men";
    static String WOMEN = "women";
    static String NONE = "none";
    String inUseBy = NONE;
    int empInBathroom = 0;

    void useBathroom(String name) throws InterruptedException {
        System.out.println(name + " using bathroom. Current employees in bathroom = " + empInBathroom);
        Thread.sleep(10000);
        System.out.println(name + " done using bathroom");
    }
    void maleUseBathroom(String name) throws InterruptedException {
        synchronized (this) {
            while (inUseBy.equals(WOMEN)) {
                this.wait();
            }
            empInBathroom++;
            inUseBy = MEN;
        }
        useBathroom(name);

        synchronized (this) {
            empInBathroom--;
            if (empInBathroom == 0) {
                inUseBy = NONE;
            }
            this.notifyAll();
        }
    }
    void femaleUseBathroom(String name) throws InterruptedException {
        synchronized (this) {
            while (inUseBy.equals(MEN)) {
                this.wait();
            }
            empInBathroom++;
            inUseBy = WOMEN;
        }
        useBathroom(name);

        synchronized (this) {
            empInBathroom--;
            if (empInBathroom == 0) {
                inUseBy = NONE;
            }
            this.notifyAll();
        }
    }

    public static void runTest() throws InterruptedException {
        final UnisexBathroom unisexBathroom = new UnisexBathroom();
        Thread female1 = new Thread(new Runnable() {
            public void run() {
                try {
                    unisexBathroom.femaleUseBathroom("Lisa");
                } catch (InterruptedException ie) {
                }
            }
        });
        Thread male1 = new Thread(new Runnable() {
            public void run() {
                try {
                    unisexBathroom.maleUseBathroom("John");
                } catch (InterruptedException ie) {
                }
            }
        });

        Thread male2 = new Thread(new Runnable() {
            public void run() {
                try {
                    unisexBathroom.maleUseBathroom("Bob");
                } catch (InterruptedException ie) {
                }
            }
        });
        Thread male3 = new Thread(new Runnable() {
            public void run() {
                try {
                    unisexBathroom.maleUseBathroom("Anil");
                } catch (InterruptedException ie) {
                }
            }
        });
        Thread male4 = new Thread(new Runnable() {
            public void run() {
                try {
                    unisexBathroom.maleUseBathroom("Wentao");
                } catch (InterruptedException ie) {
                }
            }
        });

        female1.start();
        Thread.sleep(1000);
        male1.start();
        male2.start();
        male3.start();
        male4.start();
        female1.join();
        male1.join();
        male2.join();
        male3.join();
        male4.join();
    }
}
