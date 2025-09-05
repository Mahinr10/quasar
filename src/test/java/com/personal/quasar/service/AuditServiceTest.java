package com.personal.quasar.service;

import com.personal.quasar.UnitTest;
import com.personal.quasar.model.entity.AuditEntity;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuditServiceTest extends UnitTest {
    @Mock
    private UserProfileFacade userProfileFacade;
    @InjectMocks
    private AuditService auditService = new AuditServiceImpl();

    @Test
    public void populateAuditFieldsForCreateTest() {
        var testUser = "testUser";
        when(userProfileFacade.getActiveUserId()).thenReturn("testUser");
        var auditEntity = mock(AuditEntity.class);
        when(auditEntity.getCreatedBy()).thenReturn(null);
        doNothing().when(auditEntity).setCreatedBy(anyString());
        doNothing().when(auditEntity).setCreatedDate(any());
        doNothing().when(auditEntity).setLastModifiedBy(anyString());
        doNothing().when(auditEntity).setLastModifiedDate(any());

        auditService.populateAuditFields(auditEntity);

        verify(auditEntity, times(1)).setCreatedBy(testUser);
        verify(auditEntity, times(1)).setCreatedDate(any());
        verify(auditEntity, times(1)).setLastModifiedBy(anyString());
        verify(auditEntity, times(1)).setLastModifiedDate(any());
    }

    @Test
    public void populateAuditFieldsForUpdateTest() {
        var testUser = "testUser";
        when(userProfileFacade.getActiveUserId()).thenReturn("testUser");
        var auditEntity = mock(AuditEntity.class);
        when(auditEntity.getCreatedBy()).thenReturn(testUser);
        doNothing().when(auditEntity).setCreatedBy(anyString());
        doNothing().when(auditEntity).setCreatedDate(any());
        doNothing().when(auditEntity).setLastModifiedBy(anyString());
        doNothing().when(auditEntity).setLastModifiedDate(any());

        auditService.populateAuditFields(auditEntity);

        verify(auditEntity, times(0)).setCreatedBy(testUser);
        verify(auditEntity, times(0)).setCreatedDate(any());
        verify(auditEntity, times(1)).setLastModifiedBy(anyString());
        verify(auditEntity, times(1)).setLastModifiedDate(any());
    }
}
