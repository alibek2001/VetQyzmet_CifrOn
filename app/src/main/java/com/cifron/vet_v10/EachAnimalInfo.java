package com.cifron.vet_v10;

public class EachAnimalInfo {
    int id;
    String nvp;
    String age;
    String sex;
    String color;
    String inj;
    boolean waitingVacine;

    EachAnimalInfo(int id, String nvp, String age, String sex, String color, String inj, boolean waitingVacine) {
        this.id = id;
        this.nvp = nvp;
        this.age=age;
        this.sex=sex;
        this.color=color;
        this.inj=inj;
        this.waitingVacine=waitingVacine;
    }

    public int getId() {
        return id;
    }

    public String getAge() {
        return age;
    }

    public String getColor() {
        return color;
    }

    public String getInj() {
        return inj;
    }

    public String getNvp() {
        return nvp;
    }

    public String getSex() {
        return sex;
    }

    public boolean isWaitingVacine() {
        return waitingVacine;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setInj(String inj) {
        this.inj = inj;
    }

    public void setNvp(String nvp) {
        this.nvp = nvp;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setWaitingVacine(boolean waitingVacine) {
        this.waitingVacine = waitingVacine;
    }
}
