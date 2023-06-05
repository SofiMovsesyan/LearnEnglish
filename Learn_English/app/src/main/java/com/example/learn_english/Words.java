package com.example.learn_english;

public class Words {
    private int Animals;
    private int ClothesandAccessories;
    private int Education;
    private int EntertainmentandMedia;
    private int FoodandDrink;
    private int HobbiesandLeisure;
    private int Sport;
    private int Weather;

    public Words() {
        // Default constructor required for Firebase serialization
    }

    public Words(int animals, int clothesandAccessories, int education, int entertainmentandMedia, int foodandDrink, int hobbiesandLeisure, int sport, int weather) {
        Animals = animals;
        ClothesandAccessories = clothesandAccessories;
        Education = education;
        EntertainmentandMedia = entertainmentandMedia;
        FoodandDrink = foodandDrink;
        HobbiesandLeisure = hobbiesandLeisure;
        Sport = sport;
        Weather = weather;
    }

    public int getAnimals() {
        return Animals;
    }

    public void setAnimals(int animals) {
        Animals = animals;
    }

    public int getClothesandAccessories() {
        return ClothesandAccessories;
    }

    public void setClothesandAccessories(int clothesandAccessories) {
        ClothesandAccessories = clothesandAccessories;
    }

    public int getEducation() {
        return Education;
    }

    public void setEducation(int education) {
        Education = education;
    }

    public int getEntertainmentandMedia() {
        return EntertainmentandMedia;
    }

    public void setEntertainmentandMedia(int entertainmentandMedia) {
        EntertainmentandMedia = entertainmentandMedia;
    }

    public int getFoodandDrink() {
        return FoodandDrink;
    }

    public void setFoodandDrink(int foodandDrink) {
        FoodandDrink = foodandDrink;
    }

    public int getHobbiesandLeisure() {
        return HobbiesandLeisure;
    }

    public void setHobbiesandLeisure(int hobbiesandLeisure) {
        HobbiesandLeisure = hobbiesandLeisure;
    }

    public int getSport() {
        return Sport;
    }

    public void setSport(int sport) {
        Sport = sport;
    }

    public int getWeather() {
        return Weather;
    }

    public void setWeather(int weather) {
        Weather = weather;
    }
}