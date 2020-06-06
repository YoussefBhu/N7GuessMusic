package com.example.musicapp.Entities;

import java.io.Serializable;
import java.util.ArrayList;

public class QuizObject implements Serializable {
    User user ;
    String code ;
    String owner ;
    ArrayList<Song> playlist = new ArrayList<Song>() ;
    int score;
    int current ;
    public QuizObject(String code, String owner, ArrayList<Song> playlist) {
        this.code = code;
        this.owner = owner;
        this.playlist = playlist;
        this.score = 0 ;
        this.current = 0 ;
    }

    public String getCode() {
        return code;
    }

    public String getOwner() {
        return owner;
    }

    public ArrayList<Song> getPlaylist() {
        return playlist;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setPlaylist(ArrayList<Song> playlist) {
        this.playlist = playlist;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
