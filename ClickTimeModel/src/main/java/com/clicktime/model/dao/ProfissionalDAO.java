package com.clicktime.model.dao;

import com.clicktime.model.base.BaseDAO;
import com.clicktime.model.criteria.ProfissionalCriteria;
import com.clicktime.model.entity.Profissional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public class ProfissionalDAO implements BaseDAO<Profissional> {

    @Override
    public void create(Connection conn, Profissional e) throws SQLException {
        String sql = "INSERT INTO profissional(usuario_fk, descricao, hora_inicio, hora_fim) VALUES (?, ?, ?, ?);";

        PreparedStatement ps = conn.prepareStatement(sql);
        int i = 1;
        ps.setLong(i++, e.getId());
        ps.setString(i++, e.getDescricao());

        Time t = new Time(e.getHoraInicio().getMillis());
        ps.setTime(i++, t);

        t = new Time(e.getHoraFim().getMillis());
        ps.setTime(i++, t);

        ps.execute();
        ps.close();
    }

    @Override
    public Profissional readById(Connection conn, Long id) throws SQLException {
        String sql = "SELECT * from usuario u join profissional p on u.id=p.usuario_fk where id=?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, id);

        ResultSet rs = ps.executeQuery();

        Profissional p = null;
        if (rs.next()) {
            p = new Profissional();
            p.setId(rs.getLong("id"));
            p.setNome(rs.getString("nome"));
            p.setSobrenome(rs.getString("sobrenome"));
            p.setNomeUsuario(rs.getString("nome_usuario"));
            p.setEmail(rs.getString("email"));
            p.setTelefone(rs.getString("telefone"));

            p.setDescricao(rs.getString("descricao"));
            p.setHoraInicio(new DateTime(rs.getTime("hora_inicio")));
            p.setHoraFim(new DateTime(rs.getTime("hora_fim")));
            p.setUnidadeTempo(new DateTime(rs.getTime("unidade_tempo")));
            p.setPontos(getMediaPontos(conn, id));
        }

        return p;
    }

    @Override
    public List<Profissional> readByCriteria(Connection conn, Map<String, Object> criteria, Integer offset) throws SQLException {
        String sql = "SELECT  distinct p.*, u.* from profissional p "
                + "left join usuario u on u.id=p.usuario_fk "
                + "left join execucao_servico ex on p.usuario_fk=ex.profissional_fk "
                + "left join servico s on ex.servico_fk=s.id "
                + "where 1=1";

        if (criteria != null && criteria.size() > 0) {
            Long usuarioFK = (Long) criteria.get(ProfissionalCriteria.USUARIO_FK_EQ);
            if (usuarioFK != null && usuarioFK > 0) {
                sql += " AND usuario_fk =" + usuarioFK;
            }

            Long categoriaFK = (Long) criteria.get(ProfissionalCriteria.CATEGORIA_SERVICO_ID_EQ);
            if (categoriaFK != null && categoriaFK > 0) {
                sql += " AND s.categoria_servico_fk = " + categoriaFK;
            }

            Long servicoFK = (Long) criteria.get(ProfissionalCriteria.SERVICO_ID_EQ);
            if (servicoFK != null && servicoFK > 0) {
                sql += " AND s.id = " + servicoFK;
            }

            String criterionUrlEq = (String) criteria.get(ProfissionalCriteria.NOME_USUARIO_EQ);
            if (criterionUrlEq != null && (!criterionUrlEq.isEmpty())) {
                sql += " AND nome_usuario = '" + criterionUrlEq + "'";
            }
        }
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        List<Profissional> profissionalList = new ArrayList<>();
        while (rs.next()) {
            Profissional p = new Profissional();
            //campos herdados
            p.setNome(rs.getString("nome"));
            p.setSobrenome(rs.getString("sobrenome"));
            p.setNomeUsuario(rs.getString("nome_usuario"));
            p.setEmail(rs.getString("email"));
            p.setTelefone(rs.getString("telefone"));
            p.setSenha(rs.getString("senha"));
            p.setId(rs.getLong("id"));
            //campos especializados
            p.setDescricao(rs.getString("descricao"));

            DateTime d = new DateTime(rs.getTime("hora_inicio").getTime());
            p.setHoraInicio(d);

            d = new DateTime(rs.getTime("hora_fim").getTime());
            p.setHoraFim(d);

            p.setUnidadeTempo(new DateTime(rs.getTime("unidade_tempo").getTime()));
            p.setPontos(getMediaPontos(conn, p.getId()));

            profissionalList.add(p);
        }
        rs.close();
        ps.close();

        return profissionalList;
    }

    @Override
    public void update(Connection conn, Profissional e) throws SQLException {
        String sql = "UPDATE profissional SET descricao=?, hora_inicio=?, hora_fim=? WHERE usuario_fk=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        int i = 1;
        ps.setString(i++, e.getDescricao());
        ps.setTime(i++, new Time(e.getHoraInicio().getMillis()));
        ps.setTime(i++, new Time(e.getHoraFim().getMillis()));
        ps.setLong(i++, e.getId());
        ps.execute();
        ps.close();
    }

    @Override
    public void delete(Connection conn, Long id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Float getMediaPontos(Connection conn, Long id) throws SQLException {
        String sql = "SELECT avg(pontos) pontos from solicitacao s \n"
                + "left join execucao_servico ex on ex.id=s.execucao_servico_fk\n"
                + "left join profissional p on ex.profissional_fk=p.usuario_fk\n"
                + "left join solicitacao_avaliacao sa on sa.solicitacao_fk=s.id\n"
                + "where p.usuario_fk=? and pontos is not null";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();
        Float pontos = null;
        if (rs.next()) {
            pontos = rs.getFloat("pontos");
        }
        rs.close();
        ps.close();
        return pontos;
    }
}
