#include <iostream>
#include "lab9.h"

using namespace std;

string Lab9::getName() {
    return "Лабораторная 9";
}

string Lab9::getDesc() {
    return "Управление ключами";
}

CollectCmds * Lab9::cmds() {
    CollectCmds * cmds = new CollectCmds;
    cmds->add(new DataCmd( "test9", "test" ));
    return cmds;
}

bool Lab9::cmds( std::string & res ) {
    return true;
}
