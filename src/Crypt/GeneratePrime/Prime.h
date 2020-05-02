#include <cmath>
#include <cstdlib>

class Prime {
public:
    Prime();
    Prime(int length);

    int get(int length);

private:
    double random();
    int random(int min, int max);
    void generate(int t, int q);
    int modPow(int a, int s, int mod);
    int getLengthBitValue(int value);

    int val_;
};
