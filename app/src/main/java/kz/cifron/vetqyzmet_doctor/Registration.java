package kz.cifron.vetqyzmet_doctor;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
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


import com.google.android.material.bottomsheet.BottomSheetDialog;

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
    Handler handler;
    Button cancel;
    Button next;
    ProgressDialog progressDialog;
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
    ArrayList<Integer> breedIdFromColor = new ArrayList<>();
    static ArrayList<String> staticBreedList = new ArrayList<>();
    static  ArrayList<Integer> staticBreedIdList = new ArrayList<>();
    int breedId;
    ArrayList<String> colorList = new ArrayList<>();
    ArrayList<Integer> colorIdList = new ArrayList<>();
    ArrayList<Integer> colorIdFromType = new ArrayList<>();
    static ArrayList<String> staticColorList = new ArrayList<>();
    static ArrayList<Integer> staticColorIdList = new ArrayList<>();
    int colorId;
    String typeOfAnimal = "Не выбрано";
    String porodaOfCow = "Ангус";
    String colorOfAnimal = "Белый";
    TextView dateText;
    TextView aniname;
    TextView poroda;
    TextView porodaFront;
    TextView color;
    TextView suitFront;
    boolean check1;
    boolean check2;
    boolean check3;
    boolean check4;
    boolean check5;
    boolean check6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        radioGroup = findViewById(R.id.sexOfAnimal);
        dateholder = findViewById(R.id.date_holder);
        dateText = findViewById(R.id.dateText);
        aniname = findViewById(R.id.Aniname);
        color = findViewById(R.id.suit);
        poroda = findViewById(R.id.poroda);
        porodaFront = findViewById(R.id.porodaFront);
        suitFront = findViewById(R.id.suitFront);
        handler = new Handler();

        dateDi();

        radioButton1 = findViewById(R.id.radioButton);
        radioButton2 = findViewById(R.id.radioButton2);
        regNVP = findViewById(R.id.regNVP);
        checkSex();

        regNVP.addTextChangedListener(textWatcher);

        focusToEditText();

        back = findViewById(R.id.regBack);
        cancel = findViewById(R.id.regCancel);
        next = findViewById(R.id.regNext);

        next.getBackground().setAlpha(128);

        spinner = findViewById(R.id.Birthspinner);
        spinner2 = findViewById(R.id.suitspinner);
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
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Registration.this, Page_Of_Owner.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void prev() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                typeOfAnimal = "Не выбрано";
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
                        goToNext();
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


    public void goToNext() {
        if (poroda.getText().equals("Не выбрано") || color.getText().equals("Не выбрано")) {
            Toast.makeText(Registration.this, "Направление животного или порода не выбрано", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(this, CheckData.class);
            intent.putExtra("TypeOfAnimal", aniname.getText().toString());
            intent.putExtra("INJ", regNVP.getText().toString());
            intent.putExtra("Date", dateText.getText().toString());
            intent.putExtra("ColorId", colorId);
            intent.putExtra("checkedTypeId", checkedTypeId);
            intent.putExtra("breedId", breedId);
            if (radioButton2.isChecked()) {
                intent.putExtra("SexOfAnimal", "Самец");
            } else {
                intent.putExtra("SexOfAnimal", "Самка");
            }
            intent.putExtra("Color", color.getText().toString());
            intent.putExtra("Poroda", porodaOfCow);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (regNVP.getText().toString().trim().isEmpty()) {
                check4 = false;
                disableButton();
            }
            else {
                check4 = true;
                if (checkAllChecks()) {
                    enableButton();
                }
                else {
                    disableButton();
                }
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

    private void checkSex() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                check5 = true;
                if (checkAllChecks()) {
                    enableButton();
                }
                else {
                    disableButton();
                }
            }
        });
    }

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void showCustomAlertDiType() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                this, R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.custom_alert_di_type,
                        (LinearLayout)findViewById(R.id.bottomSheetContainer)
                );

        bottomSheetDialog.setContentView(R.layout.custom_alert_di_type);
        RadioGroup rg = bottomSheetDialog.findViewById(R.id.groupOfColor);
        rg.setPadding(0,20,0,20);
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[] {getResources().getColor(R.color.orange) }
        );

        if (!aniname.getText().equals("Не выбрано")) {
            check1 = true;
        }
        else {
            check1 = false;
        }
        bottomSheetDialog.setCancelable(true);
        for (int i = 0;i < typesIdList.size();i++) {
            RadioButton radioButton = new RadioButton(this);
            //======================================================
            radioButton.setTextColor(Color.BLACK);
            radioButton.setTextSize(18);


            radioButton.setText(typesList.get(i));
            radioButton.setId(i + 100);
            radioButton.setButtonTintList(colorStateList);
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
                        aniname.setText(typeOfAnimal);
                        checkedTypeId = typesIdList.get(rg.getCheckedRadioButtonId() - 100);
                    }
                }
                check1 = true;

                checkedTypeId = typesIdList.get(rg.getCheckedRadioButtonId() - 100);
                staticColorList.clear();
                staticColorIdList.clear();
                for (int i = 0;i < colorList.size();i++) {
                    if (colorIdFromType.get(i) == checkedTypeId) {
                        staticColorList.add(colorList.get(i));
                        staticColorIdList.add(colorIdList.get(i));
                    }
                }
                if (staticColorList.size() != 0) {
                    color.setText("Не выбрано");
                    colorOfAnimal = "Не выбрано";
                    colorId = -1;
                    check2 = false;
                    spinner2.setVisibility(View.VISIBLE);
                    suitFront.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.GONE);
                    porodaFront.setVisibility(View.GONE);
                }
                else {
                    color.setText("");
                    colorId = -1;
                    poroda.setText("");
                    check2 = true;
                    check3 = true;
                    breedId = -1;
                    spinner2.setVisibility(View.GONE);
                    suitFront.setVisibility(View.GONE);
                    spinner.setVisibility(View.GONE);
                    porodaFront.setVisibility(View.GONE);
                }

                if (checkAllChecks()) {
                    enableButton();
                }
                else {
                    disableButton();
                }

                bottomSheetDialog.dismiss();

            }
        });
        bottomSheetDialog.show();
    }

    private void dateDi() {
        dateholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateText.getText().toString().equals("Не выбрано")) {
                    check6 = false;
                }
                else {
                    check6 = true;
                }
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String data = makeDataString(dayOfMonth, month, year);
                        dateText.setText(data);

                        check6 = true;
                        if (checkAllChecks()) {
                            enableButton();
                            Log.d("all", "true");
                        }
                        else {
                            Log.d("somefields", "false");
                            disableButton();
                        }
                    }

                };
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);


                int style = android.app.AlertDialog.THEME_HOLO_LIGHT;
                datePickerDialog = new DatePickerDialog(Registration.this, style, dateSetListener, year, month,day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
    }

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void porodaDi() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                this, R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.custom_alert_poroda,
                        (LinearLayout)findViewById(R.id.bottomSheetContainer)
                );

        if (poroda.getText().toString().equals("Не выбрано")) {
            check3 = false;
        }
        else {
            check3 = true;
        }

        bottomSheetDialog.setContentView(R.layout.custom_alert_poroda);
        RadioGroup rg = bottomSheetDialog.findViewById(R.id.groupOfPoroda);
        rg.setPadding(0,20,0,20);
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[] {getResources().getColor(R.color.orange) }
        );
        for (int i = 0;i < staticBreedIdList.size();i++) {
            RadioButton radioButton = new RadioButton(this);

            //======================================================
            radioButton.setTextColor(Color.BLACK);
            radioButton.setTextSize(18);

            radioButton.setText(staticBreedList.get(i));
            radioButton.setId(i + 100);
            radioButton.setButtonTintList(colorStateList);
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
                        poroda.setText(porodaOfCow);
                        breedId = staticBreedIdList.get(rg.getCheckedRadioButtonId() - 100);

                    }
                }
                check3 = true;

                if (checkAllChecks()) {
                    enableButton();
                    Log.d("all", "true");
                }
                else {
                    Log.d("somefields", "false");
                    disableButton();
                }
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void colorDi() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                this, R.style.BottomSheetDialogTheme
        );
        bottomSheetDialog.setContentView(R.layout.custom_alert_color);
        RadioGroup rg = bottomSheetDialog.findViewById(R.id.groupOfColor);
        rg.setPadding(0,20,0,20);
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[] { getResources().getColor(R.color.orange) }
        );
        if (color.getText().toString().equals("Не выбрано")) {
            check2 = false;
        }
        else {
            check2 = true;
        }
        for (int i = 0;i < staticColorList.size();i++) {
            RadioButton radioButton = new RadioButton(this);
            //======================================================
            radioButton.setTextColor(Color.BLACK);
            radioButton.setTextSize(18);

            radioButton.setText(staticColorList.get(i));
            radioButton.setId(i + 100);
            radioButton.setButtonTintList(colorStateList);
            rg.addView(radioButton);
        }
        for (int i = 0;i < rg.getChildCount();i++) {
            RadioButton btn = (RadioButton) rg.getChildAt(i);
            if (btn.getText().toString().equals(colorOfAnimal)) {
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
                        color.setText(colorOfAnimal);
                        colorId = staticColorIdList.get(rg.getCheckedRadioButtonId() - 100);
                    }
                }
                check2 = true;
                staticBreedList.clear();
                staticBreedIdList.clear();
                for (int i = 0;i < breedList.size();i++) {
                    if (colorId == breedIdFromColor.get(i)) {
                        staticBreedList.add(breedList.get(i));
                        staticBreedIdList.add(breedIdList.get(i));
                    }
                }
                if (!staticBreedList.isEmpty()) {
                    poroda.setText("Не выбрано");
                    porodaOfCow = "Не выбрано";
                    check3 = false;
                    breedId = -1;
                    spinner.setVisibility(View.VISIBLE);
                    porodaFront.setVisibility(View.VISIBLE);
                }
                else {
                    spinner.setVisibility(View.GONE);
                    breedId = -1;
                    porodaFront.setVisibility(View.GONE);
                }

                if (checkAllChecks()) {
                    enableButton();
                    Log.d("all", "true");
                }
                else {
                    Log.d("somefields", "false");
                    disableButton();
                }
                bottomSheetDialog.dismiss();

            }
        });
        bottomSheetDialog.show();
    }


    public void enableButton() {
        next.setEnabled(true);
        next.getBackground().setAlpha(255);
    }
    public void disableButton() {
        next.setEnabled(false);
        next.getBackground().setAlpha(128);
    }

    public boolean checkAllChecks() {
        if (check1 && check2 && check3 && check4 && check5 && check6) {
            return true;
        }
        return false;
    }
    private String makeDataString(int day, int month, int year) {
        return year + ", " + day + " " + getMonthFormat(month);
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

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(Registration.this);
                    progressDialog.setMessage("Загрузка данных");
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

                typeOfAnimal = "Не выбрано";

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
                    breedIdFromColor.add(jsonObject.getInt("suit_id"));
                }
                porodaOfCow = "Не выбрано";

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
                    colorIdFromType.add(jsonObject.getInt("animal_type_id"));
                }
                colorOfAnimal = "Не выбрано";


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