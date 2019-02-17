package com.gohool.mygrocerylist.mygrocerylist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.gohool.mygrocerylist.mygrocerylist.Data.DatabaseHandler;
import com.gohool.mygrocerylist.mygrocerylist.Model.ToDo;
import com.gohool.mygrocerylist.mygrocerylist.R;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText todoItem;
    private Button saveButton;
    private DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db = new DatabaseHandler(this);

        byPassActivity();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                createPopupDialog();
            }
        });
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


    private void createPopupDialog() {

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

                //Todo: Save to db
                //Todo: Go to next screen

                if (!todoItem.getText().toString().isEmpty())
                         {
                    saveGroceryToDB(v);
                }

            }
        });


    }

    private void saveGroceryToDB(View v) {

        ToDo todo = new ToDo();

        String newGrocery = todoItem.getText().toString();


        todo.setName(newGrocery);

        //Save to DB
        db.addGrocery(todo);

        Snackbar.make(v, "Item Saved!", Snackbar.LENGTH_LONG).show();

       // Log.d("Item Added ID:", String.valueOf(db.getGroceriesCount()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //start a new activity
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        }, 1200); //  1 second.

    }


    public void byPassActivity() {
        //Checks if database is empty; if not, then we just
        //go to ListActivity and show all added items

        if (db.getToDoCount() > 0) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }

    }
}
