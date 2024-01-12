package com.nbicocchi.javafx.managerworkers;

/**
 * If a number n is not a prime, it can be factored into two factors a and b:
 * <p>
 * n = a * b
 * <p>
 * Now a and b can't be both greater than the square root of n, since then the product a * b would be greater than
 * sqrt(n) * sqrt(n) = n. So in any factorization of n, at least one of the factors must be less than or equal to the
 * square root of n, and if we can't find any factors less than or equal to the square root, n must be a prime.
 */
public class PrimeSearcherFast implements PrimeSearcher {
    @Override
    public boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        int i = 2;
        for (; i * i <= number && number % i != 0; i++);
        return i * i > number;
    }
}
