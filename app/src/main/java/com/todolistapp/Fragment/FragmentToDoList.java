package com.todolistapp.Fragment;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.todolistapp.Adapter.RecylerViewAdapterToDoList;
import com.todolistapp.Model.ModelToDoList;
import com.todolistapp.NetworkCall.VolleyNetworkCall;
import com.todolistapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentToDoList extends Fragment {

    public FragmentToDoList() {
    }

    String ToDoListTopic;
    Integer ToDoListNumber;

    private RequestQueue mQueue;

    public List<ModelToDoList> ModelToDoLists;

    public RecyclerView RecylerViewToDoList;
    public RecylerViewAdapterToDoList ToDoListAdapter;

    public VolleyNetworkCall UrlAddress;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todolist, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ModelToDoLists = new ArrayList<>();

        UrlAddress = new VolleyNetworkCall();

        RecylerViewToDoList = (RecyclerView) view.findViewById(R.id.RecylerViewToDoList);
        RecylerViewToDoList.setHasFixedSize(true);
        RecylerViewToDoList.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));

        mQueue = Volley.newRequestQueue(getActivity());

        ToDoListJSONParse();

        return view;
    }

    private void ToDoListJSONParse() {

        String ToDoListURL = UrlAddress.getToDoListUrl();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ToDoListURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("ToDoList");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject todo = jsonArray.getJSONObject(i);

                                ToDoListTopic = todo.getString("ToDoListTopic");
                                ToDoListNumber = todo.getInt("ToDoListNumber");

                                ModelToDoLists.add(new ModelToDoList(ToDoListNumber,ToDoListTopic));
                            }

                            ToDoListAdapter = new RecylerViewAdapterToDoList(ModelToDoLists, getActivity());
                            RecylerViewToDoList.setAdapter(ToDoListAdapter);


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

}