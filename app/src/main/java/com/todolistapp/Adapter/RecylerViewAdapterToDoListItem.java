package com.todolistapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.todolistapp.Model.ModelToDoListItem;
import com.todolistapp.NetworkCall.VolleyNetworkCall;
import com.todolistapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RecylerViewAdapterToDoListItem extends RecyclerView.Adapter<RecylerViewAdapterToDoListItem.ToDoListItemViewHolder>
        implements Filterable
    {

    Context context;
    private List<ModelToDoListItem> ModelToDoListItems;
    private List<ModelToDoListItem> ModelToDoListItemsFiltered;

    public VolleyNetworkCall UrlAddress;

    public String ToDoListItemCheck;
    public Integer Position;
    private RequestQueue mQueue;

    public Map<String, String> paramsdelete;

    public RecylerViewAdapterToDoListItem(List<ModelToDoListItem> ModelToDoListItems,Context context) {
        this.ModelToDoListItems = ModelToDoListItems;
        ModelToDoListItemsFiltered = new ArrayList<>(ModelToDoListItems);

        this.context = context;
    }

    @Override
    public RecylerViewAdapterToDoListItem.ToDoListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_todolist_item, parent, false);
        RecylerViewAdapterToDoListItem.ToDoListItemViewHolder gvh = new RecylerViewAdapterToDoListItem.ToDoListItemViewHolder(v);
        return gvh;
    }

    @Override
    public void onBindViewHolder(final RecylerViewAdapterToDoListItem.ToDoListItemViewHolder holder, final int position) {

        UrlAddress = new VolleyNetworkCall();
        mQueue = Volley.newRequestQueue(context);

        holder.textViewToDoListItemName.setText("Mission Name:" + ModelToDoListItems.get(position).getToDoListItemTopic());
        holder.textViewToDoListItemDescription.setText("Mission Description:" + ModelToDoListItems.get(position).getToDoListItemDescription());
        holder.textViewToDoListItemDeadLine.setText(String.valueOf("Mission Deadline:" + ModelToDoListItems.get(position).getToDoListItemDeadLine()));

        if(String.valueOf(ModelToDoListItems.get(position).getToDoListItemCheck()).equals("0"))
            holder.imageViewCompleted.setImageResource(R.drawable.notcompletedicon);

        holder.imageViewCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(ModelToDoListItems.get(position).getToDoListItemCheck()).equals("0"))
                {
                    Position=position;
                    holder.imageViewCompleted.setImageResource(R.drawable.completedicon);
                    Toast.makeText(context,"Mission Completed",Toast.LENGTH_LONG).show();
                    ToDoListItemCheck="1";
                    ChangeCompleted();
                }
                else
                {
                    Position=position;
                    holder.imageViewCompleted.setImageResource(R.drawable.notcompletedicon);
                    Toast.makeText(context,"Mission UnCompleted",Toast.LENGTH_LONG).show();
                    ToDoListItemCheck="0";
                    ChangeCompleted();
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
        CardView CardViewToDoListItem;

        public ToDoListItemViewHolder(View view) {
            super(view);
            textViewToDoListItemName=view.findViewById(R.id.textViewToDoListItemName);
            textViewToDoListItemDescription=view.findViewById(R.id.textViewToDoListItemDescription);
            textViewToDoListItemDeadLine=view.findViewById(R.id.textViewToDoListItemDeadLine);
            imageViewCompleted=view.findViewById(R.id.imageViewCompleted);

        }
    }

    private void ChangeCompleted() {

        String ToDoListItemUpdateComplateURL = UrlAddress.getToDoListItemUpdateComplatedUrl();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ToDoListItemUpdateComplateURL,
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

    private void DeleteItem() {

        String ToDoListItemDeletedURL = UrlAddress.getToDoListItemDeleteddUrl();

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
                paramsdelete.put("ToDoListNumber", ModelToDoListItems.get(Position).getToDoListNumber().toString());
                paramsdelete.put("ToDoListItemNumber", ModelToDoListItems.get(Position).getToDoListItemNumber().toString());
                return paramsdelete;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void removeItem(int position) {
        Position=position;
        DeleteItem();

        Fragment fragment = new FragmentToDoListItem();
        FragmentManager fm = ((Home) context).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.FragmentContent, fragment);
        ft.commit();
    }

    @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    List<ModelToDoListItem> filteredList = new ArrayList<>();

                    if (charSequence == null || charSequence.length() == 0) {
                        filteredList.addAll(ModelToDoListItemsFiltered);
                    } else {
                        String filterPattern = charSequence.toString().toLowerCase().trim();

                        for (ModelToDoListItem item : ModelToDoListItemsFiltered) {
                            if (item.getToDoListItemTopic().toLowerCase().contains(filterPattern)) {
                                filteredList.add(item);
                            }
                        }
                    }
                    FilterResults results = new FilterResults();
                    results.values = filteredList;
                    return results;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    ModelToDoListItems.clear();
                    ModelToDoListItems.addAll((List) filterResults.values);
                    notifyDataSetChanged();
                }
            };
        }
}



