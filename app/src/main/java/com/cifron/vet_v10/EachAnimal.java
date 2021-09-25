package com.cifron.vet_v10;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class EachAnimal extends AppCompatActivity {
    ImageButton imageButton;
    RecyclerView recyclerView;
    static EachAnimalCardAdapter adapter;
    static List<EachAnimalInfo> list;
    static List<EachAnimalInfo> full;
    Button all;
    Button waiting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_animal);
        imageButton = findViewById(R.id.back);
        back();
        recyclerView = findViewById(R.id.eachRecycler);
        all = findViewById(R.id.all);
        waiting = findViewById(R.id.waiting);
        list = new ArrayList<>();
        list.add(new EachAnimalInfo(R.drawable.cow1, "4562", "3.1", "Male", "White", "6441", false));
        list.add(new EachAnimalInfo(R.drawable.main_cow, "456672", "3.5", "Female", "Black", "6441", true));
        list.add(new EachAnimalInfo(R.drawable.logo_to_card, "456672", "3.5", "Female", "Black", "6441", true));
        list.add(new EachAnimalInfo(R.drawable.logo_to_card, "456672", "3", "Female", "Brown", "6441", false));

        full = new ArrayList<>();
        full.addAll(list);
        setAdapt();
        all.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables"})
            @Override
            public void onClick(View v) {
                waiting.setBackground(getDrawable(R.drawable.categorysec));
                all.setBackground(getDrawable(R.drawable.category));
                all.setTextColor(Color.WHITE);
                waiting.setTextColor(Color.BLACK);
                all();
            }
        });
        waiting.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables"})
            @Override
            public void onClick(View v) {
                waiting.setBackground(getDrawable(R.drawable.category));
                waiting.setTextColor(Color.WHITE);
                all.setBackground(getDrawable(R.drawable.categorysec));
                all.setTextColor(Color.BLACK);
                waiting();
            }
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    public void back() {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public void setAdapt() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new EachAnimalCardAdapter(this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }
    public static void all() {
        list.clear();
        list.addAll(full);
        adapter.notifyDataSetChanged();
    }
    public static void waiting() {
        list.clear();
        list.addAll(full);
        List<EachAnimalInfo> temp = new ArrayList<>();
        for (EachAnimalInfo e: list) {
            if (e.isWaitingVacine()) {
                temp.add(e);
            }
        }
        list.clear();
        list.addAll(temp);
        adapter.notifyDataSetChanged();

    }
}