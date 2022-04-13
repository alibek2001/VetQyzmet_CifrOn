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

public class Personal extends AppCompatActivity {
    TextView tv;
    ImageButton ib;
    RecyclerView recyclerView;
    Handler handler = new Handler();
    String token = "";
    PersonAdapter adapter;
    TextView forName;
    String cityName;
    SwipeRefreshLayout refreshLayout;
    int localityId;
    ProgressDialog progressDialog;
    List<StreetAndPerson> list;
    TextView pusto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        recyclerView = findViewById(R.id.pRecycler);
        ib = findViewById(R.id.back);
        SharedPreferences localData = getSharedPreferences("localData", MODE_PRIVATE);
        SharedPreferences.Editor edit = localData.edit();
        pusto = findViewById(R.id.visInvis);
        refreshLayout = findViewById(R.id.refresherForPer);
        forName = findViewById(R.id.formName);
        cityName = localData.getString("Path", "null");
        localityId = localData.getInt("localityId", -1);
        forName.setText(cityName);

        handler.post(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(Personal.this);
                progressDialog.setMessage("Загрузка данных");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        });

        list = new ArrayList<>();
        list.add(new StreetAndPerson("Евгений Кориенко","Ерубаева 7, 2", -1, -1));
        new GetToken().start();
        LoginPost loginPost = new LoginPost();
        loginPost.start();
        swiperUp();
        prevAcc();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void swiperUp() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                list.add(new StreetAndPerson("Евгений Кориенко","Ерубаева 7, 2", -1, -1));
                new GetToken().start();
                new LoginPost().start();

                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        });
    }


    public void setAdapt(List<StreetAndPerson> list) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PersonAdapter(this, list);
        recyclerView.setAdapter(adapter);
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
        Intent intent = new Intent(Personal.this, Audandar.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    class LoginPost extends Thread {
        String data;

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


                InputStream is = con.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
                String line;
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

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
                    Log.d("ssss", someString);
                }
                
                for (int i = 0;i < oneData.length();i++) {
                    JSONObject object = oneData.getJSONObject(i);
                    list.add(new StreetAndPerson(object.getString("name") + " " + object.getString("surname"), object.getString("street") + " " + object.getString("home"), localityId, object.getInt("id")));
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
                    setAdapt(list);
                    if (list.size() == 1) {
                        pusto.setVisibility(View.VISIBLE);
                    }
                    else {
                        pusto.setVisibility(View.GONE);
                    }
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
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
                pusto.setText("Проверьте подключение к сети");
                Log.d("Inet", "Проверьте подключение к сети");
            }

        }
    }
}