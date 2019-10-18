package com.todolistapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.todolistapp.Model.ModelToDoList;
import com.todolistapp.R;

import java.util.List;

public class RecylerViewAdapterToDoList extends RecyclerView.Adapter<RecylerViewAdapterToDoList.ToDoListViewHolder>{

    Context context;
    private List<ModelToDoList> ModelToDoLists;

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

    public void DeleteItem(int position) {
        ModelToDoLists.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(RecylerViewAdapterToDoList.ToDoListViewHolder holder, final int position) {

        holder.textViewToDoListName.setText(ModelToDoLists.get(position).getToDoListTopic());

    }

    @Override
    public int getItemCount() {
        return ModelToDoLists.size();
    }

    public class ToDoListViewHolder extends RecyclerView.ViewHolder {

        TextView textViewToDoListName;

        public ToDoListViewHolder(View view) {
            super(view);
            textViewToDoListName=view.findViewById(R.id.textViewToDoListName);

        }
    }
}



