package com.personal.quasar.model.mapper;

import com.personal.quasar.model.dto.TimeZoneDTO;
import com.personal.quasar.model.entity.TimeZone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TimeZoneMapper {
    TimeZone dtoToEntity(com.personal.quasar.model.dto.TimeZoneDTO timeZoneDTO);
    @Mapping(target = "isSelected", constant = "true")
    TimeZoneDTO entityToDTO(TimeZone timeZone);
}
