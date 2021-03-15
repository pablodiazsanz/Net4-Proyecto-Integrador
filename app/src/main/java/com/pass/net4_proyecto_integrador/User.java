package com.pass.net4_proyecto_integrador;

public class User {
    private String userId,username, name, email, phoneNumber, description;
    private double latitud,longitud;

    public User() {
        super();
    }

    public User(String userId, String username, String name, String email, String phoneNumber, String description, double latitud, double longitud) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", description='" + description + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                '}';
    }
}
