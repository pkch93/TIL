package edu.pkch.jpaedu.advanced.superclass;

import javax.persistence.*;

@Entity
@DiscriminatorValue("MOVIE")
@PrimaryKeyJoinColumn(name = "MOVIE_ID")
public class Movie extends Item {
    private String director;
    private String actor;

    public String getDirector() {
        return director;
    }

    public String getActor() {
        return actor;
    }
}
