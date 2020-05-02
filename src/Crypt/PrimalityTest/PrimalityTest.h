enum NumberType {
    PRIME, 
    COMPOSITE 
};

class PrimalityTest
{
public:
    // m
    bool isPrimeMillerRabin(int number);
    NumberType MillerRabinTest(int number, int reliability);
    
    // v
    static int RELIABILITY;
    
private:
    // m
    bool isEven(int number);
    unsigned long modPow(long number, int s, int mod);
    int random(int a, int b);

};
