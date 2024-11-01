package com.example.project.model;

public class UserInfo {

    public static final String TABLE_NAME = "users";
    public static final String ID_COL = "id";
    public static final String EMAIL_COL = "email";
    public static final String PASSWORD_COL = "password";
    public static final String FIRST_NAME_COL = "firstName";
    public static final String LAST_NAME_COL = "lastName";

    private int id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
