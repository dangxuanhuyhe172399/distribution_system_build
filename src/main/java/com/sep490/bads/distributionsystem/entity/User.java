package com.sep490.bads.distributionsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sep490.bads.distributionsystem.entity.type.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.usertype.UserType;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "User")
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private UserStatus status;

    // private Long birthday;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "gender", length = 16)
    private String gender;

    @Column(name = "user_code", length = 50, nullable = false, unique = true)
    private String userCode;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "avatar", length = 255)
    private String avatar;

    @Column(name = "address", length = 255)
    private String address;
}
