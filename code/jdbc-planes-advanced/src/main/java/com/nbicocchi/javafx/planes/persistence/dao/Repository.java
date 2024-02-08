package com.nbicocchi.javafx.planes.persistence.dao;

import java.util.Optional;

public interface Repository<T, ID> {
    Optional<T> findById(ID id);
    Iterable<T> findAll();
    T save(T entity);
    void delete(T entity);
    void deleteById(ID id);
    void deleteAll();
}
