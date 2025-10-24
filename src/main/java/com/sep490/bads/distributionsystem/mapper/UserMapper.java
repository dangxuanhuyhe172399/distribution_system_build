package com.sep490.bads.distributionsystem.mapper;

import com.sep490.bads.distributionsystem.dto.UserDto;
import com.sep490.bads.distributionsystem.entity.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper extends EntityMapper<UserDto, User> {
}
