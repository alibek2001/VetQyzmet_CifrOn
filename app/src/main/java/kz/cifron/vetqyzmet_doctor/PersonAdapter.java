package kz.cifron.vetqyzmet_doctor;

import android.app.Activity;
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
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonHolder> {

    Context context;
    List<StreetAndPerson> list;

    public PersonAdapter(Context context, List<StreetAndPerson> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PersonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.person_item, parent, false);
        return new PersonHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonAdapter.PersonHolder holder, int position) {
        holder.perstreet.setText(list.get(holder.getAdapterPosition()).getStreet());
        holder.pername.setText(list.get(holder.getAdapterPosition()).getName());
        SharedPreferences localData = context.getSharedPreferences("localData", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = localData.edit();

        if (holder.getAdapterPosition() == 0) {
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
                        Intent intent = new Intent(context, Page_Of_Owner.class);
                        edit.putString("street", holder.perstreet.getText().toString());
                        edit.putString("name", holder.pername.getText().toString());
                        edit.putInt("localityId", list.get(holder.getAdapterPosition()).getLocalityId());
                        edit.putInt("personId", list.get(holder.getAdapterPosition()).getPersonId());
                        edit.apply();
                        Activity a = (Activity) context;
                        context.startActivity(intent);
                        a.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

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
        return list.size();
    }

    public static final class PersonHolder extends RecyclerView.ViewHolder {
        TextView pername;
        TextView perstreet;
        public PersonHolder(@NonNull View itemView) {
            super(itemView);
            pername = itemView.findViewById(R.id.address);
            perstreet = itemView.findViewById(R.id.client);
        }
    }
}
