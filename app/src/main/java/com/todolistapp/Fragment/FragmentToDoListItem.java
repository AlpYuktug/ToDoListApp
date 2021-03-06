package com.todolistapp.Fragment;

import android.Manifest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import com.todolistapp.Adapter.RecylerViewAdapterToDoListItem;
import com.todolistapp.HelperClass.SwipeToDeleteCallback;
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

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FragmentToDoListItem extends Fragment implements AdapterView.OnItemSelectedListener {

    public FragmentToDoListItem() {
    }

    String  ToDoListItemTopic,ToDoListItemDescription,ToDoListItemDeadLine,OrderCategoryName="";
    Integer ToDoListNumber;
    Integer ToDoListItemNumber;
    Integer ToDoListItemCheck;

    private ImageView imageViewItemAdd,imageViewExport;

    private RequestQueue mQueue;

    private List<ModelToDoListItem> ModelToDoListItems;

    private RecyclerView RecylerViewToDoListItem;
    private RecylerViewAdapterToDoListItem ToDoListItemAdapter;

    private CoordinatorLayout coordinatorLayout;

    private VolleyNetworkCall UrlAddress;

    private Spinner spinnerOrderList;
    private List<String> OrderList = new ArrayList<String>();

    private File pdfFile;
    public Image ImageTitle;

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

        setHasOptionsMenu(true);


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

                                    if(OrderCategoryName.equals(getString(R.string.All)))
                                    {
                                        ToDoListItemTopic = todoitem.getString("ToDoListItemTopic");
                                        ToDoListItemNumber = todoitem.getInt("ToDoListItemNumber");
                                        ToDoListItemCheck = todoitem.getInt("ToDoListItemCheck");
                                        ToDoListItemDescription = todoitem.getString("ToDoListItemDescription");
                                        ToDoListItemDeadLine = todoitem.getString("ToDoListItemDeadLine");

                                        ModelToDoListItems.add(new ModelToDoListItem(ToDoListNumber,ToDoListItemNumber,ToDoListItemCheck,ToDoListItemTopic,ToDoListItemDescription,ToDoListItemDeadLine));
                                    }

                                    else if(OrderCategoryName.equals(getString(R.string.Complated)))
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

                                    else if(OrderCategoryName.equals(getString(R.string.UnComplated)))
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
                                }
                            }

                            ToDoListItemAdapter = new RecylerViewAdapterToDoListItem(ModelToDoListItems,getActivity());
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

        OrderList.add(getString(R.string.All));
        OrderList.add(getString(R.string.Complated));
        OrderList.add(getString(R.string.UnComplated));

        ArrayAdapter<String> OrderCategoryAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_model, OrderList);
        OrderCategoryAdapter.setDropDownViewResource(R.layout.spinner_model);

        spinnerOrderList.setAdapter(OrderCategoryAdapter);

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
        }

        pdfFile = new File(docsFolder.getAbsolutePath(),"ToDoList.pdf");
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD,26);
        Font itemFont = FontFactory.getFont(FontFactory.HELVETICA,18);

        try {
             ImageTitle = Image.getInstance(String.valueOf(R.drawable.calendaricon));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Paragraph ITitle = new Paragraph();
        ITitle.setAlignment(Element.ALIGN_CENTER);
        ITitle.add(ImageTitle);
        document.add(ITitle);

        Paragraph Title = new Paragraph();
        Title.setFont(titleFont);
        Title.setAlignment(Element.ALIGN_CENTER);
        Title.add(getString(R.string.MissionsList));
        document.add(Title);

        Paragraph LineTitle = new Paragraph();
        LineTitle.add("-------------------------------------------------------------------------------------------------------------------------------");
        document.add(LineTitle);

        int listSize = ModelToDoListItems.size();

        for (int i = 0; i<listSize; i++){

            String MissionDetails = "\n" + getString(R.string.ToDoItemNumber) + " : " + ModelToDoListItems.get(i).ToDoListItemNumber + "\n" +
                    getString(R.string.ToDoItemName) + " : " +ModelToDoListItems.get(i).ToDoListItemTopic+ "\n" +
                    getString(R.string.ToDoItemDescription) + " : " +ModelToDoListItems.get(i).ToDoListItemDescription + "\n" +
                    getString(R.string.ToDoItemDeadLine) + " : " +ModelToDoListItems.get(i).getToDoListItemDeadLine();

            Paragraph Item = new Paragraph();
            Item.setFont(itemFont);
            Item.add(MissionDetails);
            document.add(Item);

            Paragraph Line = new Paragraph();
            Line.add("-------------------------------------------------------------------------------");
            document.add(Line);
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

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu,inflater);

        MenuItem searchItem = menu.findItem(R.id.menu_search);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ToDoListItemAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }
}
