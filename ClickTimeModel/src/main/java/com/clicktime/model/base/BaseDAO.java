
package com.clicktime.model.base;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface BaseDAO<E extends BaseEntity> {

    public void create(Connection conn, E e) throws SQLException;

    public E readById(Connection conn, Long id) throws SQLException;

    public List<E> readByCriteria(Connection conn, Map<String, Object> criteria, Integer offset) throws SQLException;

    public void update(Connection conn, E e) throws SQLException;

    public void delete(Connection conn, Long id) throws SQLException;

}