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
import androidx.core.content.ContextCompat;
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
        boolean expanded = true;
        GroupViewHolder(View v) {
            super(v);
            vItem = v;
        }

        public void expand() {
            itemRecycler.setVisibility(expanded ? View.GONE : View.VISIBLE);
            expanded = !expanded;
            lists.expand(group);
        }

        public void setInProgCnt() {
            final TextView itemCount = vItem.findViewById(R.id.item_count);
            String val = String.valueOf(group.getInProgCnt());
            itemCount.setText(val);
        }

        public void setItemRecycler(RecyclerView recyclerView) {
            this.itemRecycler = recyclerView;
            this.itemRecycler.setVisibility(expanded ? View.VISIBLE : View.GONE);
        }

        public void setGroup(Group g) {
            this.group = g;
            this.expanded = g.isExpanded();
        }

        @Override
        public void onCheckedChange(int position) {
            Log.d(TAG, "onCheckedChange(" + position + ")");
            lists.check(group.getName(), position);
            setInProgCnt();
        }

        @Override
        public void onAdd(ListType listType, int pos, boolean insert) {
            Log.d(TAG, "onAdd");
            group = lists.getGroups().get(this.getAdapterPosition());
            Log.d(TAG, "onAdd() Group[" + group.toString() + "]");
            if (itemAdapter != null) {
                if (insert)
                    itemAdapter.notifyItemInserted(pos);
                else
                    itemAdapter.notifyItemChanged(pos);
            }
                //itemAdapter.notifyDataSetChanged();
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

        holder.setInProgCnt();
        holder.setItemRecycler(vItem.findViewById(R.id.group_recycler));
        holder.getItemRecycler().setHasFixedSize(true);
        holder.getItemRecycler().setLayoutManager((new LinearLayoutManager(context)));
        holder.setItemAdapter(new ItemAdapter(holder.getGroup().getItems(), holder, g.getName()));
        holder.getItemRecycler().setAdapter(holder.getItemAdapter());

        final AppCompatImageButton add = vItem.findViewById(R.id.add_item);
        add.setOnClickListener(v -> {
            // TODO popup
            Alert alert = new Alert(context, holder).forItem(g, null);
            alert.show();
        });

        final AppCompatImageButton upDown = vItem.findViewById(R.id.up_down);
        upDown.setImageDrawable(ContextCompat.getDrawable(context, getSrc(holder.expanded)));
        upDown.setOnClickListener(v -> {
            holder.expand();
            upDown.setImageDrawable(ContextCompat.getDrawable(context, getSrc(holder.expanded)));
        });
    }

    public int getSrc(boolean expanded) {
        return (expanded ? R.drawable.ic_baseline_keyboard_arrow_up_24 :R.drawable.ic_baseline_keyboard_arrow_down_24);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
