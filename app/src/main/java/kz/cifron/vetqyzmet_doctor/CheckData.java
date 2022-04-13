 package kz.cifron.vetqyzmet_doctor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
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
import android.provider.MediaStore;
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
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
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
    int pressedImage = 2;
    String token = "";
    int gender = 0;
    Uri imUri;
    TextView checkType;
    TextView checkInj;
    TextView checkSex;
    TextView checkDate;
    TextView checkPorodaFront;
    TextView checkPoroda;
    TextView checkColor;
    TextView suitFront;
    ProgressDialog dataSendingDialog;

    @RequiresApi(api = Build.VERSION_CODES.M)
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
        suitFront = findViewById(R.id.suitFront);
        finish.getBackground().setAlpha(128);
        finish.setEnabled(false);
        finishIt();
        someMethod();
        pressedImage = 1;
        fillTextViews();

        File cacheDir = this.getCacheDir();
        cacheDir.deleteOnExit();

        imageManipulation();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        prevAcc();
    }


    private void fillTextViews() {
        checkType.setText(getIntent().getStringExtra("TypeOfAnimal"));
        checkInj.setText(getIntent().getStringExtra("INJ"));
        checkSex.setText(getIntent().getStringExtra("SexOfAnimal"));
        int breedId = getIntent().getIntExtra("breedId", -1);
        int colorId = getIntent().getIntExtra("ColorId", -1);
        if (checkSex.getText().toString().equals("Самка")) {
            gender = 1;
        }
        else {
            gender = 2;
        }
        if (colorId != - 1) {
            checkColor.setText(getIntent().getStringExtra("Color"));
            checkColor.setVisibility(View.VISIBLE);
            suitFront.setVisibility(View.VISIBLE);
        }
        else {
            checkColor.setVisibility(View.GONE);
            suitFront.setVisibility(View.GONE);
        }
        checkDate.setText(getIntent().getStringExtra("Date"));
        if (breedId != -1) {
            checkPoroda.setText(getIntent().getStringExtra("Poroda"));
            checkPoroda.setVisibility(View.VISIBLE);
            checkPorodaFront.setVisibility(View.VISIBLE);
        }
        else {
            checkPoroda.setVisibility(View.GONE);
            checkPorodaFront.setVisibility(View.GONE);
        }

    }

    public void prevAcc() {
        back.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
                        finish.setEnabled(true);
                        finish.getBackground().setAlpha(255);


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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permission, CAMERA_REQUEST);
        }

        else {
            openCamera();
        }
    }
    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Desc");
        imUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imUri);
        setResult(Activity.RESULT_OK, intent);
        startActivityForResult(intent, STORAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== STORAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imUri);
                    createTempFile(bitmap, pressedImage);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (pressedImage == 1) {
                    ima1.setVisibility(View.VISIBLE);
                    deleteImage1.setVisibility(View.VISIBLE);
                    plus1.setVisibility(View.GONE);
                    plus2.setVisibility(View.VISIBLE);
                    //ima1.setImageBitmap(bitmap);
                    //ima1.setRotation(450.0f);
                    finish.getBackground().setAlpha(255);
                    finish.setEnabled(true);
                    Picasso.with(this).load(imUri).into(ima1);
                }
                else if (pressedImage == 2) {
                    ima2.setVisibility(View.VISIBLE);
                    deleteImage2.setVisibility(View.VISIBLE);

                    plus2.setVisibility(View.GONE);
                    plus3.setVisibility(View.VISIBLE);
                    //ima2.setImageBitmap(bitmap);
                    //ima2.setRotation(450.0f);
                    Picasso.with(this).load(imUri).into(ima2);

                }
                else {
                    plus3.setVisibility(View.GONE);
                    ima3.setVisibility(View.VISIBLE);
                    deleteImage3.setVisibility(View.VISIBLE);
                    //ima3.setImageBitmap(bitmap);
                    //ima3.setRotation(450.0f);
                    Picasso.with(this).load(imUri).into(ima3);
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
                        openCamera();
                    } else {
                        Toast.makeText(this, "Please enable camera and storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean storage_accepted = grantResults[0] == (PackageManager.PERMISSION_GRANTED);
                    if (storage_accepted) {
                        openCamera();
                    } else {
                        Toast.makeText(this, "Please enable storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
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

        }
    }



        public void sentCompletedForm() {
            dataSendingDialog = new ProgressDialog(CheckData.this);
            dataSendingDialog.setMessage("Отправка данных");
            dataSendingDialog.setCancelable(false);
            dataSendingDialog.show();
            SharedPreferences user = getSharedPreferences("user", MODE_PRIVATE);
            token = user.getString("token", token);
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
            builder.addFormDataPart("statement_id", "" + localData.getInt("personId", 0))
                    .addFormDataPart("name", "Korovka")
                    .addFormDataPart("ING",  checkInj.getText().toString())
                    .addFormDataPart("gender", "" + gender)
                    .addFormDataPart("breed", "" + getIntent().getIntExtra("breedId", 0))
                    .addFormDataPart("Date_of_Birth", dateFormatter(checkDate.getText().toString()))
                    .addFormDataPart("suit", "" + getIntent().getIntExtra("ColorId", 0));

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
            builder.addFormDataPart("animal_type", "" + getIntent().getIntExtra("checkedTypeId", 1));
            if (ima3.getVisibility() != View.GONE) {
                File file3 = createTempFile(((BitmapDrawable)ima3.getDrawable()).getBitmap(), 3);
                Bitmap bmp3 = BitmapFactory.decodeFile(file3.getAbsolutePath());
                ByteArrayOutputStream bos3 = new ByteArrayOutputStream();
                bmp3.compress(Bitmap.CompressFormat.JPEG, 30, bos3);
                builder.addFormDataPart("photo3", file3.getName(), RequestBody.create(MultipartBody.FORM, bos3.toByteArray()));

            }



            Retrofit retrofit = new Retrofit.Builder().client(client).baseUrl("https://admin.vetqyzmet.kz/api/v6/").addConverterFactory(GsonConverterFactory.create()).build();
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
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    else {
                        onFailSendData();
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
                    onFailSendData();
                }
            });
        }

    private File createTempFile(Bitmap bitmap, int which) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ima" + which + ""
                + "_image.jpeg");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos);
        byte[] bitmapdata = bos.toByteArray();

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
        String monthAndDay = date.substring(6);
        String[] arr = monthAndDay.split(" ");
        if (Integer.parseInt(arr[0]) <= 9) {
            arr[0] = "0" + arr[0];
        }
        String format = year + "." + intFormatOfMonth(arr[1]) + "." + arr[0];
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

    public void onFailSendData() {
        new AlertDialog.Builder(this).setTitle("Данные не отправлены").setCancelable(false).setNegativeButton("Повторить заново", null).create().show();

    }
}