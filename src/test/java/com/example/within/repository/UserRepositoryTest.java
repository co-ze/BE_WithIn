package com.example.within.repository;

import com.example.within.entity.User;
import com.example.within.entity.UserRoleEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @AfterEach
    public void clear(){
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("사용자 생성되는 지 확인하는 테스트")
    public void signup(){
        //given
        User dto = User.builder()
                .username("김씨")
                .email("kim12@naver.com")
                .password("qwer1234")
                .role(UserRoleEnum.USER)
                .img("")
                .build();
        User dto2 = User.builder()
                .username("박씨")
                .email("park12@naver.com")
                .password("qwer1234")
                .role(UserRoleEnum.ADMIN)
                .img("")
                .build();

        // when
        User user = userRepository.save(dto);
        User user2 = userRepository.save(dto2);
        System.out.println("---userId : "+user.getId());

        //then
        Assertions.assertThat(user.getEmail()).isEqualTo(dto.getEmail());
        Assertions.assertThat(user2.getEmail()).isEqualTo(dto2.getEmail());
    }

    @Test
    @DisplayName("사용자의 리스트 반환 확인")
    void UserList(){
        // given
        User dto = User.builder()
                .username("김씨")
                .email("kim12@naver.com")
                .password("qwer1234")
                .role(UserRoleEnum.USER)
                .img("")
                .build();
        User dto2 = User.builder()
                .username("박씨")
                .email("park12@naver.com")
                .password("qwer1234")
                .role(UserRoleEnum.ADMIN)
                .img("")
                .build();
        userRepository.save(dto);
        userRepository.save(dto2);

        // when
        List<User> result = userRepository.findAll();

        // then
        Assertions.assertThat(result.size()).isEqualTo(2);

    }
}