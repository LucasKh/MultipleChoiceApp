package com.example.multiplequestionapp;

public class Users
{
    private long id;
    private String username;
    private String password;
    private String confPassword;

    public Users(){}

    public Users(long id, String username, String password, String confPassword) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.confPassword = confPassword;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfPassword() {
        return confPassword;
    }

    public void setConfPassword(String confPassword) {
        this.confPassword = confPassword;
    }
}
