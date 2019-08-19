package edu.pkch.jpaedu.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "USER")
public class User {

    @Id @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String username;

    private Integer age;

    @Embedded
    private Address address;

    @ManyToOne(optional = false)
    @JoinColumn(name = "COMPANY_ID", foreignKey = @ForeignKey(name = "USER_TO_COMPANY_FK"))
    private Company company;

    public User() {
    }

    public User(final String id, final String username, final Integer age) {
        this.id = id;
        this.username = username;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(final Company company) {
        this.company = company;
        company.addWorker(this);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}