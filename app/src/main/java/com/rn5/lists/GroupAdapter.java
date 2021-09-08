package com.rn5.lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rn5.lists.model.Group;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;
import lombok.Setter;

@Setter
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    private final List<Group> mDataset;
    private final ChangeListener listener;
    private final Context context;

    static class GroupViewHolder extends RecyclerView.ViewHolder {
        View vItem;
        GroupViewHolder(View v) {
            super(v);
            vItem = v;
        }
    }

    GroupAdapter(Context context, ChangeListener listener, ArrayList<Group> mDataset) {
        this.context = context;
        this.listener = listener;
        this.mDataset = mDataset;
    }

    @Override
    @NonNull
    public GroupAdapter.GroupViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_item, parent, false);
        return new GroupAdapter.GroupViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GroupAdapter.GroupViewHolder holder, int position) {

        final int p = holder.getAdapterPosition();
        final View vItem = holder.vItem;
        final TextView title = vItem.findViewById(R.id.group_title);
        Group g = mDataset.get(p);
        String t = g.getName();
        title.setText(t);

        final AppCompatImageButton add = vItem.findViewById(R.id.add_item);
        add.setOnClickListener(v -> {
            // TODO popup
            Alert alert = new Alert(context, listener).forItem(g, null);
            alert.show();
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
