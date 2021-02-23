package com.pass.net4_proyecto_integrador;

public class User {
    private String name, email, phoneNumber, passwd;

    public User(){

    }

    public User(String name, String email, String phoneNumber, String passwd) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.passwd = passwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", passwd='" + passwd + '\'' +
                '}';
    }
}
