package com.rn5.lists;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rn5.lists.model.Group;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import lombok.Getter;
import lombok.Setter;

import static com.rn5.lists.MainActivity.lists;

@Setter
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    private static final String TAG = GroupAdapter.class.getSimpleName();
    private final List<Group> mDataset;
    private final ChangeListener listener;
    private final Context context;

    @Getter
    @Setter
    static class GroupViewHolder extends RecyclerView.ViewHolder implements ChangeListener {
        View vItem;
        RecyclerView itemRecycler;
        ItemAdapter itemAdapter;
        Group group;
        GroupViewHolder(View v) {
            super(v);
            vItem = v;
        }

        @Override
        public void onCheckedChange(int position) {

        }

        @Override
        public void onAdd(ListType listType, int pos) {
            Log.d(TAG, "onAdd");
            group = lists.getGroups().get(this.getAdapterPosition());
            Log.d(TAG, "onAdd() Group[" + group.toString() + "]");
            if (itemAdapter != null)
                itemAdapter.notifyDataSetChanged();
        }

        @Override
        public void onTick() {

        }

        @Override
        public void onLoadComplete() {

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
        holder.setGroup(g);
        String t = g.getName();
        title.setText(t);

        holder.setItemRecycler(vItem.findViewById(R.id.group_recycler));
        holder.getItemRecycler().setHasFixedSize(true);
        holder.getItemRecycler().setLayoutManager((new LinearLayoutManager(context)));
        holder.setItemAdapter(new ItemAdapter(holder.getGroup().getItems(), listener, g.getName()));
        holder.getItemRecycler().setAdapter(holder.getItemAdapter());

        final AppCompatImageButton add = vItem.findViewById(R.id.add_item);
        add.setOnClickListener(v -> {
            // TODO popup
            Alert alert = new Alert(context, holder).forItem(g, null);
            alert.show();
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
