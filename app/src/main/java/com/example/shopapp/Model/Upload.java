package com.example.shopapp.Model;

public class Upload {
    private String name;
    private String userAvatarUrl;
    private String email;

    public Upload() {
    }

    public Upload(String name, String userAvatarUrl, String email) {
        this.name = name;
        this.userAvatarUrl = userAvatarUrl;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
