package com.example.finaltodo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText todoItem;
    private Button saveButton;
    public TextView textViewEmailSets;
    FirebaseAuth mAuth;
    DatabaseReference databaseItems;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        databaseItems = FirebaseDatabase.getInstance().getReference("ToDo");

        byPassActivity();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textViewEmailSets = (TextView) findViewById(R.id.textViewEmailSet);

        final FirebaseUser user = mAuth.getCurrentUser();
        String emails = user.getEmail();
        textViewEmailSets.setText("Hello " + emails);


        byPassActivity();



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                createPopUpDialog();
            }
        });
    }

    private void createPopUpDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup,null);
        todoItem = (EditText) view.findViewById(R.id.todoItem);
        saveButton = (Button) view.findViewById(R.id.saveButton);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!todoItem.getText().toString().isEmpty()){
                    saveItemToFB(view);
                }
            }
        });

    }

    private void saveItemToFB(View v) {

        final FirebaseUser user = mAuth.getCurrentUser();
        final String Uid = user.getUid();


        String newItem = todoItem.getText().toString().trim();


        //add to firebase database
        if(!TextUtils.isEmpty(newItem)){
            String id =  databaseItems.push().getKey();
            ToDo todo = new ToDo(newItem,getTime(),id);

            assert id != null;
            databaseItems.child(Uid).child(id).setValue(todo);
            Snackbar.make(v,"Item Saved!",Snackbar.LENGTH_LONG).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    startActivity(new Intent(MainActivity.this, ListActivity.class));
                }
            },1200);

        }else{
            Snackbar.make(v,"Error Saving Item",Snackbar.LENGTH_LONG).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    startActivity(new Intent(MainActivity.this, ListActivity.class));
                }
            },1200);

        }


    }

    public void byPassActivity(){
        //check if database is empty.
        // If not then go to list activity

        final FirebaseUser user = mAuth.getCurrentUser();
        final String Uid = user.getUid();

        databaseItems.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(Uid).exists()){
                    finish();
                    startActivity(new Intent(MainActivity.this,ListActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }
        return true;
    }

    public String getTime(){
        Calendar cal = Calendar.getInstance();
        Date date=cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedDate=dateFormat.format(date);
        return formattedDate;

    }

}
