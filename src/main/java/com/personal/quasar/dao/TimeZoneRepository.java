package com.personal.quasar.dao;

import com.personal.quasar.model.entity.TimeZone;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TimeZoneRepository extends MongoRepository<TimeZone, String> {
    Optional<List<TimeZone>> findByIsDeletedFalse();

    Optional<TimeZone> findByTimeZoneId(String timeZoneId);

    Optional<TimeZone> findByTimeZoneIdAndIsDeletedFalse(String timeZoneId);

}
