package com.nbicocchi.javafx.threads.managerworkers;

public class PrimeSearcherSlow implements PrimeSearcher {
    @Override
    public boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        int i = 2;
        for (; number % i != 0; i++);
        return i == number;
    }
}
