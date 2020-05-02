#include <iostream>
#include "lab3.h"
#include "PrimalityTest/PrimalityTest.h"

using namespace std;

string Lab3::getName() {
    return "Лабораторная 3";
}

string Lab3::getDesc() {
    return "Тест на простоту Миллера-Рабина";
}

CollectCmds * Lab3::cmds() {
    CollectCmds * cmds = new CollectCmds;
    cmds->add(new DataCmd( "mrt", "(Miller Rabin Test) Проверить число на простоту тестом Миллера-Рабина" ));
    return cmds;
}

bool Lab3::cmds( std::string & res ) {
    if (Command::is("mrt %d")) {
        MillerRabinTest(Command::dparam[0]);
        return true;
    }
    if (Command::is("mrt table")) {
        MillerTable();
        return true;
    }
    return false;
}

void Lab3::MillerRabinTest(int number) {
    PrimalityTest test;
    if (test.isPrimeMillerRabin(number))
        cout << number << " - простое";
    else
        cout << number << " - составное";
}

void Lab3::MillerTable() {
    int sizeA = 10, sizeB = 10, sizeC = 10;
    int primeNums[] = { 8363, 1657, 9781, 9049, 6673, 1867, 1901, 1303, 5479, 8111 };
    int kNums[] = { 1105, 2465, 10585, 1729, 2821, 8911, 6601, 2821, 15841, 52633 };
    int compositeNums[] = { 625, 791, 3871, 2007, 6785, 1969, 5705, 3445, 6105, 3621 };
    
    PrimalityTest test;
    
    cout << "Простые числа" << endl;
    for (size_t i = 0; i < sizeA; ++i) {
        MillerRabinTest(primeNums[i]);
        cout << endl;
    }

    cout << "\nЧисла Кармайкла" << endl;
    for (size_t i = 0; i < sizeB; ++i) {
        MillerRabinTest(kNums[i]);
        cout << endl;
    }
    
    cout << "\nСостовные не четные" << endl;
    for (size_t i = 0; i < sizeC; ++i) {
        MillerRabinTest(compositeNums[i]);
        cout << endl;
    }
}
