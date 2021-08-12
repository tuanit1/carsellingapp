package com.example.autosellingapp.items;

public class RateItem {
    private String from;
    private String to;
    private int score;

    public RateItem(String from, String to, int score) {
        this.from = from;
        this.to = to;
        this.score = score;
    }

    public RateItem(){

    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
