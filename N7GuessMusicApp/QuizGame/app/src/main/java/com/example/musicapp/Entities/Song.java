package com.example.musicapp.Entities;

import java.io.Serializable;
import java.util.ArrayList;

public class Song  implements Serializable {
    String name ;
    String artist ;
    ArrayList<String> choices =  new ArrayList<String>() ;

    public Song(String name, String artist, ArrayList<String> choices) {
        this.name = name;
        this.artist = artist;
        this.choices = choices;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }
}
