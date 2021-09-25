package com.cifron.vet_v10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class All extends AppCompatActivity {
    ImageButton imageButton;
    TextView textView;
    RecyclerView recyclerView;
    AnimalCardAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);
        imageButton = findViewById(R.id.pressBack);
        textView = findViewById(R.id.last_name);
        recyclerView = findViewById(R.id.animalCardRecycler);
        textView.setText(getIntent().getStringExtra("Path"));
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        List<AnimalCardInfo> list = new ArrayList<>();
        list.add(new AnimalCardInfo(0, "Лошади", "19 голов"));
        list.add(new AnimalCardInfo(1, "Коровы", "19 голов"));
        list.add(new AnimalCardInfo(2, "Овцы", "21 голов"));
        list.add(new AnimalCardInfo(3, "Верблюды", "5 голов"));
        setAdapt(list);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    public void setAdapt(List<AnimalCardInfo> list) {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new AnimalCardAdapter(this, list);
        recyclerView.setHasFixedSize(true);
        //recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 50, false));
        recyclerView.setAdapter(adapter);
    }
}