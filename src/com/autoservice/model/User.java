package com.autoservice.model;

public abstract class User {
    private String username;
    private String password;
    private String role;

    public User(String username, String passwordHash, String role) {
        this.username = username;
        this.password = passwordHash;
        this.role = role;
    }

    public abstract void showMenu();

    public boolean checkPassword(String inputPassword) {
        return password.equals(inputPassword);
    }

    // Геттеры
    public String getUsername() { return username; }
    public String getPasswordHash() { return password; }
    public String getRole() { return role; }
}

