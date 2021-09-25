package com.cifron.vet_v10;

public class AnimalCardInfo {
    int id;
    String name;
    String count;
    AnimalCardInfo(int id, String name, String count) {
        this.id = id;
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public String getCount() {
        return count;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setId(int id) {
        this.id = id;
    }
}
