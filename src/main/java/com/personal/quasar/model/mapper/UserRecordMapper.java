package com.personal.quasar.model.mapper;

import com.personal.quasar.model.dto.UserDTO;
import com.personal.quasar.model.jpa.UserRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRecordMapper {
    @Mapping(target = "password", ignore = true)
    UserRecord dtoToRecord(UserDTO userDTO);
}
