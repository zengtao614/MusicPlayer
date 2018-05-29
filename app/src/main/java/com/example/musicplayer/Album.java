package com.example.musicplayer;

public class Album{
    private String albumName;
    private String singerName;
    private int albumId;

    public Album(String albumName,String singerName,int albumId){
        this.albumName = albumName;
        this.singerName = singerName;
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getSingerName() {
        return singerName;
    }

    public int getAlbumId() {
        return albumId;
    }
}