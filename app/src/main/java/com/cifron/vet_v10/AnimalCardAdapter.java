package com.cifron.vet_v10;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AnimalCardAdapter extends RecyclerView.Adapter<AnimalCardAdapter.AnimalHolder> {

    Context context;
    List<AnimalCardInfo> infoList;
    public AnimalCardAdapter(Context context, List<AnimalCardInfo> infoList) {
        this.context = context;
        this.infoList = infoList;
    }

    @NonNull
    @Override
    public AnimalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.card_animal, parent, false);
        return new AnimalHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalCardAdapter.AnimalHolder holder, int position) {
        holder.count.setText(infoList.get(position).getCount());
        holder.name.setText(infoList.get(position).getName());
        int id = infoList.get(position).getId();
        if (id == 1) {
            holder.animal.setImageResource(R.drawable.cow);
        }
        if (id == 2) {
            holder.animal.setImageResource(R.drawable.qoi);
        }
        if (id == 3) {
            holder.animal.setImageResource(R.drawable.camel);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EachAnimal.class);
                intent.putExtra("Type", infoList.get(position).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }


    public static final class AnimalHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView count;
        ImageView animal;

        public AnimalHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameOfAnimal);
            count = itemView.findViewById(R.id.countOfAnimal);
            animal = itemView.findViewById(R.id.animal);
        }
    }
}
