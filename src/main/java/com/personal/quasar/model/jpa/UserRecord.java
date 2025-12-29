package com.personal.quasar.model.jpa;

import com.personal.quasar.model.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "userid")
    private UUID userId;

    @Column(name = "id")
    private String id;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "userrole")
    private UserRole userRole = UserRole.USER;

    @Column(name = "timezoneid")
    private String timeZoneId;

    @Column(name = "createdby")
    private String createdBy;

    @Column(name = "createddate")
    private Date createdDate;

    @Column(name = "lastmodifiedby")
    private String lastModifiedBy;

    @Column(name = "lastmodifieddate")
    private Date lastModifiedDate;

    @Column(name = "isdeleted")
    private Boolean isDeleted = false;
}
