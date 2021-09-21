package com.rn5.lists;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import com.rn5.lists.enums.ActionType;
import com.rn5.lists.enums.ListType;
import com.rn5.lists.model.Group;
import com.rn5.lists.model.Item;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageButton;

import static com.rn5.lists.MainActivity.lists;

public class Alert {
    private static final String TAG = Alert.class.getSimpleName();
    private final Context context;
    private final ChangeListener listener;
    private Group group;
    private Item item;
    private ListType listType;
    private boolean isEdit;
    private ItemAdapter itemAdapter;
    private GroupAdapter groupAdapter;

    public Alert(Context context, ChangeListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public Alert forGroup(GroupAdapter groupAdapter, @Nullable Group group) {
        if (group != null)
            this.group = lists.getGroup(group);
        this.groupAdapter = groupAdapter;
        listType = ListType.GROUP;
        isEdit = (group != null);
        return this;
    }

    public Alert forColor() {
        return this;
    }

    public Alert forItem(ItemAdapter itemAdapter, Group group, @Nullable Item item) {
        this.itemAdapter = itemAdapter;
        this.item = item;
        this.group = lists.getGroup(group);
        listType = ListType.ITEM;
        isEdit = (item != null);
        return this;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);

        final View view = inflater.inflate(R.layout.edit_group_popup, null);
        final TextView desc = view.findViewById(R.id.tv_description);
        final TextView title = view.findViewById(R.id.tv_title);
        final EditText editDesc = view.findViewById(R.id.edit_desc);
        final EditText editTitle = view.findViewById(R.id.edit_title);
        final GridLayout grid = view.findViewById(R.id.grid_view);

        switch (listType) {
            case ITEM:
                if (isEdit) {
                    editDesc.setText(item.getDescription());
                    editTitle.setText(item.getTitle());
                }
                if (grid != null)
                    grid.setVisibility(View.GONE);
                break;
            case GROUP:
                title.setText(context.getString(R.string.name));
                if (isEdit) {
                    editTitle.setText(group.getName());
                }
                desc.setVisibility(View.GONE);
                editDesc.setVisibility(View.GONE);

                int pos = (group != null ? group.getColor().getIntValue() : 7);
                if (grid != null) {
                    Log.d(TAG, "Grid found.");
                    for (int i = 0; i < 8; i++) {
                        AppCompatImageButton button = (AppCompatImageButton) grid.getChildAt(i);
                        if (i == pos)
                            button.setSelected(true);
                        button.setOnClickListener(colorClick(button, grid));
                    }
                }
                break;
            default:
                return;
        }

        builder.setView(view)
                .setPositiveButton(R.string.save, (dialog, id) -> {
                    String t = editTitle.getText().toString();
                    int pos = -1;
                    ActionType actionType = (isEdit ? ActionType.UPDATE : ActionType.INSERT);
                    if (t != null && !t.isEmpty())
                    switch (listType) {
                        case ITEM:
                            String d = editDesc.getText().toString();
                            if (item != null) {
                                item.setTitle(t);
                                item.setDescription(d);
                                pos = lists.add(item, group);
                                itemAdapter.notifyItemChanged(pos);
                            } else {
                                pos = lists.add(new Item().withTitle(t).withDescription(d), group);
                                itemAdapter.notifyItemInserted(pos);
                                listener.onListChange(listType, pos, ActionType.INSERT);
                            }
                            break;
                        case GROUP:
                            if (group != null) {
                                group.setName(t);
                                pos = lists.add(group);
                                groupAdapter.notifyItemChanged(pos);
                            } else {
                                pos = lists.add(new Group(t));
                                groupAdapter.notifyItemInserted(pos);
                            }
                            if (grid != null) {
                                for (int i = 0; i < 8; i++) {
                                    View button = grid.getChildAt(i);
                                    if (button.isSelected()) {
                                        listener.onListChange(ListType.GROUP, i, ActionType.COLOR);
                                        break;
                                    }
                                }
                            }
                            break;
                        default:
                            break;
                    }
                    if (pos != -1) {
                        //listener.onListChange(listType, pos, actionType);
                        lists.save();
                    }
                    listener.onTick();
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    listener.onTick();
                });
        if (isEdit) {
            builder.setNeutralButton(R.string.delete, (dialog, id) -> {
                int pos;
                if (listType == ListType.GROUP) {
                    pos = lists.remove(group);
                    groupAdapter.notifyItemRemoved(pos);
                } else {
                    pos = lists.remove(item, group);
                    itemAdapter.notifyItemRemoved(pos);
                    listener.onListChange(listType, pos, ActionType.DELETE);
                }
                if (pos != -1) {
                    //listener.onListChange(listType, pos, ActionType.DELETE);
                    lists.save();
                }
                listener.onTick();
            });
        }

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showColor(int color) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);

        int pos = -1;
        for (int c : GroupAdapter.colorBackgroundList) {
            if (c == color) {
                pos++;
                break;
            }
            pos ++;
        }

        final View view = inflater.inflate(R.layout.color_alert, null);
        final GridLayout grid = view.findViewById(R.id.grid_view);
        for (int i=0; i<8; i++) {
            AppCompatImageButton button = (AppCompatImageButton) grid.getChildAt(i);
            if (i == pos)
                button.setSelected(true);
            button.setOnClickListener(colorClick(button, grid));
        }

        builder.setView(view)
                .setPositiveButton(R.string.save, (dialog, id) -> {
                    // TODO on color selected
                    for (int i=0; i<8; i++) {
                        View button = grid.getChildAt(i);
                        if (button.isSelected()) {
                            listener.onListChange(ListType.GROUP, i, ActionType.COLOR);
                            break;
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    // TODO tick()
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private View.OnClickListener colorClick(final AppCompatImageButton button, final GridLayout grid) {
        return v -> {
            if (!button.isSelected()) {
                for (int i=0; i<8; i++) {
                    AppCompatImageButton b = (AppCompatImageButton) grid.getChildAt(i);
                    b.setSelected(b.equals(button));
                }
            }
        };
    }
}
