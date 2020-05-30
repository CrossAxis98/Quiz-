package com.example.quizam;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class BestResultViewModel extends AndroidViewModel {

    private BestResultRepository repository;

    private LiveData<List<BestResult>> allBestResults;

    private static final String TAG = "BestResultViewModel";



    public BestResultViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "BestResultViewModel: started");
        repository = new BestResultRepository(application);
        allBestResults = repository.getAllResults();
    }

    LiveData<List<BestResult>> getAllMeasurements() {
        Log.d(TAG, "getAllMeasurements: started");
       // return repository.getAllResults();
        return allBestResults;
    }

    public void insert(BestResult newRecord){
        Log.d(TAG, "insert: started");
        repository.insert(newRecord);
    }

}
