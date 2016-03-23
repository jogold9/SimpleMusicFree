package com.joshbgold.simplemusicfree;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class PlayListActivity extends ListActivity {

    private EditText editSearch;  //search text input by user
    private ImageView searchIcon;
    public SimpleAdapter simpleAdapter;
    private ListView listView;
    private ArrayList<HashMap<String, String>> songsListData;
    public ArrayList<HashMap<String, String>> songsList = new ArrayList<>();  //stores all the songs
    public ArrayList<HashMap<String, String>> filteredSongsList = new ArrayList<>();  //stores songs that match search
    private int songsAddedCounter = 0;  //counter for debugging -> are songs being added to list?
    public boolean listIsFiltered = false;
    private Context context;
    private String colorTheme = "";
    private SongsManager songsManager;
    public String musicFolderPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);

        context = getApplicationContext();

        // Set up the layout elements for this activity
        editSearch = (EditText) findViewById(R.id.search);
        searchIcon = (ImageView) findViewById(R.id.search_icon);

        songsListData = new ArrayList<>();  //Stores all the songs to put into ListView

        musicFolderPath =  loadPrefs("folder", musicFolderPath);  //if user has chosen a media folder, get their choice

        songsManager = new SongsManager(context, musicFolderPath);
        // get all songs from SD card
        this.songsList = songsManager.getPlayList();  //gets all the songs from the phone and puts them in the HashMap

        createListViewUsingSongs();  //draws the ListView on the screen using the songsList HashMap

        // selecting single ListView item
        listView = getListView();

        // listening to single playlist_item item click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                String songTitle, songPath, songUniqueID, artist, album;
                                                int songIndex;

                                                HashMap<String, String> song = (HashMap<String, String>) parent.getItemAtPosition(position);
                                                songTitle = song.get("songTitle");
                                                songPath = song.get("songPath");
                                                songUniqueID = song.get("songUniqueID");

                                                songIndex = Integer.parseInt(songUniqueID);

                                                // Starting new intent
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                                                // Sending all info about song to MainActivity
                                                intent.putExtra("songTitle", songTitle);
                                                intent.putExtra("songPath", songPath);
                                                intent.putExtra("songUniqueID", songUniqueID);
                                                intent.putExtra("songIndex", songIndex);
                                               /* intent.putExtra("artist", artist);
                                                intent.putExtra("album", album);*/

                                                setResult(100, intent);

                                                // Closing PlayListView
                                                finish();
                                            }
                                        }

        );

        /**
         * When user clicks search icon, execute a search, then update the ListView simpleAdapter
         */
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String text = editSearch.getText().toString().toLowerCase(Locale.getDefault());
                filteredSongsList = songsManager.filter(text);
                updateListViewUsingSongs();
                listIsFiltered = true;
            }
        });
    }

    private void createListViewUsingSongs() {
        // looping through playlist
        for (int i = 0; i < songsList.size(); i++) {
            // creating new HashMap
            HashMap<String, String> song = songsList.get(i);
            // adding HashList to ArrayList
            songsListData.add(song);

        }

        // Adding menuItems to ListView
        simpleAdapter = new SimpleAdapter(this, songsListData,
                R.layout.playlist_item, new String[]{"songTitle"}, new int[]{
                R.id.songTitle});

        setListAdapter(simpleAdapter);
    }

    private void updateListViewUsingSongs() {

        songsAddedCounter = 0;
        songsListData.clear();  //super important that we start from zero, and add only the filtered songs!
        songsList.clear(); //Is this line needed??

        // looping through playlist
        for (int i = 0; i < filteredSongsList.size(); i++) {
            // creating new HashMap
            HashMap<String, String> song = filteredSongsList.get(i);
            // adding HashList to ArrayList
            songsListData.add(song);
            songsAddedCounter++;
        }
        Toast.makeText(getApplicationContext(), "Search results: " + songsAddedCounter + " songs", Toast.LENGTH_LONG).show();

        simpleAdapter = null;

        simpleAdapter = new SimpleAdapter(this, songsListData,
                R.layout.playlist_item, new String[]{"songTitle"}, new int[]{
                R.id.songTitle});


        setListAdapter(simpleAdapter);
        simpleAdapter.notifyDataSetChanged();
    }

    //get prefs
    public String loadPrefs(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPreferences.getString(key, value);
    }

       /* private void setTheme() {

        loadPrefs("color", colorTheme);
        ActionBar bar = getActionBar();

        switch (colorTheme) {
            case "Royal":
                setTheme(R.style.RoyalNoActionBar);
                if (bar != null) {
                    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));  //sets action bar to color primary dark
                }
                break;
            case "Forest":
                setTheme(R.style.ForestNoActionBar);
                if (bar != null) {
                    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0D6529")));  //sets action bar to color primary dark
                }
                break;
            case "Vermilion":
                setTheme(R.style.VermilionNoActionBar);
                if (bar != null) {
                    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A91616")));  //sets action bar to color primary dark
                }
                break;
            case "Charcoal":
                setTheme(R.style.CharcoalNoActionBar);
                if (bar != null) {
                    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#656666")));  //sets action bar to color primary dark
                }
                break;
            default:
                setTheme(R.style.RoyalNoActionBar);
                if (bar != null) {
                    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));  //sets action bar to color primary dark
                }
                break;
        }
    }*/

}