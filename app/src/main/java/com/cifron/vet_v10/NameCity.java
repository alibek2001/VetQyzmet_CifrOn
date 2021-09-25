package com.cifron.vet_v10;

public class NameCity {
    String name;
    int count;
    String path;
    int level;

    NameCity(String name, int count, String path, int level) {
        this.name = name;
        this.count = count;
        this.path = path;
        this.level = level;
    }
    public int getCount() {
        return count;
    }

    public String getName() {
        return name;
    }

    public String getPath() {return path;}

    public int getLevel() {
        return level;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
