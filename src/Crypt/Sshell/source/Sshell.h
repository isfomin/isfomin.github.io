#pragma once
#define VERSION "v1.2"
#include <string>
#include <vector>
#include "structures.h"
#include "collections.h"
#include "interfaces.h"

/** Класс предназначен для сравнения введенной команды с шаблоном
*/
class Command
{
public:
    static std::vector<std::string> args;
    static std::string name;
    static std::vector<double> dparam;
    static std::vector<std::string> sparam;
    
    size_t size();
    static bool is(std::string t);

private:
    static bool compareT(std::vector<std::string> strs, std::vector<std::string> t);
    static bool compareTElem(std::string & str, std::string & t);

private:
    std::vector<DataCmd *> data_;
};

/** Класс предназначен для управления входными данными и передачи обработанных данных в соответствующий компонент
*/
class CmdManager
{
public:
    CmdManager();
    
    // Добавляет компонент в коллекцию
    void addCmp(IPgComponent * cmp);
    
    // Возвращяет структуру DataCmp по ID
    DataCmp * getDataCmpById(size_t id);
    
    // Возврящает струтуру DataCmp по имени компонента
    DataCmp * getDataCmpByName(std::string name);
    
    // Возврящает струтуру DataCmp по имени команды компонента
    DataCmp * getDataCmpByCmdName(std::string name);
    
    // Возвращает компонент по его имени
    IPgComponent * getCmpByName(std::string name);
    
    // Возвращает компонент по имени команды которая в нем объявлена
    IPgComponent * getCmpByCmdName(std::string name);
    
    // Удаляет компонент по его имени (будет реализован при необходимости)
    //void removeCmp(std::string name);
    
    // Возващает количество компонентов
    size_t sizeOfCmps();
    
    // Активировать указанный компонент
    void enableCmp(std::string name);
    
    // Деактивировать указанный компонент
    void disableCmp(std::string name);
    
    // Получить список активированных компонентов (будет реализован при необходимости)
    //std::string * getEnabledCmps();
    
    // Получить список деактивированных компонентов (будет реализован при необходимости)
    //std::string * getDisabledCmps();
    
    // true - компонент активен, false - не активен
    bool isEnabledCmp(std::string name);
    
    // Вывести список всех команд и компонентов в которых они находятся
    void printCmds(std::string & res);
    
    // Вывести список всех команд указанного компонента
    void printCmdsOfCmp(DataCmp * cmp, std::string & res);
    
    // Из входной строки получить список аргументов
    void dividedInputLine(std::string);
    
    // Из входной строки получить список аргументов. Аргументы хранятся в переменной класса args_
    void execute(std::string input, std::string & res);
    
    // Передать аргументы в компонент
    bool sendCmd(IPgComponent * cmp, std::string & res);
    
    // Приветственное сообщение
    std::string msgWelcome();
    
private:
    // Вектор для хранения всех информации о компонентах и компандах
    std::vector<DataCmp *> dataCmps_;
    
    // Аргументы полученные из входной строки. Обновляется вызовом метода dividedInputLine
    std::vector<std::string> args_;

};

/** Принимает введенную строку от пользователя, передает ее в CmdManager и выводит результат полученных из менеджера. 
*/
class Sshell
{
public:
    Sshell();
    void listen();
    void addComponent(IPgComponent * cmp);

private:
    size_t sizeOfComponents();

private:
    CmdManager * cmdManager_;
};
