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

public class AddNoteActivity extends AppCompatActivity {
    private EditText title, body;
    private Button btnAddNote;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child(FirebaseClass.NOTES_REFERENCE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        title = findViewById(R.id.addTitle);
        body = findViewById(R.id.addBody);
        btnAddNote = findViewById(R.id.btnAddNote);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle(getString(R.string.add_note));
        }

        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(thereAreNotEmptyFields()){
                    HashMap<String,Object> note = new HashMap<>();
                    note.put("title", title.getText().toString());
                    note.put("body", body.getText().toString());
                    String key = myRef.push().getKey();
                    myRef.child(key).setValue(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                finish();
                                Toast.makeText(AddNoteActivity.this,R.string.add_successful, Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(AddNoteActivity.this,R.string.error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

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
