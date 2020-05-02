#include <iostream>
#include "lab1.h"

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
    return cmds;
}

bool Lab1::cmds( std::string & res ) {
    if (Command::is("pow %p %p")) {
        
    } else if (Command::is("pow %p %p mod %p")) {
        
    } else if (Command::is("add %d %d")) {
        cout << Command::dparam[0] + Command::dparam[1];
    } else {
        res = "Command '" + Command::name + "' contain another attributes";
    }
    return true;
}

std::string Lab1::test() {
    return "Function test";
}
