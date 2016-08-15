package cz.eclub.iot.model.DAO;

import cz.eclub.iot.model.classes.AbstractEntity;

import java.util.Collection;

public interface IAbstractDao<T extends AbstractEntity> {
    boolean addNew(T entity);

    Collection<T> getAll();

    boolean exists(T entity);

    T getById(int id);

    boolean update(T entity);

    boolean delete(T entity);


}
