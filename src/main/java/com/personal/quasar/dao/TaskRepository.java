package com.personal.quasar.dao;

import com.personal.quasar.model.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends MongoRepository<Task, String> {
    public Optional<Task> findByIdAndIsDeletedFalse(String id);

    public Optional<List<Task>> findAllByIsDeletedFalse();
}
