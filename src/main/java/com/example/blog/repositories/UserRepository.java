package com.example.blog.repositories;

import com.example.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, PagingAndSortingRepository<User, Long> {
    Optional<User> findByUserId(String userId);
    Optional<User> findByEmailOrPhoneNumber(String email, String phoneNumber);
//    Optional<User> findByEmailAndPassword(String email, String password);
//    Optional<User> findByPhoneNumberAndPassword(String phoneNumber, String password);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
}
