package kz.cifron.vetqyzmet_doctor;

public class DataAnimal {
    String ownerId;
    String name;
    String ING;
    String gender;
    String breed;
    String Date_of_Birth;
    String suit;
    String photo1;
    String photo2;
    String animal_type;

    public DataAnimal(String ownerId, String name, String ING, String gender, String breed, String date_of_Birth, String suit, String photo1, String photo2, String animal_type) {
        this.ownerId = ownerId;
        this.name = name;
        this.ING = ING;
        this.gender = gender;
        this.breed = breed;
        Date_of_Birth = date_of_Birth;
        this.suit = suit;
        this.photo1 = photo1;
        this.photo2 = photo2;
        this.animal_type = animal_type;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public String getDate_of_Birth() {
        return Date_of_Birth;
    }

    public String getAnimal_type() {
        return animal_type;
    }

    public String getGender() {
        return gender;
    }

    public String getING() {
        return ING;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getPhoto1() {
        return photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public String getSuit() {
        return suit;
    }
}
