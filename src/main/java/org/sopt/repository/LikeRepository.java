package org.sopt.repository;

import org.sopt.domain.Comment;
import org.sopt.domain.Like;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);
    Optional<Like> findByUserAndComment(User user, Comment comment);
    int countByPost(Post post);
    int countByComment(Comment comment);
}
