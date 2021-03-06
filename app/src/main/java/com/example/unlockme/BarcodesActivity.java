package com.example.unlockme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.unlockme.data.LoginRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


public class BarcodesActivity extends AppCompatActivity {
    private TextView textView;
    private ImageView barcodeImage;
    private String user;

    public final static String BASE_URL = BuildConfig.BASE_URL;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcodes);
        textView = findViewById(R.id.textView);
        barcodeImage = findViewById(R.id.barcodeImage);


        Intent intent = getIntent();

        user = LoginRepository.getUser().getDisplayName();
        textView.setText("Here is your barcode," + user);
        getBarcode(LoginRepository.getUser().getUserId());
//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);
    }

    private void getBarcode(String userId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = BASE_URL + "/api/barcode/" + LoginRepository.getUser().getUserId();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            String url = obj.getString("barcode");
                            Picasso.get()
                                    .load(url)
                                    .fit()
                                    .centerCrop()
                                    .into(barcodeImage);
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


}
