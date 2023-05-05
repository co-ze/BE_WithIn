package com.example.within.repository;

import com.example.within.dto.UserResponseDto;
import com.example.within.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query("select new com.example.within.dto.UserResponseDto(u.username, u.email) from User u")
    List<UserResponseDto> selectAllMember();
}
