package kz.cifron.vetqyzmet_doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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
import java.util.ArrayList;
import java.util.List;

public class EachAnimal extends AppCompatActivity {
    ImageButton imageButton;
    RecyclerView recyclerView;
    List<CardInfo> list;
    AnimalCardAdapter adapter;
    Handler handler = new Handler();
    int personId;
    String token;
    String ing;
    String dateOfBirth;
    String breed;
    String suit;
    int stateId;
    String gender;
    String photo1;
    String photo2;
    String photo3;
    TextView type;
    String originalPath = "https://admin.vetqyzmet.kz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_animal);
        imageButton = findViewById(R.id.back);
        type = findViewById(R.id.formName);
        type.setText(getIntent().getStringExtra("type"));
        back();
        recyclerView = findViewById(R.id.eachRecycler);
        list = new ArrayList<>();


        SharedPreferences localData = getSharedPreferences("localData", MODE_PRIVATE);
        personId = localData.getInt("personId", -1);

        new GetToken().start();
        new GetData().start();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    public void back() {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EachAnimal.this, Page_Of_Owner.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    public void setAdapt() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new AnimalCardAdapter(this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    public String dateCorrector(String date) {
        String[] arr = date.split("\\.");
        String right = "";
        for (int i = arr.length - 1;i >= 0;i--) {
            right = right + arr[i];
            if (i != 0) {
                right = right + ".";
            }
        }
        return right;
    }

    class GetData extends Thread {
        @Override
        public void run() {
            try {
                SharedPreferences user = getSharedPreferences("user", MODE_PRIVATE);
                token = user.getString("token", "null");

                URL url = new URL("https://admin.vetqyzmet.kz/api/v6/tasksByStatement/" + personId + "?animal_type_id=" + getIntent().getIntExtra("id", 1));
                Log.d("personId", personId + "");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Authorization", "Bearer " + token);
                con.setReadTimeout(10000);
                con.setConnectTimeout(10000);
                con.setDoInput(true);

                InputStream is = con.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));

                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                JSONArray array = new JSONArray(response.toString());
                for (int i = 0;i < array.length();i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    stateId = jsonObject.getInt("statement_id");
                    Log.d("stateId", stateId + "");
                    ing = jsonObject.getString("ING");
                    photo1 = jsonObject.getString("photo1");
                    photo1 = originalPath + photo1;
                    photo2 = jsonObject.getString("photo2");
                    if (!photo2.equals("null")) {
                        photo2 = originalPath + photo2;
                        Log.d("photo2", photo2);
                    }
                    photo3 = jsonObject.getString("photo3");

                    if (!photo3.equals("null")) {
                        photo3 = originalPath + photo3;
                    }
                    gender = jsonObject.getString("gender");
                    breed = "" + jsonObject.getString("breed");
                    suit = "" + jsonObject.getString("suit");
                    dateOfBirth = dateCorrector(jsonObject.getString("Date_of_Birth"));
                    list.add(new CardInfo(ing, photo1, photo2, photo3, gender + "", dateOfBirth, breed, suit));

                }

                Log.d("Data", "Data uploaded");

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
                    setAdapt();
                }
            });
        }
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

