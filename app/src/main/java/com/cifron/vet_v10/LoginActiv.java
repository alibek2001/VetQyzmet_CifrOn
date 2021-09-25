package com.cifron.vet_v10;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;


import org.json.JSONException;

import org.json.JSONObject;
import org.json.JSONArray;

public class LoginActiv extends AppCompatActivity {
    TextView today;
    Button button;
    EditText username;
    EditText password;
    TextView txt;
    String data = "";
    String token = "";
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        today = findViewById(R.id.today);
        username = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        txt = findViewById(R.id.textView);
        textEn();
        String now = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            now = LocalDate.now().toString();
        }
        String[] splitted = now.split("-");
        String month = splitted[1];
        String day = Integer.parseInt(splitted[2]) + "";
        switch (month)  {
            case "01" : {
                today.setText("Сегодня " + day + " января");
                break;
            }
            case "02" : {
                today.setText("Сегодня " + day + " февраля");
                break;
            }
            case "03" : {
                today.setText("Сегодня " + day + " марта");
                break;
            }
            case "04" : {
                today.setText("Сегодня " + day + " апреля");
                break;
            }
            case "05" : {
                today.setText("Сегодня " + day + " мая");
                break;
            }
            case "06" : {
                today.setText("Сегодня " + day + " июня");
                break;
            }
            case "07" : {
                today.setText("Сегодня " + day + " июля");
                break;
            }
            case "08" : {
                today.setText("Сегодня " + day + " августа");
                break;
            }
            case "09" : {
                today.setText("Сегодня " + day + " сентября");
                break;
            }
            case "10" : {
                today.setText("Сегодня " + day + " октября");
                break;
            }
            case "11" : {
                today.setText("Сегодня " + day + " ноября");
                break;
            }
            case "12" : {
                today.setText("Сегодня " + day + " декабря");
                break;
            }
        }
        button = findViewById(R.id.enter);
        button.getBackground().setAlpha(128);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            Drawable buttonDrawable = button.getBackground();
                            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                            DrawableCompat.setTint(buttonDrawable, Color.rgb(255, 180, 88));
                            button.setBackground(buttonDrawable);
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            Drawable buttonDrawable = button.getBackground();
                            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                            DrawableCompat.setTint(buttonDrawable, Color.rgb(255, 120, 0));
                            button.setBackground(buttonDrawable);
                            new LoginPost().start();
                        }
                }
                else {
                    Drawable buttonDrawable = button.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    DrawableCompat.setTint(buttonDrawable, Color.rgb(255, 120, 0));
                    button.setBackground(buttonDrawable);
                }
                return true;
            }
        });

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    public void nextAcc() {
        SharedPreferences user = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = user.edit();
        editor.putString("username", username.getText().toString());
        editor.putString("userpassword", password.getText().toString());
        editor.putString("token", token);
        editor.apply();

        SharedPreferences password = getSharedPreferences("fastPass", MODE_PRIVATE);
        String cur = password.getString("fastPassword", "null");
        if (!cur.equals("null")) {
            Intent intent = new Intent(this, VetQyzmetPage.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, FourPasswordIn.class);
            startActivity(intent);
        }
        finish();
    }

    public void textEn() {
        username.addTextChangedListener(loginTextWatcher);
        password.addTextChangedListener(loginTextWatcher);
    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String usernameInput = username.getText().toString().trim();
            String passwordInput = password.getText().toString().trim();

            if (!usernameInput.isEmpty() && !passwordInput.isEmpty()) {
                button.setEnabled(true);
                button.getBackground().setAlpha(255);
            }
            else {
                button.setEnabled(false);
                button.getBackground().setAlpha(128);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Вы действительно хотите выйти").setCancelable(false).setNegativeButton("Нет", null).setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity();

            }
        }).create().show();
    }


    class LoginPost extends Thread {

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(LoginActiv.this);
                    progressDialog.setMessage("Checking data");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            try {
                URL url = new URL("https://admin.vetqyzmet.kz/api/v1/auth");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setReadTimeout(10000);
                con.setConnectTimeout(10000);
                con.setDoOutput(true);
                    String jsonInputString = "{\"email\" : \"" + username.getText().toString().trim() + "\", \"password\" : \"" + password.getText().toString().trim() + "\"}";
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                if (!token.isEmpty()) {
                        nextAcc();
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


            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

}