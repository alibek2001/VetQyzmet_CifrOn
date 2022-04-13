package kz.cifron.vetqyzmet_doctor;

import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class BigPage extends AppCompatActivity {

    ImageButton back;
    TextView indicator;
    private static ViewPager mPager;
    private ArrayList<String> ImagesArray = new ArrayList<>();
    TextView ing;
    TextView direction;
    TextView breed;
    TextView date;
    TextView gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_page);
        back = findViewById(R.id.bBack);
        indicator = findViewById(R.id.whichPage);
        ing = findViewById(R.id.ing);
        direction = findViewById(R.id.direction);
        breed = findViewById(R.id.breedB);
        date = findViewById(R.id.dateB);
        gender = findViewById(R.id.genderB);
        ing.setText(getIntent().getStringExtra("ing"));
        direction.setText(getIntent().getStringExtra("suit"));
        breed.setText(getIntent().getStringExtra("breed"));
        date.setText(getIntent().getStringExtra("dateOfBirth"));
        gender.setText(getIntent().getStringExtra("gender"));
        bBack();
        init();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    public void bBack() {
        back.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        v.setBackgroundResource(R.drawable.gradient);
                    }

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        v.setBackground(null);
                        onBackPressed();
                    }
                }
                else {
                    v.setBackground(null);
                }
                return true;
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void init() {
        String one = getIntent().getStringExtra("photo1");
        String two = getIntent().getStringExtra("photo2");
        String three = getIntent().getStringExtra("photo3");
        ImagesArray.add(one);
        if (!two.equals("null")) {
            ImagesArray.add(two);
        }
        if (!three.equals("null")) {
            ImagesArray.add(three);
        }
        indicator.setText("1/" + ImagesArray.size());

        mPager = findViewById(R.id.pager);


        mPager.setAdapter(new SlidingImage_Adapter(BigPage.this, ImagesArray));

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indicator.setText((position + 1) +"/" + ImagesArray.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }
}