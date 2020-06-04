package com.example.unlockme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.unlockme.data.LoginRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LearnTopicActivity extends AppCompatActivity {
    private final Map<String, String> urls = new HashMap<String, String>();
    public final static String BASE_URL = BuildConfig.BASE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_topic);
        getLinks();
    }

    public void getLinks() {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = BASE_URL + "/api/users/" + LoginRepository.getUser().getUserId() +  "/files";

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));

                            urls.put("profile_image_url", obj.getString("profile_image_url"));
                            urls.put("processed_image_url", obj.getString("processed_image_url"));
                            urls.put("barcode_url", obj.getString("barcode_url"));
                            urls.put("light_barcode", obj.getString("light_barcode"));
                            urls.put("dark_barcode", obj.getString("dark_barcode"));
                            urls.put("graph_url", obj.getString("graph_url"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse.data != null) {
                            if (error.networkResponse.statusCode == 400) {
                                try {
                                    JSONObject obj = new JSONObject(new String(error.networkResponse.data));
                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                    Log.d("MESSAGE", obj.getString("message"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                if (error.networkResponse.statusCode == 500) {
                                    try {
                                        JSONObject obj = new JSONObject(new String(error.networkResponse.data));
                                        Toast.makeText(getApplicationContext(), "Database error! Please, try again later!", Toast.LENGTH_LONG).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        JSONObject obj = new JSONObject(new String(error.networkResponse.data));
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                        Log.d("MESSAGE", obj.getString("message"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }                        Log.e("GotError",""+error.getMessage());
                    }
                });

        queue.add(volleyMultipartRequest);
    }


    public void learn_barcode(View view) {
        ArrayList<PetsModel> models = new ArrayList<PetsModel>();
        models.add(new PetsModel(urls.get("profile_image_url"),"png", "You image", "Original image you uploaded"));
        models.add(new PetsModel(urls.get("graph_url"),"svg", "Graph", "Graph made from your image"));
        models.add(new PetsModel(urls.get("barcode_url"),"png", "Barcode", "Light parts of generated barcode"));

        openCardPager(models, "Algorithm of creating barcode");
    }

    public void learn_compare(View view) {
        ArrayList<PetsModel> models = new ArrayList<PetsModel>();
        models.add(new PetsModel("https://unlockbucketme.s3.amazonaws.com/4/WIN_20200530_13_08_29_Pro.jpg","png", "Your image", "Hash of your image: " + "0xc0c90fc6cc2d3c14"));
        models.add(new PetsModel("https://unlockbucketme.s3.amazonaws.com/dev/IMAGE+2020-05-31+11%3A55%3A57+PM.jpg","png", "Hashing", "Looks similar or what is perceptual hashing?\n" +
                "Typical hash-functions are used to map data of different sizes to the fixed-size string. One of the most important features of a typical hash is that the same input always gives the same output, but if the inputs are a little bit different output won`t be any similar. They are very useful for data search, but absolutely useless for fuzzy search.\n"));

        models.add(new PetsModel("https://unlockbucketme.s3.amazonaws.com/dev/20200530100952109344im_d.png","png", "How do we compare?", "Barcodes are saved as black-white images, where for encoding one image pixel color only one bit is used. When we need to compare them, we can easily convert them to binary 2-dimensional array or matrix. So we will find the distance between every row of matrix and then find total distance."));

        openCardPager(models, "Compare and find barcodes");
    }

    public void openCardPager(ArrayList<PetsModel> models, String title) {
        final Intent intent = new Intent(this, cardViewPager.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("models", models);
        bundle.putString("cardTitle", title);
        bundle.putString("cardPagerTitle", title);

        intent.putExtras(bundle);
        startActivity(intent);
    }
}
