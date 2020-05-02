#include "Sshell.h"
#include "lab1.h"

int main()
{
    Sshell sh;
    sh.addComponent(new Lab1);
    sh.listen();
    return 0;
}
