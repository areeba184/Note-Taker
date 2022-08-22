package areeba.learn.notetaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.UUID;

public class NotepadActivity extends AppCompatActivity {

    String signedInUser;

    FirebaseDatabase database;
    DatabaseReference myRef;

    EditText editTitle, editNote;
    Button saveButton;

    HashMap<String, String> notes = new HashMap<>();

    boolean saveAllowed = false, isExistingNote = false;
    String currentNoteTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        editNote = findViewById(R.id.edit_note);
        editTitle = findViewById(R.id.edit_title);
        saveButton = findViewById(R.id.save_button);

        signedInUser = getIntent().getStringExtra("signedInUser");
        currentNoteTitle = getIntent().getStringExtra("currentNote");



        database = FirebaseDatabase
                .getInstance("https://notemaker-5d4b7-default-rtdb.asia-southeast1.firebasedatabase.app");

        myRef = database.getReference(signedInUser);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<HashMap<String, String>> typeIndicator = new GenericTypeIndicator<HashMap<String, String>>() {};
                notes = snapshot.getValue(typeIndicator);

                if (!(currentNoteTitle == null)) {
                    isExistingNote = true;
                    editTitle.setText(currentNoteTitle.split("@:@")[0]);
                    editNote.setText(notes.get(currentNoteTitle));
                    editTitle.setEnabled(false);
                }
                saveAllowed = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notes == null) {
                    notes = new HashMap<>();
                }
                if (isExistingNote) {
                    String note = editNote.getText().toString();
                    notes.put(currentNoteTitle, note);
                    myRef.setValue(notes);
                }
                else if (saveAllowed) {
                    String title = editTitle.getText().toString();
                    String uuid = UUID.randomUUID().toString();

                    title = title + "@:@" + uuid;
                    String note = editNote.getText().toString();
                    notes.put(title, note);
                    myRef.setValue(notes);
                    isExistingNote = true;
                    currentNoteTitle = title;
                }
            }
        });
    }
}