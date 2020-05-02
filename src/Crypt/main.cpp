#include "Sshell/lib.h"
#include "lab1.h"
#include "lab2.h"
#include "lab3.h"
#include "lab4.h"
#include "lab5.h"
#include "lab6.h"
#include "lab7.h"
#include "lab8.h"
#include "lab9.h"

int main()
{
    Sshell sh;
    //sh.addComponent(new Lab1);
    //sh.addComponent(new Lab2);
    sh.addComponent(new Lab3);
    sh.addComponent(new Lab4);
    //sh.addComponent(new Lab5);
    //sh.addComponent(new Lab6);
    //sh.addComponent(new Lab7);
    //sh.addComponent(new Lab8);
    //sh.addComponent(new Lab9);
    sh.listen();
    return 0;
}
