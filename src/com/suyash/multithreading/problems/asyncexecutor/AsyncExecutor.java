package com.suyash.multithreading.problems.asyncexecutor;


class Demonstration {
    public static void main( String args[] ) throws Exception{
        AsyncExecutor executor = new AsyncExecutor();
        executor.asynchronousExecution(() -> {
            System.out.println("I am done");
        });
        System.out.println("main thread exiting...");



        SynchronousExecutor executor1 = new SynchronousExecutor();
        executor1.asynchronousExecution(() -> {
            System.out.println("I am done async");
        });
        System.out.println("main thread exiting...");
    }
}
interface Callback {
    public void done();
}
class AsyncExecutor {
    public void asynchronousExecution(Callback callback) throws Exception {
        Thread t = new Thread(() -> {
            // Do some useful work
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ie) {
            }
            callback.done();
        });
        t.start();
    }
}

class SynchronousExecutor extends AsyncExecutor {

    @Override()
    public void asynchronousExecution(Callback callback) throws Exception {
        Object signal = new Object();
        final boolean[] isDone = new boolean[1];
        Callback cb = new Callback() {
            @Override
            public void done() {
                callback.done();
                synchronized (signal) {
                    signal.notify();
                    isDone[0] = true;
                }
            }
        };
        super.asynchronousExecution(cb);
        synchronized (signal) {
            while (!isDone[0]) {
                signal.wait();
            }
        }
    }

}
