package areeba.learn.notetaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class SignupActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    HashMap<String, String> logins = new HashMap<>();

    EditText editUsername, editPassword, editConfirmPassword;
    Button signupButton;

    Boolean signupAllowed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editUsername = findViewById(R.id.signup_username);
        editPassword = findViewById(R.id.signup_password);
        editConfirmPassword = findViewById(R.id.signup_password_confirm);
        signupButton = findViewById(R.id.signup_button);

        database = FirebaseDatabase
                .getInstance("https://notemaker-5d4b7-default-rtdb.asia-southeast1.firebasedatabase.app");

        myRef = database.getReference("logins");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<HashMap<String, String>> typeIndicator = new GenericTypeIndicator<HashMap<String, String>>() {};
                logins = snapshot.getValue(typeIndicator);
                signupAllowed = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (logins.containsKey(editUsername.getText().toString())) {
                    editUsername.setError("Username already exists");
                    Toast.makeText(getApplicationContext(), "Username already exists!", Toast.LENGTH_SHORT).show();
                }
                else if (!editPassword.getText().toString().equals(editConfirmPassword.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Passwords should be same!", Toast.LENGTH_SHORT).show();
                }
                else if (signupAllowed){
                    logins.put(editUsername.getText().toString(), editPassword.getText().toString());
                    myRef.setValue(logins);
                    Toast.makeText(getApplicationContext(), "Signup Success!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}