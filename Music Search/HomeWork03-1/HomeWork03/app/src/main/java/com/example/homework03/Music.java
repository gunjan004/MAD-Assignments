package com.example.homework03;

import java.io.Serializable;

public class Music implements Serializable {

    String titleTrack, trackPrice, artistName, date, trackImage, albumPrice, albumName, genre;

    public Music(){

    }

    @Override
    public String toString() {
        return "Music{" +
                "titleTrack='" + titleTrack + '\'' +
                ", trackPrice='" + trackPrice + '\'' +
                ", artistName='" + artistName + '\'' +
                ", date='" + date + '\'' +
                ", trackImage='" + trackImage + '\'' +
                ", albumPrice='" + albumPrice + '\'' +
                ", albumName='" + albumName + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }
}
