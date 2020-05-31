package com.example.quizam;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class BestResultRepository {

    private BestResultDao bestResultDao;
    private LiveData<List<BestResult>> allResults;

    BestResultRepository(Application application){
        BestResultDatabase db = BestResultDatabase.getDatabase(application);
        bestResultDao = db.bestResultDao();
        allResults = bestResultDao.getSortedRecords();
    }

    LiveData<List<BestResult>> getAllResults() {
        return allResults;
    }

    void insert(BestResult newRecord)
    {
        BestResultDatabase.databaseWriteExecutor.execute(() -> {
            bestResultDao.insert(newRecord);
        });
    }

}
