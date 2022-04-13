package kz.cifron.vetqyzmet_doctor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class FourPasswordIn extends AppCompatActivity {
    ImageView[] imageViews = new ImageView[4];
    Button[] buttons = new Button[10];
    ImageButton imageButton;
    String str = "";
    String confirm = "";
    int inWhich = 1;
    ImageButton finger;
    TextView setupac;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_password_in);
        buttons[0] = findViewById(R.id.button0);
        buttons[1] = findViewById(R.id.button);
        buttons[2] = findViewById(R.id.button2);
        buttons[3] = findViewById(R.id.button3);
        buttons[4] = findViewById(R.id.button4);
        buttons[5] = findViewById(R.id.button5);
        buttons[6] = findViewById(R.id.button6);
        buttons[7] = findViewById(R.id.button7);
        buttons[8] = findViewById(R.id.button8);
        buttons[9] = findViewById(R.id.button9);
        setupac = findViewById(R.id.setupAndconfirm);
        finger = findViewById(R.id.buttonFinger);
        finger.setVisibility(View.INVISIBLE);

        imageViews[0] = findViewById(R.id.imageView);
        imageViews[1] = findViewById(R.id.imageView2);
        imageViews[2] = findViewById(R.id.imageView3);
        imageViews[3] = findViewById(R.id.imageView4);

        imageButton = findViewById(R.id.imageButton);
        SharedPreferences password = getSharedPreferences("fastPass", MODE_PRIVATE);
        SharedPreferences.Editor editor = password.edit();
        editor.putString("fastPassword", "null");
        editor.apply();


        View.OnTouchListener oclBtnOk = new View.OnTouchListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (v.getId()) {
                    case R.id.button: {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            changeBackD(1);
                        }

                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            changeBackU(1, v, event);
                        }

                        break;
                    }
                    case R.id.button2: {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            changeBackD(2);
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            changeBackU(2, v, event);
                        }
                        break;
                    }
                    case R.id.button3: {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            changeBackD(3);
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            changeBackU(3, v, event);
                        }
                        break;
                    }
                    case R.id.button4: {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            changeBackD(4);
                        }

                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            changeBackU(4, v, event);
                        }

                        break;
                    }
                    case R.id.button5: {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            changeBackD(5);
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            changeBackU(5, v, event);
                        }
                        break;
                    }
                    case R.id.button6: {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            changeBackD(6);
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            changeBackU(6, v, event);
                        }
                        break;
                    }
                    case R.id.button7: {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            changeBackD(7);
                        }

                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            changeBackU(7, v, event);
                        }

                        break;
                    }
                    case R.id.button8: {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            changeBackD(8);
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            changeBackU(8, v, event);
                        }
                        break;
                    }
                    case R.id.button9: {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            changeBackD(9);
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            changeBackU(9, v, event);
                        }
                        break;
                    }
                    case R.id.button0: {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            changeBackD(0);
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            changeBackU(0, v, event);
                        }
                        break;
                    }

                    case R.id.imageButton: {

                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            Drawable buttonDrawable = imageButton.getBackground();
                            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                            DrawableCompat.setTint(buttonDrawable, Color.rgb(239, 242, 245));
                            imageButton.setBackground(buttonDrawable);
                            imageButton.setImageResource(R.drawable.ic_delete_white);
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());

                            if (str.length() != 0 && rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                                str = str.substring(0, str.length() - 1);
                                imageViews[str.length()].setImageResource(R.drawable.ic_ellipse_13);
                            }
                            Drawable buttonDrawable = imageButton.getBackground();
                            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                            DrawableCompat.setTint(buttonDrawable, Color.WHITE);
                            imageButton.setBackground(buttonDrawable);
                            imageButton.setImageResource(R.drawable.ic_delete);
                        }
                        break;
                    }

                }
                if (str.length() == 4) {
                    if (inWhich != 1) {
                        if (str.equals(confirm)) {
                            SharedPreferences password = getSharedPreferences("fastPass", MODE_PRIVATE);
                            SharedPreferences.Editor editor = password.edit();
                            editor.putString("fastPassword", str);
                            editor.apply();
                            str = "";
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    nextAcc();
                                }
                            }, 300);
                        }
                        else {
                            confirm = str;
                            clearPass();
                            inWhich++;
                        }
                    }
                    else {
                        confirm = str;
                        clearPass();
                        inWhich++;
                    }
                }
                return true;
            }
        };
        for (Button button : buttons) {
            button.setOnTouchListener(oclBtnOk);
        }
        imageButton.setOnTouchListener(oclBtnOk);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void nextAcc() {
        Intent intent = new Intent(FourPasswordIn.this, VetQyzmetPage.class);
        startActivity(intent);
    }
    @SuppressLint("ResourceAsColor")
    public void changeBackD(int n) {
        Drawable buttonDrawable = buttons[n].getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        DrawableCompat.setTint(buttonDrawable, Color.rgb( 233,131,5));
        buttons[n].setBackground(buttonDrawable);
        buttons[n].setTextColor(Color.WHITE);
    }
    public void changeBackU(int n, View v, MotionEvent event) {
        Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
            imageViews[str.length()].setImageResource(R.drawable.ic_ellipse_12);
            str = str + n;
        }
        Drawable buttonDrawable = buttons[n].getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        DrawableCompat.setTint(buttonDrawable, Color.rgb(239, 242, 245));
        buttons[n].setBackground(buttonDrawable);
        buttons[n].setTextColor(Color.BLACK);
    }

    public void clearPass() {
        str = "";
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(150);
                setupac.setText("Подтвердите код быстрого доступа");
                str = "";
                for (ImageView i : imageViews) {
                    i.setImageResource(R.drawable.ic_ellipse_13);
                }
            }
        }, 300);
    }

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
}