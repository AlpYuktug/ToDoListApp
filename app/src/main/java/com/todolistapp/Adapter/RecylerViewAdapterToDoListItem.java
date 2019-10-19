package com.todolistapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.todolistapp.Activitiy.Home;
import com.todolistapp.Fragment.FragmentToDoListItem;
import com.todolistapp.Model.ModelToDoListItem;
import com.todolistapp.R;

import java.util.List;

public class RecylerViewAdapterToDoListItem extends RecyclerView.Adapter<RecylerViewAdapterToDoListItem.ToDoListItemViewHolder>{

    Context context;
    private List<ModelToDoListItem> ModelToDoListItems;

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
                    holder.imageViewCompleted.setImageResource(R.drawable.completedicon);
                    Toast.makeText(context,"Mission Complated",Toast.LENGTH_LONG).show();
                }
                else
                {
                    holder.imageViewCompleted.setImageResource(R.drawable.notcompletedicon);
                    Toast.makeText(context,"Mission UnComplated",Toast.LENGTH_LONG).show();
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
}



