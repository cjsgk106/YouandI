package com.example.andorid.youandi.album;

public class Photo {
    private String date;
    private String username;
    private String photo;
    private String ptext;

    public Photo(String username, String date, String photo, String ptext){
        this.username = username;
        this.date = date;
        this.photo = photo;
        this.ptext = ptext;
    }

    public String getUsername(){return username;}
    public String getDate(){return date;}
    public String getPhoto(){return photo;}
    public String getPtext(){return ptext;}
}
