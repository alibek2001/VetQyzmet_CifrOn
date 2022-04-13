package kz.cifron.vetqyzmet_doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
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

public class Audandar extends AppCompatActivity {
    TextView tv;
    ImageButton ib;
    RecyclerView recyclerView;
    TextView audan_Name;
    CityAdapter cityAdapter;
    ProgressDialog progressDialog;
    Handler handler = new Handler();
    String token = "";
    List<NameCity> list;
    JSONArray jsonArray;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView pusto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audandar);

        tv = findViewById(R.id.formName);
        ib = findViewById(R.id.back);
        recyclerView = findViewById(R.id.aRecycler);
        audan_Name = findViewById(R.id.audan_name);
        pusto = findViewById(R.id.visInvis);
        swipeRefreshLayout = findViewById(R.id.refresher);
        list = new ArrayList<>();
        list.add(new NameCity("с. Аккала", 23, "", -1));

        prevAcc();
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(Audandar.this);
                progressDialog.setMessage("Загрузка данных");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        });
        new GetToken().start();
        new LoginPost().start();
        swiperUp();


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void swiperUp() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                list.add(new NameCity("с. Аккала", 23, "", -1));
                new GetToken().start();
                new LoginPost().start();
                cityAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void setAdapt(List<NameCity> list) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        cityAdapter = new CityAdapter(this, list);
        recyclerView.setAdapter(cityAdapter);
    }

    public void prevAcc() {
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Audandar.this, VetQyzmetPage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    class LoginPost extends Thread {

        @Override
        public void run() {

            try {
                SharedPreferences user = getSharedPreferences("user", MODE_PRIVATE);
                token = user.getString("token", "null");

                URL url = new URL("https://admin.vetqyzmet.kz/api/v7/getInfo");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Authorization", "Bearer " + token);
                con.setReadTimeout(10000);
                con.setConnectTimeout(10000);
                con.setDoInput(true);
                con.connect();
                if (con.getResponseCode() == 200) {
                    InputStream is = con.getInputStream();

                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    JSONArray array = new JSONArray(response.toString());
                    jsonArray = new JSONArray(response.toString());
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object = array.getJSONObject(i);
                        String someString = object.getString("locality_name");
                        JSONArray data = object.getJSONArray("data");
                        int id = Integer.parseInt(object.getString("id"));
                        list.add(new NameCity(someString, data.length(), "", id));
                    }

                    Log.d("Data", "Data uploaded");
                }

                con.disconnect();

            } catch (UnsupportedEncodingException e) {
                Log.d("Khuina", e.getMessage());
            } catch (ProtocolException e) {
                Log.d("Proto", e.getMessage());
            } catch (MalformedURLException e) {
                Log.d("URL", e.getMessage());
            } catch (IOException e) {
                pusto.setText("Проверьте подключение к сети");
                Log.d("Inet", "Проверьте подключение к сети");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            handler.post(new Runnable() {
                @Override
                public void run() {
//                    list.remove(0);
                    setAdapt(list);
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (list.size() == 1) {
                        pusto.setVisibility(View.VISIBLE);
                    }
                    else {
                        pusto.setVisibility(View.GONE);
                    }
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