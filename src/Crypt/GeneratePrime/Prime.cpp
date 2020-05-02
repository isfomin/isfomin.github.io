#include "Prime.h"
#include <ctime>

// Public constructs
Prime::Prime()
    : val_(0)
{ }

Prime::Prime(int length) {
    generate(length, 5);
}

// Public methods
int Prime::get(int length) {
    int q = 5;
    generate(length, q);
    return val_;
}

// Private methods
void Prime::generate(int t, int q) {
    int u = 0;
    int p = 0;
    int twoInT = pow(2, t);

    start: ;
    srand( time(0) );
    double qci = random();
    int n = ceil( pow(2, t - 1 ) / q );
    int N = n + n * qci;
    if (N % 2 != 0) N++;
    u = 0;

    do {
        p = (N + u) * q + 1;
        if (p > twoInT) goto start;
        u += 2;
    } while( modPow(2, p - 1, p) != 1 || modPow(2, N + u, p) == 1 );

    val_ = p;
}

int Prime::modPow(int a, int s, int mod) {
    if (a >= mod) a %= mod;
    long c = a;

    for (size_t i = 0; i < s - 1; ++i) {
        c *= a;
        if (c >= mod)
            c %= mod;
    }
    a = c;
    return a;
}

int Prime::getLengthBitValue(int value) {
    int i = 0;
    while (value > 0) {
        value = value >> 1;
        ++i;
    }
    return i;
}

double Prime::random() {
    const int m = 100, // генерация псевдослучайных чисел в диапазоне значений от 0 до 100 (выбирается случайно m > 0)
               a = 8, // множитель (выбирается случайно 0 <= a <= m)
             inc = 65; // инкрементирующее значение (выбирается случайно 0 <= inc <= m)
    int x = rand() % (m + 1);
     x = ((a * x) + inc) % m; // формула линейного конгруэнтного метода генерации псевдослучайных чисел
     return (x / double(m));
}

int Prime::random(int min, int max) {
    return min + rand() % (max - min + 1);    // [min, max]
}
