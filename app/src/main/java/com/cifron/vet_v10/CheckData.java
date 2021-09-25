package com.cifron.vet_v10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CheckData extends AppCompatActivity {

    Button finish;
    Button restart;
    ImageButton back;
    FrameLayout imageHolder1;
    FrameLayout imageHolder2;
    FrameLayout imageHolder3;
    FrameLayout plus1;
    FrameLayout plus2;
    FrameLayout plus3;
    ImageView deleteImage1;
    ImageView deleteImage2;
    ImageView deleteImage3;
    public static final int CAMERA_REQUEST=100;
    public static final int STORAGE_REQUEST=101;
    ImageView ima1;
    ImageView ima2;
    ImageView ima3;
    String[] cameraPermission;
    String[] storagePermission;
    int pressedImage = 2;
    String token = "";
    int gender = 0;
    Map<String, RequestBody> map;
    TextView checkType;
    TextView checkInj;
    TextView checkSex;
    TextView checkDate;
    TextView checkPorodaFront;
    TextView checkPoroda;
    TextView checkColor;
    ProgressDialog dataSendingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_data);
        checkType = findViewById(R.id.checkType);
        checkInj = findViewById(R.id.checkInj);
        checkSex = findViewById(R.id.checkSex);
        checkDate = findViewById(R.id.checkDate);
        checkPorodaFront = findViewById(R.id.checkPorodaFront);
        checkPoroda = findViewById(R.id.checkParoda);
        ima1 = findViewById(R.id.ima1);
        finish = findViewById(R.id.finish);
        restart = findViewById(R.id.restart);
        back = findViewById(R.id.сheckBackB);
        checkColor = findViewById(R.id.checkColor);
        finishIt();
        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            Bitmap path = (Bitmap) extras.get("imguri");
            ima1.setImageBitmap(path);
        }
        fillTextViews();
        SharedPreferences user = getSharedPreferences("user", MODE_PRIVATE);
        map = new HashMap<>();

        File cacheDir = this.getCacheDir();
        cacheDir.deleteOnExit();

        imageManipulation();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    private void fillTextViews() {
        checkType.setText(getIntent().getStringExtra("TypeOfAnimal"));
        checkInj.setText(getIntent().getStringExtra("INJ"));
        checkSex.setText(getIntent().getStringExtra("SexOfAnimal"));
        if (checkSex.getText().toString().equals("Самка")) {
            gender = 1;
        }
        else {
            gender = 2;
        }
        checkColor.setText(getIntent().getStringExtra("Color"));
        checkDate.setText(getIntent().getStringExtra("Date"));
        if (checkType.getText().toString().equals("КРС")) {
            checkPoroda.setText(getIntent().getStringExtra("Poroda"));
            checkPoroda.setVisibility(View.VISIBLE);
            checkPorodaFront.setVisibility(View.VISIBLE);
        }
        else {
            checkPoroda.setVisibility(View.GONE);
            checkPorodaFront.setVisibility(View.GONE);
        }
    }

    public void finishIt() {
        finish.setOnTouchListener((v, event) -> {
            Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
            if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Drawable buttonDrawable = finish.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    DrawableCompat.setTint(buttonDrawable, Color.rgb(255, 180, 88));
                    finish.setBackground(buttonDrawable);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Drawable buttonDrawable = finish.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    DrawableCompat.setTint(buttonDrawable, Color.rgb(255, 120, 0));
                    finish.setBackground(buttonDrawable);
                    sendData();
                }
            }
            else {
                Drawable buttonDrawable = finish.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, Color.rgb(255, 120, 0));
                finish.setBackground(buttonDrawable);
            }
            return true;
        });

        restart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        restart.setBackgroundResource(R.drawable.plus_button_background_gray);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        restart.setBackgroundResource(R.drawable.ic_white_button);
                        onBackPressed();
                    }
                }
                else {
                    restart.setBackgroundResource(R.drawable.ic_white_button);
                }
                return true;
            }
        });
    }

    public void sendData() {
        new GetToken().start();
        //Intent intent = new Intent(CheckData.this, DataSent.class);
        //new Sending().start();
        //checkHeader();
        sentCompletedForm();
    }


    public void imageManipulation() {
        imageHolder1 = findViewById(R.id.imageHolder1);
        imageHolder2 = findViewById(R.id.imageHolder2);
        imageHolder3 = findViewById(R.id.imageHolder3);
        plus1 = findViewById(R.id.plus1);
        plus2 = findViewById(R.id.plus2);
        plus3 = findViewById(R.id.plus3);
        deleteImage1 = findViewById(R.id.delete_image1);
        deleteImage2 = findViewById(R.id.delete_image2);
        deleteImage3 = findViewById(R.id.delete_image3);

        ima2 = findViewById(R.id.ima2);
        ima3 = findViewById(R.id.ima3);

        ima2.setVisibility(View.GONE);
        ima3.setVisibility(View.GONE);


        plus1.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        v.setBackgroundResource(R.drawable.plus_button_background_gray);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        v.setBackgroundResource(R.drawable.plus_button_background);
                        someMethod();
                        pressedImage = 1;


                    }
                }
                else {
                    v.setBackgroundResource(R.drawable.plus_button_background);
                }
                return true;
            }
        });


        plus2.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        v.setBackgroundResource(R.drawable.plus_button_background_gray);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        v.setBackgroundResource(R.drawable.plus_button_background);
                        someMethod();
                        pressedImage = 2;


                    }
                }
                else {
                    v.setBackgroundResource(R.drawable.plus_button_background);
                }
                return true;
            }
        });

        plus3.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        v.setBackgroundResource(R.drawable.plus_button_background_gray);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        v.setBackgroundResource(R.drawable.plus_button_background);
                        someMethod();
                        pressedImage = 3;


                    }
                }
                else {
                    v.setBackgroundResource(R.drawable.plus_button_background);
                }
                return true;
            }
        });

        deleteImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ima2.getVisibility() == View.GONE) {
                    ima1.setVisibility(View.GONE);
                    deleteImage1.setVisibility(View.GONE);

                    plus1.setVisibility(View.VISIBLE);
                    plus2.setVisibility(View.GONE);
                    finish.getBackground().setAlpha(128);
                    finish.setEnabled(false);
                }
                else {
                    if (ima3.getVisibility() == View.GONE) {
                        Bitmap bitmap = ((BitmapDrawable)ima2.getDrawable()).getBitmap();
                        ima1.setImageBitmap(bitmap);

                        ima2.setVisibility(View.GONE);
                        deleteImage2.setVisibility(View.GONE);

                        plus2.setVisibility(View.VISIBLE);
                        plus3.setVisibility(View.GONE);
                    }
                    else {
                        Bitmap bitmap3 = ((BitmapDrawable)ima3.getDrawable()).getBitmap();
                        Bitmap bitmap2 = ((BitmapDrawable)ima2.getDrawable()).getBitmap();
                        ima1.setImageBitmap(bitmap2);
                        ima2.setImageBitmap(bitmap3);

                        ima3.setVisibility(View.GONE);
                        deleteImage3.setVisibility(View.GONE);
                        plus3.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        deleteImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ima3.getVisibility() == View.GONE) {
                    deleteImage2.setVisibility(View.GONE);
                    ima2.setVisibility(View.GONE);

                    plus2.setVisibility(View.VISIBLE);
                    plus3.setVisibility(View.GONE);
                }
                else {
                    Bitmap bitmap = ((BitmapDrawable)ima3.getDrawable()).getBitmap();
                    ima2.setImageBitmap(bitmap);

                    ima3.setVisibility(View.GONE);
                    deleteImage3.setVisibility(View.GONE);
                    plus3.setVisibility(View.VISIBLE);
                }
            }
        });

        deleteImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage3.setVisibility(View.GONE);
                ima3.setVisibility(View.GONE);

                plus3.setVisibility(View.VISIBLE);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void someMethod() {
        int picd = 0;
        if (picd == 0) {
            if (!checkCameraPermission()) {
                requestCameraPermission();
            }
            else {
                pickFromGallery();
            }
        }
        else if (picd == 1) {
            if (!checkStoragePermission()) {
                requestStoragePermission();
            }
            else {
                pickFromGallery();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== 101) {
            //CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //Uri uri = result.getUri();
                if (pressedImage == 1) {
                    ima1.setVisibility(View.VISIBLE);
                    deleteImage1.setVisibility(View.VISIBLE);
                    plus1.setVisibility(View.GONE);
                    plus2.setVisibility(View.VISIBLE);
                    //Picasso.with(this).load(uri).into(ima1);
                }
                else if (pressedImage == 2) {
                    ima2.setVisibility(View.VISIBLE);
                    deleteImage2.setVisibility(View.VISIBLE);

                    plus2.setVisibility(View.GONE);
                    plus3.setVisibility(View.VISIBLE);
                    //Picasso.with(this).load(uri).into(ima2);

                }
                else {
                    plus3.setVisibility(View.GONE);
                    ima3.setVisibility(View.VISIBLE);
                    deleteImage3.setVisibility(View.VISIBLE);
                    //Picasso.with(this).load(uri).into(ima3);
                }
                finish.getBackground().setAlpha(255);
                finish.setEnabled(true);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == (PackageManager.PERMISSION_GRANTED);
                    boolean storage_accepted = grantResults[1] == (PackageManager.PERMISSION_GRANTED);
                    if (camera_accepted && storage_accepted) {
                        pickFromGallery();
                    }
                    else {
                        Toast.makeText(this, "Please enable camera and storage permission", Toast.LENGTH_SHORT);
                    }
                }
                break;
            }
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean storage_accepted = grantResults[0] == (PackageManager.PERMISSION_GRANTED);
                    if (storage_accepted) {
                        pickFromGallery();
                    }
                    else {
                        Toast.makeText(this, "Please enable storage permission", Toast.LENGTH_SHORT);
                    }
                }
                break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void pickFromGallery() {
        //CropImage.activity().start(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);

    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
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
                    Log.d("token", token);
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

            //checkHeader();
        }
    }



        public void sentCompletedForm() {
            dataSendingDialog = new ProgressDialog(CheckData.this);
            dataSendingDialog.setMessage("Checking data");
            dataSendingDialog.setCancelable(false);
            dataSendingDialog.show();
            SharedPreferences user = getSharedPreferences("user", MODE_PRIVATE);
            token = user.getString("token", "null");
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder()
                            .header("Authorization", "Bearer " + token)
                            .header("Accept", "multipart/form-data")
                            .build();
                    return chain.proceed(newRequest);
                }
            }).connectTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).build();

            SharedPreferences localData = getSharedPreferences("localData", MODE_PRIVATE);

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("owner_id", "" + localData.getInt("personId", 0)).addFormDataPart("name", "Korovka")
                    .addFormDataPart("ING",  checkInj.getText().toString())
                    .addFormDataPart("gender", "" + gender)
                    .addFormDataPart("breed", "" + getIntent().getIntExtra("breedId", 1))
                    .addFormDataPart("Date_of_Birth", dateFormatter(checkDate.getText().toString()))
                    .addFormDataPart("suit", "" + getIntent().getIntExtra("ColorId", 1));

            File file1 = createTempFile(((BitmapDrawable)ima1.getDrawable()).getBitmap(), 1);
            Bitmap bmp = BitmapFactory.decodeFile(file1.getAbsolutePath());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 30, bos);
            builder.addFormDataPart("photo1", file1.getName(), RequestBody.create(MultipartBody.FORM, bos.toByteArray()));

            if (ima2.getVisibility() != View.GONE) {
                File file2 = createTempFile(((BitmapDrawable)ima2.getDrawable()).getBitmap(), 2);
                Bitmap bmp2 = BitmapFactory.decodeFile(file2.getAbsolutePath());
                ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
                bmp2.compress(Bitmap.CompressFormat.JPEG, 30, bos2);
                builder.addFormDataPart("photo2", file2.getName(), RequestBody.create(MultipartBody.FORM, bos2.toByteArray()));

            }
            if (ima3.getVisibility() != View.GONE) {
                File file3 = createTempFile(((BitmapDrawable)ima3.getDrawable()).getBitmap(), 3);
                Bitmap bmp3 = BitmapFactory.decodeFile(file3.getAbsolutePath());
                ByteArrayOutputStream bos3 = new ByteArrayOutputStream();
                bmp3.compress(Bitmap.CompressFormat.JPEG, 30, bos3);
                builder.addFormDataPart("photo3", file3.getName(), RequestBody.create(MultipartBody.FORM, bos3.toByteArray()));

            }
            builder.addFormDataPart("animal_type", "" + getIntent().getIntExtra("checkedTypeId", 1));



            Retrofit retrofit = new Retrofit.Builder().client(client).baseUrl("https://admin.vetqyzmet.kz/api/v1/").addConverterFactory(GsonConverterFactory.create()).build();
            RetroApi retroApi = retrofit.create(RetroApi.class);
            RequestBody requestBody = builder.build();
            Call<DataAnimalWithImage> call = retroApi.uploadAllData(requestBody);
            call.enqueue(new Callback<DataAnimalWithImage>() {
                @Override
                public void onResponse(Call<DataAnimalWithImage> call, retrofit2.Response<DataAnimalWithImage> response) {
                    Log.d("resp", response.toString());
                    Log.d("respM", response.message());
                    if (response.isSuccessful()) {
                        Intent intent = new Intent(CheckData.this, DataSent.class);
                        startActivity(intent);
                    }
                    if (dataSendingDialog.isShowing()) {
                        dataSendingDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<DataAnimalWithImage> call, Throwable t) {
                    Log.d("no", "baby");
                    if (dataSendingDialog.isShowing()) {
                        dataSendingDialog.dismiss();
                    }
                }
            });
        }

    private File createTempFile(Bitmap bitmap, int which) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ima" + which + ""
                + "_image.jpeg");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
//write the bytes in file

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            Log.d("file writed", "times");
        } catch (IOException e) {
            Log.d("file can not be written", "pidr");
        }
        return file;
    }

    public String dateFormatter(String date) {
        String year = date.substring(0, 4);
        String month = date.substring(6);
        String format = year + "." + intFormatOfMonth(month);
        return format;
    }

    public String intFormatOfMonth(String month) {
        if (month.equals("Январь")) {
            return "01";
        }
        else if (month.equals("Февраль")){
            return "02";
        }
        else if (month.equals("Март")){
            return "03";
        }
        else if (month.equals("Апрель")){
            return "04";
        }
        else if (month.equals("Май")){
            return "05";
        }
        else if (month.equals("Июнь")){
            return "06";
        }
        else if (month.equals("Июль")){
            return "07";
        }
        else if (month.equals("Август")){
            return "08";
        }
        else if (month.equals("Сентябрь")){
            return "09";
        }
        else if (month.equals("Октябрь")){
            return "10";
        }
        else if (month.equals("Ноябрь")) {
            return "11";
        }
        else {
            return "12";
        }
    }
}