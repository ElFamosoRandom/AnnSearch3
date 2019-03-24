package com.grochowski.testapp.databaseClass;

public class interestPoint {

    private String id;
    private String indice1;
    private String indice2;
    private String indice3;
    private Double latitude;
    private Double longitude;
    private String name;

    public interestPoint() {

    }

    public interestPoint(String id, String indice1, String indice2, String indice3, Double latitude, Double longitude, String name) {
        this.id = id;
        this.indice1 = indice1;
        this.indice2 = indice2;
        this.indice3 = indice3;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getIndice1() {
        return indice1;
    }

    public String getIndice2() {
        return indice2;
    }

    public String getIndice3() {
        return indice3;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
    public String getName() {
        return name;
    }
}
