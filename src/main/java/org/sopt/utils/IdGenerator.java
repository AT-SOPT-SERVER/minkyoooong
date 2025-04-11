package org.sopt.utils;

// id 생성은 비즈니스 로직이라기보단, 부수 기능에 가까우므로 service에서 분리하는 것이 깔끔하다.
public class IdGenerator {
    private static int id = 1;

    public static int generateId() {
        return id++;
    }
}