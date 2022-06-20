package com.example.stillhet.StatesForAdapter;

public class DiscussionState {
    private String Head, discussTheme, Body, Creator, Like;

    public DiscussionState(String head, String theme, String body, String creator, String like) {
        this.Head = head;
        this.discussTheme = theme;
        this.Body = body;
        this.Creator = creator;
        this.Like = like;
    }

    public String getHead() { return this.Head; }

    public void setHead(String head) { this.Head = head; }

    public String getDiscussTheme() { return this.discussTheme; }

    public void setDiscussTheme(String discussTheme) { this.discussTheme = discussTheme; }

    public String getBody() { return this.Body; }

    public void setBody(String body) { this.Body = body; }

    public String getCreator() { return this.Creator; }

    public void setCreator(String creator) { this.Creator = creator; }

    public String getLike() { return this.Like; }

    public void setLike(String like) { this.Like = like; }
}
