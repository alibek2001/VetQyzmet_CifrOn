package kz.cifron.vetqyzmet_doctor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FormAdapter extends RecyclerView.Adapter<FormAdapter.FormViewHolder>{
    Context context;
    List<NameCity> cities;

    public FormAdapter(Context context, List<NameCity> cities) {
        this.context = context;
        this.cities = cities;
    }

    @NonNull

    @Override
    public FormViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cityItem = LayoutInflater.from(context).inflate(R.layout.city_item, parent, false);
        return new FormAdapter.FormViewHolder(cityItem);
    }

    @Override
    public void onBindViewHolder(@NonNull FormAdapter.FormViewHolder holder, int position) {
        holder.cityName.setText(cities.get(position).getName());
        holder.cityCount.setText("" + cities.get(position).getCount());
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
                        Intent intent = new Intent(context, FormsLoc.class);
                        intent.putExtra("Path", holder.cityName.getText().toString());
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

    public static final class FormViewHolder extends RecyclerView.ViewHolder {
        TextView cityName;
        TextView cityCount;
        public FormViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.cityName);
            cityCount = itemView.findViewById(R.id.cityCount);
        }
    }
}
