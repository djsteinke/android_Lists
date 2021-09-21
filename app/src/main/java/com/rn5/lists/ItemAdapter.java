package com.rn5.lists;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.rn5.lists.model.Group;
import com.rn5.lists.model.Item;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import static com.rn5.lists.MainActivity.black;
import static com.rn5.lists.MainActivity.gray;
import static com.rn5.lists.MainActivity.lists;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private static final String TAG = ItemAdapter.class.getSimpleName();
    private final ChangeListener changeListener;
    private final Context context;
    private final Group group;

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
    ItemAdapter(Context context, Group group, ChangeListener changeListener) {
        this.context = context;
        this.group = group;
        this.changeListener = changeListener;
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
        final Item i = lists.getGroupItems(group).get(p);

        title.setText(i.getTitle());
        String description = i.getDescription();
        if (description == null || description.isEmpty())
            desc.setVisibility(View.GONE);
        else {
            desc.setVisibility(View.VISIBLE);
            desc.setText(description);
        }
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(i.isChecked());

        //vItem.setBackgroundColor(checkBox.isChecked()?gray:white);
        title.setTextColor(checkBox.isChecked()?gray:black);
        if (i.getId() == -1) {
            AppCompatImageView add = vItem.findViewById(R.id.add_image);
            add.setVisibility(View.VISIBLE);
            checkBox.setVisibility(View.INVISIBLE);
            title.setTextColor(context.getColor(R.color.gray_med));
            vItem.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_corner_rectangle));
            vItem.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.white)));
            vItem.setOnClickListener(v -> {
                Alert alert = new Alert(context, changeListener).forItem(this, group, null);
                alert.show();
            });
        } else {
            desc.setTextColor(checkBox.isChecked() ? gray : black);

            checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
                title.setTextColor(b ? gray : black);
                desc.setTextColor(b ? gray : black);
                changeListener.onCheckedChange(i.getId());
                changeListener.onTick();
            });

            vItem.setOnClickListener(v -> {
                Alert alert = new Alert(context, changeListener).forItem(this, group, i);
                alert.show();
            });
        }

    }

    @Override
    public int getItemCount() {
        return lists.getItemCnt(group);
    }
}
