package com.sep490.bads.distributionsystem.mapper;

import com.sep490.bads.distributionsystem.dto.CategoryDto;
import com.sep490.bads.distributionsystem.entity.Category;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
@Component
public interface CategoryMapper extends EntityMapper<CategoryDto, Category> {
}
