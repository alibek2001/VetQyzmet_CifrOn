package kz.cifron.vetqyzmet_doctor;

public class CardInfo {
    String ing;
    String photo1;
    String photo2;
    String photo3;
    String gender;
    String dateOfBirth;
    String breed;
    String suit;

    public CardInfo(String ing, String photo1, String photo2, String photo3, String gender, String dateOfBirth, String breed, String suit) {
        this.ing = ing;
        this.photo1 = photo1;
        this.photo2 = photo2;
        this.photo3 = photo3;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.breed = breed;
        this.suit = suit;
    }

    public String getIng() {
        return ing;
    }

    public String getPhoto1() {
        return photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getBreed() {
        return breed;
    }

    public String getSuit() {
        return suit;
    }

    public void setIng(String ing) {
        this.ing = ing;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }
}
