package com.example.quizam;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BestResultListAdapter extends RecyclerView.Adapter<BestResultListAdapter.ViewHolder> {

    private static final String TAG = "MeasurementListAdapter";

    private final LayoutInflater inflater;
    private  List<BestResult> bestResults;



    public BestResultListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: started");
        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: started");
        if(bestResults != null){
            BestResult current = bestResults.get(position);
            String percent = String.format("%.2f%%",((1000/(double) current.getDbTotalGuessess())));
            holder.bestResTextView.setText(current.getDbNick() + " - " + percent );


        }
        else{
            holder.bestResTextView.setText("Brak");

        }

    }

    void setBestResults(List<BestResult> bestResultsG){
        Log.d(TAG, "setBestResults: started");
        bestResults = bestResultsG;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: started");
        if(bestResults != null){
            return bestResults.size();
        }
        else{
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView bestResTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bestResTextView = (TextView) itemView.findViewById(R.id.textView);

        }
    }


}

