package com.arianameble.quiz;

import android.content.SharedPreferences;
import
        android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static final String CHOICES = "pref_numberOfChoices";
    public static final String CATEGORIES = "pref_categoriesToInclude";

    private boolean phoneDevice = true;

    private boolean preferencesChanged = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        androidx.preference.PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        androidx.preference.PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(preferenceChangeListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

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
