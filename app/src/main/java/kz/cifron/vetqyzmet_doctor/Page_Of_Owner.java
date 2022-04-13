package kz.cifron.vetqyzmet_doctor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.DrawableCompat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Page_Of_Owner extends AppCompatActivity {

    Button button;
    TextView streetOwner;
    TextView nameOwner;
    TextView phoneNumberTV;
    ImageButton back;
    String token;
    Handler handler = new Handler();
    int localityId;
    int personId;
    long createdTime;
    int[] counts_of_animal = new int[5];
    TextView krs, mrs, camel, horse, pig;
    TextView whichTime;
    String phoneNumber;
    int status;
    ConstraintLayout finisher;
    ProgressDialog progressDialog;
    Button btnCancel;
    Button btnFinish;
    Dialog dialog;
    ConstraintLayout constForkrs;
    ConstraintLayout constFormrs;
    ConstraintLayout constForhorse;
    ConstraintLayout constForpig;
    ConstraintLayout constForcamel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_of_owner);
        button = findViewById(R.id.id_constraintLayout_button_add_animal);
        streetOwner = findViewById(R.id.streeetOwner);
        nameOwner = findViewById(R.id.nameOwner);
        back = findViewById(R.id.backOwner);
        constFormrs = findViewById(R.id.id_constraintLayout_mrs);
        constForcamel = findViewById(R.id.id_constraintLayout_verblud);
        constForhorse = findViewById(R.id.id_constraintLayout_loshad);
        constForpig = findViewById(R.id.id_constraintLayout_svinya);
        krs = findViewById(R.id.krs);
        mrs = findViewById(R.id.mrs);
        camel = findViewById(R.id.camel);
        horse = findViewById(R.id.horse);
        pig = findViewById(R.id.pig);
        whichTime = findViewById(R.id.whichTime);
        finisher = findViewById(R.id.finisher);
        phoneNumberTV = findViewById(R.id.phoneNumberOwner);
        constForkrs = findViewById(R.id.id_constraintLayout_krs);
        finishHim();
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(Page_Of_Owner.this);
                progressDialog.setMessage("Загрузка данных");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        });

        SharedPreferences localData = getSharedPreferences("localData", MODE_PRIVATE);
        streetOwner.setText(localData.getString("street", "null"));
        nameOwner.setText(localData.getString("name", "null"));
        localityId = localData.getInt("localityId", -1);
        personId = localData.getInt("personId", -1);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        Drawable buttonDrawable = v.getBackground();
                        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                        DrawableCompat.setTint(buttonDrawable, Color.rgb(255, 180, 88));
                        v.setBackground(buttonDrawable);
                    }

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        Drawable buttonDrawable = v.getBackground();
                        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                        DrawableCompat.setTint(buttonDrawable, Color.rgb(255, 120, 0));
                        v.setBackground(buttonDrawable);
                        Intent intent = new Intent(Page_Of_Owner.this, Registration.class);
                        intent.putExtra("local", localityId);
                        intent.putExtra("personId", personId);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }
                else {
                    Drawable buttonDrawable = v.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    DrawableCompat.setTint(buttonDrawable, Color.rgb(255, 120, 0));
                    v.setBackground(buttonDrawable);
                }
                return true;
            }
        });
        constForkrs.setOnTouchListener(toEach);
        constFormrs.setOnTouchListener(toEach);
        constForpig.setOnTouchListener(toEach);
        constForhorse.setOnTouchListener(toEach);
        constForcamel.setOnTouchListener(toEach);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    View.OnTouchListener toEach = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
            if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundColor(Color.rgb(239, 242, 245));
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.drawable_border_bottom);
                    Intent intent = new Intent(Page_Of_Owner.this, EachAnimal.class);
                    String sendString = "";
                    int sendInt = 0;
                    if (v.getId() == R.id.id_constraintLayout_verblud) {
                        sendString = "Верблюдь";
                        sendInt = 5;
                    }
                    else if (v.getId() == R.id.id_constraintLayout_krs) {
                        sendString = "КРС";
                        sendInt = 1;
                    }
                    else if (v.getId() == R.id.id_constraintLayout_svinya) {
                        sendString = "Свинья";
                        sendInt = 4;
                    }
                    else if (v.getId() == R.id.id_constraintLayout_mrs) {
                        sendString = "МРС";
                        sendInt = 2;
                    }
                    else {
                        sendString = "Лошадь";
                        sendInt = 3;
                    }
                    intent.putExtra("type", sendString);
                    intent.putExtra("id", sendInt);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
            else {
                v.setBackgroundResource(R.drawable.drawable_border_bottom);
            }

            return true;
        }
    };

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Page_Of_Owner.this, Personal.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    class LoginPost extends Thread {
        String data;

        @Override
        public void run() {

            try {
                SharedPreferences user = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor edit = user.edit();
                token = user.getString("token", "null");

                URL url = new URL("https://admin.vetqyzmet.kz/api/v7/getInfo");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Authorization", "Bearer " + token);
                con.setReadTimeout(10000);
                con.setConnectTimeout(10000);
                con.setDoInput(true);


                InputStream is = con.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
                String line;
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                Log.d("Three", "Works");

                JSONArray array = new JSONArray(response.toString());
                JSONArray oneData = null;
                for (int i = 0;i < array.length();i++) {

                    JSONObject object = array.getJSONObject(i);
                    String someString = object.getString("locality_name");
                    JSONArray data = object.getJSONArray("data");
                    int id = Integer.parseInt(object.getString("id"));
                    if (id == localityId) {
                        oneData = data;
                    }
                    Log.d("aaaa", someString);
                }


                for (int i = 0;i < oneData.length();i++) {
                    JSONObject object = oneData.getJSONObject(i);
                    int id = object.getInt("id");
                    if (id == personId) {
                        counts_of_animal[0] = object.getInt("krs_count");
                        counts_of_animal[1] = object.getInt("mrs_count");
                        counts_of_animal[3] = object.getInt("pig_count");
                        counts_of_animal[2] = object.getInt("horse_count");
                        counts_of_animal[4] = object.getInt("camel_count");
                        createdTime = object.getLong("status2_time");
                        phoneNumber = object.getString("phone");
                    }
                }
                //================================================================
                setPhoneNumber(phoneNumber);
                //================================================================
                long nowTime = System.currentTimeMillis() / 1000;
                long difference = createdTime - nowTime;
                passedTime(difference);
                edit.putString("owner_id", personId + "");
                edit.apply();
                Log.d("Page_Of_Owner", "Data uploaded");

                con.disconnect();

            } catch (UnsupportedEncodingException e) {
                Log.d("Khuina", e.getMessage());
            } catch (ProtocolException e) {
                Log.d("Proto", e.getMessage());
            } catch (MalformedURLException e) {
                Log.d("URL", e.getMessage());
            } catch (IOException e) {
                Log.d("IO", e.getMessage());
            } catch (JSONException e) {
                e.printStackTrace();
            }


            handler.post(new Runnable() {
                @Override
                public void run() {

                    krs.setText("Всего " + counts_of_animal[0]);
                    mrs.setText("Всего " +  counts_of_animal[1]);
                    camel.setText("Всего " +counts_of_animal[4]);
                    horse.setText("Всего " + counts_of_animal[2]);
                    pig.setText("Всего " + counts_of_animal[3]);
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });
        }

    }
//==================================================
    public void passedTime(long difference) {

        int hours = Math.abs((int) ((difference / (60*60)) % 24));
        int days = Math.abs((int) (difference / (60*60*24)));
        int minutes = Math.abs((int) ((difference / (60)) % 60));

        handler.post(new Runnable() {
            @Override
            public void run() {
                if(days >= 1){
                    whichTime.setText(days + " дня и " + hours +" часов " + minutes + " минут назад");
                }else if(hours == 0 && days == 0){
                    whichTime.setText(minutes + " минут назад");
                }else if(minutes == 0 && hours == 0 && days == 0){
                    whichTime.setText("Только что");
                }else {
                    whichTime.setText(hours +" часов " + minutes + " минут назад");
                }
            }
        });
    }

    public void setPhoneNumber(String phoneNumber) {

        handler.post(new Runnable() {
            @Override
            public void run() {
               phoneNumberTV.setText(phoneNumber);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        new GetToken().start();
        new LoginPost().start();
    }

    public void finishHim() {
        finisher.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        v.setBackgroundColor(Color.rgb(239,242,245));
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        v.setBackgroundColor(Color.WHITE);
                        showDialogOfConf();
                    }
                }
                else {
                    v.setBackgroundColor(Color.WHITE);
                }
                return true;
            }
        });
    }

    class GetToken extends Thread {
        @Override
        public void run() {
            try {
                SharedPreferences user = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor edit = user.edit();
                URL url = new URL("https://admin.vetqyzmet.kz/api/v1/auth");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setReadTimeout(10000);
                con.setConnectTimeout(10000);
                con.setDoOutput(true);
                String jsonInputString = "{\"email\" : \"" + user.getString("username", "null") + "\", \"password\" : \"" + user.getString("userpassword", "null") + "\"}";
                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    JSONObject jsonObject = new JSONObject(response.toString());
                    token = jsonObject.getString("token");
                    edit.putString("token", token);
                    edit.apply();
                    con.disconnect();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (UnsupportedEncodingException e) {
                Log.d("Khuina", e.getMessage());
            } catch (ProtocolException e) {
                Log.d("Proto", e.getMessage());
            } catch (MalformedURLException e) {
                Log.d("URL", e.getMessage());
            } catch (IOException e) {
                Log.d("IO", e.getMessage());
            }

        }
    }


    class FinishPost extends Thread {

        @Override
        public void run() {
            try {
                URL url = new URL("https://admin.vetqyzmet.kz/api/v1/user/statementStatus");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("PUT");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Authorization", "Bearer " + token);
                con.setReadTimeout(10000);
                con.setConnectTimeout(10000);
                con.setDoOutput(true);
                String jsonInputString = "{\"statement_id\" : \"" + personId + "\"}";
                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    JSONObject jsonObject = new JSONObject(response.toString());
                    status = jsonObject.getInt("status");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (UnsupportedEncodingException e) {
                Log.d("Khuina", e.getMessage());
            } catch (ProtocolException e) {
                Log.d("Proto", e.getMessage());
            } catch (MalformedURLException e) {
                Log.d("URL", e.getMessage());
            } catch (IOException e) {
                Log.d("IO", e.getMessage());
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void showDialogOfConf() {
        dialog = new Dialog(Page_Of_Owner.this);
        dialog.setContentView(R.layout.page_of_owner_dialog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_alert_dialog));
        dialog.setCancelable(true);


        btnCancel = dialog.findViewById(R.id.btn_otmena);
        btnFinish = dialog.findViewById(R.id.btn_zavershit);

        btnFinish.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        v.setBackgroundResource(R.drawable.plus_button_background_gray);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        v.setBackgroundResource(R.drawable.ic_white_button);
                        new GetToken().start();
                        new FinishPost().start();
                        Intent intent = new Intent(Page_Of_Owner.this, VetQyzmetPage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finishAffinity();
                        startActivity(intent);
                        dialog.dismiss();
                    }
                }
                else {
                    v.setBackgroundResource(R.drawable.ic_white_button);
                }
                return true;
            }
        });

        btnCancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        v.getBackground().setAlpha(128);
                    }

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        v.getBackground().setAlpha(255);
                        dialog.dismiss();
                    }
                }
                else {
                    v.getBackground().setAlpha(255);
                }
                return true;
            }
        });
        dialog.show();
    }
}