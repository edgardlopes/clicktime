package com.clicktime.model.dao;

import com.clicktime.model.base.BaseDAO;
import com.clicktime.model.criteria.HorarioAtendimentoCriteria;
import com.clicktime.model.entity.DiaAtendimento;
import com.clicktime.model.entity.HorarioAtendimento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public class HorarioAtendimentoDAO implements BaseDAO<HorarioAtendimento> {

    @Override
    public void create(Connection conn, HorarioAtendimento e) throws SQLException {
        String sql = "INSERT INTO horario_atendimento(dia_atendimento_fk, hora_inicio, hora_fim, status) VALUES (?, ?, ?, ?) RETURNING id, status;";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setLong(1, e.getDiaAtendimento().getId());

        Time horaInicio = new Time(e.getHoraInicio().getMillis());
        ps.setTime(2, horaInicio);

        Time horaFim = new Time(e.getHoraFim().getMillis());
        ps.setTime(3, horaFim);

        ps.setString(4, e.getStatus());

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            e.setId(rs.getLong("id"));
            e.setStatus(rs.getString("status"));
        }
        rs.close();
        ps.close();

    }

    @Override
    public HorarioAtendimento readById(Connection conn, Long id) throws SQLException {
        String sql = "SELECT * FROM horario_atendimento where id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, id);

        ResultSet rs = ps.executeQuery();
        HorarioAtendimento horario = null;
        if (rs.next()) {
            horario = new HorarioAtendimento();
            horario.setId(rs.getLong("id"));
            horario.setStatus(rs.getString("status"));
            horario.setHoraFim(new DateTime(rs.getTime("hora_fim").getTime()));
            horario.setHoraInicio(new DateTime(rs.getTime("hora_inicio").getTime()));

            DiaAtendimento diaAtendimento = new DiaAtendimento();
            diaAtendimento.setId(rs.getLong("dia_atendimento_fk"));
            horario.setDiaAtendimento(diaAtendimento);
        }

        return horario;
    }

    @Override
    public List<HorarioAtendimento> readByCriteria(Connection conn, Map<String, Object> criteria, Integer offset) throws SQLException {
        String sql = "SELECT * from horario_atendimento ha left join solicitacao_horario_atendimento sha on sha.horario_atendimento_fk=ha.id where 1=1 ";

        if (criteria != null && criteria.size() > 0) {
            Long diaAtendimentoFkEq = (Long) criteria.get(HorarioAtendimentoCriteria.DIA_ATENDIMENTO_FK_EQ);
            if (diaAtendimentoFkEq != null) {
                sql += " AND dia_atendimento_fk = " + diaAtendimentoFkEq;
            }

            Long solicitacaoFK = (Long) criteria.get(HorarioAtendimentoCriteria.SOLICITACAO_FK_EQ);
            if (solicitacaoFK != null) {
                sql += " AND solicitacao_fk=" + solicitacaoFK;
            }
        }

        sql += " order by hora_inicio asc";

        PreparedStatement ps = conn.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        List<HorarioAtendimento> horarioAtendimentoList = new ArrayList<>();
        while (rs.next()) {
            HorarioAtendimento h = new HorarioAtendimento();
            h.setId(rs.getLong("id"));
            h.setStatus(rs.getString("status"));

            DateTime horaInicio = new DateTime(rs.getTime("hora_inicio").getTime());
            h.setHoraInicio(horaInicio);

            DateTime horaFim = new DateTime(rs.getTime("hora_fim"));
            h.setHoraFim(horaFim);

            DiaAtendimento da = new DiaAtendimento();
            da.setId(rs.getLong("dia_atendimento_fk"));
            h.setDiaAtendimento(da);

            horarioAtendimentoList.add(h);
        }
        rs.close();
        ps.close();

        return horarioAtendimentoList;
    }

    @Override
    public void update(Connection conn, HorarioAtendimento e) throws SQLException {
        String sql = "UPDATE horario_atendimento SET status=? WHERE id=?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, e.getStatus());
        ps.setLong(2, e.getId());

        ps.execute();
        ps.close();

    }

    @Override
    public void delete(Connection conn, Long id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<HorarioAtendimento> readByCriteriaCliente(Connection conn, Map<String, Object> criteria, Integer offset) throws SQLException {
        String sql = "SELECT * from horario_atendimento ha where 1=1 ";

        if (criteria != null && criteria.size() > 0) {
            Long diaAtendimentoFkEq = (Long) criteria.get(HorarioAtendimentoCriteria.DIA_ATENDIMENTO_FK_EQ);
            if (diaAtendimentoFkEq != null) {
                sql += " AND dia_atendimento_fk = " + diaAtendimentoFkEq;
            }
        }

        sql += " order by hora_inicio asc";

        PreparedStatement ps = conn.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        List<HorarioAtendimento> horarioAtendimentoList = new ArrayList<>();
        while (rs.next()) {
            HorarioAtendimento h = new HorarioAtendimento();
            h.setId(rs.getLong("id"));
            h.setStatus(rs.getString("status"));

            DateTime horaInicio = new DateTime(rs.getTime("hora_inicio").getTime());
            h.setHoraInicio(horaInicio);

            DateTime horaFim = new DateTime(rs.getTime("hora_fim"));
            h.setHoraFim(horaFim);

            DiaAtendimento da = new DiaAtendimento();
            da.setId(rs.getLong("dia_atendimento_fk"));
            h.setDiaAtendimento(da);

            horarioAtendimentoList.add(h);
        }
        rs.close();
        ps.close();

        return horarioAtendimentoList;
    }
}
