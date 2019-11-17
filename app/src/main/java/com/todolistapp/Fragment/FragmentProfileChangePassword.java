package com.todolistapp.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.todolistapp.Activitiy.Login;
import com.todolistapp.NetworkCall.VolleyNetworkCall;
import com.todolistapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class FragmentProfileChangePassword extends Fragment {

    public FragmentProfileChangePassword() {
    }

    private RequestQueue mQueue;

    private VolleyNetworkCall UrlAddress;

    private SharedPreferences UserInformationSP;
    public String UserEmail, UserPassword, OldPassword, NewPassword, ConfirmNewPassword;

    public EditText editTextOldPassword, editTextNewPassword, editTextNewPasswordAgain;
    public ImageView imageViewDone;

    public boolean EditTextControl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_change_password, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        UrlAddress = new VolleyNetworkCall();

        mQueue = Volley.newRequestQueue(getActivity());

        UserInformationSP = getContext().getSharedPreferences("UserInformationSP", MODE_PRIVATE);
        UserEmail = UserInformationSP.getString("UserEmail", "");

        GetPassword();

        editTextOldPassword = view.findViewById(R.id.editTextOldPassword);
        editTextNewPassword = view.findViewById(R.id.editTextNewPassword);
        editTextNewPasswordAgain = view.findViewById(R.id.editTextNewPasswordAgain);

        imageViewDone = view.findViewById(R.id.imageViewDone);
        imageViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckValue();

                if (EditTextControl)
                {
                    if (NewPassword.equals(ConfirmNewPassword))
                    {
                        if (OldPassword.equals(UserPassword))
                        {
                            ChangePassword();
                            Intent intent = new Intent(getActivity(), Login.class);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(getContext(), getString(R.string.WrongPassword), Toast.LENGTH_LONG).show();

                    }
                    else
                        Toast.makeText(getContext(), getString(R.string.DontMatchPassword), Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getContext(), getString(R.string.MustBlank), Toast.LENGTH_LONG).show();

            }
        });


        return view;
    }

    public void CheckValue() {

        OldPassword = editTextOldPassword.getText().toString().trim();
        NewPassword = editTextNewPassword.getText().toString().trim();
        ConfirmNewPassword = editTextNewPasswordAgain.getText().toString().trim();


        if (TextUtils.isEmpty(OldPassword) || TextUtils.isEmpty(NewPassword) || TextUtils.isEmpty(ConfirmNewPassword)) {
            EditTextControl = false;
        } else {
            EditTextControl = true;
        }
    }

    public void GetPassword() {

        String GetInformationURL = UrlAddress.getUserGetInformation()+UserEmail;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, GetInformationURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONArray("User");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject user = jsonArray.getJSONObject(i);
                                UserPassword = user.getString("UserPassword");
                            }

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

        mQueue.add(request);
    }

    public void ChangePassword() {

        String ChangePasswordURL = UrlAddress.getUserChangePassword()+UserEmail;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ChangePasswordURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        Toast.makeText(getContext(), ServerResponse, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("UserPassword", NewPassword);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}