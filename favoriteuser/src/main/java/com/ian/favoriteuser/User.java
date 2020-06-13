package com.ian.favoriteuser;

public class User {
    private String username;
    private String avatar;

    public User(String username, String avatar) {
        this.username = username;
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
