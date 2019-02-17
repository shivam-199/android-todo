package com.example.finaltodo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<ToDo> listItems;
    private List<ToDo> todoList;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private EditText todoItem;
    private Button saveButton;
    DatabaseReference databaseItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        databaseItems = FirebaseDatabase.getInstance().getReference("ToDo");


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                createPopDialog();

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        todoList = new ArrayList<>();
        final FirebaseUser user = mAuth.getCurrentUser();
        final String Uid = user.getUid();
        databaseItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                todoList.clear();
                for(DataSnapshot listSnapshot : dataSnapshot.child(Uid).getChildren()){
                    ToDo list = listSnapshot.getValue(ToDo.class);

                    todoList.add(list);
                }

                recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(),todoList);
                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createPopDialog() {

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup,null);
        todoItem = (EditText)  view.findViewById(R.id.todoItem);
        saveButton = (Button) view.findViewById(R.id.saveButton);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItemToDB(view);
            }
        });
    }

    private void saveItemToDB(View view) {

        final FirebaseUser user = mAuth.getCurrentUser();
        final String Uid = user.getUid();


        String newItem = todoItem.getText().toString().trim();



        // Save the item to firebase db

        if(!TextUtils.isEmpty(newItem)){
            String id =  databaseItems.push().getKey();
            ToDo todo = new ToDo(newItem,getTime(),id);
            assert id != null;
            databaseItems.child(Uid).child(id).setValue(todo);
            Snackbar.make(view,"Item Saved!",Snackbar.LENGTH_LONG).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    Intent intent = new Intent(ListActivity.this, ListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
            },1200);

        }else{
            Snackbar.make(view,"Error Saving Item",Snackbar.LENGTH_LONG).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    Intent intent = new Intent(ListActivity.this, ListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            },1200);

        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(ListActivity.this,LoginActivity.class));
        }
        return true;
    }

    public static String getTime(){
        Calendar cal = Calendar.getInstance();
        Date date=cal.getTime();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedTime=timeFormat.format(date);

        return (formattedTime);

    }

}
