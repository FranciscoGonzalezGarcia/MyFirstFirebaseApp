package com.example.myfirstfirebaseapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditNotesActivity extends AppCompatActivity {
    private EditText title, body;
    private Button btnEditNote, btnDeleteNote;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child(FirebaseClass.NOTES_REFERENCE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);
        title = findViewById(R.id.editTitle);
        body = findViewById(R.id.editBody);
        btnEditNote = findViewById(R.id.btnEditNote);
        btnDeleteNote = findViewById(R.id.btnDeleteNote);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle(getString(R.string.edit_note));
        }

        Bundle bundle = getIntent().getExtras();
        title.setText(bundle.getString("title"));
        body.setText(bundle.getString("body"));
        final String key = bundle.getString("key");

        btnEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(thereAreNotEmptyFields()){
                    HashMap<String,Object> note = new HashMap<>();
                    note.put("title", title.getText().toString());
                    note.put("body", body.getText().toString());
                    myRef.child(key).updateChildren(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                finish();
                                Toast.makeText(EditNotesActivity.this,R.string.edit_successful, Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(EditNotesActivity.this,R.string.error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        btnDeleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            finish();
                            Toast.makeText(EditNotesActivity.this,R.string.delete_successful, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(EditNotesActivity.this,R.string.error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private boolean thereAreNotEmptyFields() {
        if(title.getText().toString().isEmpty()){
            title.setError(getString(R.string.field_error));
            return false;
        }else if (body.getText().toString().isEmpty()){
            body.setError(getString(R.string.field_error));
            return false;
        }else {
            return true;
        }
    }
}
