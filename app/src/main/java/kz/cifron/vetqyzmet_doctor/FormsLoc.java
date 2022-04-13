package kz.cifron.vetqyzmet_doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FormsLoc extends AppCompatActivity {

    ImageView back;
    TextView name;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms_loc);
        back = findViewById(R.id.formBack);
        backP();
        name = findViewById(R.id.formName);
        button = findViewById(R.id.regBut);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextAcc();
            }
        });
        name.setText(getIntent().getStringExtra("Path"));

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void backP() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void nextAcc() {
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }

}