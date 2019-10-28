package com.todolistapp.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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

import static android.content.Context.MODE_PRIVATE;

public class FragmentToDoListCreate extends Fragment {

    public FragmentToDoListCreate() {
    }

    private EditText editTextToDoListName;

    private String ToDoListTopic;

    private ImageView imageViewToDoListAdd,imageViewCancel;

    private RequestQueue mQueue;
    private VolleyNetworkCall UrlAddress;

    private SharedPreferences UserInformationSP;
    private String UserEmail;
    private Boolean EditTextControl;

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
        imageViewCancel      =  view.findViewById(R.id.imageViewCancel);

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
                CheckValue();
                if(EditTextControl)
                    ToDoListAddJSON();
                else
                    Toast.makeText(getContext(),"Please Fill a To-Do List Name",Toast.LENGTH_LONG).show();
            }
        });
        imageViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new FragmentToDoList();

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.FragmentContent, fragment)
                        .commit();
            }
        });
        return view;
    }

    private void ToDoListAddJSON() {

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