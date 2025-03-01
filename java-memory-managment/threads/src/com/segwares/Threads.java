package com.segwarez;

public class Threads {
    public static void main(String[] args) throws InterruptedException, ClassNotFoundException {
        Thread.UncaughtExceptionHandler handler =
                (t, e) -> System.out.println("Uncaught exception: " + e.getMessage());

        Thread t = new Thread(() -> {
            Thread tr = Thread.currentThread();
            System.out.println("Current thread: "
                    + tr.getName());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("After sleep");
        });
        t.setUncaughtExceptionHandler(handler);
        t.start();
        t.join(1000);

        System.out.println("After join");
        System.out.println("Current thread: " + t.getName());
        Thread.sleep(5000);
        System.out.println("Second sleep");
        Class cls = Class.forName("Threads");
        ClassLoader loader = cls.getClassLoader();
        Class.forName("Threads", true, loader);
    }
}
