package com.clicktime.model.service;

import com.clicktime.model.ConnectionManager;
import com.clicktime.model.base.service.BaseServicoService;
import com.clicktime.model.dao.ServicoDAO;
import com.clicktime.model.entity.Servico;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class ServicoService implements BaseServicoService {

    @Override
    public void create(Servico entity) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();
        ServicoDAO dao = new ServicoDAO();
        try {
            dao.create(connection, entity);
            connection.commit();
        } catch (Exception ex) {
            connection.rollback();
            ex.printStackTrace();
        } finally {
            connection.close();
        }
    }

    @Override
    public Servico readById(Long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Servico> readByCriteria(Map<String, Object> criteria, Integer offset) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();
        ServicoDAO dao = new ServicoDAO();
        List<Servico> servicos = null;
        try {
            servicos = dao.readByCriteria(connection, criteria, offset);
            connection.commit();
        } catch (Exception ex) {
            connection.rollback();
            ex.printStackTrace();
        } finally {
            connection.close();
        }

        return servicos;
    }

    @Override
    public void update(Servico entity) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, String> validateForCreate(Map<String, Object> fields) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, String> validateForUpdate(Map<String, Object> fields) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
