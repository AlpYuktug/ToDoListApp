package com.todolistapp.Fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.todolistapp.Activitiy.OpenPDF;
import com.todolistapp.Adapter.RecylerViewAdapterToDoList;
import com.todolistapp.Adapter.RecylerViewAdapterToDoListItem;
import com.todolistapp.HelperClass.SwipeToDeleteCallback;
import com.todolistapp.Model.ModelToDoList;
import com.todolistapp.Model.ModelToDoListItem;
import com.todolistapp.NetworkCall.VolleyNetworkCall;
import com.todolistapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.Context.MODE_PRIVATE;

public class FragmentToDoListItem extends Fragment {

    public FragmentToDoListItem() {
    }

    String  ToDoListTopic, ToDoListItemTopic,ToDoListItemDescription,ToDoListItemDeadLine,ToDoListItemNumberExist;
    Integer ToDoListNumber;
    Integer ToDoListItemNumber;
    Integer ToDoListItemCheck;

    public EditText editTextToDoItemName,editTextToDoItemDesc;
    public ImageView imageViewToDoListAdd,imageViewItemAdd,imageViewCalendar;
    public TextView textViewSelectedDate;


    private RequestQueue mQueue;

    public List<ModelToDoListItem> ModelToDoListItems;

    public RecyclerView RecylerViewToDoListItem;
    public RecylerViewAdapterToDoListItem ToDoListItemAdapter;

    public CoordinatorLayout coordinatorLayout;

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
        View view = inflater.inflate(R.layout.fragment_todolist_item, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ModelToDoListItems = new ArrayList<>();

        UrlAddress = new VolleyNetworkCall();

        editTextToDoItemName =  view.findViewById(R.id.editTextToDoItemName);
        editTextToDoItemDesc =  view.findViewById(R.id.editTextToDoItemDesc);
        imageViewToDoListAdd =  view.findViewById(R.id.imageViewToDoListAdd);
        imageViewItemAdd     =  view.findViewById(R.id.imageViewItemAdd);
        imageViewCalendar    =  view.findViewById(R.id.imageViewCalendar);
        textViewSelectedDate =  view.findViewById(R.id.textViewSelectedDate);

        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);

        RecylerViewToDoListItem = (RecyclerView) view.findViewById(R.id.RecylerViewToDoListItem);
        RecylerViewToDoListItem.setHasFixedSize(true);
        RecylerViewToDoListItem.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));

        mQueue = Volley.newRequestQueue(getActivity());

        enableSwipeToDeleteAndUndo();

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //ask user for granting permissions on api22+
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);



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

                createPdf(ModelToDoListItems);

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

    private void ToDoListJSONParse() {

        ModelToDoListItems.clear();

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
                                     ToDoListItemCheck = todoitem.getInt("ToDoListItemCheck");
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
                        Toast.makeText(getContext(), ServerResponse, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
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


    private void enableSwipeToDeleteAndUndo() {

            SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                    final int position = viewHolder.getAdapterPosition();
                    ToDoListItemAdapter.removeItem(position);

                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Missions was removed from the list.", Snackbar.LENGTH_LONG);

                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                }
            };
            ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
            itemTouchhelper.attachToRecyclerView(RecylerViewToDoListItem);

            ToDoListJSONParse();
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



    private void createPdf(List<ModelToDoListItem> List){
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        document.finishPage(page);

        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"todo.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(getContext(), "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getContext(), "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        document.close();

        Intent intent =  new Intent(getContext(), OpenPDF.class);
        getContext().startActivity(intent);

    }


}
