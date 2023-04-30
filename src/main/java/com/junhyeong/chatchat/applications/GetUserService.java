package com.junhyeong.chatchat.applications;

import com.junhyeong.chatchat.exceptions.UserNotFound;
import com.junhyeong.chatchat.models.user.User;
import com.junhyeong.chatchat.models.user.UserName;
import com.junhyeong.chatchat.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GetUserService {
    private final UserRepository userRepository;

    public GetUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User find(UserName userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(UserNotFound::new);

        return user;
    }
}
