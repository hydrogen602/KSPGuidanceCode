package com.fournoobs;

import org.javatuples.Triplet;

public class Util {
    
    private Util() {}

    public static double dotProduct(Triplet<Double, Double, Double> u, Triplet<Double, Double, Double> v) {
        return u.getValue0() * v.getValue0() + u.getValue1() * v.getValue1() + u.getValue2() * u.getValue2();
    }
    
    public static double mag(Triplet<Double, Double, Double> v) {
        return Math.sqrt(dotProduct(v, v));
    }
}
