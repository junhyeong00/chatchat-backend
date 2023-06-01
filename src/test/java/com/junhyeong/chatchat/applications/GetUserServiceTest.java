package com.junhyeong.chatchat.applications;

import com.junhyeong.chatchat.applications.user.GetUserService;
import com.junhyeong.chatchat.exceptions.UserNotFound;
import com.junhyeong.chatchat.models.user.User;
import com.junhyeong.chatchat.models.commom.UserName;
import com.junhyeong.chatchat.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetUserServiceTest {
    private UserRepository userRepository;
    private GetUserService getUserService;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        getUserService = new GetUserService(userRepository);
    }

    @Test
    void whenUserExists() {
        UserName userName = new UserName("test123");

        given(userRepository.findByUserName(userName))
                .willReturn(Optional.of(User.fake(userName)));

        assertDoesNotThrow(() -> getUserService.find(userName));
    }

    @Test
    void whenUserNotExists() {
        UserName userName = new UserName("notExists@gmail.com");

        given(userRepository.findByUserName(userName))
                .willReturn(Optional.empty());

        assertThrows(UserNotFound.class,
                () -> getUserService.find(userName));
    }
}
