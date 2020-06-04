package com.example.unlockme;

import android.os.AsyncTask;
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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Allows to fetch string data from a server, given the url and a callable.
 * The callable is an object of a class implementing the FetchDataCallbackInterface
 * which defines the callback method fetchDataCallback
 */
public class FetchData extends AsyncTask {
    HttpURLConnection urlConnection;
    String url;
    FetchDataCallbackInterface callbackInterface;
    public final static String BASE_URL = BuildConfig.BASE_URL;

    /**
     * Constructor
     * @param url
     * @param callbackInterface class which defines the callback method
     */
    public FetchData(String url, FetchDataCallbackInterface callbackInterface) {
        this.url = url;
        this.callbackInterface = callbackInterface;
    }

    protected ArrayList<String> doInBackground(String... args) {
//        StringBuilder result = new StringBuilder();
//        try {
//            URL url = new URL(this.url);
//            urlConnection = (HttpURLConnection) url.openConnection();
//            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                result.append(line);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            urlConnection.disconnect();
//        }
//        return result.toString();

        final String url = BASE_URL + "/api/account/" + '1' + "/images";
        final ArrayList<String> imageUrls = new ArrayList<String>();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray r = response.getJSONArray("content");

                    for (int i = 0; i < r.length(); ++i) {
                        imageUrls.add(r.getJSONObject(i).getString("key"));
                    }

                    //String url_link = r.getJSONObject(0).getString("key");
                    //ImageView v1 = findViewById(R.id.imageView2);
                    //Bitmap b = getBitmapFromURL(url_link);
                    //v1.setImageBitmap(b);
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

        return imageUrls;
    }

    protected void onPostExecute(ArrayList<String> result) {
        super.onPostExecute(result);
        // pass the result to the callback function
        this.callbackInterface.fetchDataCallback(result);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }
}

