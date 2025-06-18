package org.sopt.domain;

import jakarta.persistence.*;
import org.sopt.global.entity.BaseEntity;

@Entity
@Table(name = "likes", uniqueConstraints = {  // 조합을 unique로 -> 중복 막기
        @UniqueConstraint(columnNames = {"user_id", "post_id"}),
        @UniqueConstraint(columnNames = {"user_id", "comment_id"})
})
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    protected Like() {}

    private Like(User user, Post post, Comment comment) {
        this.user = user;
        this.post = post;
        this.comment = comment;
    }

    public static Like forPost(User user, Post post) {
        return new Like(user, post, null);
    }

    public static Like forComment(User user, Comment comment) {
        return new Like(user, null, comment);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Post getPost() {
        return post;
    }

    public Comment getComment() {
        return comment;
    }
}
