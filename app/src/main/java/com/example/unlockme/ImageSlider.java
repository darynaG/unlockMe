package com.example.unlockme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ImageSlider extends AppCompatActivity implements FetchDataCallbackInterface {
    ArrayList<String> imageUrls = new ArrayList<String>();
    private ViewPager viewPager;
    FetchDataCallbackInterface callbackInterface;
    private String user_id = "5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        callbackInterface = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slider);

        ViewPager viewPager = findViewById(R.id.view_pager);
        Intent intent = getIntent();
        String id = intent.getStringExtra(MainActivity.EXTRA_ID);
        user_id = id;

        if(this.imageUrls.size() == 0) {
            // automatically calls the renderData function
            getUrls();
            //new FetchData("https://incenter.pythonanywhere.com/api/account/1/images", this).execute();
        }
        else {
            renderData();
        }
    }


    private void getUrls() {
        RequestQueue queue = Volley.newRequestQueue(this);

        final String url ="https://incenter.pythonanywhere.com/api/account/" + user_id + "/images";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray r = response.getJSONArray("content");

                    for (int i = 0; i < r.length(); ++i) {
                        imageUrls.add(r.getJSONObject(i).getString("key"));
                    }
                    callbackInterface.fetchDataCallback(imageUrls);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(request);
    }

    @Override
    public void fetchDataCallback(ArrayList<String> result) {
        imageUrls = result;
        renderData();
    }

    public void renderData() {
        ViewPager viewPager = findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, imageUrls);
        viewPager.setAdapter(adapter);
        // do something with your data here
    }
}
