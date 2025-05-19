package model;

public abstract class User {
    private String name;
    private String password;
    private String role;

    public User(String username, String passwordHash, String role) {
        this.name = username;
        this.password = passwordHash;
        this.role = role;
    }

    public abstract void showMenu();

    public boolean checkPassword(String inputPassword) {
        return password.equals(inputPassword);
    }

    // Геттеры
    public String getUsername() { return name; }
    public String getRole() { return role; }
}

