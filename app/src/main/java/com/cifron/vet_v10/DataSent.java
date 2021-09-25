package com.cifron.vet_v10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class DataSent extends AppCompatActivity {

    ConstraintLayout con;
    ConstraintLayout con2;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_sent);
        con = findViewById(R.id.id_constraintLayout_button_to_page_owner);
        con2 = findViewById(R.id.id_constraintLayout_button_return_main_page);
        textView = findViewById(R.id.textView7);
        con.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        con.getBackground().setAlpha(128);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        con.getBackground().setAlpha(255);
                        Intent intent = new Intent(DataSent.this, Page_Of_Owner.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finishAffinity();
                    }
                }
                else {
                    con.getBackground().setAlpha(255);
                }
                return true;
            }
        });


        con2.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        con2.setBackgroundResource(R.drawable.plus_button_background_gray);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        con2.setBackgroundResource(R.drawable.ic_white_button);
                        Intent intent = new Intent(DataSent.this, VetQyzmetPage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
                else {
                    con2.setBackgroundResource(R.drawable.ic_white_button);
                }

                return true;
            }
        });

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onBackPressed() {
        
    }
}