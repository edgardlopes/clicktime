package com.clicktime.model.service;

import com.clicktime.model.ConnectionManager;
import com.clicktime.model.ErrorMessage;
import com.clicktime.model.base.service.BaseCategoriaServicoService;
import com.clicktime.model.criteria.CategoriaServicoCriteria;
import com.clicktime.model.dao.CategoriaServicoDAO;
import com.clicktime.model.entity.CategoriaServico;
import com.clicktime.model.fields.CategoriaServicoFields;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoriaServicoService implements BaseCategoriaServicoService {

    @Override
    public void create(CategoriaServico entity) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();
        try {
            new CategoriaServicoDAO().create(connection, entity);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            connection.rollback();
            throw e;
        } finally {
            connection.close();
        }
    }

    @Override
    public CategoriaServico readById(Long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CategoriaServico> readByCriteria(Map<String, Object> criteria, Integer offset) throws Exception {
        List<CategoriaServico> categorias;
        Connection connection = ConnectionManager.getInstance().getConnection();

        try {
            CategoriaServicoDAO dao = new CategoriaServicoDAO();
            categorias = dao.readByCriteria(connection, criteria, offset);
            connection.commit();
        } catch (Exception ex) {
            connection.rollback();
            ex.printStackTrace();
            throw ex;
        } finally {
            connection.close();
        }

        return categorias;
    }

    @Override
    public void update(CategoriaServico entity) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, String> validateForCreate(Map<String, Object> fields) throws Exception {
        Map<String, String> errors = new HashMap<>();
        String nome = (String) fields.get(CategoriaServicoFields.NOME);
        if (nome == null || nome.isEmpty()) {
            errors.put(CategoriaServicoFields.NOME, ErrorMessage.NOT_NULL);
        } else {
            Map<String, Object> criteria = new HashMap<>();
            criteria.put(CategoriaServicoCriteria.NOME_EQ, nome);
            if (readByCriteria(criteria, null).size() > 0) {
                errors.put(CategoriaServicoFields.NOME, ErrorMessage.NOME_CATEGORIA_UNIQUE);
            }
        }
        return errors;
    }

    @Override
    public Map<String, String> validateForUpdate(Map<String, Object> fields) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
