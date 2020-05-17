package com.example.quizam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.preference.PreferenceManager;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static final String CHOICES = "pref_numberOfChoices";
    public static final String CATEGORIES = "pref_categoriesToInclude";

    private boolean phoneDevice = true;

    private boolean preferencesChanged = true;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        androidx.preference.PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        androidx.preference.PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(preferenceChangeListener);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        if ((screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE) || (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE))
        {
            phoneDevice = false;
        }

         if(phoneDevice) {
           setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
      //  Context mContext = getApplicationContext();
      //  SharedPreferences sharedPreferences = mContext.getSharedPreferences(CATEGORIES, Context.MODE_PRIVATE);
        if(preferencesChanged)
        {
            FirstFragment quizFragment = (FirstFragment) getSupportFragmentManager().findFragmentById(R.id.quizFragment);
            quizFragment.updateGuessRows(PreferenceManager.getDefaultSharedPreferences(this));
            quizFragment.updateCategories(PreferenceManager.getDefaultSharedPreferences(this));
            quizFragment.resetQuiz();
            preferencesChanged = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int orientation = getResources().getConfiguration().orientation;

        if(orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }
        else
        {
            return false;
        }


    }

    @Override
   public boolean onOptionsItemSelected(MenuItem item) {

        Intent preferenceIntent = new Intent(this, SecondFragment.class);
        startActivity(preferenceIntent);

        return super.onOptionsItemSelected(item);
    }

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            preferencesChanged = true;

            FirstFragment quizFragment = (FirstFragment) getSupportFragmentManager().findFragmentById(R.id.quizFragment);

            if(key==CHOICES)
            {
                quizFragment.updateGuessRows(sharedPreferences);

                quizFragment.resetQuiz();
            }
            else if (key.equals(CATEGORIES) )
            {
                Set<String> categories = sharedPreferences.getStringSet(CATEGORIES, null);

                if(categories != null && categories.size()>0)
                {
                    quizFragment.updateCategories(sharedPreferences);
                    quizFragment.resetQuiz();
                }
                else
                {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    categories.add(getString(R.string.default_furniture));
                    editor.putStringSet(CATEGORIES, categories);
                    editor.apply();

                    Toast.makeText(MainActivity.this, R.string.default_furniture_message, Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(MainActivity.this, R.string.restarting_quiz,Toast.LENGTH_SHORT).show();
            }

        }
    };


}
