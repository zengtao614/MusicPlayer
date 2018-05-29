package com.example.musicplayer;


public class Music {

    private String Mname;
    private String Mpath;
    private String Sname;
    private int albumId;

    public Music(String Mname, String Sname,String Mpath,int albumId) {
        this.Mname = Mname;
        this.Sname = Sname;
        this.Mpath = Mpath;
        this.albumId = albumId;
    }

    public String getMname() {
        return Mname;
    }

    public String getMpath(){
        return Mpath;
    }
    public String getSname() {
        return Sname;
    }
    public int   getalbumId() {
        return albumId;
    }

}