package kz.cifron.vetqyzmet_doctor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class VetQyzmetPage extends AppCompatActivity {

    ConstraintLayout reg;
    ConstraintLayout services;
    ConstraintLayout migration;
    ConstraintLayout spravki;
    ImageButton logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_qyzmet_page);

        reg = findViewById(R.id.id_constraintLayout_registration_animal);
        services = findViewById(R.id.id_constraintLayout_services);
        migration = findViewById(R.id.id_constraintLayout_migration);
        spravki = findViewById(R.id.id_constraintLayout_spravki);
        logout = findViewById(R.id.logoutBut);

        logout();

        reg.setOnTouchListener(touchListener);
        services.setOnTouchListener(touchListener);
        migration.setOnTouchListener(touchListener);
        spravki.setOnTouchListener(touchListener);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void logout() {
        logout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());

                if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                        v.setBackgroundColor(Color.rgb(239, 242, 245));
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        v.setBackgroundColor(Color.rgb(255, 255, 255));
                        showAlertOf();
                    }
                }
                else {
                    v.setBackgroundColor(Color.rgb(255,255,255));
                }
                return true;
            }
        });
    }

    View.OnTouchListener touchListener = (v, event) -> {
        Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());

        if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                v.setBackgroundColor(Color.rgb(239, 242, 245));
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                String soz = "";
                v.setBackgroundColor(Color.rgb(255,255,255));

                if (v.getId() == R.id.id_constraintLayout_registration_animal) {
                    Intent intent = new Intent(VetQyzmetPage.this, Audandar.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                else {
                    Intent intent = new Intent(VetQyzmetPage.this, EmptyActivity.class);
                    if (v.getId() == R.id.id_constraintLayout_migration) {
                        soz = "????????????????";
                    }
                    else if (v.getId() == R.id.id_constraintLayout_services) {
                        soz = "??????????????";
                    }
                    else {
                        soz = "??????????????";
                    }
                    intent.putExtra("soz", soz);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

            }
        }
        else {
            v.setBackgroundColor(Color.rgb(255,255,255));
        }
        return true;
    };

    @Override
    public void onBackPressed() {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
    }

    public void showAlertOf() {
        new AlertDialog.Builder(this).setTitle("???? ?????????????????????????? ???????????? ?????????? ???? ?????????????????").setCancelable(false).setNegativeButton("??????", null).setPositiveButton("????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(VetQyzmetPage.this, LoginActiv.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                SharedPreferences user = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = user.edit();
                editor.putString("username", "null");
                editor.putString("userpassword", "null");
                editor.apply();
            }
        }).create().show();
    }
}