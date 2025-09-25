package com.personal.quasar.service.impl;

import com.personal.quasar.common.exception.InvalidFieldException;
import com.personal.quasar.common.exception.ResourceDoesNotExistException;
import com.personal.quasar.dao.TimeZoneRepository;
import com.personal.quasar.model.dto.TimeZoneDTO;
import com.personal.quasar.model.entity.TimeZone;
import com.personal.quasar.model.mapper.TimeZoneMapper;
import com.personal.quasar.service.AuditService;
import com.personal.quasar.service.TimeZoneService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.personal.quasar.util.ValidationErrorMessages.PROVIDED_INVALID_TIMEZONE;

public class TimeZoneServiceImpl implements TimeZoneService {
    @Autowired
    TimeZoneRepository timeZoneRepository;

    @Autowired
    AuditService auditService;

    @Autowired
    TimeZoneMapper timeZoneMapper;

    @Override
    public List<TimeZoneDTO> getAllTimeZones() {
        Set<String> selectedTimeZoneIds = timeZoneRepository.findByIsDeletedFalse()
                .map(
                        timeZones -> timeZones.stream()
                                .map(TimeZone::getTimeZoneId)
                                .collect(java.util.stream.Collectors.toSet())
                )
                .orElse(new HashSet<>());
        return getTimeZoneDTOs(selectedTimeZoneIds);
    }

    @Override
    public TimeZoneDTO updateTimeZone(TimeZoneDTO timeZoneDTO) {
        if (timeZoneDTO.getIsSelected()) {
            handleSelectedTimeZone(timeZoneDTO);
        } else {
            handleUnselectedTimeZone(timeZoneDTO);
        }
        return timeZoneDTO;
    }

    @Override
    public Boolean isValidTImeZone(String timeZoneId)  {
        return timeZoneRepository.findByTimeZoneId(timeZoneId).isPresent();
    }

    private List<TimeZoneDTO> getTimeZoneDTOs(Set<String> selectedTimeZoneIds) {
        List<TimeZoneDTO> timeZoneDTOs = new ArrayList<>();
        Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
        for(var timeZone: availableZoneIds) {
            TimeZoneDTO timeZoneDTO = new TimeZoneDTO();
            timeZoneDTO.setTimeZoneId(timeZone);
            if(selectedTimeZoneIds.contains(timeZone)) {
                timeZoneDTO.setIsSelected(true);
            } else {
                timeZoneDTO.setIsSelected(false);
            }
            timeZoneDTOs.add(timeZoneDTO);
        }
        return timeZoneDTOs;
    }

    private void handleSelectedTimeZone(TimeZoneDTO timeZoneDTO) {
        var existingTimeZone = timeZoneRepository.findByTimeZoneId(timeZoneDTO.getTimeZoneId());
        if (existingTimeZone.isPresent()) {
            updateExistingTimeZoneIfDeleted(existingTimeZone.get());
        } else {
            createNewTimeZone(timeZoneDTO);
        }
    }

    private void handleUnselectedTimeZone(TimeZoneDTO timeZoneDTO) {
        var existingTimeZoneOpt = timeZoneRepository.findByTimeZoneId(timeZoneDTO.getTimeZoneId());
        existingTimeZoneOpt.ifPresent(this::markTimeZoneAsDeleted);
    }

    private void updateExistingTimeZoneIfDeleted(TimeZone existingTimeZone) {
        existingTimeZone.setIsDeleted(false);
        auditService.populateAuditFields(existingTimeZone);
        timeZoneRepository.save(existingTimeZone);
    }

    private void createNewTimeZone(TimeZoneDTO timeZoneDTO) {
        TimeZone newTimeZone = timeZoneMapper.dtoToEntity(timeZoneDTO);
        auditService.populateAuditFields(newTimeZone);
        timeZoneRepository.save(newTimeZone);
    }

    private void markTimeZoneAsDeleted(TimeZone existingTimeZone) {
        existingTimeZone.setIsDeleted(true);
        auditService.populateAuditFields(existingTimeZone);
        timeZoneRepository.save(existingTimeZone);
    }
}
