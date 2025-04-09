package org.sopt.repository;

import org.sopt.domain.Post;

import java.util.ArrayList;
import java.util.List;

public class PostRepository {

    List<Post> postList = new ArrayList<>();

    public void save(Post post) {
        postList.add(post);
    }

    public List<Post> findAll() {
        return postList;
    }

    public Post findPostById(int id) {
        for (Post post : postList) {
            if (post.getId() == id) {
                return post;
            }
        }

        return null;
    }

    public boolean delete(int id) {
        for (Post post : postList) {
            if (post.getId() == id) {
                postList.remove(post);
                return true;
            }
        }
        return false;
    }

    // 중복 검사 메서드 -> repository에 추가
    // 데이터를 직접 다루는 책임은 Repository에 있으므로 이곳에 구현
    public boolean existsByTitle(String title) {
        for (Post post : postList) {
            if (post.getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }

    // 가장 마지막 게시물을 가져오는 기능
    public Post findLastPost() {
        if (postList.isEmpty()) return null;
        return postList.get(postList.size() - 1);
    }
}
