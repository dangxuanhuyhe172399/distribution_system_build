package com.sep490.bads.distributionsystem.mapper;

import java.util.List;

/**
 * @author SyPT - IIST
 * created on 9/28/2020
 */

public interface EntityMapper<D, E> {

    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntity(List<D> dtoList);

    List<D> toDto(List<E> entityList);
}
