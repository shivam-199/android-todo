package com.example.finaltodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class DetailsActivity extends AppCompatActivity {

    private TextView itemName;
    private TextView dateAdded;
    private int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        itemName = (TextView) findViewById(R.id.itemNameDet);
        dateAdded = (TextView) findViewById(R.id.dateAddedDet);


        Bundle bundle = getIntent().getExtras();

        if ( bundle != null ) {
            itemName.setText(bundle.getString("name"));
            dateAdded.setText(bundle.getString("date"));
            itemId = bundle.getInt("id");
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
                startActivity(new Intent(DetailsActivity.this,LoginActivity.class));
        }
        return true;
    }
}
