package kz.cifron.vetqyzmet_doctor;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class AnimalCardAdapter extends RecyclerView.Adapter<AnimalCardAdapter.AnimalHolder> {

    Context context;
    List<CardInfo> infoList;
    public AnimalCardAdapter(Context context, List<CardInfo> infoList) {
        this.context = context;
        this.infoList = infoList;
    }

    @NonNull
    @Override
    public AnimalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.cow_card, parent, false);
        return new AnimalHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalCardAdapter.AnimalHolder holder, int position) {
        String dateOfBirth = infoList.get(position).getDateOfBirth();
        String gender = infoList.get(position).getGender();
        String ing = infoList.get(position).getIng();
        String suit = infoList.get(position).getSuit();
        String breed = infoList.get(position).getBreed();
        String photo1 = infoList.get(position).getPhoto1();
        String photo2 = infoList.get(position).getPhoto2();
        String photo3 = infoList.get(position).getPhoto3();
        holder.textView2.setText(dateOfBirth);
        holder.textView3.setText(gender);
        holder.textView.setText(ing);
        holder.textView4.setText(breed);
        holder.textView5.setText(suit);
        Glide.with(context).load(photo1).listener(
                new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }
        ).centerCrop().into(holder.mainIm);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BigPage.class);
                intent.putExtra("dateOfBirth", dateOfBirth);
                intent.putExtra("gender", gender);
                intent.putExtra("ing", ing);
                intent.putExtra("breed", breed);
                intent.putExtra("suit", suit);
                intent.putExtra("photo1", photo1);
                intent.putExtra("photo2", photo2);
                intent.putExtra("photo3", photo3);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }


    public static final class AnimalHolder extends RecyclerView.ViewHolder{
        TextView textView;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        TextView textView5;
        ImageView mainIm;
        ProgressBar progressBar;

        public AnimalHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.inj);
            textView2 = itemView.findViewById(R.id.age);
            textView3 = itemView.findViewById(R.id.sex);
            textView4 = itemView.findViewById(R.id.color);
            textView5 = itemView.findViewById(R.id.direction);
            progressBar = itemView.findViewById(R.id.card_progress_bar);
            mainIm = itemView.findViewById(R.id.eImage);
        }
    }
}
