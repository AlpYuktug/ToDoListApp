package com.todolistapp.Fragment;

import android.content.SharedPreferences;
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
import com.todolistapp.Adapter.RecylerViewAdapterToDoListItem;
import com.todolistapp.Model.ModelToDoList;
import com.todolistapp.Model.ModelToDoListItem;
import com.todolistapp.NetworkCall.VolleyNetworkCall;
import com.todolistapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FragmentToDoListItem extends Fragment {

    public FragmentToDoListItem() {
    }

    String ToDoListItemTopic,ToDoListItemDescription,ToDoListItemDeadLine;
    Integer ToDoListNumber;
    Integer ToDoListItemNumber;
    Integer ToDoListItemCheck;

    private RequestQueue mQueue;

    public List<ModelToDoListItem> ModelToDoListItems;

    public RecyclerView RecylerViewToDoListItem;
    public RecylerViewAdapterToDoListItem ToDoListItemAdapter;

    public VolleyNetworkCall UrlAddress;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todolist_item, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ModelToDoListItems = new ArrayList<>();

        UrlAddress = new VolleyNetworkCall();

        RecylerViewToDoListItem = (RecyclerView) view.findViewById(R.id.RecylerViewToDoListItem);
        RecylerViewToDoListItem.setHasFixedSize(true);
        RecylerViewToDoListItem.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));

        mQueue = Volley.newRequestQueue(getActivity());

        ToDoListJSONParse();

        return view;
    }

    private void ToDoListJSONParse() {

        String ToDoListURL = UrlAddress.getToDoListItemUrl();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ToDoListURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("ToDoListItem");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject todoitem = jsonArray.getJSONObject(i);

                                SharedPreferences ClickToDoListItemSP = getContext().getSharedPreferences("ToDoListNumberStroge",MODE_PRIVATE);
                                Integer ClickToDoListItem = ClickToDoListItemSP.getInt("TodoListNumber", 0);

                                ToDoListNumber = todoitem.getInt("ToDoListNumber");

                                if (ClickToDoListItem == ToDoListNumber)
                                {
                                     ToDoListItemTopic = todoitem.getString("ToDoListItemTopic");
                                     ToDoListItemNumber = todoitem.getInt("ToDoListItemNumber");
                                     ToDoListItemCheck = todoitem.getInt("ToDoListNumber");
                                     ToDoListItemDescription = todoitem.getString("ToDoListItemDescription");
                                     ToDoListItemDeadLine = todoitem.getString("ToDoListItemDeadLine");

                                     ModelToDoListItems.add(new ModelToDoListItem(ToDoListNumber,ToDoListItemNumber,ToDoListItemCheck,ToDoListItemTopic,ToDoListItemDescription,ToDoListItemDeadLine));
                                }


                            }

                            ToDoListItemAdapter = new RecylerViewAdapterToDoListItem(ModelToDoListItems, getActivity());
                            RecylerViewToDoListItem.setAdapter(ToDoListItemAdapter);


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
