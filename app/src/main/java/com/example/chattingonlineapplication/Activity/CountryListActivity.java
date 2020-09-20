package com.example.chattingonlineapplication.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Adapter.ListCountryAdapter;
import com.example.chattingonlineapplication.R;
import com.example.chattingonlineapplication.Webservice.Model.CountryModel;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class CountryListActivity extends AppCompatActivity {

    //normal variable
    private ListCountryAdapter listCountryAdapter;
    private ArrayList<CountryModel> lstCountry;

    //binding View
    private MaterialSearchView materialSearchView;
    private Toolbar toolbarCountryList;
    private RecyclerView lstViewCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_list);
        reflection();

        try {
            lstCountry = (ArrayList<CountryModel>) getIntent().getExtras().getSerializable("COUNTRY_LIST");
        } catch (Exception e) {
            e.printStackTrace();
        }

        setSupportActionBar(toolbarCountryList);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true );

        toolbarCountryList.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listCountryAdapter = new ListCountryAdapter(this, lstCountry);
        lstViewCountry.setLayoutManager(linearLayoutManager);
        lstViewCountry.setAdapter(listCountryAdapter);

    }

    private void executeSearch(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.itemCountrySearch);
        materialSearchView.setMenuItem(menuItem);
        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listCountryAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    public void reflection() {
        lstViewCountry = findViewById(R.id.lstViewCountry);
        lstCountry = new ArrayList<>();
        toolbarCountryList = findViewById(R.id.toolbarCountryList);
        materialSearchView = findViewById(R.id.searchViewLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.country_list_menu, menu);
        executeSearch(menu);
        return true;
    }
}