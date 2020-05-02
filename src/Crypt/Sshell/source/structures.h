#pragma once
#include <string>

class IPgComponent;
class CollectCmds;

// Хранит аргументы введенные рользователем
struct Args
{
    int size;
    std::string * items;
    
    // Для тестирования, выводит все аргументы
    void show();
};

// Хранит информацию о команде
struct DataCmd
{
    DataCmd( std::string name, std::string desc );
    std::string name;
    std::string desc;
};

// Хранит информацию о компоненте
struct DataCmp
{
    // Название компонента
    std::string name;
    
    // Описания компонента
    std::string desc;
    
    // Активность компонента
    bool enabled;
    
    // Коллекция команд компонента
    CollectCmds * cmds;
    
    // Указатель на компонент
    IPgComponent * cmp;
};
