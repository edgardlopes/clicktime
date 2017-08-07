package com.clicktime.model.dao;

import com.clicktime.model.base.BaseDAO;
import com.clicktime.model.criteria.DiaAtendimentoCriteria;
import com.clicktime.model.entity.DiaAtendimento;
import com.clicktime.model.entity.Profissional;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public class DiaAtendimentoDAO implements BaseDAO<DiaAtendimento> {

    

    @Override
    public void create(Connection conn, DiaAtendimento e) throws SQLException {
        String sql = "INSERT INTO dia_atendimento(profissional_fk, data_at) VALUES (?, ?) RETURNING id;";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, e.getProfissional().getId());

        Date d = new Date(e.getData().getMillis());
        ps.setDate(2, d);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            e.setId(rs.getLong("id"));
        }

        rs.close();
        ps.close();
        
    }

    @Override
    public DiaAtendimento readById(Connection conn, Long id) throws SQLException {
        String sql = "SELECT da.data_at, da.id dia_atendimento_fk, p.*, u.* from dia_atendimento da left join profissional p on da.profissional_fk=p.usuario_fk left join usuario u on p.usuario_fk=u.id where da.id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, id);

        ResultSet rs = ps.executeQuery();
        DiaAtendimento diaAtendimento = null;
        if(rs.next()){
            diaAtendimento = new DiaAtendimento();
            diaAtendimento.setData(new DateTime(rs.getDate("data_at").getTime()));
            diaAtendimento.setId(rs.getLong("dia_atendimento_fk"));
            
            Profissional profissional = new Profissional();
            profissional.setId(rs.getLong("usuario_fk"));
            profissional.setDescricao(rs.getString("descricao"));
            profissional.setHoraInicio(new DateTime(rs.getTime("hora_inicio")));
            profissional.setHoraFim(new DateTime(rs.getTime("hora_fim")));
            profissional.setUnidadeTempo(new DateTime(rs.getTime("unidade_tempo")));
            profissional.setNome(rs.getString("nome"));
            profissional.setSobrenome(rs.getString("sobrenome"));
            profissional.setNomeUsuario(rs.getString("nome_usuario"));
            profissional.setEmail(rs.getString("email"));
            profissional.setTelefone(rs.getString("telefone"));
            diaAtendimento.setProfissional(profissional);
        }
        
        return diaAtendimento;
    }

    @Override
    public List<DiaAtendimento> readByCriteria(Connection conn, Map<String, Object> criteria, Integer offset) throws SQLException {
        String sql = "  SELECT  * FROM dia_atendimento WHERE 1=1 ";

        if (criteria != null && !criteria.isEmpty()) {
            Long profissionalFkEq = (Long) criteria.get(DiaAtendimentoCriteria.PROFISSIONAL_FK_EQ);
            if (profissionalFkEq != null) {
                sql += " AND profissional_fk = " + profissionalFkEq;
            }
            
            DateTime dataAtEq = (DateTime) criteria.get(DiaAtendimentoCriteria.DIA_ATENDIMENTO_EQ);
            if (dataAtEq != null) {
                sql += " AND data_at = '" + dataAtEq.toString("dd/MM/yyyy") + "'";
            }
        }
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        List<DiaAtendimento> diaAtendimentoList = new ArrayList<>();

        while (rs.next()) {
            DiaAtendimento da = new DiaAtendimento();
            
            DateTime dt = new DateTime(rs.getDate("data_at").getTime());
            da.setData(dt);
            
            da.setId(rs.getLong("id"));
            
            Profissional p = new  Profissional();
            p.setId(rs.getLong("profissional_fk"));
            da.setProfissional(p);
                        
            diaAtendimentoList.add(da);
        }
        
        rs.close();
        ps.close();

        return diaAtendimentoList;
    }

    @Override
    public void update(Connection conn, DiaAtendimento e) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Connection conn, Long id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
