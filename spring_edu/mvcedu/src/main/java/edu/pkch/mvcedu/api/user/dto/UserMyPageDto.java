package edu.pkch.mvcedu.api.user.dto;

public class UserMyPageDto {
    private String username;
    private String email;
    private int age;

    public UserMyPageDto() {
    }

    public UserMyPageDto(final String username, final String email, final int age) {
        this.username = username;
        this.email = email;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(final int age) {
        this.age = age;
    }
}
