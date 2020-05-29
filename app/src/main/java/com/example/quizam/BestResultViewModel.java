package com.example.quizam;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class BestResultViewModel extends AndroidViewModel {

    private BestResultRepository repository;

    private LiveData<List<BestResult>> allBestResults;



    public BestResultViewModel(@NonNull Application application) {
        super(application);
        repository = new BestResultRepository(application);
        allBestResults = repository.getAllResults();
    }

    LiveData<List<BestResult>> getAllMeasurements() {
       // return repository.getAllResults();
        return allBestResults;
    }

    public void insert(BestResult newRecord){
        repository.insert(newRecord);
    }

}
