package com.personal.quasar.service.impl;

import com.personal.quasar.UnitTest;
import com.personal.quasar.dao.TimeZoneRepository;
import com.personal.quasar.model.dto.TimeZoneDTO;
import com.personal.quasar.model.entity.TimeZone;
import com.personal.quasar.model.mapper.TimeZoneMapper;
import com.personal.quasar.service.AuditService;
import com.personal.quasar.service.TimeZoneService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TimeZoneServiceImplTest extends UnitTest {
    @Mock
    private TimeZoneRepository timeZoneRepository;

    @Mock
    private AuditService auditService;

    @Mock
    private TimeZoneMapper timeZoneMapper;

    @InjectMocks
    private TimeZoneServiceImpl timeZoneService;

    @Test
    public void getAllTimeZonesTest() {
        var zone1 = "zone1";
        var zone2 = "zone2";
        var zone3 = "zone3";

        when(timeZoneRepository.findByIsDeletedFalse()).thenReturn(Optional.of(List.of(getTimeZone(zone1))));

        try(MockedStatic<ZoneId> mockedStatic = mockStatic(ZoneId.class)) {
            mockedStatic.when(ZoneId::getAvailableZoneIds).thenReturn(Set.of(zone1, zone2, zone3));
            List<TimeZoneDTO> result = timeZoneService.getAllTimeZones();
            Assertions.assertEquals(3, result.size());
        }
    }

    @Test
    public void testUpdateTimeZone_IsSelectedTrue_RecordPresent_IsDeletedTrue() {
        TimeZoneDTO timeZoneDTO = new TimeZoneDTO();
        timeZoneDTO.setTimeZoneId("zone1");
        timeZoneDTO.setIsSelected(true);

        TimeZone existingTimeZone = new TimeZone();
        existingTimeZone.setTimeZoneId("zone1");
        existingTimeZone.setIsDeleted(true);

        TimeZone savedTimeZone = new TimeZone();
        savedTimeZone.setTimeZoneId("zone3");
        savedTimeZone.setIsDeleted(false);

        when(timeZoneRepository.findByTimeZoneId("zone1")).thenReturn(Optional.of(existingTimeZone));
        when(timeZoneRepository.save(any())).thenReturn(savedTimeZone);
        when(timeZoneMapper.entityToDTO(any())).thenReturn(timeZoneDTO);

        var result = timeZoneService.updateTimeZone(timeZoneDTO);
        Assertions.assertNotNull(result);

        verify(auditService).populateAuditFields(existingTimeZone);
        verify(timeZoneRepository).save(existingTimeZone);
    }

    @Test
    public void testUpdateTimeZone_IsSelectedTrue_RecordNotPresent() {
        TimeZoneDTO timeZoneDTO = new TimeZoneDTO();
        timeZoneDTO.setTimeZoneId("zone2");
        timeZoneDTO.setIsSelected(true);

        TimeZone newTimeZone = new TimeZone();

        when(timeZoneRepository.findByTimeZoneId("zone2")).thenReturn(Optional.empty());
        when(timeZoneMapper.dtoToEntity(timeZoneDTO)).thenReturn(newTimeZone);

        timeZoneService.updateTimeZone(timeZoneDTO);

        verify(auditService).populateAuditFields(newTimeZone);
        verify(timeZoneRepository).save(newTimeZone);
    }

    @Test
    public void testUpdateTimeZone_IsSelectedFalse_RecordPresent() {
        TimeZoneDTO timeZoneDTO = new TimeZoneDTO();
        timeZoneDTO.setTimeZoneId("zone3");
        timeZoneDTO.setIsSelected(false);

        TimeZone existingTimeZone = new TimeZone();
        existingTimeZone.setTimeZoneId("zone3");
        existingTimeZone.setIsDeleted(false);

        TimeZone savedTimeZone = new TimeZone();
        savedTimeZone.setTimeZoneId("zone3");
        savedTimeZone.setIsDeleted(true);

        when(timeZoneRepository.findByTimeZoneId("zone3")).thenReturn(Optional.of(existingTimeZone));
        when(timeZoneRepository.save(existingTimeZone)).thenReturn(savedTimeZone);
        when(timeZoneMapper.entityToDTO(any())).thenReturn(timeZoneDTO);

        var result = timeZoneService.updateTimeZone(timeZoneDTO);

        Assertions.assertNotNull(result);

        verify(auditService, times(1)).populateAuditFields(existingTimeZone);
        verify(timeZoneRepository, times(1)).save(existingTimeZone);
    }

    @Test
    public void testUpdateTimeZone_IsSelectedFalse_RecordNotPresent() {
        TimeZoneDTO timeZoneDTO = new TimeZoneDTO();
        timeZoneDTO.setTimeZoneId("zone4");
        timeZoneDTO.setIsSelected(false);

        when(timeZoneRepository.findByTimeZoneId("zone4")).thenReturn(Optional.empty());

        timeZoneService.updateTimeZone(timeZoneDTO);

        verify(timeZoneRepository, never()).save(any());
        verify(auditService, never()).populateAuditFields(any());
    }

    @Test
    public void isValidTimeZoneTest() {
        TimeZone timeZone = getTimeZone("Europe/London");
        when(timeZoneRepository.findByTimeZoneId(timeZone.getTimeZoneId())).thenReturn(Optional.of(timeZone));
        Assertions.assertTrue(timeZoneService.isValidTImeZone(timeZone.getTimeZoneId()));
    }

    private TimeZone getTimeZone(String timeZoneId) {
        TimeZone timeZone = new TimeZone();
        timeZone.setTimeZoneId(timeZoneId);
        timeZone.setIsDeleted(false);
        return timeZone;
    }

    private TimeZoneDTO getTImeZoneDTO(String zoneId, Boolean isSelected) {
        TimeZoneDTO timeZoneDTO = new TimeZoneDTO();
        timeZoneDTO.setTimeZoneId(zoneId);
        timeZoneDTO.setIsSelected(isSelected);
        return timeZoneDTO;
    }
}