package com.personal.quasar.dao.jpa;

import com.personal.quasar.model.jpa.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRecordRepository extends JpaRepository<UserRecord, String> {

}
