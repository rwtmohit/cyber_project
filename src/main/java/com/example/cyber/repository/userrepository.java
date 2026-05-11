package com.example.cyber.repository;

import com.example.cyber.entity.user;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface userrepository extends MongoRepository<user, String> {
    Optional<user> findByEmail(String email);
}

