package edu.pkch.jpaedu.advanced.identify2;

import javax.persistence.*;

@Entity
@IdClass(GCId.class)
public class GC {
    @Id
    @ManyToOne
    private C child;

    @Id
    private Long id;
}
