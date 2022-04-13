package kz.cifron.vetqyzmet_doctor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EachAnimalCardAdapter extends RecyclerView.Adapter<EachAnimalCardAdapter.EHolder> {
    Context context;
    List<EachAnimalInfo> infoList;
    public EachAnimalCardAdapter(Context context, List<EachAnimalInfo> infoList) {
        this.context = context;
        this.infoList = infoList;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull

    @Override
    public EHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.cow_card, parent, false);
        return new EachAnimalCardAdapter.EHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull  EachAnimalCardAdapter.EHolder holder, int position) {
        holder.textView.setText(infoList.get(position).getInj());
        holder.textView2.setText(infoList.get(position).getAge());
        holder.textView3.setText(infoList.get(position).getSex());
        holder.textView4.setText(infoList.get(position).getColor());
        holder.textView5.setText(infoList.get(position).getInj());
        holder.mainIm.setImageResource(infoList.get(position).getId());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BigPage.class);
            intent.putExtra("NVP", infoList.get(position).getNvp());
            intent.putExtra("Age", infoList.get(position).getAge());
            intent.putExtra("Sex", infoList.get(position).getSex());
            intent.putExtra("Color", infoList.get(position).getColor());
            intent.putExtra("INJ", infoList.get(position).getInj());
            intent.putExtra("Image", infoList.get(position).getId());
            Activity a = (Activity) context;
            a.startActivity(intent);
            a.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        });
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }


    public static final class EHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        TextView textView5;
        ImageView mainIm;

        public EHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.inj);
            textView2 = itemView.findViewById(R.id.age);
            textView3 = itemView.findViewById(R.id.sex);
            textView4 = itemView.findViewById(R.id.color);
            textView5 = itemView.findViewById(R.id.direction);
            mainIm = itemView.findViewById(R.id.eImage);

        }
    }
}
