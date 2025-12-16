package com.personal.quasar.controller;

import com.personal.quasar.model.dto.TimeZoneDTO;
import com.personal.quasar.service.TimeZoneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/timezone")
@Slf4j
public class TimeZoneController {
    @Autowired
    private TimeZoneService timeZoneService;

    @GetMapping
    public ResponseEntity<List<TimeZoneDTO>> getAll() {
        List<TimeZoneDTO> timeZones = timeZoneService.getAllTimeZones();
        return ResponseEntity.ok(timeZones);
    }

    @PutMapping
    public ResponseEntity<TimeZoneDTO> updateTimeZone(@RequestBody TimeZoneDTO timeZoneDTO) {
        TimeZoneDTO updatedTimeZone = timeZoneService.updateTimeZone(timeZoneDTO);
        return ResponseEntity.ok(updatedTimeZone);
    }

    @GetMapping("/selected")
    public ResponseEntity<Set<TimeZoneDTO>> getSelectedAll() {
        log.info("getSelectedAll method started");
        Set<TimeZoneDTO> timeZoneDTOs = timeZoneService.getSelectedTimeZones();
        return  ResponseEntity.ok(timeZoneDTOs);
    }
}
