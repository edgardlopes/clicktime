package com.clicktime.model.base;

import com.clicktime.model.TransactionExecutor;
import java.util.List;
import java.util.Map;

public abstract class BaseService<E extends BaseEntity> {

    protected BaseDAO<E> dao;
    protected final TransactionExecutor executor = new TransactionExecutor();

    public BaseService(BaseDAO dao) {
        this.dao = dao;
    }

    public void create(E entity) throws Exception {
        executor.execute(conn -> {
            dao.create(conn, entity);
            return Void.TYPE;
        });
    }

    public E readById(Long id) throws Exception {
        return executor.execute(conn -> dao.readById(conn, id));
    }

    public List<E> readByCriteria(Map<String, Object> criteria, Integer offset) throws Exception {
        return executor.execute(conn -> dao.readByCriteria(conn, criteria, offset));
    }

    public void update(E entity) throws Exception {
        executor.execute(conn -> {
            dao.update(conn, entity);
            return Void.TYPE;
        });
    }

    public void delete(Long id) throws Exception{
        executor.execute(conn -> {
            dao.delete(conn, id);
            return Void.TYPE;
        });
                
    }

    public abstract Map<String, String> validateForCreate(Map<String, Object> fields) throws Exception;

    public abstract Map<String, String> validateForUpdate(Map<String, Object> fields) throws Exception;

  
}
