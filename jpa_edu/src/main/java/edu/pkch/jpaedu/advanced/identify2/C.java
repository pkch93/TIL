package edu.pkch.jpaedu.advanced.identify2;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

@Entity
@IdClass(CId.class)
public class C {

    @Id
    @ManyToOne
    private P parent;

    @Id
    private Long childId;
}
