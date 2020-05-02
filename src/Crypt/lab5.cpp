#include <iostream>
#include "lab5.h"

using namespace std;

string Lab5::getName() {
    return "Лабораторная 5";
}

string Lab5::getDesc() {
    return "Электронная жеребьевка";
}

CollectCmds * Lab5::cmds() {
    CollectCmds * cmds = new CollectCmds;
    cmds->add(new DataCmd( "test5", "test" ));
    return cmds;
}

bool Lab5::cmds( std::string & res ) {
    return true;
}
