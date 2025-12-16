package com.personal.quasar.dao;

import com.personal.quasar.model.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public interface TaskRepository extends MongoRepository<Task, String> {
    Optional<Task> findByIdAndIsDeletedFalse(String id);

    Optional<List<Task>> findAllByIsDeletedFalse();

    Optional<List<Task>> findByScheduledDateAndIsDeletedFalseAndCreatedByOrderByCreatedDateAsc(
            Date scheduleDate, String createdBy);
}
