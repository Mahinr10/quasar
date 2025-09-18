package com.personal.quasar.service;

import com.personal.quasar.model.dto.UserDTO;

import java.util.List;

public interface TimeZoneSchedulerService {
    List<UserDTO> getUsersByTimeZoneId(String timeZoneId);

    List<String> getSelectedTimeZoneAtZeroHour();

}
