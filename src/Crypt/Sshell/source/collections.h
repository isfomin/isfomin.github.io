#pragma once
#include "structures.h"
#include "functions.h"
#include <string>
#include <vector>

class CollectCmds
{
public:
    CollectCmds();

    void add(DataCmd * data);
    DataCmd * get(size_t idx);
    size_t size();
    void next(size_t idx);
    DataCmd * next();

private:
    DataCmd * dataCmd[100];
    size_t index_;
    size_t size_;
};
