package com.example.musicapp.Entities;

import java.io.Serializable;

public class Score implements Serializable{
    String name ;
    int Score ;

    public Score(String name, int score) {
        this.name = name;
        Score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return Score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        Score = score;
    }


}
