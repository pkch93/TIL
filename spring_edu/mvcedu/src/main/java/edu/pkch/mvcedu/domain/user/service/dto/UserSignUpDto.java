package edu.pkch.mvcedu.domain.user.service.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

public class UserSignUpDto {

    @Email
    private String email;

    @Min(2)
    @Max(10)
    private String username;

    @Min(6)
    @Max(20)
    @Pattern(regexp = "\\w{6,20}")
    private String password;

    private int age;

    public UserSignUpDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(final int age) {
        this.age = age;
    }
}
