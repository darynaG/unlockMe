package com.example.unlockme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.unlockme.ui.login.LoginActivity;

import java.util.ArrayList;

public class firstPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
    }

    public void register(View view) {
//        final Intent intent = new Intent(this, cardViewPager.class);
//        Bundle bundle = new Bundle();
//        ArrayList<PetsModel> models = new ArrayList<PetsModel>();
//        models.add(new PetsModel("https://miro.medium.com/max/864/1*j0rBD8kMengxgcCIUnyujQ.png","png", "You image", "Original image you uploaded"));
//        models.add(new PetsModel("https://cdn.shopify.com/s/files/1/0496/1029/files/Freesample.svg","svg", "Barcode", "Light parts of generated barcode"));
//        models.add(new PetsModel("https://miro.medium.com/max/864/1*j0rBD8kMengxgcCIUnyujQ.png","png", "Graph", "Graph generated from your image"));
//
//        bundle.putParcelableArrayList("models", models);
//        bundle.putString("cardTitle", "cardTitlw");
//        bundle.putString("cardPagerTitle", "cardPagerTitlw");
//
//        intent.putExtras(bundle);
        final Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        final Intent intent = new Intent(this, LoginWithPhotoActivity.class);
        startActivity(intent);
    }


    public void openCardPager() {
        final Intent intent = new Intent(this, cardViewPager.class);
        Bundle bundle = new Bundle();
        ArrayList<PetsModel> models = new ArrayList<PetsModel>();
        models.add(new PetsModel("https://cdn.shopify.com/s/files/1/0496/1029/files/Freesample.svg","svg", "Barcode", "Light parts of generated barcode"));
        models.add(new PetsModel("https://miro.medium.com/max/864/1*j0rBD8kMengxgcCIUnyujQ.png","png", "You image", "Original image you uploaded"));
        bundle.putParcelableArrayList("models", models);
        bundle.putString("cardTitle", "cardTitlw");
        bundle.putString("cardPagerTitle", "cardPagerTitlw");

        intent.putExtras(bundle);
        startActivity(intent);
    }
}
