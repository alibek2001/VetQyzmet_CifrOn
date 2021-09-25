package com.cifron.vet_v10;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    Context context;
    List<NameCity> cities;


    public CityAdapter(Context context, List<NameCity> cities) {
        this.context = context;
        this.cities = cities;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cityItem = LayoutInflater.from(context).inflate(R.layout.city_item, parent, false);
        return new CityViewHolder(cityItem);
    }

    @Override
    public void onBindViewHolder(@NonNull CityAdapter.CityViewHolder holder, int position) {
        holder.cityName.setText(cities.get(position).getName());
        holder.cityCount.setText("" + cities.get(position).getCount());
        SharedPreferences localData = context.getSharedPreferences("localData", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = localData.edit();

        if (position == 0) {
            holder.itemView.setVisibility(View.GONE);
        }
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());

                if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                        v.setBackgroundColor(Color.rgb(239, 242, 245));

                    }

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        v.setBackgroundColor(Color.rgb(255,255,255));
                        Intent intent = new Intent(context, Personal.class);
                        edit.putString("Path", holder.cityName.getText().toString());
                        edit.putInt("localityId", cities.get(position).getLevel());
                        edit.apply();
                        context.startActivity(intent);
                    }
                }
                else {
                    v.setBackgroundColor(Color.rgb(255,255,255));
                }
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return cities.size();
    }


    public static final class CityViewHolder extends RecyclerView.ViewHolder {
        TextView cityName;
        TextView cityCount;
        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.cityName);
            cityCount = itemView.findViewById(R.id.cityCount);
        }
    }
}
