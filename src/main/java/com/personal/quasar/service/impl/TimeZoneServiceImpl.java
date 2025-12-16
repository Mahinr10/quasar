package com.personal.quasar.service.impl;

import com.personal.quasar.common.exception.InvalidFieldException;
import com.personal.quasar.common.exception.ResourceDoesNotExistException;
import com.personal.quasar.dao.TimeZoneRepository;
import com.personal.quasar.model.dto.TimeZoneDTO;
import com.personal.quasar.model.entity.TimeZone;
import com.personal.quasar.model.mapper.TimeZoneMapper;
import com.personal.quasar.service.AuditService;
import com.personal.quasar.service.TimeZoneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.personal.quasar.util.ValidationErrorMessages.PROVIDED_INVALID_TIMEZONE;

@Component
@Slf4j
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
            return handleSelectedTimeZone(timeZoneDTO);
        } else {
            return handleUnselectedTimeZone(timeZoneDTO);
        }
    }

    @Override
    public Boolean isValidTImeZone(String timeZoneId)  {
        return timeZoneRepository.findByTimeZoneId(timeZoneId).isPresent();
    }

    @Override
    public Set<TimeZoneDTO> getSelectedTimeZones() {
        log.info("getSelectedTimeZones method started");
        List<TimeZone> selectedTimeZones = timeZoneRepository.findByIsDeletedFalse().orElse(new ArrayList<>());
        return selectedTimeZones.stream()
                .map(timeZoneMapper::entityToDTO)
                .collect(Collectors.toSet());
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

    private TimeZoneDTO handleSelectedTimeZone(TimeZoneDTO timeZoneDTO) {
        var existingTimeZone = timeZoneRepository.findByTimeZoneId(timeZoneDTO.getTimeZoneId());
        if (existingTimeZone.isPresent()) {
            return updateExistingTimeZoneIfDeleted(existingTimeZone.get());
        } else {
            return createNewTimeZone(timeZoneDTO);
        }
    }

    private TimeZoneDTO handleUnselectedTimeZone(TimeZoneDTO timeZoneDTO) {
        var existingTimeZoneOpt = timeZoneRepository.findByTimeZoneId(timeZoneDTO.getTimeZoneId());
        existingTimeZoneOpt.ifPresent(this::markTimeZoneAsDeleted);
        return existingTimeZoneOpt.map(timeZoneMapper::entityToDTO).orElse(null);
    }

    private TimeZoneDTO updateExistingTimeZoneIfDeleted(TimeZone existingTimeZone) {
        existingTimeZone.setIsDeleted(false);
        auditService.populateAuditFields(existingTimeZone);
        var timeZone = timeZoneRepository.save(existingTimeZone);
        return timeZoneMapper.entityToDTO(timeZone);
    }

    private TimeZoneDTO createNewTimeZone(TimeZoneDTO timeZoneDTO) {
        TimeZone newTimeZone = timeZoneMapper.dtoToEntity(timeZoneDTO);
        auditService.populateAuditFields(newTimeZone);
        return timeZoneMapper.entityToDTO(timeZoneRepository.save(newTimeZone));
    }

    private void markTimeZoneAsDeleted(TimeZone existingTimeZone) {
        existingTimeZone.setIsDeleted(true);
        auditService.populateAuditFields(existingTimeZone);
        timeZoneRepository.save(existingTimeZone);
    }
}
