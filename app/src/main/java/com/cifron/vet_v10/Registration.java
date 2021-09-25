package com.cifron.vet_v10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



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
import java.util.Calendar;

public class Registration extends AppCompatActivity {

    DatePickerDialog datePickerDialog;
    ImageButton back;
    String token= "";
    Handler handler = new Handler();
    Button cancel;
    Button next;
    ProgressDialog progressDialog;
    public static final int CAMERA_REQUEST=100;
    public static final int STORAGE_REQUEST=101;
    Uri imUri;
    String[] cameraPermission;
    String[] storagePermission;
    LinearLayout spinner;
    LinearLayout spinner1;
    RelativeLayout dateholder;
    LinearLayout spinner2;
    EditText regNVP;
    RadioGroup radioGroup;
    RadioButton radioButton1;
    RadioButton radioButton2;
    ArrayList<String> typesList = new ArrayList<>();
    ArrayList<Integer> typesIdList = new ArrayList<>();
    int checkedTypeId;
    ArrayList<String> breedList = new ArrayList<>();
    ArrayList<Integer> breedIdList = new ArrayList<>();
    int breedId;
    ArrayList<String> colorList = new ArrayList<>();
    ArrayList<Integer> colorIdList = new ArrayList<>();
    int colorId;
    String typeOfAnimal = "Крупный рогатый скот";
    String porodaOfCow = "Ангус";
    String colorOfAnimal = "Белый";
    TextView dateText;
    TextView aniname;
    TextView poroda;
    TextView porodaFront;
    TextView color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        radioGroup = findViewById(R.id.sexOfAnimal);
        dateholder = findViewById(R.id.date_holder);
        dateText = findViewById(R.id.dateText);
        aniname = findViewById(R.id.Aniname);
        color = findViewById(R.id.color);
        poroda = findViewById(R.id.poroda);
        porodaFront = findViewById(R.id.porodaFront);
        dateDi();
        radioButton1 = findViewById(R.id.radioButton);
        radioButton2 = findViewById(R.id.radioButton2);
        regNVP = findViewById(R.id.regNVP);
        regNVP.addTextChangedListener(textWatcher);
        focusToEditText();

