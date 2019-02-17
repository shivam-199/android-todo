package com.gohool.mygrocerylist.mygrocerylist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gohool.mygrocerylist.mygrocerylist.Data.DatabaseHandler;
import com.gohool.mygrocerylist.mygrocerylist.Model.ToDo;
import com.gohool.mygrocerylist.mygrocerylist.R;
import com.gohool.mygrocerylist.mygrocerylist.UI.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<ToDo> todoList;
    private List<ToDo> listItems;
    private DatabaseHandler db;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private EditText todoItem;
    private Button saveButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                createPopDialog();


            }
        });

        db = new DatabaseHandler(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        todoList = new ArrayList<>();
        listItems = new ArrayList<>();

        // Get items from database
        todoList = db.getAllGroceries();

        for (ToDo c : todoList) {
            ToDo grocery = new ToDo();
            grocery.setName(c.getName());
            grocery.setId(c.getId());
            grocery.setDateItemAdded("Added on: " + c.getDateItemAdded());

            listItems.add(grocery);
        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, listItems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void createPopDialog() {

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        todoItem = (EditText) view.findViewById(R.id.todoItem);
        saveButton = (Button) view.findViewById(R.id.saveButton);


        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGroceryToDB(v);
            }
        });



    }

    private void saveGroceryToDB(View v) {

        ToDo grocery = new ToDo();

        String newGrocery = todoItem.getText().toString();

        grocery.setName(newGrocery);

        //Save to DB
        db.addGrocery(grocery);

        Snackbar.make(v, "Item Saved!", Snackbar.LENGTH_LONG).show();

        // Log.d("Item Added ID:", String.valueOf(db.getGroceriesCount()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //start a new activity
                startActivity(new Intent(ListActivity.this, ListActivity.class));
                finish();
            }
        }, 1200); //  1 second.



    }

}
