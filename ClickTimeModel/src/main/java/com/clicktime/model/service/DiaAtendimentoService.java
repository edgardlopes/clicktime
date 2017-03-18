package com.clicktime.model.service;

import com.clicktime.model.ConnectionManager;
import com.clicktime.model.base.service.BaseDiaAtendimentoService;
import com.clicktime.model.criteria.DiaAtendimentoCriteria;
import com.clicktime.model.dao.DiaAtendimentoDAO;
import com.clicktime.model.entity.DiaAtendimento;
import com.clicktime.model.entity.Profissional;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public class DiaAtendimentoService implements BaseDiaAtendimentoService {

    @Override
    public void create(DiaAtendimento entity) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();
        DiaAtendimentoDAO dao = new DiaAtendimentoDAO();

        try {
            dao.create(connection, entity);
            connection.commit();
        } catch (Exception ex) {
            connection.rollback();
            ex.printStackTrace();
            throw ex;
        } finally {
            connection.close();
        }
    }

    @Override
    public DiaAtendimento readById(Long id) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();
        
        DiaAtendimento diaAtendimento = null;
        try {
            diaAtendimento = new DiaAtendimentoDAO().readById(connection, id);
        } catch (Exception e) {
            connection.rollback();
            throw e;
        }finally{
            connection.close();
        }
        
        return diaAtendimento;
    }

    @Override
    public List<DiaAtendimento> readByCriteria(Map<String, Object> criteria, Integer offset) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();
        DiaAtendimentoDAO dao = new DiaAtendimentoDAO();

        List<DiaAtendimento> list = new ArrayList<DiaAtendimento>();
        try {
            list = dao.readByCriteria(connection, criteria, offset);
            connection.commit();
        } catch (Exception exception) {
            connection.rollback();
            exception.printStackTrace();
            throw exception;
        } finally {
            connection.close();
        }
        return list;
    }

    @Override
    public void update(DiaAtendimento entity) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void readOrCreate(DiaAtendimento da) throws Exception {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put(DiaAtendimentoCriteria.PROFISSIONAL_FK_EQ, da.getProfissional().getId());
        criteria.put(DiaAtendimentoCriteria.DIA_ATENDIMENTO_EQ, da.getData());

        List<DiaAtendimento> daList = readByCriteria(criteria, null);
        if (daList.isEmpty()) {
            create(da);
        } else {
            da.setId(daList.get(0).getId());
        }
    }

    @Override
    public DiaAtendimento readDiaAtendimentoFromDate(DateTime date, Profissional profissional) throws Exception {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put(DiaAtendimentoCriteria.PROFISSIONAL_FK_EQ, profissional.getId());
        criteria.put(DiaAtendimentoCriteria.DIA_ATENDIMENTO_EQ, date);

        List<DiaAtendimento> daList = readByCriteria(criteria, null);
        DiaAtendimento dia = null;
        if (!daList.isEmpty() && daList.size() == 1) {
            dia = daList.get(0);
        } 
        
        return dia;
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
