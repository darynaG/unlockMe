package com.example.unlockme;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class cardViewPager extends AppCompatActivity {
    private int dotscount = 0;
    private PetAdapter adapter;
    private ArrayList<PetsModel> models;
    private ViewPager viewPager;

    private TextView cardTitle;
    private TextView cardPagerTitle;
    LinearLayout sliderDotspanel = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view_pager);

        viewPager = findViewById(R.id.view_pager);
        sliderDotspanel = findViewById(R.id.slider_dots);
        cardTitle = findViewById(R.id.textView7);
        cardPagerTitle = findViewById(R.id.textView6);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        models = bundle.getParcelableArrayList("models");
        adapter = new PetAdapter(models, this);
        viewPager.setAdapter(adapter);

        cardTitle.setText(bundle.getString("cardTitle"));
        cardPagerTitle.setText(bundle.getString("cardPagerTitle"));

        viewPager.setPadding(30, 0, 30, 0);
        dotscount = adapter.getCount();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        } );
    }
}
