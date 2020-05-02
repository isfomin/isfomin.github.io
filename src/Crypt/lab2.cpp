#include "lab2.h"
#include <iostream>
#include "md5/lib.h"

using namespace std;

string Lab2::getName() {
    return "Лабораторная 2";
}

string Lab2::getDesc() {
    return "Элиптические кривые. Дополнение.";
}

CollectCmds * Lab2::cmds() {
    CollectCmds * cmds = new CollectCmds;
    cmds->add(new DataCmd( "l2-1", "Это очень важная команда" ));
    cmds->add(new DataCmd( "hash", "Test command" ));
    cmds->add(new DataCmd( "l2-3", "Desc3!" ));
    return cmds;
}

bool Lab2::cmds( std::string & res ) {
    if ( Command::is("hash") ) {
        cout << "HASH: " << md5("52") << endl;
    }
    return true;
}
