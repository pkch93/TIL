package edu.pkch.jpaedu.advanced.mapped_superclass;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany
    @JoinColumn
    private List<Product> products = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
