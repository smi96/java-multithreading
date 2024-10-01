package com.suyash.multithreading.problems.printfoobar;

class Demo {
    public static void main(String[] args) throws InterruptedException {
        PrintFooBar.runTest();
    }
}

class PrintFooBar {
    int n;
    boolean isFooChance = true;
    int fooPrinted = 0;
    int barPrinted = 0;
    public PrintFooBar(int n) {
        this.n = n;
    }

    public void PrintFoo() throws InterruptedException {
        while (fooPrinted < n) {
            synchronized (this) {
                while (!isFooChance) {
                    this.wait();
                }
                System.out.println("Foo");
                isFooChance = false;
                this.notifyAll();
            }
            fooPrinted++;
        }
    }

    public void PrintBar() throws InterruptedException {
        while (barPrinted < n) {
            synchronized (this) {
                while (isFooChance) {
                    this.wait();
                }
                System.out.println("Bar");
                isFooChance = true;
                this.notifyAll();
            }
            barPrinted++;
        }
    }

    public static void runTest() throws InterruptedException {
        PrintFooBar pfb = new PrintFooBar(10);
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pfb.PrintFoo();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pfb.PrintBar();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
