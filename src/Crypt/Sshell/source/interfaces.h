#pragma once
#include "structures.h"
#include "collections.h"

// Интерфейс содержит метода для работы с командами. Этот тип реализуют класс для которых требуется управление командами
class IPgComponent
{
public:
    // Возвращает название компонента. Это название является идентификатором компонента и используется в классе CmpManager
    virtual std::string getName() = 0;
    
    // Возвращает описание компонента
    virtual std::string getDesc() = 0;
    
    // Вызвращает коллекцию команд
    virtual CollectCmds * cmds() = 0;
    
    // Метод принимает аргументы введенные пользователем, обрабатывает их возвращает результат
    virtual bool cmds( std::string & res ) = 0;
    //virtual ~IPgComponent() = 0;
};
