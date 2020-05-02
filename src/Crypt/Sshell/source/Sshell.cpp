#include <iostream>
#include <vector>
#include <stdlib.h>
#include "Sshell.h"
#include "functions.h"

using namespace std;

/*************** CmdManager ***************/
CmdManager::CmdManager()
{ }

void CmdManager::addCmp(IPgComponent * cmp) {
    DataCmp * data = new DataCmp;
    data->name = cmp->getName();
    data->desc = cmp->getDesc();
    data->enabled = true;
    data->cmds = cmp->cmds();
    data->cmp = cmp;
    
    dataCmps_.push_back(data);
}

DataCmp * CmdManager::getDataCmpById(size_t id) {
    return dataCmps_[id];
}

DataCmp * CmdManager::getDataCmpByName(std::string name) {
    for (size_t i = 0; i < dataCmps_.size(); ++i)
        if (name == dataCmps_[i]->name)
            return dataCmps_[i];
    return 0;
}

DataCmp * CmdManager::getDataCmpByCmdName(std::string name) {
    for (size_t i = 0; i < dataCmps_.size(); ++i) {
        CollectCmds * cmds = getDataCmpById(i)->cmds;
        cmds->next(0);
        
        for (DataCmd * dataCmd = cmds->next(); dataCmd != 0; dataCmd = cmds->next()) {
            if (dataCmd->name == name)
                return getDataCmpById(i);
        }
    }
    return 0;
}

IPgComponent * CmdManager::getCmpByName(std::string name) {
    return getDataCmpByName(name)->cmp;
}

IPgComponent * CmdManager::getCmpByCmdName(std::string name) {
    return getDataCmpByCmdName(name)->cmp;
}

size_t CmdManager::sizeOfCmps() {
    return dataCmps_.size();
}

void CmdManager::enableCmp(std::string name) {
    getDataCmpByName(name)->enabled = true;
}

void CmdManager::disableCmp(std::string name) {
    getDataCmpByName(name)->enabled = true;
}

bool CmdManager::isEnabledCmp(std::string name) {
    return getDataCmpByName(name)->enabled;
}

void CmdManager::printCmds(std::string & res) {
    size_t size = sizeOfCmps();

    for (size_t i = 0; i < size; ++i) {
        DataCmp * cmp = getDataCmpById(i);
        if (!cmp->enabled) break;
        
        res += cmp->name + "\n";
        res += cmp->desc + "\n";
        res += "Commands: \n";
        printCmdsOfCmp(cmp, res);
        if ( (i + 1) != size )
            res += "\n";
    }
}

void CmdManager::printCmdsOfCmp(DataCmp * cmp, std::string & res) {
    std::string text = "";
    cmp->cmds->next(0);
    for (DataCmd * cmd = cmp->cmds->next(); cmd != 0; cmd = cmp->cmds->next()) {
        text += "\t" + cmd->name + " - " + cmd->desc + "\n";
    }
    res += text;
}

void CmdManager::execute(std::string input, std::string & res) {
    args_.clear();
    dividedInputLine(input);
    Command::args = args_;
    Command::name = args_[0];
    DataCmp * dataCmp = getDataCmpByCmdName( Command::name );
    
    if (Command::is("help") || Command::is("?")) {
        printCmds(res);
        return;
    }
        
    if (dataCmp == 0) {
        res = "Ошибка. Команда '" + Command::name + "' не найдена";
        return;
    }
    
    if ( !sendCmd(dataCmp->cmp, res) )
        res = "Ошибка. Команда '" + Command::name + "' не содержит введенные аргументы";
}

void CmdManager::dividedInputLine(std::string input) {
    split(input, ' ', args_);
}

bool CmdManager::sendCmd(IPgComponent * cmp, std::string & res) {
    cmp->cmds(res);
}

string CmdManager::msgWelcome() {
    return "\nSimple Shell " VERSION "\nДля просмотра досупных команд введите ? или help";
}





/*************** Sshell ***************/
Sshell::Sshell()
    : cmdManager_(new CmdManager)
{ }

void Sshell::listen() {
    cout << cmdManager_->msgWelcome() << endl;
    while (true) {
        string line, res = "";
        cout << "# ";
        getline(cin, line);
        trim(line);
        if (line == "") continue;
        cmdManager_->execute(line, res);
        cout << res + "\n";
    }
}

void Sshell::addComponent(IPgComponent * cmp) {
    cmdManager_->addCmp(cmp);
}






/*************** Command ***************/
std::vector<std::string> Command::args;
std::string Command::name;
std::vector<double> Command::dparam;
std::vector<std::string> Command::sparam;

bool Command::is(std::string t) {
    std::vector<std::string> t_args;
    split(t, ' ', t_args);
    return compareT(Command::args, t_args);
}

bool Command::compareT(std::vector<std::string> strs, std::vector<std::string> t) {
    dparam.clear();
    sparam.clear();
    if (strs.size() != t.size())
        return false;
        
    for (size_t i = 0; i < strs.size(); ++i) {
        if (!compareTElem(strs[i], t[i]))
            return false;
    }
    return true;
}

bool Command::compareTElem(std::string & str, std::string & t) {
    if (t == "%d") {
        for (size_t i = 0; i < str.length(); ++i)
            if (!isdigit((int)str[i]))
                return false;
        dparam.push_back( atoi(str.c_str()) );
        return true;
    } else if (t == "%s") {
        sparam.push_back( str );
        return true;
    } else
        return str == t;
}
