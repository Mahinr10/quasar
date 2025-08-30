package com.personal.quasar.dao;

import com.personal.quasar.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TaskRepository extends MongoRepository<Task, String> {
    public Optional<Task> findByIdAndIsDeletedFalse(String id);
}
