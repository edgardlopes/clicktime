
package com.clicktime.model.dao;

import com.clicktime.model.base.BaseDAO;
import com.clicktime.model.criteria.ServicoCriteria;
import com.clicktime.model.entity.Servico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServicoDAO implements BaseDAO<Servico>{
    
    @Override
    public void create(Connection conn, Servico e) throws SQLException {
        String sql = "INSERT INTO servico(categoria_servico_fk, nome) VALUES (?, ?) returning id";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, e.getCategoria().getId());
        ps.setString(2, e.getNome());
        
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            e.setId(rs.getLong("id"));
        }
        rs.close();
        ps.close();
    }

    @Override
    public Servico readById(Connection conn, Long id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Servico> readByCriteria(Connection conn, Map<String, Object> criteria, Integer offset) throws SQLException {
        String sql = "SELECT s.* FROM categoria_servico AS cs, servico AS s WHERE cs.id = s.categoria_servico_fk AND 1=1";
        
        String criterionCategFkEq = (String) criteria.get(ServicoCriteria.CATEGORIA_SERVICO_FK_EQ);
        if(criterionCategFkEq != null && (!criterionCategFkEq.isEmpty())){
            sql += " AND cs.id = " + criterionCategFkEq;
        }
        
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        
        List<Servico> servicos = new ArrayList<>();
        while(rs.next()){
            Servico servico = new Servico();
            servico.setNome(rs.getString("nome"));
            servico.setId(rs.getLong("id"));
            
            servicos.add(servico);
        }
        
        rs.close();
        ps.close();
        
        return servicos;
        
    }

    @Override
    public void update(Connection conn, Servico e) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Connection conn, Long id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
