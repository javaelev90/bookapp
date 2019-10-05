package se.library.bookapp.repository;

import java.util.List;

public interface CRUDAssociationEntity<T> {

    int create(T entity, String sql);

    int delete(int id, String sql);

    int update(T entity);

    T find(int id, String sql);

    List<T> findAll(int id);



}
