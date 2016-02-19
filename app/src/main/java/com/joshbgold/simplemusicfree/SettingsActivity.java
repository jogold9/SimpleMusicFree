package com.joshbgold.simplemusicfree;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingsActivity extends MainActivity {

    private Spinner colorChoiceSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        android.app.ActionBar actionBar = getActionBar();

        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));  //sets action bar to color primary dark
        }

        colorChoiceSpinner = (Spinner) findViewById(R.id.spinnerChooseColor);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.colors_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        colorChoiceSpinner.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //get user choice from spinner/drop down, then save to preferences
        String colorChoice = colorChoiceSpinner.getSelectedItem().toString();
        savePrefs("color", colorChoice);
    }

    //save prefs
    public void savePrefs(String key, String value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    //get prefs
    public String loadPrefs(String key, String value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPreferences.getString(key, value);
    }
}
