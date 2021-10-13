package com.rn5.lists;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rn5.lists.enums.ActionType;
import com.rn5.lists.enums.GroupColor;
import com.rn5.lists.enums.ListType;
import com.rn5.lists.model.DragListener;
import com.rn5.lists.model.Group;

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
    public static int[] colorList = new int[8];
    public static int[] colorBackgroundList = new int[8];
    private final DragListener dragListener;
    private final Context context;

    @Getter
    @Setter
    static class GroupViewHolder extends RecyclerView.ViewHolder implements ChangeListener{
        final View vItem;
        final RecyclerView itemRecycler;
        final Context context;
        Group group;
        ItemAdapter itemAdapter;
        boolean expanded = true;
        ChangeListener listener;

        GroupViewHolder(View vItem, Context context) {
            super(vItem);
            this.context = context;
            this.vItem = vItem;
            itemRecycler = vItem.findViewById(R.id.group_recycler);
            itemRecycler.setLayoutManager((new LinearLayoutManager(context)));
        }

        public void initialize(Group group) {
            this.group = group;
            this.expanded = group.isExpanded();
            itemRecycler.setVisibility(expanded ? View.VISIBLE : View.GONE);
            itemAdapter = new ItemAdapter(context, group, this);
            itemRecycler.setAdapter(itemAdapter);
            setInProgCnt();
        }

        public void expand() {
            itemRecycler.setVisibility(expanded ? View.GONE : View.VISIBLE);
            expanded = !expanded;
            lists.expand(group);
        }

        public void setInProgCnt() {
            group = lists.getGroup(group);
            final TextView itemCount = vItem.findViewById(R.id.item_count);
            String val = String.valueOf(group.getInProgCnt());
            itemCount.setText(val);
        }

        @Override
        public void onCheckedChange(long id) {
            Log.d(TAG, "onCheckedChange(" + id + ")");
            int pos = lists.check(group.getName(), id);
            itemAdapter.notifyItemRangeChanged(pos, lists.getGroupItems(group).size()-pos);
            setInProgCnt();
        }

        @Override
        public void onListChange(ListType listType, int pos, ActionType action) {
            Log.d(TAG, "onListChange() " + listType + " " + pos + " " + action);
            if (itemAdapter != null) {
                switch (action) {
                    case INSERT:
                    case DELETE:
                        setInProgCnt();
                        break;
                    case COLOR:
                        GroupColor c = GroupColor.getFromIntValue(pos);
                        vItem.setBackgroundTintList(ColorStateList.valueOf(context.getColor(c.getGroupColor())));
                        lists.setColor(group, c);
                        Log.d(TAG, "GroupColor[" + c + "]");
                        break;
                    case UPDATE:
                    default:
                        break;
                }
            }
        }

        @Override
        public void onTick() {
            if (listener != null)
                listener.onTick();
        }
    }

    GroupAdapter(Context context, DragListener dragListener) {
        this.context = context;
        this.dragListener = dragListener;

        GroupAdapter.colorList = context.getResources().getIntArray(R.array.color_list);
        GroupAdapter.colorBackgroundList = context.getResources().getIntArray(R.array.background_colors);
    }

    @Override
    @NonNull
    public GroupAdapter.GroupViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_item, parent, false);
        return new GroupAdapter.GroupViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(GroupAdapter.GroupViewHolder holder, int position) {
        final int p = holder.getAdapterPosition();
        final View vItem = holder.vItem;
        final TextView title = vItem.findViewById(R.id.group_title);
        Group g = lists.getGroups().get(p);
        holder.initialize(g);
        String t = g.getName();
        title.setText(t);
        vItem.setBackgroundTintList(ColorStateList.valueOf(context.getColor(g.getColor().getGroupColor())));

        final AppCompatImageButton edit = vItem.findViewById(R.id.edit_group);
        edit.setOnClickListener(v -> {
            Alert alert = new Alert(context, holder).forGroup(this, g);
            alert.show();
        });

        AppCompatImageButton drag = vItem.findViewById(R.id.drag);
        drag.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dragListener.requestDrag(holder);
                return true;
            } else {
                return false;
            }
        });

        final AppCompatImageButton upDown = vItem.findViewById(R.id.up_down);
        upDown.setBackground(ContextCompat.getDrawable(context, getSrc(holder.expanded)));

        title.setOnClickListener(v -> {
            holder.expand();
            upDown.setBackground(ContextCompat.getDrawable(context, getSrc(holder.expanded)));
        });

        upDown.setOnClickListener(v -> {
            holder.expand();
            upDown.setBackground(ContextCompat.getDrawable(context, getSrc(holder.expanded)));
        });
    }

    public int getSrc(boolean expanded) {
        return (expanded ? R.drawable.ic_baseline_close_24 :R.drawable.ic_baseline_keyboard_arrow_down_24);
    }

    @Override
    public int getItemCount() {
        return lists.getGroups().size();
    }
}
