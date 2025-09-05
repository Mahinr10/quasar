package com.personal.quasar.service;

import com.personal.quasar.model.entity.AuditEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AuditServiceImpl implements AuditService {

    @Autowired
    UserProfileFacade userProfileFacade;
    @Override
    public void populateAuditFields(AuditEntity auditEntity) {
        if(auditEntity.getCreatedBy() == null) {
            populateCreateAuditFields(auditEntity);
        }
        populateUpdateAuditFields(auditEntity);

    }
    private void populateCreateAuditFields(AuditEntity auditEntity) {
        auditEntity.setCreatedBy(userProfileFacade.getActiveUserId());
        auditEntity.setCreatedDate(new Date());
    }

    private void populateUpdateAuditFields(AuditEntity auditEntity) {
        auditEntity.setLastModifiedBy(userProfileFacade.getActiveUserId());
        auditEntity.setLastModifiedDate(new Date());
    }

}
