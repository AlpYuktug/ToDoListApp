package com.todolistapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.todolistapp.Activitiy.Home;
import com.todolistapp.Fragment.FragmentToDoListItem;
import com.todolistapp.Model.ModelToDoList;
import com.todolistapp.NetworkCall.VolleyNetworkCall;
import com.todolistapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecylerViewAdapterToDoList extends RecyclerView.Adapter<RecylerViewAdapterToDoList.ToDoListViewHolder>{

    Context context;
    private List<ModelToDoList> ModelToDoLists;
    public Integer Position=0;

    public VolleyNetworkCall UrlAddress;
    private RequestQueue mQueue;

    public Map<String, String> paramsdelete;

    public RecylerViewAdapterToDoList(List<ModelToDoList> ModelToDoLists, Context context) {
        this.ModelToDoLists = ModelToDoLists;
        this.context = context;
    }

    @Override
    public RecylerViewAdapterToDoList.ToDoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_todolist, parent, false);
        RecylerViewAdapterToDoList.ToDoListViewHolder gvh = new RecylerViewAdapterToDoList.ToDoListViewHolder(v);
        return gvh;
    }


    @Override
    public void onBindViewHolder(RecylerViewAdapterToDoList.ToDoListViewHolder holder, final int position) {

        UrlAddress = new VolleyNetworkCall();
        mQueue = Volley.newRequestQueue(context);

        holder.textViewToDoListName.setText(ModelToDoLists.get(position).getToDoListTopic());

        holder.textViewToDoListName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = context.getSharedPreferences("ToDoListNumberStroge",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("TodoListNumber",ModelToDoLists.get(position).getToDoListNumber());
                editor.putString("TodoListTopic",ModelToDoLists.get(position).getToDoListTopic());

                editor.commit();

                Fragment fragment = new FragmentToDoListItem();
                FragmentManager fm = ((Home) context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.FragmentContent, fragment);
                ft.commit();
            }
        });

        holder.CardViewToDoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = context.getSharedPreferences("ToDoListNumberStroge",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("TodoListNumber",ModelToDoLists.get(position).getToDoListNumber());
                editor.putString("TodoListTopic",ModelToDoLists.get(position).getToDoListTopic());

                editor.commit();

                Fragment fragment = new FragmentToDoListItem();
                FragmentManager fm = ((Home) context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.FragmentContent, fragment);
                ft.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return ModelToDoLists.size();
    }

    public class ToDoListViewHolder extends RecyclerView.ViewHolder {

        TextView textViewToDoListName;
        CardView CardViewToDoList;

        public ToDoListViewHolder(View view) {
            super(view);
            textViewToDoListName=view.findViewById(R.id.textViewToDoListName);
            CardViewToDoList=view.findViewById(R.id.CardViewToDoList);
        }
    }

    private void DeleteList() {

        String ToDoListItemDeletedURL = UrlAddress.getToDoListDeleteddUrl();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ToDoListItemDeletedURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        //Toast.makeText(context, ServerResponse, Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Toast.makeText(context, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                paramsdelete = new HashMap<String, String>();
                paramsdelete.put("ToDoListNumber", ModelToDoLists.get(Position).getToDoListNumber().toString());
                return paramsdelete;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void removeItem(int position) {
        Position=position;
        DeleteList();
    }


    public void removeItemList(int position) {
        ModelToDoLists.remove(position);
        notifyItemRemoved(position);
    }
}



