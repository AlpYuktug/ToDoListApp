package com.todolistapp.Fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.todolistapp.Adapter.RecylerViewAdapterToDoListItem;
import com.todolistapp.Model.ModelToDoListItem;
import com.todolistapp.NetworkCall.VolleyNetworkCall;
import com.todolistapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class FragmentToDoListAddItem extends Fragment {

    public FragmentToDoListAddItem() {
    }

    String  ToDoListTopic, ToDoListItemTopic,ToDoListItemDescription,ToDoListItemDeadLine,ToDoListItemNumberExist;

    public EditText editTextToDoItemName,editTextToDoItemDesc;
    public ImageView imageViewToDoListAdd,imageViewItemAdd,imageViewCalendar;
    public TextView textViewSelectedDate;

    private RequestQueue mQueue;

    public VolleyNetworkCall UrlAddress;

    public Boolean EditTextControl;

    public SharedPreferences ToDoListNumberStrogeSP;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todolist_item_add, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        UrlAddress = new VolleyNetworkCall();

        editTextToDoItemName =  view.findViewById(R.id.editTextToDoItemName);
        editTextToDoItemDesc =  view.findViewById(R.id.editTextToDoItemDesc);
        imageViewToDoListAdd =  view.findViewById(R.id.imageViewToDoListAdd);
        imageViewItemAdd     =  view.findViewById(R.id.imageViewItemAdd);
        imageViewCalendar    =  view.findViewById(R.id.imageViewCalendar);
        textViewSelectedDate =  view.findViewById(R.id.textViewSelectedDate);

        mQueue = Volley.newRequestQueue(getActivity());

        imageViewCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar takvim = Calendar.getInstance();
                int year = takvim.get(Calendar.YEAR);
                int month = takvim.get(Calendar.MONTH);
                int day = takvim.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month += 1;
                                textViewSelectedDate.setText(year + "-" + month + "-" + dayOfMonth);
                                ToDoListItemDeadLine = year + "-" + month + "-" + dayOfMonth;
                            }
                        }, year, month, day);

                dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Choose", dpd);
                dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", dpd);
                dpd.show();

            }
        });

        imageViewItemAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ToDoListItemAddJSON();
            }
        });

        return view;

    }

    private void ToDoListItemAddJSON() {

        CheckValue();

        ToDoListNumberStrogeSP = getContext().getSharedPreferences("ToDoListNumberStroge",MODE_PRIVATE);
        ToDoListItemNumberExist = String.valueOf(ToDoListNumberStrogeSP.getInt("TodoListNumber", 0));
        ToDoListTopic = ToDoListNumberStrogeSP.getString("TodoListTopic", "");

        String ToDoListItemAddDefaultURL = UrlAddress.getToDoListItemAddExistUrl();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ToDoListItemAddDefaultURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        //Toast.makeText(getContext(), ServerResponse, Toast.LENGTH_LONG).show();
                        Fragment fragment = new FragmentToDoListItem();

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

                params.put("ToDoListNumber", ToDoListItemNumberExist);
                params.put("ToDoListTopic", ToDoListTopic);
                params.put("ToDoListItemTopic", ToDoListItemTopic);
                params.put("ToDoListItemDescription", ToDoListItemDescription);
                params.put("ToDoListItemDeadLine", ToDoListItemDeadLine);
                params.put("ToDoListItemCheck","0");

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }


    public void CheckValue() {

        ToDoListItemTopic = editTextToDoItemName.getText().toString().trim();
        ToDoListItemDescription = editTextToDoItemDesc.getText().toString().trim();
        ToDoListItemDeadLine = textViewSelectedDate.getText().toString().trim();

        if ( TextUtils.isEmpty(ToDoListItemTopic)|| TextUtils.isEmpty(ToDoListItemDescription)|| TextUtils.isEmpty(ToDoListItemDeadLine)) {
            EditTextControl = false;
        } else {
            EditTextControl = true;
        }
    }

}
