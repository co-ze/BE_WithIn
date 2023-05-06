package com.example.within.repository;

import com.example.within.dto.UserPageResponseDto;
import com.example.within.dto.UserResponseDto;
import com.example.within.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query("select new com.example.within.dto.UserResponseDto(u.username, u.email) from User u")
    List<UserResponseDto> selectAllUser();

    @Query("select new com.example.within.dto.UserPageResponseDto(u.username, u.role, u.email, u.img) from User u where u.id = :userId")
    Optional<UserPageResponseDto> selectUser(@Param("userId") Long userId);

}
