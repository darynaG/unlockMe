package com.example.unlockme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class Images extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.unlockme.MESSAGE";

    private ImageView imageView2;
    private String url;
    private RequestBuilder<PictureDrawable> requestBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        Intent intent = getIntent();

        url = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        imageView2 = (ImageView) findViewById(R.id.imageView2);

        RequestBuilder<PictureDrawable> requestBuilder;
        requestBuilder =
                Glide.with(this)
                        .as(PictureDrawable.class)
//                        .placeholder(R.drawable.image_loading)
//                        .error(R.drawable.image_error)
                        .transition(withCrossFade())
                        .listener(new SvgSoftwareLayerSetter());

        requestBuilder.load(url).into(imageView2);

    }

    public void getAllObjects(View view) {

        final Intent intent = new Intent(this, BarcodesActivity.class);

        intent.putExtra(EXTRA_MESSAGE, "Dana");
        startActivity(intent);
    }

}

