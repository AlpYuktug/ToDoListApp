package com.todolistapp.Activitiy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.todolistapp.NetworkCall.VolleyNetworkCall;
import com.todolistapp.R;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    public ImageView imageViewSignUp;
    public EditText editTextUserEmail,editTextUserPassword;
    public TextView textViewLogin;

    public String UserEmail,UserPassword;

    public RequestQueue requestQueue;
    public ProgressDialog progressDialog;

    Boolean EditTextControl;

    public VolleyNetworkCall UrlAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        UrlAddress = new VolleyNetworkCall();

        editTextUserEmail = findViewById(R.id.editTextToDoListName);
        editTextUserPassword = findViewById(R.id.editTextToDoItemName);

        requestQueue = Volley.newRequestQueue(Register.this);
        progressDialog = new ProgressDialog(Register.this);

        imageViewSignUp = findViewById(R.id.imageViewSignUp);
        imageViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!CheckNetwork())
                {
                    Toast.makeText(Register.this, String.valueOf(R.string.CheckNetwork), Toast.LENGTH_LONG).show();
                }

                else {
                    CheckValue();

                    if (EditTextControl)
                        UserRegister();
                    else
                        Toast.makeText(Register.this, String.valueOf(R.string.CheckBlank), Toast.LENGTH_LONG).show();
                }
            }
        });

        textViewLogin = findViewById(R.id.textViewLogin);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    public void CheckValue() {

        UserEmail = editTextUserEmail.getText().toString().trim();
        UserPassword = editTextUserPassword.getText().toString().trim();

        if (TextUtils.isEmpty(UserEmail) || TextUtils.isEmpty(UserPassword)) {
            EditTextControl = false;
        } else {
            EditTextControl = true;
        }
    }

    public void UserRegister() {

        progressDialog.setMessage(String.valueOf(R.string.ProgressRegister));
        progressDialog.show();

        String LoginURL = UrlAddress.getUserRegisterUrl()+UserEmail;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LoginURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        progressDialog.dismiss();

                        if(ServerResponse.equalsIgnoreCase("\n\nEmail already taken"))
                        {
                            String DisplayError = String.valueOf(R.string.WrongInformation);
                            Toast.makeText(Register.this, DisplayError, Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            finish();
                            Toast.makeText(Register.this, ServerResponse, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Register.this, Login.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("UserEmail", UserEmail);
                params.put("UserPassword", UserPassword);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Register.this);
        requestQueue.add(stringRequest);
    }

    protected boolean CheckNetwork() {

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            return true;

        } else {

            return false;

        }

    }


}


