package kz.cifron.vetqyzmet_doctor;

import java.io.File;

public class DataAnimalWithImage {
    String ownerId;
    String name;
    String ING;
    String gender;
    String breed;
    String Date_of_Birth;
    String suit;
    File photo1;
    File photo2;
    File photo3;
    String animal_type;

    public DataAnimalWithImage(String ownerId, String name, String ING, String gender, String breed, String date_of_Birth, String suit, File photo1, File photo2, File photo3, String animal_type) {
        this.ownerId = ownerId;
        this.name = name;
        this.ING = ING;
        this.gender = gender;
        this.breed = breed;
        Date_of_Birth = date_of_Birth;
        this.suit = suit;
        this.photo1 = photo1;
        this.photo2 = photo2;
        this.photo3 = photo3;
        this.animal_type = animal_type;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getName() {
        return name;
    }

    public String getING() {
        return ING;
    }

    public String getGender() {
        return gender;
    }

    public String getBreed() {
        return breed;
    }

    public String getDate_of_Birth() {
        return Date_of_Birth;
    }

    public String getSuit() {
        return suit;
    }

    public File getPhoto1() {
        return photo1;
    }

    public File getPhoto2() {
        return photo2;
    }

    public File getPhoto3() {
        return photo3;
    }

    public String getAnimal_type() {
        return animal_type;
    }
}
