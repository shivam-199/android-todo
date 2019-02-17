package com.example.firebasedb;

public class Artist {
    private String artistName;
    private String artistId;


    Artist(){}

    public Artist(String artistId, String artistName) {
        this.artistName = artistName;
        this.artistId = artistId;

    }

    public Artist(String artistId) {
        this.artistId = artistId;

    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    }
