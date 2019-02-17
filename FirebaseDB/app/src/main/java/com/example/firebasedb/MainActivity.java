package com.example.firebasedb;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editTextName;
    Button buttonAddArtist;
    //Spinner spinnerGenres;

    DatabaseReference databaseArtists;
    List<Artist> artistList;
    ListView listViewArtists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        databaseArtists = FirebaseDatabase.getInstance().getReference("artist");

        editTextName = findViewById(R.id.editTextName);
        buttonAddArtist = findViewById(R.id.buttonAddArtist);
        //spinnerGenres = findViewById(R.id.spinnerGenre);
        artistList = new ArrayList<>();

        listViewArtists =  findViewById(R.id.listViewArtists);

        buttonAddArtist.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                addArtist();
            }
        });

        listViewArtists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Artist artist = artistList.get(i);

                return false;
            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseArtists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                artistList.clear();

                for(DataSnapshot artistSnapshot : dataSnapshot.getChildren()){
                    Artist artist = artistSnapshot.getValue(Artist.class);

                    artistList.add(artist);
                }

                ArtistList adapter = new ArtistList(MainActivity.this,artistList);
                listViewArtists.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addArtist(){
        String name = editTextName.getText().toString().trim();
        //String genre = spinnerGenres.getSelectedItem().toString();
        if(!TextUtils.isEmpty(name)){
            String id =  databaseArtists.push().getKey();
            Artist artist = new Artist(id,name);
            assert id != null;
            databaseArtists.child(id).setValue(artist);
            Toast.makeText(this,"Artist added",Toast.LENGTH_SHORT).show();
            editTextName.setText("");
        }else {
            Toast.makeText(this,"You should enter a name",Toast.LENGTH_SHORT).show();
        }
    }

    private void showUpdateDialog(final String artistId, String artistName){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog,null);

        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.btnUpdate);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.btnDelete);


        dialogBuilder.setTitle("Updating Artist "+ artistName);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    editTextName.setError("Name Required");
                    editTextName.requestFocus();
                    return;
                }
                updateArtist(artistId,name);
                alertDialog.dismiss();

            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteArtist(artistId);
                alertDialog.dismiss();

            }
        });



    }

    private void deleteArtist(String artistId) {

        DatabaseReference dArtist = FirebaseDatabase.getInstance().getReference("artist").child(artistId);
        dArtist.removeValue();
        Toast.makeText(this, "Artist Deleted", Toast.LENGTH_SHORT).show();
    }

    private boolean updateArtist(String id, String name){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("artist").child(id);
        Artist artist = new Artist(id,name);
        databaseReference.setValue(artist);
        Toast.makeText(getApplicationContext(),"Artist Updated Successfully", Toast.LENGTH_SHORT).show();
        return true;
    }
}
