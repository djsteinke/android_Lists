package com.rn5.lists;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rn5.lists.enums.ActionType;
import com.rn5.lists.enums.ListType;
import com.rn5.lists.model.DragListener;
import com.rn5.lists.model.Group;
import com.rn5.lists.model.Item;
import com.rn5.lists.model.Lists;
import com.rn5.lists.model.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import lombok.SneakyThrows;

import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;

public class MainActivity extends AppCompatActivity implements ChangeListener, DragListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public GroupAdapter groupAdapter;
    public static Lists lists;
    public static Settings settings;
    private Vibrator vibrator;
    public static LoadListener loadListener;

    public static int black;
    public static int gray;
    public static int white;
    public static File filePathApp;

    private ItemTouchHelper groupTouchHelper;

    @SneakyThrows
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            /*
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();

             */
            Alert alert = new Alert(this, this).forGroup(groupAdapter, null);
            alert.show();
        });

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        filePathApp = this.getExternalFilesDir("Lists");
        // specify an adapter (see also next example)

        settings = Settings.load();
        lists = Lists.load();
        fixLists();

        gray = getColor(R.color.gray);
        white = getColor(R.color.white);
        black = getColor(R.color.black);

        RecyclerView groupRecycler = findViewById(R.id.recyclerView);
        groupRecycler.setLayoutManager(new LinearLayoutManager(this));
        groupAdapter = new GroupAdapter(this, this);
        groupRecycler.setAdapter(groupAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                lists.swapGroups(fromPosition, toPosition);
                groupAdapter.notifyItemMoved(fromPosition, toPosition);
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            }
            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }
        };
        groupTouchHelper = new ItemTouchHelper(simpleCallback);
        groupTouchHelper.attachToRecyclerView(groupRecycler);
    }


    private void fixLists() {
        for (Group g : lists.getGroups()) {
            Item addItem = new Item(-1).withTitle("Add new item");
            if (!g.getItems().contains(addItem))
                g.add(addItem);
        }
        lists.save();
    }

    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        groupTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onCheckedChange(long id) {
        //orderList(position);
    }

    @Override
    public void onListChange(ListType listType, int pos, ActionType action) {
        Log.d(TAG, "onAdd()" + listType);
        switch (listType) {
            case GROUP:
                switch (action) {
                    case INSERT:
                        groupAdapter.notifyItemInserted(pos);
                        break;
                    case UPDATE:
                        groupAdapter.notifyItemChanged(pos);
                        break;
                    case DELETE:
                        groupAdapter.notifyItemRemoved(pos);
                        break;
                    default:
                        break;
                }
            case ITEM:
            default:
                break;
        }
    }

    @Override
    public void onTick() {
        if (vibrator != null)
            vibrator.vibrate(3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_group) {
            Alert alert = new Alert(this, this).forGroup(groupAdapter, null);
            alert.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
