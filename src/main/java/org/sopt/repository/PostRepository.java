package org.sopt.repository;

import org.sopt.domain.Post;
import org.sopt.domain.TagType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // spring data jpa 사용 (중복 제목 조회, 키워드 검색 쿼리)
    boolean existsByTitle(String title);

    @Query("SELECT p FROM Post p WHERE p.title LIKE %:title%")
    List<Post> findPostsByTitleContaining(@Param("title") String title);

    @Query("SELECT p FROM Post p WHERE p.writer.nickname LIKE %:nickname%")
    List<Post> findPostsByWriterNicknameContaining(@Param("nickname") String nickname);

    List<Post> findByTag(TagType tag);
    List<Post> findAllByOrderByCreatedAtDesc();

    Optional<Post> findByTitle(String title);
    List<Post> findAllByWriterIdOrderByCreatedAtDesc(Long writerId);

}