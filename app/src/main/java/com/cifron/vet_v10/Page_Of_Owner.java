package com.cifron.vet_v10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    ImageButton back;
    String token;
    Handler handler = new Handler();
    int localityId;
    int personId;
    int[] counts_of_animal = new int[5];
    TextView krs, mrs, camel, horse, pig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_of_owner);
        button = findViewById(R.id.id_constraintLayout_button_add_animal);
        streetOwner = findViewById(R.id.streeetOwner);
        nameOwner = findViewById(R.id.nameOwner);
        back = findViewById(R.id.backOwner);
        krs = findViewById(R.id.krs);
        mrs = findViewById(R.id.mrs);
        camel = findViewById(R.id.camel);
        horse = findViewById(R.id.horse);
        pig = findViewById(R.id.pig);

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
        Log.d("OnCreate", "Ok");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page_Of_Owner.this, Personal.class);
                startActivity(intent);
            }
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Page_Of_Owner.this, Personal.class);
        startActivity(intent);
    }

    class LoginPost extends Thread {
        String data;
        ProgressDialog progressDialog;

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(Page_Of_Owner.this);
                    progressDialog.setMessage("Checking data");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            try {
                SharedPreferences user = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor edit = user.edit();
                token = user.getString("token", "null");

                URL url = new URL("https://admin.vetqyzmet.kz/api/v1/getInfo");
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
                        counts_of_animal[2] = object.getInt("pig_count");
                        counts_of_animal[3] = object.getInt("horse_count");
                        counts_of_animal[4] = object.getInt("camel_count");
                    }
                }
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
                    camel.setText("Всего " +counts_of_animal[2]);
                    horse.setText("Всего " + counts_of_animal[3]);
                    pig.setText("Всего " + counts_of_animal[4]);
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        new GetToken().start();
        new LoginPost().start();
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
}