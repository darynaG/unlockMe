package com.example.unlockme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.PictureDrawable;

import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private Bitmap bitmap;

    RequestQueue requestQueue;

    public static final String EXTRA_MESSAGE = "com.example.unlockme.MESSAGE";
    public static final String EXTRA_ID = "com.example.unlockme.ID";

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public void sendMessage(View view) {
        // Do something in response to button
//        Intent intent = new Intent(this, ResultViewActivity.class);
//        EditText editText = (EditText) findViewById(R.id.editText);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);

//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.android.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imageView = (ImageView) findViewById(R.id.imageView);

            if (imageBitmap != null) {
                String s = saveToInternalStorage(imageBitmap);
                this.bitmap = MainActivity.RotateBitmap(imageBitmap, 90);

                if (isWriteStoragePermissionGranted()) {
                    // galleryAddPic();

                }
                 //galleryAddPic(imageBitmap, "jpeg" + imagename);

//                String s = saveToInternalStorage(imageBitmap);
//
//                if (s != null) {
//                    Log.d("path", s);
//                } else {
//                    Log.d("null", "null");
//                }
            }

//                    this.bitmap = MainActivity.RotateBitmap(imageBitmap, 90);

            imageView.setImageBitmap(this.bitmap);
        }
    }

    public void getAll(View view) {
//        final Intent intent = new Intent(this, ImageSlider.class);
        EditText editText = (EditText) findViewById(R.id.editText);

        String id = editText.getText().toString();
        uploadBitmap(this.bitmap, id);
//        startActivity(intent);


//        final Intent intent = new Intent(this, Images.class);
//        String message = "hello beautiful";
//        //intent.putExtra(EXTRA_MESSAGE, message);
//        String id = "3";
//        intent.putExtra(EXTRA_ID, id);
//        startActivity(intent);
//        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url ="https://incenter.pythonanywhere.com/api/image";
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    String r = response.getString("description");
//                    textView.setText(r);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//
//        // Add the request to the RequestQueue.
//        requestQueue.add(request);

    }

    private void uploadBitmap(final Bitmap bitmap, final String user) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://incenter.pythonanywhere.com/api/accounts?user=" + user;
        final Intent intent = new Intent(this, ImageSlider.class);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            intent.putExtra(EXTRA_ID, user);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("GotError",""+error.getMessage());
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

    private void uploadImage(final Bitmap bitmap) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url ="https://incenter.pythonanywhere.com/api/image";
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
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("GotError",""+error.getMessage());
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

    public void getGraph(View view) {
        uploadImage(this.bitmap);
    }


    public void openSlider(View view) {
        final Intent intent = new Intent(this, ImageSlider.class);
        EditText editText = (EditText) findViewById(R.id.editText);

        String id = editText.getText().toString();
        intent.putExtra(EXTRA_ID, id);
        startActivity(intent);
    }

    public static Bitmap RotateBitmap(Bitmap img, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        Log.d("MYINT", "value: " + orientation);
        Log.d("MYINTNORMAL", "value: " + ExifInterface.ORIENTATION_ROTATE_90);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return MainActivity.RotateBitmap(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return MainActivity.RotateBitmap(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return MainActivity.RotateBitmap(img, 270);
            default:
                return img;
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        long image_name = System.currentTimeMillis();
        File file = new File(directory, "Image-"+ image_name + ".jpg");

        if (!file.exists()) {
            Log.d("path", file.toString());

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private void galleryAddPic() {
//        String root = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
//        Log.d("root", root);
//        File myDir = new File(root);
//        myDir.mkdirs();
        long image_name = System.currentTimeMillis();
        String fname = "Image-" + image_name + ".jpg";
//        File file = new File(myDir, fname);
//        if (file.exists()) file.delete();
//        Log.i("LOAD", root + fname);
//        try {
//            FileOutputStream out = new FileOutputStream(file);
//            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
//            out.flush();
//            out.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        MediaStore.Images.Media.insertImage(getContentResolver(), this.bitmap, fname , "unlockme app");
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("log tag", "Permission is granted2");
                return true;
            } else {

                Log.v("log tag", "Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("log tag", "Permission is granted2");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d("log tag", "External storage2");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v("log tag","Permission: " + permissions[0] + "was " + grantResults[0]);
                    //resume tasks needing this permission
                    //downloadPdfFile();
                    galleryAddPic();

                } else {

                }
                break;

            case 3:
                Log.d("log tag", "External storage1");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v("log tag","Permission: " + permissions[0] + "was " + grantResults[0]);
                    //resume tasks needing this permission

                } else {
                }
                break;
        }
    }
}
