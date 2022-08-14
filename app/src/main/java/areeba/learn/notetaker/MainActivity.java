package areeba.learn.notetaker;

import static android.content.SharedPreferences.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    Button buttonClick;
    EditText editText;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("AreebaSP", MODE_PRIVATE);
        buttonClick = findViewById(R.id.button_clickMe);
        editText = findViewById(R.id.editText1);

        String savedString = sharedPreferences.getString("myText", "");
        Boolean newUser = sharedPreferences.getBoolean("firstTimer", true);

        if (newUser == true) {
            Toast.makeText(getApplicationContext(), "First Time!", Toast.LENGTH_SHORT).show();
            Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstTimer", false);
            editor.commit();
        }


        editText.setText(savedString);

        buttonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();

                Editor editor = sharedPreferences.edit();
                editor.putString("myText", text);
                editor.commit();
                /*Intent intent = new Intent(MainActivity.this, NewActivity.class);
                startActivity(intent);
                finish();*/
            }
        });
    }
}