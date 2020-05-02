#include <iostream>
#include "lab8.h"

using namespace std;

string Lab8::getName() {
    return "Лабораторная 8";
}

string Lab8::getDesc() {
    return "Протоколы идентификации с нулевым разглашением";
}

CollectCmds * Lab8::cmds() {
    CollectCmds * cmds = new CollectCmds;
    cmds->add(new DataCmd( "test8", "test" ));
    return cmds;
}

bool Lab8::cmds( std::string & res ) {
    return true;
}
