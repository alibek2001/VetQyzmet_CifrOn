package com.cifron.vet_v10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

public class BigPage extends AppCompatActivity {

    Fragment_services services;
    Fragment_vacine vacine;
    Spinner spinner;
    EachAnimalInfo e;
    RecyclerView recyclerView;
    InfoAdapter infoAdapter;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_page);
        back = findViewById(R.id.bBack);
        bBack();
        spinner = findViewById(R.id.spinner);
        recyclerView = findViewById(R.id.infoRecycler);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(BigPage.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.spinner_items));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        services = new Fragment_services();
        vacine = new Fragment_vacine();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    setFragment(services);
                }
                else if (position == 1) {
                    setFragment(vacine);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setFragment(services);
            }
        });
        e = new EachAnimalInfo(1, getIntent().getStringExtra("NVP"), getIntent().getStringExtra("Age"), getIntent().getStringExtra("Sex"), getIntent().getStringExtra("Color"), getIntent().getStringExtra("INJ"), true);
        setAdapt();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }
    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
    public void setAdapt() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        infoAdapter = new InfoAdapter(this, e);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(infoAdapter);
        recyclerView.setHasFixedSize(true);
    }
    public void bBack() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}