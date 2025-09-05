package com.personal.quasar.service;

import com.personal.quasar.model.entity.AuditEntity;

public interface AuditService {
    void populateAuditFields(AuditEntity auditEntity);
}
