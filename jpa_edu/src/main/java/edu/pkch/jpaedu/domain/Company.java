package edu.pkch.jpaedu.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NamedEntityGraph(name="Company.users", attributeNodes = {
        @NamedAttributeNode("users")
})
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "COMPANY_NAME")
    private String name;

    @OneToMany
    @JoinColumn(name = "USER_ID")
    private List<User> users = new ArrayList<>();

    public Company() {
    }

    public Company(final String name) {
        this.name = name;
    }

    public void addWorker(User user) {
        users.add(user);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        return users;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Company company = (Company) o;
        return Objects.equals(id, company.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
