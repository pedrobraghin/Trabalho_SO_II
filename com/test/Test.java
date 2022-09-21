package com.test;

public class Test {
    public static void main(String[] args) {
        
        Thread t1 = new Thread();
        Thread t2 = new Thread();
        try {
            System.out.println("esperando...");
            t1.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t2.notifyAll();
    }
}
