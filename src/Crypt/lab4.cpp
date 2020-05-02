#include <iostream>
#include "lab4.h"
#include "GeneratePrime/Prime.h"

using namespace std;

string Lab4::getName() {
    return "Лабораторная 4";
}

string Lab4::getDesc() {
    return "Процедура генерации простых чисел ГОСТ Р 34.10-94";
}

CollectCmds * Lab4::cmds() {
    CollectCmds * cmds = new CollectCmds;
    cmds->add(new DataCmd( "gen", "Сгенерировать простое число указанной длинны" ));
    return cmds;
}

bool Lab4::cmds( std::string & res ) {
    if (Command::is("gen %d")) {
        Prime prime;
        cout << "Простое число " << prime.get( Command::dparam[0] ) << endl;
        return true;
    }
    return false;
}
