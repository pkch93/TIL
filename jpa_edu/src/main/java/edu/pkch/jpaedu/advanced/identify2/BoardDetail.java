package edu.pkch.jpaedu.advanced.identify2;

import javax.persistence.*;

@Entity
public class BoardDetail {
    @Id
    private Long boardId;

    @MapsId
    @OneToOne
    private Board board;
    private String content;
    // .,.
}
