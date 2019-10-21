package com.todolistapp.Fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.Context.MODE_PRIVATE;

public class FragmentToDoListItem extends Fragment implements AdapterView.OnItemSelectedListener {

    public FragmentToDoListItem() {
    }

    String  ToDoListItemTopic,ToDoListItemDescription,ToDoListItemDeadLine,OrderCategoryName="";
    Integer ToDoListNumber;
    Integer ToDoListItemNumber;
    Integer ToDoListItemCheck;

    public ImageView imageViewItemAdd,imageViewExport;

    private RequestQueue mQueue;

    public List<ModelToDoListItem> ModelToDoListItems;

    public RecyclerView RecylerViewToDoListItem;
    public RecylerViewAdapterToDoListItem ToDoListItemAdapter;

    public CoordinatorLayout coordinatorLayout;

    public VolleyNetworkCall UrlAddress;

    public Spinner spinnerOrderList;
    public List<String> OrderList = new ArrayList<String>();

    private File pdfFile;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;

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

        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);

        imageViewItemAdd =  view.findViewById(R.id.imageViewItemAdd);
        imageViewExport =  view.findViewById(R.id.imageViewExport);

        RecylerViewToDoListItem = (RecyclerView) view.findViewById(R.id.RecylerViewToDoListItem);
        RecylerViewToDoListItem.setHasFixedSize(true);
        RecylerViewToDoListItem.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));

        mQueue = Volley.newRequestQueue(getActivity());

        enableSwipeToDeleteAndUndo();

        spinnerOrderList = view.findViewById(R.id.spinnerOrderList);
        spinnerOrderList.setOnItemSelectedListener(this);


        imageViewItemAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new FragmentToDoListAddItem();

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.FragmentContent, fragment)
                        .commit();
            }
        });

        imageViewExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createPdfWrapper();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });

        GetOrderList();

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
                                    ToDoListItemCheck = todoitem.getInt("ToDoListItemCheck");

                                    if(OrderCategoryName.equals("All"))
                                    {
                                        ToDoListItemTopic = todoitem.getString("ToDoListItemTopic");
                                        ToDoListItemNumber = todoitem.getInt("ToDoListItemNumber");
                                        ToDoListItemCheck = todoitem.getInt("ToDoListItemCheck");
                                        ToDoListItemDescription = todoitem.getString("ToDoListItemDescription");
                                        ToDoListItemDeadLine = todoitem.getString("ToDoListItemDeadLine");

                                        ModelToDoListItems.add(new ModelToDoListItem(ToDoListNumber,ToDoListItemNumber,ToDoListItemCheck,ToDoListItemTopic,ToDoListItemDescription,ToDoListItemDeadLine));
                                    }

                                    else if(OrderCategoryName.equals("Completed"))
                                    {
                                        if (ToDoListItemCheck == 1)
                                        {
                                            ToDoListItemTopic = todoitem.getString("ToDoListItemTopic");
                                            ToDoListItemNumber = todoitem.getInt("ToDoListItemNumber");
                                            ToDoListItemCheck = todoitem.getInt("ToDoListItemCheck");
                                            ToDoListItemDescription = todoitem.getString("ToDoListItemDescription");
                                            ToDoListItemDeadLine = todoitem.getString("ToDoListItemDeadLine");

                                            ModelToDoListItems.add(new ModelToDoListItem(ToDoListNumber,ToDoListItemNumber,ToDoListItemCheck,ToDoListItemTopic,ToDoListItemDescription,ToDoListItemDeadLine));
                                        }

                                    }

                                    else if(OrderCategoryName.equals("UnCompleted"))
                                    {
                                        if (ToDoListItemCheck == 0)
                                        {
                                        ToDoListItemTopic = todoitem.getString("ToDoListItemTopic");
                                        ToDoListItemNumber = todoitem.getInt("ToDoListItemNumber");
                                        ToDoListItemCheck = todoitem.getInt("ToDoListItemCheck");
                                        ToDoListItemDescription = todoitem.getString("ToDoListItemDescription");
                                        ToDoListItemDeadLine = todoitem.getString("ToDoListItemDeadLine");

                                        ModelToDoListItems.add(new ModelToDoListItem(ToDoListNumber,ToDoListItemNumber,ToDoListItemCheck,ToDoListItemTopic,ToDoListItemDescription,ToDoListItemDeadLine));
                                        }
                                    }

                                    else
                                    {
                                        ToDoListItemTopic = todoitem.getString("ToDoListItemTopic");
                                        ToDoListItemNumber = todoitem.getInt("ToDoListItemNumber");
                                        ToDoListItemCheck = todoitem.getInt("ToDoListItemCheck");
                                        ToDoListItemDescription = todoitem.getString("ToDoListItemDescription");
                                        ToDoListItemDeadLine = todoitem.getString("ToDoListItemDeadLine");

                                        ModelToDoListItems.add(new ModelToDoListItem(ToDoListNumber,ToDoListItemNumber,ToDoListItemCheck,ToDoListItemTopic,ToDoListItemDescription,ToDoListItemDeadLine));
                                    }

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

    private void GetOrderList() {

        OrderList.clear();

        String OrderListUrl = UrlAddress.getToDoListItemOrderCategoryUrl();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, OrderListUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("OrderCategory");

                            OrderList.add("Choose Order Filter");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject category = jsonArray.getJSONObject(i);
                                OrderCategoryName = category.getString("OrderCategoryName");
                                OrderList.add(OrderCategoryName);
                            }

                            ArrayAdapter<String> OrderCategoryAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_model, OrderList);
                            OrderCategoryAdapter.setDropDownViewResource(R.layout.spinner_model);

                            spinnerOrderList.setAdapter(OrderCategoryAdapter);

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

    private void createPdfWrapper() throws FileNotFoundException, DocumentException {

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        }else {
            createPdf();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(getContext(), "WRITE_EXTERNAL Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void createPdf() throws FileNotFoundException, DocumentException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            //Log.i(TAG, "Created a new directory for PDF");
        }

        pdfFile = new File(docsFolder.getAbsolutePath(),"ToDoList.pdf");
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();

        int listSize = ModelToDoListItems.size();

        for (int i = 0; i<listSize; i++){
            document.add(new Paragraph(String.valueOf(ModelToDoListItems.get(i))));
        }


        document.close();
        Intent OpenPDF = new Intent(getContext(), com.todolistapp.Activitiy.OpenPDF.class);
        startActivity(OpenPDF);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        OrderCategoryName = parent.getItemAtPosition(i).toString();
        ToDoListJSONParse();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
