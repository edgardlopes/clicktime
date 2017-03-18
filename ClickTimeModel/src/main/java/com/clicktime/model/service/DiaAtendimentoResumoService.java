
package com.clicktime.model.service;

import com.clicktime.model.ConnectionManager;
import com.clicktime.model.dao.DiaAtendimentoResumoDAO;
import com.clicktime.model.entity.DiaAtendimento;
import com.clicktime.model.entity.DiaAtendimentoResumo;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class DiaAtendimentoResumoService {
    public List<DiaAtendimentoResumo> reportFromMonth(DiaAtendimento day) throws Exception{
        Connection connection = ConnectionManager.getInstance().getConnection();
        
        List<DiaAtendimentoResumo> resumoList = new ArrayList<DiaAtendimentoResumo>();
        try {
            DiaAtendimentoResumoDAO dao = new DiaAtendimentoResumoDAO();
            resumoList = dao.readByReportOfMonth(connection, day);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        }finally{
            
            connection.close();
        }
        return resumoList;
    }
}
