package com.joshbgold.simplemusicfree;

import android.content.Context;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class SongsManager {

    public HashMap<String, String> song;
    public String songTitle;
    private String uniqueSongIDString = "0";
    public Context mContext;
    protected String lowerCaseName = "";

    // SDCard Path
    public String MEDIA_PATH = "/storage";
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

        File home = new File(MEDIA_PATH);

        uniqueSongIDString = "0";
        int uniqueSongIDInt = 0;

        if (songsList != null) {
            songsList.clear();
        }

        if (home.listFiles() != null) {
            //search audio files in root media directory user has selected
            searchForAudioFiles(home, uniqueSongIDInt);

            //search subfolders of root media directory for audio files
            File[] someFiles = home.listFiles();
            for (int i = 0; i < someFiles.length; i++) {
                File selectedFile = someFiles[i];
                if (selectedFile.isDirectory()) {
                    if (selectedFile.listFiles() != null) {
                        searchForAudioFiles(selectedFile, uniqueSongIDInt);
                    }
                }
            }
        }
        // return songs playlist_item array
        return songsList;
    }

    private void searchForAudioFiles(File home, int uniqueSongIDInt) {
            if ((home.listFiles(new FileExtensionFilter()).length > 0)) {
                for (File file : home.listFiles(new FileExtensionFilter())) {  //for each file that is an audio file in home directory
                    song = new HashMap<>();  //make a hashmap data structure to store song info

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
    }

    /**
     * Class to filter files which are having .mp3 extension
     */
    class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            lowerCaseName = name.toLowerCase();
            return (name.endsWith(".mp3") || name.endsWith(".wma") || name.endsWith(".wav") || name.endsWith(".m4a") || name.endsWith(".flac"));
        }
    }

    // Filter Class
    public ArrayList<HashMap<String, String>> filter(String searchString) {
        searchString = searchString.toLowerCase(Locale.getDefault());

        songsList.clear();
        songsList = getPlayList();

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
