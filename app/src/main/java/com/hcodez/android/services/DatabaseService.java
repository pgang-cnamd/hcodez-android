package com.hcodez.android.services;

import androidx.lifecycle.LiveData;

/**
 * Interface that describes how a service should behave
 * @param <T> the entity that this service handles
 */
public interface DatabaseService<T> {

    /**
     * Add a new entity to the database
     * @param entity the entity
     * @return live data object that will showcase the entity
     */
    LiveData<T> addNew(final T entity);

    /**
     * Add a new entity to the database(synchronously)
     * @param entity the entity
     * @return the entity after it received the id
     */
    T addNewSync(final T entity);

    /**
     * Delete an entity
     * @param entity the entity
     */
    LiveData<Boolean> delete(final T entity);

    /**
     * Delete an entity(synchronously)
     * @param entity the entity
     */
    Boolean deleteSync(final T entity);
}
