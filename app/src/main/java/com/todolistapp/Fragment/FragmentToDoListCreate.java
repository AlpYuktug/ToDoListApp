package com.todolistapp.Fragment;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class FragmentToDoListCreate extends Fragment {

    public FragmentToDoListCreate() {
    }

    public EditText editTextToDoListName;

    public String ToDoListTopic,ToDoListItemTopic,ToDoListItemDescription,ToDoListItemDeadLine,ToDoListItemCheck;

    public ImageView imageViewToDoListAdd;

    private RequestQueue mQueue;
    public VolleyNetworkCall UrlAddress;

    public SharedPreferences UserInformationSP;
    public String UserEmail;
    public Boolean EditTextControl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todolist_create, container, false);

        editTextToDoListName =  view.findViewById(R.id.editTextToDoListName);
        imageViewToDoListAdd =  view.findViewById(R.id.imageViewToDoListAdd);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        UrlAddress = new VolleyNetworkCall();

        mQueue = Volley.newRequestQueue(getActivity());

        UserInformationSP = getContext().getSharedPreferences("UserInformationSP",MODE_PRIVATE);
        UserEmail = UserInformationSP.getString("UserEmail", "");

        imageViewToDoListAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToDoListAddJSON();
            }
        });

        return view;
    }


    private void ToDoListAddJSON() {

        CheckValue();

        String ToDoListItemAddDefaultURL = UrlAddress.getToDoListAddUrl();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ToDoListItemAddDefaultURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        //Toast.makeText(getContext(), ServerResponse, Toast.LENGTH_LONG).show();
                        Fragment fragment = new FragmentToDoList();

                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.FragmentContent, fragment)
                                .commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Toast.makeText(getContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("ToDoListTopic", ToDoListTopic);
                params.put("ToDoListOwner", UserEmail);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    public void CheckValue() {

        ToDoListTopic = editTextToDoListName.getText().toString().trim();

        if (TextUtils.isEmpty(ToDoListTopic)) {
            EditTextControl = false;
        } else {
            EditTextControl = true;
        }
    }

}