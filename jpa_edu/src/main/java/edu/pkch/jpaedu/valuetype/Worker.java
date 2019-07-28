package edu.pkch.jpaedu.valuetype;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Worker {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;

    private String city;
    private String street;
    private String zipcode;
}
