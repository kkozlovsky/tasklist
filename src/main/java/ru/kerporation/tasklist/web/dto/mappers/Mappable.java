package ru.kerporation.tasklist.web.dto.mappers;

import java.util.List;

public interface Mappable<E, D> {

    D toDto(final E entity);

    List<D> toDto(final List<E> entity);

    E toEntity(final D dto);

    List<E> toEntity(final List<D> dtos);

}