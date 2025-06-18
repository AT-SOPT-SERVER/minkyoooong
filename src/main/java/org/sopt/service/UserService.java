package org.sopt.service;

import org.sopt.domain.User;
import org.sopt.dto.UserRequest;
import org.sopt.dto.UserResponse;
import org.sopt.global.CustomException;
import org.sopt.global.ErrorCode;
import org.sopt.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse register(UserRequest request) {

        if (userRepository.existsByNickname(request.nickname())) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }
        User user = userRepository.save(new User(request.nickname()));
        return new UserResponse(user.getId(), user.getNickname());
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
