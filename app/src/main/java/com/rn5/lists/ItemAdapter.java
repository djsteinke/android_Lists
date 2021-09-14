package com.rn5.lists;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.rn5.lists.model.Item;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.rn5.lists.MainActivity.black;
import static com.rn5.lists.MainActivity.gray;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private final List<Item> mDataset;
    private final ChangeListener changeListener;
    private final String groupName;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        View vItem;
        ItemViewHolder(View v) {
            super(v);
            vItem = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    ItemAdapter(List<Item> myDataset, ChangeListener changeListener, String groupName) {
        mDataset = myDataset;
        this.groupName = groupName;
        this.changeListener = changeListener;
    }

    public void clear() {
        int size = mDataset.size();
        mDataset.clear();
        notifyItemRangeRemoved(0, size);
    }

    // Create new views (invoked by the layout manager)
    @Override
    @NonNull
    public ItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ItemAdapter.ItemViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ItemAdapter.ItemViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final int p = holder.getAdapterPosition();
        final View vItem = holder.vItem;
        final TextView title = vItem.findViewById(R.id.title);
        final TextView desc = vItem.findViewById(R.id.description);
        CheckBox checkBox = vItem.findViewById(R.id.checkbox);

        title.setText(mDataset.get(p).getTitle());
        String description = mDataset.get(p).getDescription();
        if (description == null || description.isEmpty())
            desc.setVisibility(View.GONE);
        else {
            desc.setVisibility(View.VISIBLE);
            desc.setText(description);
        }
        checkBox.setChecked(mDataset.get(p).isChecked());

        //vItem.setBackgroundColor(checkBox.isChecked()?gray:white);
        title.setTextColor(checkBox.isChecked()?gray:black);
        desc.setTextColor(checkBox.isChecked()?gray:black);

        checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            //listItems.get(p).setChecked(b);
            title.setTextColor(b?gray:black);
            desc.setTextColor(b?gray:black);
            changeListener.onTick();
            changeListener.onCheckedChange(p);
        });

        vItem.setOnLongClickListener(view -> {
            //listItems.remove(p);
            //notifyDataSetChanged();
            changeListener.onTick();
            return false;
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
