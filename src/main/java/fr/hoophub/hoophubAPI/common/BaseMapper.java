package fr.hoophub.hoophubAPI.common;

import java.util.List;

public interface BaseMapper<E, D> {
    D toDto(E e);

    List<D> toDtoList(List<E> eList);
}
