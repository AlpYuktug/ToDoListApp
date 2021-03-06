package com.todolistapp.Activitiy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class Login extends AppCompatActivity {

    public ImageView imageViewLogin;
    public EditText editTextUserEmail,editTextUserPassword;
    public TextView textViewRegister;

    public String UserEmail,UserPassword;

    public RequestQueue requestQueue;
    public ProgressDialog progressDialog;

    Boolean EditTextControl;

    public SharedPreferences UserInformationSP;

    public VolleyNetworkCall UrlAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        UrlAddress = new VolleyNetworkCall();

        editTextUserEmail = findViewById(R.id.editTextToDoListName);
        editTextUserPassword = findViewById(R.id.editTextToDoItemName);

        requestQueue = Volley.newRequestQueue(Login.this);
        progressDialog = new ProgressDialog(Login.this);

        imageViewLogin = findViewById(R.id.imageViewLogin);
        imageViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!CheckNetwork())
                {
                    Toast.makeText(Login.this, getString(R.string.CheckNetwork), Toast.LENGTH_LONG).show();
                }
                else {
                        CheckValue();
                    if (EditTextControl)
                        UserLogin();
                    else
                        Toast.makeText(Login.this, getString(R.string.CheckBlank), Toast.LENGTH_LONG).show();
                }
            }
        });

        textViewRegister = findViewById(R.id.textViewRegister);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(Login.this, Register.class);
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

    public void UserLogin() {

        progressDialog.setMessage(getString(R.string.ProgressLogin));
        progressDialog.show();

        String LoginURL = UrlAddress.getUserLoginUrl();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LoginURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        progressDialog.dismiss();

                        if(ServerResponse.equalsIgnoreCase("\n\nSuccess")) {

                            SaveUserInformation();
                            finish();
                            Intent intent = new Intent(Login.this, Home.class);
                            intent.putExtra("UserEmail", UserEmail);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                        else {
                            String DisplayError = getString(R.string.WrongInformation);
                            Toast.makeText(Login.this, DisplayError, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(Login.this, volleyError.toString(), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        requestQueue.add(stringRequest);
    }

    public void SaveUserInformation() {

        UserInformationSP = getSharedPreferences("UserInformationSP", MODE_PRIVATE);
        SharedPreferences.Editor UserInformationSPEdit = UserInformationSP.edit();
        UserInformationSPEdit.putString("UserEmail", UserEmail);

        UserInformationSPEdit.commit();
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

