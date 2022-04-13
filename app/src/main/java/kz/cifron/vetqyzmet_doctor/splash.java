package kz.cifron.vetqyzmet_doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences password = getSharedPreferences("fastPass", MODE_PRIVATE);
                String cur = password.getString("fastPassword", "null");
                Intent i;
                if (cur.equals("null")) {
                    i = new Intent(splash.this, LoginActiv.class);
                }
                else {
                    i = new Intent(splash.this, MainActivity.class);
                }
                startActivity(i);
                finish();
            }
        }, 3000);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}