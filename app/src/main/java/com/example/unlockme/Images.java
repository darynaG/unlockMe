package com.example.unlockme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;

import org.json.JSONObject;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class Images extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.unlockme.MESSAGE";

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


    }

    public void getAllObjects(View view) {

        final Intent intent = new Intent(this, BarcodesActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText2);

        intent.putExtra(EXTRA_MESSAGE, editText.getText().toString());
        startActivity(intent);
    }

}

