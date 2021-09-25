package com.cifron.vet_v10;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Tasks extends AppCompatActivity {
    ViewPager2 viewPager;
    TabLayout tabLayout;
    ViewPagerAdapter viewPagerAdapter;
    LinearLayout l1;
    LinearLayout l2;
    LinearLayout l3;
    TextView t1;
    TextView t2;
    TextView t3;
    ImageView i1;
    ImageView i2;
    ImageView i3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tab_layout);
        l1 = findViewById(R.id.buttonTasks);
        l2 = findViewById(R.id.buttonSearch);
        l3 = findViewById(R.id.buttonProfile);
        t1 = findViewById(R.id.textTasks);
        t2 = findViewById(R.id.textSearch);
        t3 = findViewById(R.id.textProfile);
        i1 = findViewById(R.id.imageTasks);
        i2 = findViewById(R.id.imageSearch);
        i3 = findViewById(R.id.imageProfile);

        String[] ttabs = new String[]{"Вакцины/Услуги", "Формы"};
        viewPagerAdapter = new ViewPagerAdapter(this);

        viewPager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(ttabs[position])
        ).attach();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        lF();
        lS();
        lT();
    }
    public void lF() {
        l1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                t1.setTextColor(Color.rgb(233, 131, 5));

                i1.setColorFilter(Color.rgb(233, 131, 5));
            }
        });
    }
    public void lS() {
        l2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                t2.setTextColor(Color.rgb(233, 131, 5));

                i2.setColorFilter(Color.rgb(233, 131, 5));
            }
        });
    }
    public void lT() {
        l3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                t3.setTextColor(Color.rgb(233, 131, 5));

                i3.setColorFilter(Color.rgb(233, 131, 5));
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Вы действительно хотите выйти").setCancelable(false).setNegativeButton("Нет", null).setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity();
                //moveTaskToBack(false);
            }
        }).create().show();
    }

}