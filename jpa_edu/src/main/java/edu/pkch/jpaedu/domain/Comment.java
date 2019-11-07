package edu.pkch.jpaedu.domain;

import javax.persistence.*;

@Entity
@Table(name = "COMMENT")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;
    private String content;

    public Comment() {
    }

    public Comment(String content) {
        this.content = content;
    }

    /**
     *  N : 1 다대일 매핑 (with Article)
     */
//    @ManyToOne
//    @JoinColumn(name = "ARTICLE_ID", foreignKey = @ForeignKey(name = "ARTICLE_TO_COMMENT_FK"))
//    private Article article;

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

//    public Article getArticle() {
//        return article;
//    }
}
