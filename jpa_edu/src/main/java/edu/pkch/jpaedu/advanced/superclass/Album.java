package edu.pkch.jpaedu.advanced.superclass;

import javax.persistence.*;

@Entity
@DiscriminatorValue("ALBUM")
@PrimaryKeyJoinColumn(name = "ALBUM_ID")
public class Album extends Item {
    private String artist;

    public String getArtist() {
        return artist;
    }
}
