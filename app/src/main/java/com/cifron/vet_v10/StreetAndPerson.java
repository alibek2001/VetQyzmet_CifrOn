package com.cifron.vet_v10;

public class StreetAndPerson {
    String name;
    String street;
    int localityId;
    int personId;

    public StreetAndPerson(String name, String street, int localityId, int personId) {
        this.name = name;
        this.street = street;
        this.localityId = localityId;
        this.personId = personId;
    }
    public String getName() {
        return name;
    }

    public String getStreet() {
        return street;
    }

    public int getLocalityId() {
        return localityId;
    }

    public int getPersonId() {
        return personId;
    }
}
