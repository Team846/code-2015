package com.team846.frc2015;

import com.team846.util.bottles.BottleTypeGrabber;
import com.team846.util.bottles.Bottles;

public class BottlesTest {
    static class BottleFoo {

    }

    public static void main(String[] args) {
        Class<BottleFoo> foo = new BottleTypeGrabber<BottleFoo>(){}.getBottleClass();
        System.out.println(foo);
    }
    // TODO: Implement Tests for Bottling
}
