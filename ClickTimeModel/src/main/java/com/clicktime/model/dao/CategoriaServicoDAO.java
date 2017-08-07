package com.clicktime.model.dao;

import com.clicktime.model.base.BaseDAO;
import com.clicktime.model.criteria.CategoriaServicoCriteria;
import com.clicktime.model.entity.CategoriaServico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoriaServicoDAO implements BaseDAO<CategoriaServico> {

    @Override
    public void create(Connection conn, CategoriaServico e) throws SQLException {
        String sql  = "INSERT INTO categoria_servico(nome) VALUES (?) returning id";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, e.getNome());
        
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            e.setId(rs.getLong("id"));
        }
        rs.close();
        ps.close();
    }

    @Override
    public CategoriaServico readById(Connection conn, Long id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CategoriaServico> readByCriteria(Connection conn, Map<String, Object> criteria, Integer offset) throws SQLException {
        List<CategoriaServico> categorias = new ArrayList<>();
        String sql = "SELECT * from categoria_servico WHERE 1=1";

        if (criteria != null && criteria.size() > 0) {
            String nome = (String) criteria.get(CategoriaServicoCriteria.NOME_EQ);
            if (nome != null && !nome.isEmpty()) {
                sql += " and nome ilike '" + nome + "'";
            }
        }

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            CategoriaServico categoria = new CategoriaServico();
            categoria.setId(rs.getLong("id"));
            categoria.setNome(rs.getString("nome"));
            categorias.add(categoria);
        }
        rs.close();
        ps.close();

        return categorias;
    }

    @Override
    public void update(Connection conn, CategoriaServico e) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Connection conn, Long id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
