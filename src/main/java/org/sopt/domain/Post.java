package org.sopt.domain;

import jakarta.persistence.*;
import org.sopt.global.entity.BaseEntity;

@Entity
public class Post extends BaseEntity {

    @Id // id 직접 생성 x, jpa가 생성
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30, unique = true) // 제목 30자, 내용 1000자 제한
    private String title; // 제목 중복 x, 유일성 보장을 위해 unique 조건 추가

    @Column(nullable = false, length = 1000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TagType tag; // 태그 추가 (필수)

    @ManyToOne(fetch = FetchType.LAZY) // user - post 연관관계 맵핑
    @JoinColumn(name = "user_id", nullable = false)
    private User writer;

    protected Post() {
    }

    public Post(String title, String content, TagType tag, User writer) {
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.writer = writer;
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

    public TagType getTag() {
        return tag;
    }


    public void update(String title, String content, TagType tag) { // 수정 시 null이 아닐 경우에만
        if (title != null && !title.isBlank()) this.title = title;
        if (content != null && !content.isBlank()) this.content = content;
        if (tag != null) this.tag = tag; // 태그는 필수
    }

    public User getWriter() {
        return writer;
    }
}