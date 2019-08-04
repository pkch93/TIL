package edu.pkch.jpaedu.dto;

public class UserDto {
    private String username;
    private Integer age;

    public UserDto() {
    }

    public UserDto(final String username, final Integer age) {
        this.username = username;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(final Integer age) {
        this.age = age;
    }
}
