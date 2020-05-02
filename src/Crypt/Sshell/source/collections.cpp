#include "collections.h"
#include <cctype>

// CollectCmds
CollectCmds::CollectCmds() 
    : index_(0), size_(0) 
{}

void CollectCmds::add(DataCmd * data) {
    dataCmd[index_++] = data;
    size_++;
}

DataCmd * CollectCmds::get(size_t idx) {
    if (idx >= size_ || idx < 0)
        return 0;
    return dataCmd[idx];
}

size_t CollectCmds::size() {
    return size_;
}

void CollectCmds::next(size_t idx) {
    index_ = idx;
}

DataCmd * CollectCmds::next() {
    if (index_ == size())
        return 0;
    return get(index_++);
}
