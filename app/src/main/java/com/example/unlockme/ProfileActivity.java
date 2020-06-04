package com.example.unlockme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.unlockme.data.LoginDataSource;
import com.example.unlockme.data.LoginRepository;
import com.example.unlockme.data.model.LoggedInUser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.example.unlockme.MainActivity.EXTRA_MESSAGE;


public class ProfileActivity extends AppCompatActivity {
    private Bitmap image;
    private ImageView profile_image;

    private String profile_picture_url;
    private String barcode_url;
    private String light_barcode_url;
    private String dark_barcode_url;
    private String graph_url;
    private String processed_img_url;
    public final static String BASE_URL = BuildConfig.BASE_URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        this.image = (Bitmap) intent.getParcelableExtra("BitmapImage");
        this.profile_image = (ImageView) findViewById(R.id.profile_image);
//        if (this.image != null) {
//            this.profile_image.setImageBitmap(this.image);
//        } else {
//            getImage();
//        }
        getImage();
        getLinks();

    }

    public void getLinks() {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url =BASE_URL + "/api/users/" + LoginRepository.getUser().getUserId() +  "/files";
        final Map<String, String> urls = new HashMap<String, String>();

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
                            setLinks(urls);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("GotError",""+error.getMessage());
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
                        }
                    }
                });

        queue.add(volleyMultipartRequest);
    }

    private void setLinks(Map<String,String> urls) {
        this.processed_img_url = urls.get("processed_image_url");
        this.profile_picture_url = urls.get("profile_image_url");
        this.barcode_url = urls.get("barcode_url");
        this.light_barcode_url = urls.get("light_barcode");
        this.dark_barcode_url = urls.get("dark_barcode");
        this.graph_url = urls.get("graph_url");
    }

    public boolean getImage() {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = BASE_URL + "/api/v1/users/" + LoginRepository.getUser().getUserId() +  "/image";
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            String url = obj.getString("profile_img");
                            Picasso.get()
                                    .load(url)
                                    .fit()
                                    .centerCrop()
                                    .into(profile_image);
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

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //no inspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void openLearnPage(View view) {
//        ArrayList<PetsModel> models = new ArrayList<PetsModel>();
//        models.add(new PetsModel(this.profile_picture_url,"png", "You image", "Original image you uploaded"));
//        models.add(new PetsModel(this.graph_url,"svg", "Graph", "Graph made from your image"));
//        models.add(new PetsModel(this.barcode_url,"png", "Barcode", "Light parts of generated barcode"));
//
//        openCardPager(models);
        final Intent intent = new Intent(this, LearnTopicActivity.class);
        startActivity(intent);
    }

    public void openCardPager(ArrayList<PetsModel> models) {
        final Intent intent = new Intent(this, cardViewPager.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("models", models);
        bundle.putString("cardTitle", "Algorithm of creating barcode");
        bundle.putString("cardPagerTitle", "Algorithm of creating barcode");

        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void getLinksToBarcodes(final Bitmap bitmap) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = BASE_URL + "/api/barcode";
        final Intent intent = new Intent(this, ImageSlider.class);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            ArrayList<String> urls = new ArrayList<String>();
                            JSONObject obj = new JSONObject(new String(response.data));
                            urls.add(obj.getString("barcode1"));
                            urls.add(obj.getString("barcode2"));
                            urls.add(obj.getString("processed img"));

                            Bundle bundle = new Bundle();

                            bundle.putStringArrayList("urls", urls);
                            bundle.putBoolean("is_barcode", true);
                            bundle.putString("image_type", "svg");

                            intent.putExtras(bundle);

                            // intent.putExtra(EXTRA_MESSAGE, urls);
                            startActivity(intent);

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
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("file", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        queue.add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void openImageSliderWithBarcodes(View view) {
        getLinksToBarcodes(this.image);
    }

    public void openImageSlider(View view) {
        final Intent intent = new Intent(this, ImageSlider.class);

        String url = BASE_URL + "/api/account/" + LoginRepository.getUser().getUserId() + "/images";
        Log.println(Log.DEBUG, "URL", url);

        Bundle bundle = new Bundle();

        bundle.putBoolean("is_barcode", false);
        bundle.putString("id", LoginRepository.getUser().getUserId());
        bundle.putString("url", url);
        bundle.putString("image_type", "png");
        intent.putExtras(bundle);

        startActivity(intent);
    }

    private void uploadImage(final Bitmap bitmap) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = BASE_URL + "/api/image" + LoginRepository.getUser().getUserId();
        final Intent intent = new Intent(this, Images.class);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            String url_link = obj.getString("link");
                            intent.putExtra(EXTRA_MESSAGE, url_link);
                            startActivity(intent);

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
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("file", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        queue.add(volleyMultipartRequest);
    }

    public void getImageGraph(View view) {
        final Intent intent = new Intent(this, Images.class);
        if (graph_url !=null) {
            if (graph_url.isEmpty()) {
                getLinks();
            }
        }
        intent.putExtra(EXTRA_MESSAGE, graph_url);
        startActivity(intent);
    }

    public void changeImage(View view) {
        final Intent intent = new Intent(this, TakePhotoActivity.class);
        startActivity(intent);
    }
}