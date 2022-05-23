package com.example.dao.entity;

import com.example.dao.DaoException;

public interface EntityDao<T> {
    T create(T t) throws DaoException;

    T findById(int id) throws DaoException;

    void update(T t) throws DaoException;

    void delete(T t) throws DaoException;
}
