#include <iostream>
#include "lab7.h"

using namespace std;

string Lab7::getName() {
    return "Лабораторная 7";
}

string Lab7::getDesc() {
    return "Протокол честной раздачи карт";
}

CollectCmds * Lab7::cmds() {
    CollectCmds * cmds = new CollectCmds;
    cmds->add(new DataCmd( "test7", "test" ));
    return cmds;
}

bool Lab7::cmds( std::string & res ) {
    return true;
}
