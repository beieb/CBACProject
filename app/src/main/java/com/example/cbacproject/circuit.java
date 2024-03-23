package com.example.cbacproject;

public class circuit {
    private String nom;
    private String lat;
    private String lon;

    public circuit(String nom, String lat, String lon) {
        this.nom = nom;
        this.lat = lat;
        this.lon = lon;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
