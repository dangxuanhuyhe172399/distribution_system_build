package com.sep490.bads.distributionsystem.mapper;

import org.mapstruct.Mapper;

import java.time.Instant;

@Mapper(componentModel = "spring", imports = Instant.class)
public interface AuthMapper{
//    @Mapping(target = "token", source = "accessToken")
//    @Mapping(target = "refreshToken", source = "refreshToken")
//    @Mapping(target = "tokenType", constant = "Bearer")
//    @Mapping(target = "userId", source = "user.id")
//    @Mapping(target = "userEmail", source = "user.email")
//    @Mapping(target = "userName", source = "user.username")
//    @Mapping(target = "fullName", source = "user.fullName")
//    @Mapping(target = "role", source = "user.role.roleName")
//    @Mapping(target = "expiresAt", expression = "java(Instant.now().plusSeconds(expireSeconds))")
//    AuthResponse toAuthResponse(User user, String accessToken, String refreshToken, Long expireSeconds);
}
