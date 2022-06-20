package com.example.stillhet.StatesForAdapter;

public class MusicState {
    private String SongName, Artist, Time, Link;

    public MusicState(String name, String artist, String time, String link) {
        this.SongName = name;
        this.Artist = artist;
        this.Time = time;
        this.Link = link;
    }

    public String getSongName() { return this.SongName; }

    public void setSongName(String title) { this.SongName = title; }

    public String getArtist() { return this.Artist; }

    public void setArtist(String artist) { this.Artist = artist; }

    public String getTime() { return this.Time; }

    public void setTime(String time) { this.Time = time; }

    public String getLink() { return this.Link; }

    public void setLink(String link) { this.Link = link; }
}