        back = findViewById(R.id.regBack);
        cancel = findViewById(R.id.regCancel);
        next = findViewById(R.id.regNext);
        next.getBackground().setAlpha(128);
        spinner = findViewById(R.id.Birthspinner);
        spinner2 = findViewById(R.id.Colorspinner);
        spinner1 =findViewById(R.id.Identspinner);
        prev();
        next();
        new GetToken().start();
        new TypesPost().start();
        new GetToken().start();
        new BreedPost().start();
        new GetToken().start();
        new ColorPost().start();
        buttonBas();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    public void focusToEditText() {
        regNVP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    regNVP.setBackgroundResource(R.drawable.orange_stroke_but);
                }
                else {
                    regNVP.setBackgroundResource(R.drawable.white_stroke_but);
                }
            }
        });
    }


    public void prev() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        cancel.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        cancel.setBackgroundResource(R.drawable.plus_button_background_gray);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        cancel.setBackgroundResource(R.drawable.ic_white_button);
                        onBackPressed();
                    }
                }
                else {
                    cancel.setBackgroundResource(R.drawable.ic_white_button);
                }
                return true;
            }
        });
    }
    public void next() {
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        next.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        Drawable buttonDrawable = next.getBackground();
                        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                        DrawableCompat.setTint(buttonDrawable, Color.rgb(255, 180, 88));
                        next.setBackground(buttonDrawable);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        Drawable buttonDrawable = next.getBackground();
                        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                        DrawableCompat.setTint(buttonDrawable, Color.rgb(255, 120, 0));
                        next.setBackground(buttonDrawable);
                        someMethod();
                    }
                }
                else {
                    Drawable buttonDrawable = next.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    DrawableCompat.setTint(buttonDrawable, Color.rgb(255, 120, 0));
                    next.setBackground(buttonDrawable);
                }
                return true;
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
        startActivityForResult(intent, STORAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==STORAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(this, CheckData.class);
                intent.putExtra("imguri", bitmap);
                intent.putExtra("TypeOfAnimal", aniname.getText().toString());
                intent.putExtra("INJ", regNVP.getText().toString());
                intent.putExtra("Date", dateText.getText().toString());
                intent.putExtra("ColorId", colorId);
                intent.putExtra("checkedTypeId", checkedTypeId);
                intent.putExtra("breedId", breedId);
                if (radioButton2.isChecked()) {
                    intent.putExtra("SexOfAnimal", "Самец");
                }
                else {
                    intent.putExtra("SexOfAnimal", "Самка");
                }
                intent.putExtra("Color", color.getText().toString());
                if (aniname.getText().toString().equals("Крупный рогатый скот")) {
                    intent.putExtra("Poroda", porodaOfCow);
                }
                startActivity(intent);
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
                    }
                    else {
                        Toast.makeText(this, "Please enable camera and storage permission", Toast.LENGTH_SHORT);
                    }
                }
                break;
            }
        }
    }


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String first = regNVP.getText().toString().trim();

            if (!first.isEmpty()) {
                next.setEnabled(true);
                next.getBackground().setAlpha(255);
            }
            else {
                next.setEnabled(false);
                next.getBackground().setAlpha(128);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    public void buttonBas() {
        spinner1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                showCustomAlertDiType();
            }
        });
        spinner.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                porodaDi();
            }
        });

        spinner2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                colorDi();
            }
        });
    }
    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void showCustomAlertDiType() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_alert_di_type);
        RadioGroup rg = dialog.findViewById(R.id.groupOfColor);
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[] {getResources().getColor(R.color.orange) }
        );
        for (int i = 0;i < typesIdList.size();i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(typesList.get(i));
            radioButton.setId(i + 100);
            radioButton.setButtonTintList(colorStateList);
            if (radioButton.getId() == 100) {
                radioButton.setChecked(true);
            }
            rg.addView(radioButton);
        }
        for (int i = 0;i < rg.getChildCount();i++) {
            RadioButton btn = (RadioButton) rg.getChildAt(i);
            if (btn.getText().toString().trim().equals(typeOfAnimal)) {
                btn.setChecked(true);
            }
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCount = group.getChildCount();
                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId) {
                        typeOfAnimal = btn.getText().toString();
                    }
                }
            }
        });
        dialog.setCancelable(false);
        Button button = dialog.findViewById(R.id.okAlertB);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                aniname.setText(typeOfAnimal);
                if (!typeOfAnimal.equals("Крупный рогатый скот")) {
                    spinner.setVisibility(View.GONE);
                    porodaFront.setVisibility(View.GONE);
                }
                else {
                    spinner.setVisibility(View.VISIBLE);
                    porodaFront.setVisibility(View.VISIBLE);
                }
                checkedTypeId = typesIdList.get(rg.getCheckedRadioButtonId() - 100);
            }
        });
        dialog.show();
    }

    private void dateDi() {
        dateholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String data = makeDataString(dayOfMonth, month, year);
                        dateText.setText(data);
                    }

                };
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                int style = android.app.AlertDialog.THEME_HOLO_LIGHT;
                datePickerDialog = new DatePickerDialog(Registration.this, style, dateSetListener, year, month,day);

                datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("day","id","android")).setVisibility(View.GONE);
                datePickerDialog.show();
            }
        });
    }

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void porodaDi() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_alert_poroda);
        RadioGroup rg = dialog.findViewById(R.id.groupOfPoroda);
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[] {getResources().getColor(R.color.orange) }
        );
        for (int i = 0;i < breedIdList.size();i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(breedList.get(i));
            radioButton.setId(i + 100);
            radioButton.setButtonTintList(colorStateList);
            if (radioButton.getId() == 100) {
                radioButton.setChecked(true);
            }
            rg.addView(radioButton);
        }
        for (int i = 0;i < rg.getChildCount();i++) {
            RadioButton btn = (RadioButton) rg.getChildAt(i);
            if (btn.getText().toString().trim().equals(porodaOfCow)) {
                btn.setChecked(true);
            }
        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCount = group.getChildCount();
                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId) {
                        porodaOfCow = btn.getText().toString();
                    }
                }
            }
        });
        dialog.setCancelable(false);
        Button button = dialog.findViewById(R.id.okAlertB);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                poroda.setText(porodaOfCow);
                breedId = breedIdList.get(rg.getCheckedRadioButtonId() - 100);
            }
        });
        dialog.show();
    }

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void colorDi() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_alert_color);
        RadioGroup rg = dialog.findViewById(R.id.groupOfColor);
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[] {getResources().getColor(R.color.orange) }
        );
        for (int i = 0;i < breedIdList.size();i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(colorList.get(i));
            radioButton.setId(i + 100);
            radioButton.setButtonTintList(colorStateList);
            if (radioButton.getId() == 100) {
                radioButton.setChecked(true);
            }
            rg.addView(radioButton);
        }
        for (int i = 0;i < rg.getChildCount();i++) {
            RadioButton btn = (RadioButton) rg.getChildAt(i);
            if (btn.getText().toString().trim().equals(colorOfAnimal)) {
                btn.setChecked(true);
            }
        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCount = group.getChildCount();
                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId) {
                        colorOfAnimal = btn.getText().toString();
                    }
                }
            }
        });
        dialog.setCancelable(false);
        Button button = dialog.findViewById(R.id.okAlertB);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                color.setText(colorOfAnimal);
                colorId = colorIdList.get(rg.getCheckedRadioButtonId() - 100);
            }
        });
        dialog.show();
    }
    private String makeDataString(int day, int month, int year) {
        return year + ", " + getMonthFormat(month);
    }

    private String getMonthFormat(int month) {
        if (month == 1) {
            return "Январь";
        }
        else if (month == 2){
            return "Февраль";
        }
        else if (month == 3){
            return "Март";
        }
        else if (month == 4){
            return "Апрель";
        }
        else if (month == 5){
            return "Май";
        }
        else if (month == 6){
            return "Июнь";
        }
        else if (month == 7){
            return "Июль";
        }
        else if (month == 8){
            return "Август";
        }
        else if (month == 9){
            return "Сентябрь";
        }
        else if (month == 10){
            return "Октябрь";
        }
        else if (month == 11) {
            return "Ноябрь";
        }
        else {
            return "Декабрь";
        }
    }

    class TypesPost extends Thread {
        String data;

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(Registration.this);
                    progressDialog.setMessage("Checking data");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            try {
                SharedPreferences user = getSharedPreferences("user", MODE_PRIVATE);
                token = user.getString("token", "null");

                URL url = new URL("https://admin.vetqyzmet.kz/api/animalType");
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
                for (int i = 0;i < array.length();i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    typesIdList.add(jsonObject.getInt("id"));
                    typesList.add(jsonObject.getString("name"));
                }
                typeOfAnimal = typesList.get(0);

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

        }

    }

    class BreedPost extends Thread {
        @Override
        public void run() {
            try {
                SharedPreferences user = getSharedPreferences("user", MODE_PRIVATE);
                token = user.getString("token", "null");

                URL url = new URL("https://admin.vetqyzmet.kz/api/breed");
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
                for (int i = 0;i < array.length();i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    breedIdList.add(jsonObject.getInt("id"));
                    breedList.add(jsonObject.getString("name"));
                }
                porodaOfCow = breedList.get(0);

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
        }
    }

    class ColorPost extends Thread {
        @Override
        public void run() {
            try {
                SharedPreferences user = getSharedPreferences("user", MODE_PRIVATE);
                token = user.getString("token", "null");

                URL url = new URL("https://admin.vetqyzmet.kz/api/suit");
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
                for (int i = 0;i < array.length();i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    colorIdList.add(jsonObject.getInt("id"));
                    colorList.add(jsonObject.getString("name"));
                }
                colorOfAnimal = colorList.get(0);

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
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    showCustomAlertDiType();
                    aniname.setText(typeOfAnimal);
                    poroda.setText(porodaOfCow);
                    color.setText(colorOfAnimal);
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