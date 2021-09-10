package com.rn5.lists;

public interface ChangeListener {
    void onCheckedChange(int position);
    void onAdd(ListType listType, int pos);
    void onTick();
    void onLoadComplete();
}
