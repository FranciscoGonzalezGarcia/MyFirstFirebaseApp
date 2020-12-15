package com.example.myfirstfirebaseapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    private RecyclerView recyclerView;
    private ArrayList<NoteClass> notesList;
    private AdapterNotes adapterNotes;
    private TextView lblNotFoundNotes;
    private FloatingActionButton btnAddNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView = findViewById(R.id.rcvNotes);
        lblNotFoundNotes = findViewById(R.id.lblNotFoundNotes);
        btnAddNote = findViewById(R.id.fabAddNewNote);
        recyclerView.setLayoutManager(linearLayoutManager);
        notesList = new ArrayList<>();
        adapterNotes = new AdapterNotes(notesList);
        recyclerView.setAdapter(adapterNotes);

        showNotes();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle(getString(R.string.notes));
        }

        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });


    }

    private void showNotes(){
        myRef.child(FirebaseClass.NOTES_REFERENCE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notesList.removeAll(notesList);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    NoteClass note = snapshot.getValue(NoteClass.class);
                    note.setKey(snapshot.getKey());
                    notesList.add(note);
                }

                adapterNotes.notifyDataSetChanged();

                if(notesList.size()==0){
                    lblNotFoundNotes.setVisibility(View.VISIBLE);
                }else {
                    lblNotFoundNotes.setVisibility(View.GONE);
                }

                adapterNotes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = recyclerView.getChildLayoutPosition(view);
                        String noteKey = notesList.get(position).getKey();
                        Intent intent = new Intent(MainActivity.this, EditNotesActivity.class);
                        intent.putExtra("title", notesList.get(position).getTitle());
                        intent.putExtra("body", notesList.get(position).getBody());
                        intent.putExtra("key", noteKey);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
