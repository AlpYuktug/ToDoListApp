package com.todolistapp.Activitiy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.todolistapp.Fragment.FragmentToDoList;
import com.todolistapp.Fragment.FragmentToDoListCreate;
import com.todolistapp.R;

public class Home extends AppCompatActivity {

    private Toolbar toolbar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);
        //navigation.setBackgroundColor(getColor(R.color.Back));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Fragment fragment = new FragmentToDoList();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.FragmentContent, fragment)
                .commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.ItemDisplayToDoList:
                    Fragment fragment = new FragmentToDoList();
                    GetFragment(fragment);
                    return true;

                case R.id.ItemAddToDoList:
                    Fragment fragmentAddToDoList = new FragmentToDoListCreate();
                    GetFragment(fragmentAddToDoList);
                    return true;

                case R.id.ItemLogout:
                    finish();
                    Intent intent = new Intent(Home.this, Login.class);
                    startActivity(intent);
                }

            return false;
        }
    };

    private void GetFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.FragmentContent, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

}
