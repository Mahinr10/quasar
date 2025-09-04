package com.personal.quasar.model.mapper;

import com.personal.quasar.model.dto.UserDTO;
import com.personal.quasar.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO entityToDTO(User user);
    User dtoToEntity(UserDTO userDTO);
}
