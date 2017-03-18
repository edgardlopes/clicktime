
package com.clicktime.model.base;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface BaseDAO<E extends BaseEntity> {

    public void create(Connection conn, E e) throws Exception;

    public E readById(Connection conn, Long id) throws Exception;

    public List<E> readByCriteria(Connection conn, Map<String, Object> criteria, Integer offset) throws Exception;

    public void update(Connection conn, E e) throws Exception;

    public void delete(Connection conn, Long id) throws Exception;

}