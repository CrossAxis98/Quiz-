package com.example.quizam;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bestResults_table")
public class BestResult {

    @NonNull
    @PrimaryKey
    private String dbNick;

    @NonNull
    private Integer dbTotalGuessess;

    public BestResult(String dbNick, Integer dbTotalGuessess)
    {
        this.dbNick=dbNick;
        this.dbTotalGuessess=dbTotalGuessess;
    }

    public String getDbNick(){return this.dbNick;}

    public Integer getDbTotalGuessess(){return this.dbTotalGuessess;}
}
