package com.todolistapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.todolistapp.Activitiy.Home;
import com.todolistapp.Fragment.FragmentToDoListItem;
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

        holder.textViewToDoListName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = context.getSharedPreferences("ToDoListNumberStroge",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("TodoListNumber",ModelToDoLists.get(position).getToDoListNumber());
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

        public ToDoListViewHolder(View view) {
            super(view);
            textViewToDoListName=view.findViewById(R.id.textViewToDoListName);

        }
    }
}



