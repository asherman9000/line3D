package com.example.test;

public class Time {
    private static long startTime = System.nanoTime();
    public static long prevTime = startTime;
    public static double deltaTime() {
        double retNum = (double)System.nanoTime()-prevTime;
        prevTime = System.nanoTime();
        return retNum/1000000000 < 1 ? retNum/1000000000 : 0.001;
    }
}

