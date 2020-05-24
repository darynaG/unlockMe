package com.example.unlockme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.unlockme.data.LoginRepository;
import com.example.unlockme.data.model.LoggedInUser;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.example.unlockme.MainActivity.EXTRA_ID;

public class RegisterActivity extends AppCompatActivity {
    private EditText password;
    private EditText username;
    private EditText email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        password = (EditText) findViewById(R.id.passwordReg);
        username = (EditText) findViewById(R.id.usernameReg);
        email = (EditText) findViewById(R.id.emailReg);

    }

    public void register(View view) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://incenter.pythonanywhere.com/api/accounts";
        final Intent intent = new Intent(this, TakePhotoActivity.class);
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("password", password.getText().toString());
            jsonObject.put("username", username.getText().toString());
            jsonObject.put("email", email.getText().toString());
        } catch (JSONException e) {
            // handle exception
        }


        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            LoginRepository.user = new LoggedInUser(response.getString("user_id"),
                                    response.getString("username"));
                            Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() {

                try {
                    Log.i("json", jsonObject.toString());
                    return jsonObject.toString().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        //adding the request to volley
        queue.add(putRequest);

    }

}
