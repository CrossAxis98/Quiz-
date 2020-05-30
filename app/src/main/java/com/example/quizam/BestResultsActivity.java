package com.example.quizam;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

public class BestResultsActivity extends AppCompatActivity {

    private static final String TAG = "BestResultsActivity";

    private BestResultViewModel bestResultViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.best_results_layout);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final BestResultListAdapter adapter = new BestResultListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bestResultViewModel = new ViewModelProvider(this).get(BestResultViewModel.class);

        bestResultViewModel.getAllMeasurements().observe(this, new Observer<List<BestResult>>() {
            @Override
            public void onChanged(List<BestResult> bestResults) {
                adapter.setBestResults(bestResults);
            }
        });

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: started");

        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
