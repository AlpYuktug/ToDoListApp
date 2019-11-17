package com.todolistapp.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.todolistapp.Activitiy.Login;
import com.todolistapp.Activitiy.Register;
import com.todolistapp.Adapter.RecylerViewAdapterToDoList;
import com.todolistapp.HelperClass.SwipeToDeleteCallback;
import com.todolistapp.Model.ModelToDoList;
import com.todolistapp.NetworkCall.VolleyNetworkCall;
import com.todolistapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class FragmentProfile extends Fragment {

    public FragmentProfile() {
    }

    private RequestQueue mQueue;

    private VolleyNetworkCall UrlAddress;

    private SharedPreferences UserInformationSP;
    private String UserEmail;

    public TextView textViewEMail,textViewChangePassword,textViewDeleteAccount;
    public ImageView imageViewLogOut;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        UrlAddress = new VolleyNetworkCall();

        mQueue = Volley.newRequestQueue(getActivity());

        UserInformationSP = getContext().getSharedPreferences("UserInformationSP",MODE_PRIVATE);
        UserEmail = UserInformationSP.getString("UserEmail", "");

        textViewEMail = view.findViewById(R.id.textViewEMail);
        textViewEMail.setText(UserEmail);

        textViewChangePassword = view.findViewById(R.id.textViewChangePassword);
        textViewChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new FragmentProfileChangePassword();

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.FragmentContent, fragment)
                        .commit();
            }
        });


        textViewDeleteAccount = view.findViewById(R.id.textViewDeleteAccount);
        textViewDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(R.string.DeleteSure));
                builder.setMessage(getString(R.string.DeleteSureDesc));
                builder.setNegativeButton(getString(R.string.Cancel), null);
                builder.setPositiveButton(getString(R.string.Done), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AccountDelete();

                        Toast.makeText(getContext(),getString(R.string.DeleteCompleted),Toast.LENGTH_LONG).show();
                        getActivity().finish();
                        Intent intent = new Intent(getContext(),Login.class);
                        startActivity(intent);
                    }
                });
                builder.show();
            }
        });

        imageViewLogOut = view.findViewById(R.id.imageViewLogOut);
        imageViewLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                Intent intent = new Intent(getContext(),Login.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void AccountDelete() {

        String DeleteURL = UrlAddress.getUserDelete()+UserEmail;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DeleteURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

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
                params.put("UserEmail", UserEmail);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
