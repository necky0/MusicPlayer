package com.example.necky0.musicplayer;

import android.media.MediaPlayer;

public class Song {
    private String title;
    private String author;
    private String time;
    private int songLocation;

    public Song(String title, String author, int songLocation) {
        this.title = title;
        this.author = author;
        this.songLocation = songLocation;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getTime() {
        return time;
    }

    public int getSongLocation() {
        return songLocation;
    }
}
