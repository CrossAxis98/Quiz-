package com.example.quizam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.preference.PreferenceManager;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static final String CHOICES = "pref_numberOfChoices";

    public static final String CATEGORIES = "pref_categoriesToInclude";

    private static final String TAG = "MainActivity";

    /*Czy nastąpiła zmiana preferencji*/
    private boolean preferencesChanged = true;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d(TAG, "onCreate: started");

        /* Domyślne ustawienia dla obiektu SharedPreferences */
        androidx.preference.PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        /* Nasłuchiwanie zmian obiektu */
        androidx.preference.PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        Log.d(TAG, "onStart: started");
        //Context mContext = getApplicationContext();
        //SharedPreferences sharedPreferences = mContext.getSharedPreferences(CATEGORIES, Context.MODE_PRIVATE);
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
        Log.d(TAG, "onCreateOptionsMenu: started");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
   public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: started");

        switch (item.getItemId()) {

            case R.id.action_settings:
                 Intent preferenceIntent = new Intent(this, SettingsActivity.class);
                 startActivity(preferenceIntent);
            return true;

            case R.id.action_best:
                Intent bestResIntent = new Intent(this, BestResultsActivity.class);
                startActivity(bestResIntent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }



    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            preferencesChanged = true;
            Log.d(TAG, "onSharedPreferenceChanged: started");

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
