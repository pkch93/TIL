package edu.pkch.mvcedu.domain.user.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(length = 20, nullable = false)
    private String password;

    @Column(length = 10, unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private int age;

    public User() {
    }

    private User(Builder builder) {
        this.email = builder.email;
        this.username = builder.username;
        this.password = builder.password;
        this.age = builder.age;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", age=" + age +
                '}';
    }

    public static class Builder {
        private final String email;
        private final String username;
        private final String password;
        private final int age;

        public Builder(String email, String username, String password, int age) {
            this.email = email;
            this.username = username;
            this.password = password;
            this.age = age;
        }

        public User build() {
            return new User(this);
        }
    }
}
