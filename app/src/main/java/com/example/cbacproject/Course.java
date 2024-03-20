package com.example.cbacproject;

public class Course {
    private String name;
    private String season;
    private String round;
    private String locality;
    private String country;
    private String first;
    private String firstTime;
    private String constructorFirst;
    private String second;
    private String secondTime;
    private String constructorSecond;
    private String third;
    private String thirdTime;
    private String constructorThird;

    public Course(String name, String season, String round, String locality, String country, String first, String firstTime, String constructorFirst, String second, String secondTime, String constructorSecond, String third, String thirdTime, String constructorThird) {
        this.name=name;
        this.season = season;
        this.round = round;
        this.locality = locality;
        this.country = country;
        this.first = first;
        this.firstTime = firstTime;
        this.constructorFirst = constructorFirst;
        this.second = second;
        this.secondTime = secondTime;
        this.constructorSecond = constructorSecond;
        this.third = third;
        this.thirdTime = thirdTime;
        this.constructorThird = constructorThird;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(String firstTime) {
        this.firstTime = firstTime;
    }

    public String getConstructorFirst() {
        return constructorFirst;
    }

    public void setConstructorFirst(String constructorFirst) {
        this.constructorFirst = constructorFirst;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getSecondTime() {
        return secondTime;
    }

    public void setSecondTime(String secondTime) {
        this.secondTime = secondTime;
    }

    public String getConstructorSecond() {
        return constructorSecond;
    }

    public void setConstructorSecond(String constructorSecond) {
        this.constructorSecond = constructorSecond;
    }

    public String getThird() {
        return third;
    }

    public void setThird(String third) {
        this.third = third;
    }

    public String getThirdTime() {
        return thirdTime;
    }

    public void setThirdTime(String thirdTime) {
        this.thirdTime = thirdTime;
    }

    public String getConstructorThird() {
        return constructorThird;
    }

    public void setConstructorThird(String constructorThird) {
        this.constructorThird = constructorThird;
    }
}
