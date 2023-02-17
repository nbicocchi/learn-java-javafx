package com.nbicocchi.javafx.primes;

public class PrimeSearcherFast implements PrimeSearcher {
    @Override
    public boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        int i = 2;
        for (; i * i <= number && number % i != 0; i++)
            ;
        return i * i > number;
    }
}
