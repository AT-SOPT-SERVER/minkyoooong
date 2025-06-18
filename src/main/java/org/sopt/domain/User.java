package org.sopt.domain;

import jakarta.persistence.*;
import org.sopt.global.entity.BaseEntity;

@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10) // 이름 null 불가. 길이 10자 제한.
    private String nickname;

    protected User() {}

    public User(String nickname) {
        this.nickname = nickname;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }
}