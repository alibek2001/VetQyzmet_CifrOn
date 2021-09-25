package com.cifron.vet_v10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.InfoHolder> {
    Context context;
    EachAnimalInfo info;

    InfoAdapter(Context context, EachAnimalInfo info) {
        this.context = context;
        this.info = info;
    }

    @NonNull
    @Override
    public InfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.info_item, parent, false);
        return new InfoAdapter.InfoHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoAdapter.InfoHolder holder, int position) {
        if (position == 0) {
            holder.textView.setText("НВП");
            holder.textView2.setText(info.getNvp());
        }
        else if (position == 1) {
            holder.textView.setText("Пол");
            holder.textView2.setText(info.getSex());
        }
        else if (position == 2) {
            holder.textView.setText("ИНЖ");
            holder.textView2.setText(info.getInj());
        }
        else if (position == 3) {
            holder.textView.setText("Возраст");
            holder.textView2.setText(info.getAge());
        }
        else if (position == 4) {
            holder.textView.setText("Масть");
            holder.textView2.setText(info.getColor());
        }

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public static final class InfoHolder extends RecyclerView.ViewHolder {

        TextView textView;
        TextView textView2;
        public InfoHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.infoName);
            textView2 = itemView.findViewById(R.id.infoProperty);
        }
    }
}
