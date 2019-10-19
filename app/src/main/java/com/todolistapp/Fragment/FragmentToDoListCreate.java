package com.todolistapp.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.todolistapp.Adapter.RecylerViewAdapterToDoList;
import com.todolistapp.Model.ModelToDoList;
import com.todolistapp.NetworkCall.VolleyNetworkCall;
import com.todolistapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class FragmentToDoListCreate extends Fragment {

    public FragmentToDoListCreate() {
    }

    public EditText editTextToDoListName,editTextToDoItemName,editTextToDoItemDesc;
    public ImageView imageViewToDoListAdd,imageViewItemAdd;
    public TextView textViewSelectedDate;

    public String ToDoListItemTopic,ToDoListItemDescription,ToDoListItemDeadLine,ToDoListItemCheck;

    private RequestQueue mQueue;
    public VolleyNetworkCall UrlAddress;

    public SharedPreferences UserInformationSP;
    public String UserEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todolist_create, container, false);

        editTextToDoListName =  view.findViewById(R.id.editTextToDoListName);
        editTextToDoItemName =  view.findViewById(R.id.editTextToDoItemName);
        editTextToDoItemDesc =  view.findViewById(R.id.editTextToDoItemDesc);
        imageViewToDoListAdd =  view.findViewById(R.id.imageViewToDoListAdd);
        imageViewItemAdd     =  view.findViewById(R.id.imageViewItemAdd);
        textViewSelectedDate =  view.findViewById(R.id.textViewSelectedDate);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        UrlAddress = new VolleyNetworkCall();

        mQueue = Volley.newRequestQueue(getActivity());

        UserInformationSP = getContext().getSharedPreferences("UserInformationSP",MODE_PRIVATE);
        UserEmail = UserInformationSP.getString("UserEmail", "");


        imageViewItemAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ToDoListItemAddJSON();

            }
        });

        imageViewToDoListAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ToDoListCreateJSON();

            }
        });

        return view;
    }

    private void ToDoListCreateJSON() {

        String ToDoLisItemAddURL = UrlAddress.getToDoListItemAddUrl();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ToDoLisItemAddURL,
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
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void ToDoListItemAddJSON() {

        String ToDoListItemAddDefaultURL = UrlAddress.getToDoListItemAddDefaultUrl();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ToDoListItemAddDefaultURL,
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
                params.put("ToDoListItemTopic", ToDoListItemTopic);
                params.put("ToDoListItemDescription", ToDoListItemDescription);
                params.put("ToDoListItemDeadLine", ToDoListItemDeadLine);
                params.put("ToDoListItemCheck",ToDoListItemCheck);

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

}