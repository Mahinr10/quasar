package com.personal.quasar.dao;

import com.personal.quasar.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByIdAndIsDeletedFalse(String id);
    Optional<User> findByEmailAndIsDeletedFalse(String email);
    Boolean existsByEmailAndIsDeletedFalse(String email);
}
