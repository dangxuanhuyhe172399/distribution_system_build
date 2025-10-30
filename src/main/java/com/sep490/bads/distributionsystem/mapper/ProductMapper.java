package com.sep490.bads.distributionsystem.mapper;

import com.sep490.bads.distributionsystem.dto.ProductDto;
import com.sep490.bads.distributionsystem.entity.Product;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ProductMapper extends EntityMapper<ProductDto, Product> {
}
