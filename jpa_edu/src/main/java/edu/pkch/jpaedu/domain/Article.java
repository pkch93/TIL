package edu.pkch.jpaedu.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ARTICLE")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ARTICLE_ID")
    private Long id;
    @Column(nullable = false, length = 30)
    private String title;
    @Lob
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_BY")
    private Date createdBy;

    /**
     * 1 : N 일대다 매핑 (with Comment)
     * 일반적으로 1 : N에서는 N 쪽에서 외래키를 관리하므로
     * N 쪽에서 외래키를 관리하겠다는 명시가 없다면 연결 테이블이 생성된다.
     */
    @OneToMany
    /**
     * 외래키 매핑, 이를 통해 연결테이블 없이 외래키로만 매핑할 수 있다.
     */
    @JoinColumn(name = "ARTICLE_ID", foreignKey = @ForeignKey(name = "ARTICLE_COMMENT_FK"))
    private List<Comment> comments = new ArrayList<>();


    public Article() {
    }

    public Article(final String title, final String content) {
        this.title = title;
        this.content = content;
        createdBy = new Date();
    }

    public Article(String title, String content, List<Comment> comments) {
        this(title, content);
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Date getCreatedBy() {
        return createdBy;
    }

    public List<Comment> getComments() {
        return comments;
    }
}
