#include <string>
#include "Sshell.h"

class Lab1 : public IPgComponent
{
public:
    std::string getName();
    std::string getDesc();
    CollectCmds * cmds();
    bool cmds( std::string & res );
    std::string test();
};
