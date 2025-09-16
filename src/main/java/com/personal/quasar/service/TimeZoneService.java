package com.personal.quasar.service;

import com.personal.quasar.common.exception.InvalidFieldException;
import com.personal.quasar.common.exception.ResourceDoesNotExistException;
import com.personal.quasar.model.dto.TimeZoneDTO;

import java.util.List;
import java.util.TimeZone;

public interface TimeZoneService {
    List<TimeZoneDTO> getAllTimeZones();
    TimeZoneDTO updateTimeZone(TimeZoneDTO timeZoneDTO);
    Boolean isValidTImeZone(String timeZoneId);

}
