#include <iostream>
#include "lab6.h"

using namespace std;

string Lab6::getName() {
    return "Лабораторная 6";
}

string Lab6::getDesc() {
    return "Протоколы разделения секрета";
}

CollectCmds * Lab6::cmds() {
    CollectCmds * cmds = new CollectCmds;
    cmds->add(new DataCmd( "test6", "test" ));
    return cmds;
}

bool Lab6::cmds( std::string & res ) {
    return true;
}
