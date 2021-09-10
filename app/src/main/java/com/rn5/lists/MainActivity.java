package com.rn5.lists;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.rn5.lists.model.Group;
import com.rn5.lists.model.Lists;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import lombok.SneakyThrows;

import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public GroupAdapter groupAdapter;
    public static Lists lists;
    private Vibrator vibrator;
    private RecyclerView groupRecycler;
    public static LoadListener loadListener;
    private ArrayList<Group> groups;

    public static int black;
    public static int gray;
    public static int white;
    public static File filePathApp;

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
            Alert alert = new Alert(this, this).forGroup(null);
            alert.show();
        });

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        filePathApp = this.getExternalFilesDir("Lists");
        // specify an adapter (see also next example)

        lists = Lists.load();
        groups = lists.getGroups();

        gray = getResources().getColor(R.color.gray);
        white = getResources().getColor(R.color.white);
        black = getResources().getColor(R.color.black);

        groupRecycler = findViewById(R.id.recyclerView);
        groupRecycler.setHasFixedSize(true);
        groupRecycler.setLayoutManager(new LinearLayoutManager(this));
        groupAdapter = new GroupAdapter(this, this, groups);
        groupRecycler.setAdapter(groupAdapter);
    }

    @Override
    public void onLoadComplete() {

    }

    @Override
    public void onCheckedChange(int position) {
        //orderList(position);
    }

    @Override
    public void onAdd(ListType listType, int pos) {
        Log.d(TAG, "onAdd()" + listType);
        switch (listType) {
            case ITEM:
                break;
            case GROUP:
                boolean insert = (groups.size() == ++pos);
                Log.d(TAG, "onAdd()" + groups + " pos[" + pos + "]");
                if (insert)
                    groupAdapter.notifyItemInserted(--pos);
                else
                    groupAdapter.notifyItemChanged(pos);
                break;
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
