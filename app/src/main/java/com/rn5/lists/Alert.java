package com.rn5.lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rn5.lists.model.Group;
import com.rn5.lists.model.Item;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import static com.rn5.lists.MainActivity.lists;

public class Alert {
    private final Context context;
    private final ChangeListener listener;
    private Group group;
    private Item item;
    private ListType listType;

    public Alert(Context context, ChangeListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public Alert forGroup(@Nullable Group group) {
        this.group = group;
        listType = ListType.GROUP;
        return this;
    }

    public Alert forItem(Group group, @Nullable Item item) {
        this.item = item;
        this.group = group;
        listType = ListType.ITEM;
        return this;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);

        final View view = inflater.inflate(R.layout.add_alert, null);
        final TextView desc = view.findViewById(R.id.tv_description);
        final TextView title = view.findViewById(R.id.tv_title);
        final EditText editDesc = view.findViewById(R.id.edit_desc);
        final EditText editTitle = view.findViewById(R.id.edit_title);

        switch (listType) {
            case ITEM:
                if (item != null) {
                    editDesc.setText(item.getDescription());
                    editTitle.setText(item.getTitle());
                }
                break;
            case GROUP:
                title.setText(context.getString(R.string.name));
                if (group != null) {
                    editTitle.setText(group.getName());
                }
                desc.setVisibility(View.GONE);
                editDesc.setVisibility(View.GONE);
                break;
            default:
                return;
        }

        builder.setView(view)
                .setPositiveButton(R.string.save, (dialog, id) -> {
                    String t = editTitle.getText().toString();
                    int pos = -1;
                    boolean insert = false;
                    if (t != null && !t.isEmpty())
                    switch (listType) {
                        case ITEM:
                            String d = editDesc.getText().toString();
                            if (item != null) {
                                item.setTitle(t);
                                item.setDescription(d);
                                pos = lists.add(item, group.getName());
                            } else {
                                pos = lists.add(new Item().withTitle(t).withDescription(d), group.getName());
                                insert = true;
                            }
                            break;
                        case GROUP:
                            if (group != null) {
                                group.setName(t);
                                pos = lists.add(group);
                            } else {
                                pos = lists.add(new Group(t));
                                insert = true;
                            }
                            break;
                        default:
                            break;
                    }
                    if (pos != -1) {
                        listener.onAdd(listType, pos, insert);
                        lists.save();
                    }
                    listener.onTick();
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    listener.onTick();
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
