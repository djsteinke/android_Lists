package com.rn5.lists.model;

import androidx.recyclerview.widget.RecyclerView;

public interface DragListener {
    void requestDrag(RecyclerView.ViewHolder viewHolder);
}
