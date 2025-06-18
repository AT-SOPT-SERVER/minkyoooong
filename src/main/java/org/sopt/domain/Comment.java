package org.sopt.domain;

import jakarta.persistence.*;
import org.sopt.global.entity.BaseEntity;

@Entity
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    protected Comment() {}

    public Comment(String content, User writer, Post post) {
        this.content = content;
        this.writer = writer;
        this.post = post;
    }

    public void update(String content) {
        if (content != null && !content.isBlank()) {
            this.content = content;
        }
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public User getWriter() {
        return writer;
    }

    public Post getPost() {
        return post;
    }
}
