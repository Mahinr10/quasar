package com.personal.quasar.model.mapper;

import com.personal.quasar.model.dto.TaskDTO;
import com.personal.quasar.model.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDTO entityToDTO(Task task);
    Task dtoToEntity(TaskDTO taskDTO);

}
