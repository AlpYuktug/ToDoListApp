package com.todolistapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.todolistapp.Model.ModelToDoListItem;
import com.todolistapp.NetworkCall.VolleyNetworkCall;
import com.todolistapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecylerViewAdapterToDoListItem extends RecyclerView.Adapter<RecylerViewAdapterToDoListItem.ToDoListItemViewHolder>{

    Context context;
    private List<ModelToDoListItem> ModelToDoListItems;

    public VolleyNetworkCall UrlAddress;

    public String ToDoListItemCheck;
    public Integer Position;
    private RequestQueue mQueue;


    public RecylerViewAdapterToDoListItem(List<ModelToDoListItem> ModelToDoListItems, Context context) {
        this.ModelToDoListItems = ModelToDoListItems;
        this.context = context;
    }

    @Override
    public RecylerViewAdapterToDoListItem.ToDoListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_todolist_item, parent, false);
        RecylerViewAdapterToDoListItem.ToDoListItemViewHolder gvh = new RecylerViewAdapterToDoListItem.ToDoListItemViewHolder(v);
        return gvh;
    }

    public void DeleteItem(int position) {
        ModelToDoListItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(final RecylerViewAdapterToDoListItem.ToDoListItemViewHolder holder, final int position) {

        UrlAddress = new VolleyNetworkCall();
        mQueue = Volley.newRequestQueue(context);

        holder.textViewToDoListItemName.setText(ModelToDoListItems.get(position).getToDoListItemTopic());
        holder.textViewToDoListItemDescription.setText(ModelToDoListItems.get(position).getToDoListItemDescription());
        holder.textViewToDoListItemDeadLine.setText(String.valueOf(ModelToDoListItems.get(position).getToDoListItemDeadLine()));

        if(String.valueOf(ModelToDoListItems.get(position).getToDoListItemCheck()).equals("0"))
            holder.imageViewCompleted.setImageResource(R.drawable.notcompletedicon);

        holder.imageViewCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(ModelToDoListItems.get(position).getToDoListItemCheck()).equals("0"))
                {
                    Position=position;
                    holder.imageViewCompleted.setImageResource(R.drawable.completedicon);
                    Toast.makeText(context,"Mission Complated",Toast.LENGTH_LONG).show();
                    ToDoListItemCheck="1";
                    ChangeComplated();
                }
                else
                {
                    Position=position;
                    holder.imageViewCompleted.setImageResource(R.drawable.notcompletedicon);
                    Toast.makeText(context,"Mission UnComplated",Toast.LENGTH_LONG).show();
                    ToDoListItemCheck="0";
                    ChangeComplated();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ModelToDoListItems.size();
    }

    public class ToDoListItemViewHolder extends RecyclerView.ViewHolder {

        TextView textViewToDoListItemName,textViewToDoListItemDescription,textViewToDoListItemDeadLine;
        ImageView imageViewCompleted;

        public ToDoListItemViewHolder(View view) {
            super(view);
            textViewToDoListItemName=view.findViewById(R.id.textViewToDoListItemName);
            textViewToDoListItemDescription=view.findViewById(R.id.textViewToDoListItemDescription);
            textViewToDoListItemDeadLine=view.findViewById(R.id.textViewToDoListItemDeadLine);
            imageViewCompleted=view.findViewById(R.id.imageViewCompleted);
        }
    }

    private void ChangeComplated() {

        String ToDoListItemUpdateComplateURL = UrlAddress.getToDoListItemUpdateComplatedUrl();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ToDoListItemUpdateComplateURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        Toast.makeText(context, ServerResponse, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(context, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("ToDoListNumber", ModelToDoListItems.get(Position).getToDoListNumber().toString());
                params.put("ToDoListItemNumber", ModelToDoListItems.get(Position).getToDoListItemNumber().toString());
                params.put("ToDoListItemCheck", ToDoListItemCheck);

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

}



