package com.rn5.lists;

import com.rn5.lists.enums.ActionType;
import com.rn5.lists.enums.ListType;

public interface ChangeListener {
    void onCheckedChange(long id);
    void onListChange(ListType listType, int pos, ActionType action);
    void onTick();
}
