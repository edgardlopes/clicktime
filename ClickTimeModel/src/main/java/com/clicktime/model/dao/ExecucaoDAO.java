package com.clicktime.model.dao;

import com.clicktime.model.base.BaseDAO;
import com.clicktime.model.criteria.ExecucaoCriteria;
import com.clicktime.model.entity.CategoriaServico;
import com.clicktime.model.entity.Execucao;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.entity.Servico;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public class ExecucaoDAO implements BaseDAO<Execucao> {

    @Override
    public void create(Connection conn, Execucao e) throws Exception {
        String sql = "INSERT INTO execucao_servico(profissional_fk, servico_fk, descricao, duracao, valor, ativo) VALUES (?, ?, ?, ?, ?, ?) RETURNING id;";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, e.getProfissional().getId());
        ps.setLong(2, e.getServico().getId());
        ps.setString(3, e.getDescricao());

        Time t = new Time(e.getDuracao().getMillis());
        ps.setTime(4, t);
        ps.setFloat(5, e.getValor());
        ps.setBoolean(6, true);
        
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            e.setId(rs.getLong("id"));
        }

        rs.close();
        ps.close();
    }

    @Override
    public Execucao readById(Connection conn, Long id) throws Exception {
        String sql = "SELECT ex.*, s.nome as servico_nome, s.id,  cs.id categoria_fk, cs.nome categoria_nome from execucao_servico as ex  "
                + "left JOIN servico as s on ex.servico_fk=s.id "
                + "left join categoria_servico cs on cs.id=s.categoria_servico_fk "
                + " where ativo=true and ex.id=?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, id);

        ResultSet rs = ps.executeQuery();

        Execucao execucao = null;
        if (rs.next()) {
            execucao = new Execucao();
            execucao.setId(rs.getLong("id"));
            execucao.setValor(rs.getFloat("valor"));
            execucao.setDescricao(rs.getString("descricao"));
            execucao.setDuracao(new DateTime(rs.getTime("duracao").getTime()));

            Servico servico = new Servico();
            servico.setId(rs.getLong("servico_fk"));
            servico.setNome(rs.getString("servico_nome"));
            execucao.setServico(servico);
            
            CategoriaServico categoria = new CategoriaServico();
            categoria.setId(rs.getLong("categoria_fk"));
            categoria.setNome(rs.getString("categoria_nome"));
            servico.setCategoria(categoria);
            
            Profissional profissional = new Profissional();
            profissional.setId(rs.getLong("profissional_fk"));
            execucao.setProfissional(profissional);
        }

        return execucao;
    }

    @Override
    public List<Execucao> readByCriteria(Connection conn, Map<String, Object> criteria, Integer offset) throws Exception {
        String sql = "SELECT ex.*, s.nome as nome_servico,  p.descricao as profissional_descricao, p.hora_inicio, p.hora_fim, u.nome as usuario_nome, sobrenome, nome_usuario, email, telefone FROM execucao_servico as ex "
                + "join profissional as p on ex.profissional_fk=p.usuario_fk "
                + "LEFT JOIN servico as s ON s.id = ex.servico_fk "
                + "left join usuario as u on p.usuario_fk=u.id where 1=1 and ativo=true ";

        Long criterionProfissionalFk = (Long) criteria.get(ExecucaoCriteria.PROFISSIONAL_FK_EQ);
        if (criterionProfissionalFk != null) {
            sql += "AND ex.profissional_fk =" + criterionProfissionalFk;
        }

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Execucao> execucaoList = new ArrayList<Execucao>();

        Profissional p = null;
        while (rs.next()) {
            Execucao e = new Execucao();
            e.setId(rs.getLong("id"));

            Servico s = new Servico();
            s.setId(rs.getLong("servico_fk"));
            s.setNome(rs.getString("nome_servico"));
            e.setServico(s);

            e.setDescricao(rs.getString("descricao"));

            Time t = rs.getTime("duracao");
            DateTime d = new DateTime(t.getTime());
            e.setDuracao(d);

            e.setValor(rs.getFloat("valor"));

            if (p == null) {
                p = new Profissional();
                p.setNome(rs.getString("usuario_nome"));
                p.setSobrenome(rs.getString("sobrenome"));
                p.setNomeUsuario(rs.getString("nome_usuario"));
                p.setDescricao(rs.getString("profissional_descricao"));
                p.setTelefone(rs.getString("telefone"));
                p.setEmail(rs.getString("email"));
                p.setId(rs.getLong("profissional_fk"));

                Date horaInicio = rs.getDate("hora_inicio");
                p.setHoraInicio(new DateTime(horaInicio.getTime()));

                Date horaFim = rs.getDate("hora_fim");
                p.setHoraFim(new DateTime(horaFim.getTime()));
            }
            e.setProfissional(p);

            execucaoList.add(e);
        }

        rs.close();
        ps.close();

        return execucaoList;
    }

    @Override
    public void update(Connection conn, Execucao e) throws Exception {
        String sql = "UPDATE execucao_servico  SET descricao=?, duracao=?, valor=? WHERE id=?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, e.getDescricao());
        ps.setTime(2, new Time(e.getDuracao().getMillis()));
        ps.setFloat(3, e.getValor());
        ps.setLong(4, e.getId());
        
        ps.execute();
        ps.close();
    }

    @Override
    public void delete(Connection conn, Long id) throws Exception {
        //verificar
        String sql = "UPDATE execucao_servico SET ativo=? where id=?";
        
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setBoolean(1, false);
        ps.setLong(2, id);
        ps.execute();

        ps.close();
    }

    public void updateUnidadeTempo(DateTime duracao, Long profissionalFK, Connection conn) throws Exception {
        String sql = "UPDATE profissional SET unidade_tempo=? where usuario_fk=?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setTime(1, new Time(duracao.getMillis()));
        ps.setLong(2, profissionalFK);

        ps.execute();
        ps.close();
    }
}
