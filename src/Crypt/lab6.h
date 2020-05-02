#include <string>
#include "Sshell/lib.h"

class Lab6 : public IPgComponent
{
public:
    std::string getName();
    std::string getDesc();
    CollectCmds * cmds();
    bool cmds( std::string & res );
};
