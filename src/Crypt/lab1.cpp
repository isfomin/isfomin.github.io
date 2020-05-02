#include "lab1.h"
#include <iostream>
#include <stdlib.h>

using namespace std;

string Lab1::getName() {
    return "Лабораторная 1";
}

string Lab1::getDesc() {
    return "Элиптические кривые";
}

CollectCmds * Lab1::cmds() {
    CollectCmds * cmds = new CollectCmds;
    cmds->add(new DataCmd( "add", "Сложение двух чисел на элиптической кривой" ));
    cmds->add(new DataCmd( "mul", "Умножение двух чисел на элиптической кривой" ));
    cmds->add(new DataCmd( "pow", "Возведение в степень двух чисел на элиптической кривой" ));
    cmds->add(new DataCmd( "size", "" ));
    return cmds;
}

bool Lab1::cmds( std::string & res ) {
    if ( Command::is("add %d %d mod %d") ) {
        cout << (int)(Command::dparam[0] + Command::dparam[1]) % (int)Command::dparam[2];
    } else if ( Command::is("size") ) {
        cout << "int: " << sizeof(int) << endl;
        cout << "uint: " << sizeof(unsigned int) << endl;
        cout << "short: " << sizeof(short) << endl;
        cout << "long: " << sizeof(long) << endl;
        cout << "char: " << sizeof(char) << endl;
    }
    return true;
}
