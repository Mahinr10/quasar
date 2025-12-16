package com.personal.quasar.model.mapper;

import com.personal.quasar.model.dto.UserDTO;
import com.personal.quasar.model.entity.User;
import com.personal.quasar.model.enums.UserRole;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Date;

@Mapper(componentModel = "spring")
public interface UserDocumentMapper {
    @Mapping(target = "id", expression = "java(toHexId(document))")

    @Mapping(target = "firstName", expression = "java(document.getString(\"firstName\"))")
    @Mapping(target = "lastName", expression = "java(document.getString(\"lastName\"))")
    @Mapping(target = "email", expression = "java(document.getString(\"email\"))")
    @Mapping(target = "timeZoneId", expression = "java(document.getString(\"timeZoneId\"))")
    @Mapping(target = "userRole", expression = "java(toUserRole(document.get(\"userRole\")))")

//    @Mapping(target = "createdBy", expression = "java(document.getString(\"createdBy\"))")
//    @Mapping(target = "createdDate", expression = "java(toDate(document.get(\"createdDate\")))")
//    @Mapping(target = "lastModifiedBy", expression = "java(document.getString(\"lastModifiedBy\"))")
//    @Mapping(target = "lastModifiedDate", expression = "java(toDate(document.get(\"lastModifiedDate\")))")
//    @Mapping(target = "isDeleted", expression = "java(toBoolean(document.get(\"isDeleted\")))")
    UserDTO toDto(Document document);

    // -------- helpers --------

    default String toHexId(Document document) {
        ObjectId id = document.getObjectId("_id");
        return id != null ? id.toHexString() : null;
    }

    default UserRole toUserRole(Object raw) {
        if (raw == null) return UserRole.USER;

        try {
            return UserRole.valueOf(raw.toString().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return UserRole.USER;
        }
    }

    default Boolean toBoolean(Object raw) {
        if (raw == null) return Boolean.FALSE;
        if (raw instanceof Boolean b) return b;
        return Boolean.parseBoolean(raw.toString());
    }

    default Date toDate(Object raw) {
        if (raw == null) return null;
        if (raw instanceof Date d) return d;
        if (raw instanceof Long l) return new Date(l);
        return null;
    }
}
