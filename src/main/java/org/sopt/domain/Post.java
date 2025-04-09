package org.sopt.domain;

public class Post {

    private int id;
    private String title;

    public Post(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    // 제목의 유효성 -> Post 객체 자체의 책임으로 domain에서 처리
    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) { // title.trim().isEmpty()로 문자열이 공백만 있는지 검사
            throw new IllegalArgumentException("제목은 비어있을 수 없습니다.");
        }
        if (title.length() > 30) {
            throw new IllegalArgumentException("제목은 30자를 넘을 수 없습니다.");
        }
    }
}
