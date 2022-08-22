package areeba.learn.notetaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotesListActivity extends AppCompatActivity {

    String signedInUsername;

    FirebaseDatabase database;
    DatabaseReference myRef;
    ImageView addNoteImage;

    HashMap<String, String> notes = new HashMap<>();

    ListView listView;
    TextView textView, addNoteText;

    List<String> notesList;
    List<String> notesTitles;
    int backCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        listView = findViewById(R.id.notes_list_view);
        textView = findViewById(R.id.note_element);
        addNoteImage = findViewById(R.id.add_note_image);
        addNoteText = findViewById(R.id.add_note_text);
        listView.setVisibility(View.INVISIBLE);


        signedInUsername = getIntent().getStringExtra("signedInUser");

        database = FirebaseDatabase
                .getInstance("https://notemaker-5d4b7-default-rtdb.asia-southeast1.firebasedatabase.app");

        myRef = database.getReference(signedInUsername);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<HashMap<String, String>> typeIndicator = new GenericTypeIndicator<HashMap<String, String>>() {};
                notes = snapshot.getValue(typeIndicator);
                loadList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addNoteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNote();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (backCounter == 1) {
            super.onBackPressed();
        }
        if (backCounter == 0) {
            backCounter++;
            Toast.makeText(getApplicationContext(), "Press back again to exit!", Toast.LENGTH_SHORT).show();
        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                backCounter=0;
            }
        }, 2000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            addNote();
        }
        return super.onOptionsItemSelected(item);
    }

    void addNote() {
        Intent intent = new Intent(NotesListActivity.this, NotepadActivity.class);
        intent.putExtra("signedInUser", signedInUsername);
        startActivity(intent);
    }

    void loadList() {
        if (notes == null) {
            notes = new HashMap<>();
        } else {
            addNoteText.setVisibility(View.INVISIBLE);
            addNoteImage.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }
        notesList = new ArrayList<>(notes.keySet());
        notesTitles = new ArrayList<>();
        for (String noteTitle : notesList) {
            notesTitles.add(noteTitle.split("@:@")[0]);
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_element,
                R.id.note_element,
                notesTitles);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(NotesListActivity.this, NotepadActivity.class);
                intent.putExtra("signedInUser", signedInUsername);
                intent.putExtra("currentNote", notesList.get(i));
                startActivity(intent);
            }
        });
    }
}