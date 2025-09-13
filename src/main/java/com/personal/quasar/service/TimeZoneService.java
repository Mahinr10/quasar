package com.personal.quasar.service;

import com.personal.quasar.model.dto.TimeZoneDTO;

import java.util.List;
import java.util.TimeZone;

public interface TimeZoneService {
    List<TimeZoneDTO> getAllTimeZones();
    TimeZoneDTO updateTimeZone(TimeZoneDTO timeZoneDTO);
}
