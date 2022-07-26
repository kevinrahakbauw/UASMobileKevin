package com.example.uas.models;

public class Data {
    private String judul, deskripsi, imageUrl;

    public Data() {
    }

    public Data(String judul, String deskripsi, String imageUrl) {
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.imageUrl = imageUrl;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
