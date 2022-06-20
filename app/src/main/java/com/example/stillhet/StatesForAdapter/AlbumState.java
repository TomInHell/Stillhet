package com.example.stillhet.StatesForAdapter;

public class AlbumState {
    private String AlbumName, Creator, Description, Image;

    public AlbumState(String albumName, String creator, String description, String image) {
        this.AlbumName = albumName;
        this.Creator = creator;
        this.Description = description;
        this.Image = image;
    }

    public String getAlbumName() { return this.AlbumName; }

    public void setAlbumName(String albumName) { this.AlbumName = albumName; }

    public String getCreator() { return this.Creator; }

    public void setCreator(String creator) { this.Creator = creator; }

    public String getDescription() { return this.Description; }

    public void setDescription(String description) { this.Description = description; }

    public String getImage() { return this.Image; }

    public void setImage(String image) { this.Image = image; }
}
