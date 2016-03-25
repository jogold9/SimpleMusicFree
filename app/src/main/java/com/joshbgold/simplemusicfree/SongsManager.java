package com.joshbgold.simplemusicfree;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class SongsManager {

    public Context mContext;
    public HashMap<String, String> song;
    public String songTitle;
    public String uniqueSongIDString = "0";
    public int uniqueSongIDInt = 0;

    // SDCard Path
    public String MEDIA_PATH = "";
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> filteredSongsList = new ArrayList<>();


    public SongsManager(Context context, String folderPath) {
        mContext = context;
        if (folderPath != null && !"".equals(folderPath)) {
            MEDIA_PATH = folderPath;
        }
    }

    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     */
    public ArrayList<HashMap<String, String>> getPlayList() {

        if (MEDIA_PATH != null && !MEDIA_PATH.equals("")) {
            File home = new File(MEDIA_PATH);

            if (songsList != null) {
                songsList.clear();
            }

            File[] listFiles = home.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        searchForAudioFiles(file);
                    }
                }
            }
        }
        // return songs playlist_item array
        return songsList;
    }

    //search all subfolders of home root media directory for audio files
    private void scanDirectory(File directory) {
        if (directory != null) {
            File[] listFiles = directory.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        searchForAudioFiles(file);
                    }

                }
            }
        }
    }

    private void searchForAudioFiles(File file) {
        String songTitleLowerCase = file.getName().toLowerCase();
        if (isAudioFile(songTitleLowerCase)) {
            HashMap<String, String> song = new HashMap<String, String>(); //make a hashmap data structure to store song info

            songTitle = file.getName();

            //remove track numbers from song titles
            songTitle = songTitle.replaceFirst("^\\d*\\s", "");  //replaces leading digits & following space
            songTitle = songTitle.replaceFirst("^\\d*\\-\\d*", "");  //replaces leading digits, following hyphen, and following digits

            song.put("songTitle", songTitle);
            song.put("songPath", file.getPath());
            song.put("songUniqueID", uniqueSongIDString);

            uniqueSongIDInt++;
            uniqueSongIDString = String.valueOf(uniqueSongIDInt);

            // Adding each song to SongList
            songsList.add(song);
        }
    }

    private boolean isAudioFile(String songTitleLowerCase) {
        return songTitleLowerCase.endsWith(".mp3") || songTitleLowerCase.endsWith(".wma") || songTitleLowerCase.endsWith(".wav") || songTitleLowerCase.endsWith(".m4a") || songTitleLowerCase.endsWith("" +
                ".flac");
    }

    // Filter Class
    public ArrayList<HashMap<String, String>> filter(String searchString) {
        searchString = searchString.toLowerCase(Locale.getDefault());

        //songsList.clear();
        //songsList = getPlayList();

        //searchString is empty, so show all songs in results
        if (searchString.length() == 0) {

            if (filteredSongsList != null) {
                filteredSongsList.clear();
            }
            filteredSongsList = songsList;
        }

        //only return songs that match the search string
        else {

            if (filteredSongsList != null) {
                filteredSongsList.clear();
            }

            for (HashMap<String, String> song : songsList) {
                if (song != null) {
                    String songTitle = song.get("songTitle");
                    if (songTitle.toLowerCase().contains(searchString)) {
                        filteredSongsList.add(song);
                    }
                }
            }
        }

        return filteredSongsList;
    }

}
