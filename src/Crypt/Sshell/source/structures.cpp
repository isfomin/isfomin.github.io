#include "structures.h"
#include <iostream>

// Args
void Args::show() {
    for (size_t i = 0; i < size; ++i)
        std::cout << items[i] << std::endl;
}

// DataCmd
DataCmd::DataCmd( std::string name, std::string desc )
        : name(name), desc(desc)
{ }
