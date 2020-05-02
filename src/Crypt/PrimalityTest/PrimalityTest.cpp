#include "PrimalityTest.h"
#include <cmath>
#include <iostream>
#include <cstdlib>

int PrimalityTest::RELIABILITY = 5;

bool PrimalityTest::isPrimeMillerRabin(int number) {
    NumberType type = MillerRabinTest(number, RELIABILITY);
    return type == PRIME;
}

/**
    n - проверяемое число
    t - параметр надежности
*/
NumberType PrimalityTest::MillerRabinTest(int n, int t) {
    if (n == 1 || n == 2) return PRIME;
    if ( isEven(n) ) return COMPOSITE;
    
    // n = r*2^s + 1
    int r = n;
    int s = 0;
    do {
        s++;
        r = r >> 1;
    } while ( r % 2 == 0 );
    
    unsigned long b[s];
    for (int i = 0; i < t; ++i) {
        int a = random(2, n - 2); // random
        b[0] = modPow(a, r, n);
        
        for (int j = 1; j <= s; ++j) {
            b[j] = modPow(b[j - 1] * b[j - 1], 1, n);
            
            if (b[j] == 1 && b[j - 1] != 1 && (n - b[j - 1]) != 1)
                return COMPOSITE;
        }
        
        if (b[s] != 1)
            return COMPOSITE;
    }
    return PRIME;
}

unsigned long PrimalityTest::modPow(long a, int s, int mod) {
    if (a >= mod) a %= mod;
    long c = a;
    
    for (size_t i = 0; i < s - 1; ++i) {
        a *= c;
        if (a >= mod) 
            a %= mod;
    }
    return a;
}

bool PrimalityTest::isEven(int number) {
    return !number % 2;
}

int PrimalityTest::random(int min, int max) {
    return min + rand() % (max - min);    // [min, max)
}
