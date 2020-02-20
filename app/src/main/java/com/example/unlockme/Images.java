package com.example.unlockme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class Images extends AppCompatActivity {
    private ImageView mImageView;
    private String url;
    private RequestBuilder<PictureDrawable> requestBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        Intent intent = getIntent();

        url = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text

        mImageView = (ImageView) findViewById(R.id.imageView2);

        requestBuilder =
                Glide.with(this)
                        .as(PictureDrawable.class)
//                        .placeholder(R.drawable.image_loading)
//                        .error(R.drawable.image_error)
                        .transition(withCrossFade())
                        .listener(new SvgSoftwareLayerSetter());

        requestBuilder.load(url).into(mImageView);

    }

    public void getAllObjects(View view) {
        requestBuilder.load(url).into(mImageView);
    }

}

