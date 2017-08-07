package com.clicktime.model.dao;

import com.clicktime.model.base.BaseDAO;
import com.clicktime.model.criteria.SolicitacaoCriteria;
import com.clicktime.model.entity.DiaAtendimento;
import com.clicktime.model.entity.Execucao;
import com.clicktime.model.entity.HorarioAtendimento;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.entity.Servico;
import com.clicktime.model.entity.Solicitacao;
import com.clicktime.model.entity.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public class SolicitacaoDAO implements BaseDAO<Solicitacao> {

    public static final Integer LIMIT = 10;

    @Override
    public void create(Connection conn, Solicitacao e) throws SQLException {
        String sql = "INSERT INTO solicitacao(usuario_fk, execucao_servico_fk, status) VALUES (?, ?, ?) RETURNING id;";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, e.getUsuario().getId());
        ps.setLong(2, e.getExecucao().getId());
        ps.setString(3, e.getStatus());

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            e.setId(rs.getLong("id"));
        }

        rs.close();
        ps.close();

        updateHorarioAtendimentoList(conn, e);
    }

    @Override
    public Solicitacao readById(Connection conn, Long id) throws SQLException {
        String sql = "SELECT s.id solicitacao_fk, s.descricao solicitacao_descricao , s.status solicitacao_status, "
                + "     es.id execucao_fk, es.servico_fk servico_fk, es.descricao execucao_descricao, es.duracao execucao_duracao, es.valor execucao_valor, ser.nome servico_nome, "
                + "     up.nome profissional_nome, up.sobrenome profissional_sobrenome, up.nome_usuario profissional_nome_usuario ,up.email profissional_email, up.telefone profissional_telefone, "
                + "     p.usuario_fk profissional_usuario_fk, p.descricao profissional_descricao, p.hora_inicio profissional_hora_inicio, p.hora_fim profissional_hora_fim, p.unidade_tempo profissional_unidade_tempo, "
                + "     s.usuario_fk cliente_fk, u.nome cliente_nome, u.email cliente_email, u.telefone cliente_telefone,  u.sobrenome cliente_sobrenome, u.nome_usuario cliente_nome_usuario, "
                + "     min(ha.hora_inicio) hora_inicio, max(ha.hora_fim) hora_fim,  "
                + "     da.data_at, da.id dia_atendimento_fk "
                + "from solicitacao s  "
                + "left join solicitacao_horario_atendimento sh on sh.solicitacao_fk=s.id "
                + "left join horario_atendimento ha on sh.horario_atendimento_fk=ha.id "
                + "left join usuario u on s.usuario_fk=u.id "
                + "left join execucao_servico es on s.execucao_servico_fk=es.id "
                + "left join profissional p on es.profissional_fk=p.usuario_fk "
                + "left join usuario up on p.usuario_fk=up.id "
                + "left join servico ser on ser.id=es.servico_fk "
                + "left join dia_atendimento da on da.id=ha.dia_atendimento_fk "
                + "where s.id=? "
                + "group by s.id, u.id, es.id, p.usuario_fk, up.id, ser.id, da.id ";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, id);

        ResultSet rs = ps.executeQuery();

        Solicitacao solicitacao = null;
        if (rs.next()) {
            solicitacao = new Solicitacao();
            solicitacao.setStatus(rs.getString("solicitacao_status"));
            solicitacao.setId(rs.getLong("solicitacao_fk"));
            solicitacao.setDescricao(rs.getString("solicitacao_descricao"));
            //usuario
            Usuario usuario = new Usuario();
            usuario.setId(rs.getLong("cliente_fk"));
            usuario.setNome(rs.getString("cliente_nome"));
            usuario.setSobrenome(rs.getString("cliente_sobrenome"));
            usuario.setNomeUsuario(rs.getString("cliente_nome_usuario"));
            usuario.setEmail(rs.getString("cliente_email"));
            usuario.setTelefone(rs.getString("cliente_telefone"));
            solicitacao.setUsuario(usuario);

            //execucao
            Execucao execucao = new Execucao();
            execucao.setId(rs.getLong("execucao_fk"));
            execucao.setDescricao(rs.getString("execucao_descricao"));
            execucao.setDuracao(new DateTime(rs.getTime("execucao_duracao").getTime()));
            execucao.setValor(rs.getFloat("execucao_valor"));
            //servico
            Servico servico = new Servico();
            servico.setId(rs.getLong("servico_fk"));
            servico.setNome(rs.getString("servico_nome"));
            execucao.setServico(servico);
            //profissional
            Profissional profissional = new Profissional();
            profissional.setId(rs.getLong("profissional_usuario_fk"));
            profissional.setDescricao(rs.getString("profissional_descricao"));
            profissional.setEmail(rs.getString("profissional_email"));
            profissional.setHoraFim(new DateTime(rs.getTime("profissional_hora_fim").getTime()));
            profissional.setHoraInicio(new DateTime(rs.getTime("profissional_hora_inicio").getTime()));
            profissional.setUnidadeTempo(new DateTime(rs.getTime("profissional_unidade_tempo").getTime()));
            profissional.setNome(rs.getString("profissional_nome"));
            profissional.setSobrenome(rs.getString("profissional_sobrenome"));
            profissional.setNomeUsuario(rs.getString("profissional_nome_usuario"));
            profissional.setTelefone(rs.getString("profissional_telefone"));

            execucao.setProfissional(profissional);
            solicitacao.setExecucao(execucao);

            //horario_atendimento
            HorarioAtendimento horario = new HorarioAtendimento();
            horario.setHoraInicio(new DateTime(rs.getTime("hora_inicio").getTime()));
            horario.setHoraFim(new DateTime(rs.getTime("hora_fim").getTime()));
            //dia atendimento
            DiaAtendimento diaAtendimento = new DiaAtendimento();
            diaAtendimento.setData(new DateTime(rs.getDate("data_at").getTime()));
            diaAtendimento.setId(rs.getLong("dia_atendimento_fk"));
            horario.setDiaAtendimento(diaAtendimento);

            solicitacao.getHorarioAtendimentoList().add(horario);
        }

        return solicitacao;
    }

    @Override
    public List<Solicitacao> readByCriteria(Connection conn, Map<String, Object> criteria, Integer offset) throws SQLException {
        String sql = "SELECT s.id solicitacao_fk, pontos, s.descricao solicitacao_descricao ,s.status solicitacao_status, "
                + "     es.id execucao_fk, es.servico_fk servico_fk, es.descricao execucao_descricao, es.duracao execucao_duracao, es.valor execucao_valor, ser.nome servico_nome, "
                + "     up.nome profissional_nome, up.sobrenome profissional_sobrenome, up.nome_usuario profissional_nome_usuario ,up.email profissional_email, up.telefone profissional_telefone, "
                + "     p.usuario_fk profissional_usuario_fk, p.descricao profissional_descricao, p.hora_inicio profissional_hora_inicio, p.hora_fim profissional_hora_fim, p.unidade_tempo profissional_unidade_tempo, "
                + "     s.usuario_fk cliente_fk, u.nome cliente_nome, u.email cliente_email, u.telefone cliente_telefone,  u.sobrenome cliente_sobrenome, u.nome_usuario cliente_nome_usuario, "
                + "     min(ha.hora_inicio) hora_inicio, max(ha.hora_fim) hora_fim,  "
                + "     da.data_at, da.id dia_atendimento_fk "
                + "from solicitacao s  "
                + "left join solicitacao_horario_atendimento sh on sh.solicitacao_fk=s.id "
                + "left join horario_atendimento ha on sh.horario_atendimento_fk=ha.id "
                + "left join usuario u on s.usuario_fk=u.id "
                + "left join execucao_servico es on s.execucao_servico_fk=es.id "
                + "left join profissional p on es.profissional_fk=p.usuario_fk "
                + "left join usuario up on p.usuario_fk=up.id "
                + "left join servico ser on ser.id=es.servico_fk "
                + "left join dia_atendimento da on da.id=ha.dia_atendimento_fk "
                + "left join solicitacao_avaliacao on s.id=solicitacao_avaliacao.solicitacao_fk "
                + "where 1=1 ";

        if (criteria != null && criteria.size() > 0) {
            Long profissionalFK = (Long) criteria.get(SolicitacaoCriteria.PROFISSIONAL_FK_EQ);
            if (profissionalFK != null) {
                sql += " and p.usuario_fk=" + profissionalFK;
            }

            Long clienteFK = (Long) criteria.get(SolicitacaoCriteria.CLIENTE_FK_EQ);
            if (clienteFK != null) {
                sql += " and s.usuario_fk=" + clienteFK;
            }

            DateTime dataInicio = (DateTime) criteria.get(SolicitacaoCriteria.DIA_ATENDIMENTO_ME);
            if (dataInicio != null) {
                sql += " and da.data_at >='" + dataInicio.toString("dd/MM/yyyy") + "'";
            }
            DateTime dataFim = (DateTime) criteria.get(SolicitacaoCriteria.DIA_ATENDIMENTO_LE);
            if (dataFim != null) {
                sql += " and da.data_at <='" + dataFim.toString("dd/MM/yyyy") + "'";
            }

            String status = (String) criteria.get(SolicitacaoCriteria.STATUS_EQ);
            if (status != null && !status.isEmpty()) {
                sql += " and s.status ilike '" + status + "'";
            }
        }

        sql += " group by s.id, u.id, es.id, p.usuario_fk, up.id, ser.id, da.id, solicitacao_avaliacao.solicitacao_fk";
        sql += " order by solicitacao_fk desc";
        sql += " limit " + LIMIT;
        if (offset != null) {
            sql += " offset " + offset;
        }

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        List<Solicitacao> solicitacaoList = new ArrayList<Solicitacao>();
        while (rs.next()) {

            Solicitacao solicitacao = new Solicitacao();
            solicitacao.setStatus(rs.getString("solicitacao_status"));
            solicitacao.setId(rs.getLong("solicitacao_fk"));
            solicitacao.setPontos(rs.getFloat("pontos"));
            solicitacao.setDescricao(rs.getString("solicitacao_descricao"));
            //usuario
            Usuario usuario = new Usuario();
            usuario.setId(rs.getLong("cliente_fk"));
            usuario.setNome(rs.getString("cliente_nome"));
            usuario.setSobrenome(rs.getString("cliente_sobrenome"));
            usuario.setNomeUsuario(rs.getString("cliente_nome_usuario"));
            usuario.setEmail(rs.getString("cliente_email"));
            usuario.setTelefone(rs.getString("cliente_telefone"));
            solicitacao.setUsuario(usuario);

            //execucao
            Execucao execucao = new Execucao();
            execucao.setId(rs.getLong("execucao_fk"));
            execucao.setDescricao(rs.getString("execucao_descricao"));
            execucao.setDuracao(new DateTime(rs.getTime("execucao_duracao").getTime()));
            execucao.setValor(rs.getFloat("execucao_valor"));
            //servico
            Servico servico = new Servico();
            servico.setId(rs.getLong("servico_fk"));
            servico.setNome(rs.getString("servico_nome"));
            execucao.setServico(servico);
            //profissional
            Profissional profissional = new Profissional();
            profissional.setId(rs.getLong("profissional_usuario_fk"));
            profissional.setDescricao(rs.getString("profissional_descricao"));
            profissional.setEmail(rs.getString("profissional_email"));
            profissional.setHoraFim(new DateTime(rs.getTime("profissional_hora_fim").getTime()));
            profissional.setHoraInicio(new DateTime(rs.getTime("profissional_hora_inicio").getTime()));
            profissional.setUnidadeTempo(new DateTime(rs.getTime("profissional_unidade_tempo").getTime()));
            profissional.setNome(rs.getString("profissional_nome"));
            profissional.setSobrenome(rs.getString("profissional_sobrenome"));
            profissional.setNomeUsuario(rs.getString("profissional_nome_usuario"));
            profissional.setTelefone(rs.getString("profissional_telefone"));

            execucao.setProfissional(profissional);
            solicitacao.setExecucao(execucao);

            //horario_atendimento
            HorarioAtendimento horario = new HorarioAtendimento();
            horario.setHoraInicio(new DateTime(rs.getTime("hora_inicio").getTime()));
            horario.setHoraFim(new DateTime(rs.getTime("hora_fim").getTime()));
            //dia atendimento
            DiaAtendimento diaAtendimento = new DiaAtendimento();
            diaAtendimento.setData(new DateTime(rs.getDate("data_at").getTime()));
            diaAtendimento.setId(rs.getLong("dia_atendimento_fk"));
            horario.setDiaAtendimento(diaAtendimento);

            solicitacao.getHorarioAtendimentoList().add(horario);
            solicitacaoList.add(solicitacao);
        }

        return solicitacaoList;
    }

    public List<Solicitacao> readByCriteriaSemPaginacao(Connection conn, Map<String, Object> criteria) throws SQLException {
        String sql = "SELECT s.id solicitacao_fk, pontos, s.descricao solicitacao_descricao ,s.status solicitacao_status, "
                + "     es.id execucao_fk, es.servico_fk servico_fk, es.descricao execucao_descricao, es.duracao execucao_duracao, es.valor execucao_valor, ser.nome servico_nome, "
                + "     up.nome profissional_nome, up.sobrenome profissional_sobrenome, up.nome_usuario profissional_nome_usuario ,up.email profissional_email, up.telefone profissional_telefone, "
                + "     p.usuario_fk profissional_usuario_fk, p.descricao profissional_descricao, p.hora_inicio profissional_hora_inicio, p.hora_fim profissional_hora_fim, p.unidade_tempo profissional_unidade_tempo, "
                + "     s.usuario_fk cliente_fk, u.nome cliente_nome, u.email cliente_email, u.telefone cliente_telefone,  u.sobrenome cliente_sobrenome, u.nome_usuario cliente_nome_usuario, "
                + "     min(ha.hora_inicio) hora_inicio, max(ha.hora_fim) hora_fim,  "
                + "     da.data_at, da.id dia_atendimento_fk "
                + "from solicitacao s  "
                + "left join solicitacao_horario_atendimento sh on sh.solicitacao_fk=s.id "
                + "left join horario_atendimento ha on sh.horario_atendimento_fk=ha.id "
                + "left join usuario u on s.usuario_fk=u.id "
                + "left join execucao_servico es on s.execucao_servico_fk=es.id "
                + "left join profissional p on es.profissional_fk=p.usuario_fk "
                + "left join usuario up on p.usuario_fk=up.id "
                + "left join servico ser on ser.id=es.servico_fk "
                + "left join dia_atendimento da on da.id=ha.dia_atendimento_fk "
                + " left join solicitacao_avaliacao on s.id = solicitacao_avaliacao.solicitacao_fk"
                + " where 1=1 ";

        if (criteria != null && criteria.size() > 0) {
            Long profissionalFK = (Long) criteria.get(SolicitacaoCriteria.PROFISSIONAL_FK_EQ);
            if (profissionalFK != null) {
                sql += " and p.usuario_fk=" + profissionalFK;
            }

            Long clienteFK = (Long) criteria.get(SolicitacaoCriteria.CLIENTE_FK_EQ);
            if (clienteFK != null) {
                sql += " and s.usuario_fk=" + clienteFK;
            }

            DateTime dataInicio = (DateTime) criteria.get(SolicitacaoCriteria.DIA_ATENDIMENTO_ME);
            if (dataInicio != null) {
                sql += " and da.data_at >='" + dataInicio.toString("dd/MM/yyyy") + "'";
            }
            DateTime dataFim = (DateTime) criteria.get(SolicitacaoCriteria.DIA_ATENDIMENTO_LE);
            if (dataFim != null) {
                sql += " and da.data_at <='" + dataFim.toString("dd/MM/yyyy") + "'";
            }

            String status = (String) criteria.get(SolicitacaoCriteria.STATUS_EQ);
            if (status != null && !status.isEmpty()) {
                sql += " and s.status ilike '" + status + "'";
            }
            
            
            Boolean isAvaliacao = (Boolean) criteria.get(SolicitacaoCriteria.IS_AVALIACAO);
            if(isAvaliacao != null && isAvaliacao){
                sql += " and solicitacao_avaliacao.solicitacao_fk is null";
            }
        }

        sql += " group by s.id, u.id, es.id, p.usuario_fk, up.id, ser.id, da.id, solicitacao_avaliacao.solicitacao_fk ";
        sql += " order by solicitacao_fk desc";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        List<Solicitacao> solicitacaoList = new ArrayList<Solicitacao>();
        while (rs.next()) {

            Solicitacao solicitacao = new Solicitacao();
            solicitacao.setStatus(rs.getString("solicitacao_status"));
            solicitacao.setId(rs.getLong("solicitacao_fk"));
            solicitacao.setDescricao(rs.getString("solicitacao_descricao"));
            solicitacao.setPontos(rs.getFloat("pontos"));
            //usuario
            Usuario usuario = new Usuario();
            usuario.setId(rs.getLong("cliente_fk"));
            usuario.setNome(rs.getString("cliente_nome"));
            usuario.setSobrenome(rs.getString("cliente_sobrenome"));
            usuario.setNomeUsuario(rs.getString("cliente_nome_usuario"));
            usuario.setEmail(rs.getString("cliente_email"));
            usuario.setTelefone(rs.getString("cliente_telefone"));
            solicitacao.setUsuario(usuario);

            //execucao
            Execucao execucao = new Execucao();
            execucao.setId(rs.getLong("execucao_fk"));
            execucao.setDescricao(rs.getString("execucao_descricao"));
            execucao.setDuracao(new DateTime(rs.getTime("execucao_duracao").getTime()));
            execucao.setValor(rs.getFloat("execucao_valor"));
            //servico
            Servico servico = new Servico();
            servico.setId(rs.getLong("servico_fk"));
            servico.setNome(rs.getString("servico_nome"));
            execucao.setServico(servico);
            //profissional
            Profissional profissional = new Profissional();
            profissional.setId(rs.getLong("profissional_usuario_fk"));
            profissional.setDescricao(rs.getString("profissional_descricao"));
            profissional.setEmail(rs.getString("profissional_email"));
            profissional.setHoraFim(new DateTime(rs.getTime("profissional_hora_fim").getTime()));
            profissional.setHoraInicio(new DateTime(rs.getTime("profissional_hora_inicio").getTime()));
            profissional.setUnidadeTempo(new DateTime(rs.getTime("profissional_unidade_tempo").getTime()));
            profissional.setNome(rs.getString("profissional_nome"));
            profissional.setSobrenome(rs.getString("profissional_sobrenome"));
            profissional.setNomeUsuario(rs.getString("profissional_nome_usuario"));
            profissional.setTelefone(rs.getString("profissional_telefone"));

            execucao.setProfissional(profissional);
            solicitacao.setExecucao(execucao);

            //horario_atendimento
            HorarioAtendimento horario = new HorarioAtendimento();
            horario.setHoraInicio(new DateTime(rs.getTime("hora_inicio").getTime()));
            horario.setHoraFim(new DateTime(rs.getTime("hora_fim").getTime()));
            //dia atendimento
            DiaAtendimento diaAtendimento = new DiaAtendimento();
            diaAtendimento.setData(new DateTime(rs.getDate("data_at").getTime()));
            diaAtendimento.setId(rs.getLong("dia_atendimento_fk"));
            horario.setDiaAtendimento(diaAtendimento);

            solicitacao.getHorarioAtendimentoList().add(horario);
            solicitacaoList.add(solicitacao);
        }

        return solicitacaoList;
    }

    @Override
    public void update(Connection conn, Solicitacao e) throws SQLException {
        String sql = "UPDATE solicitacao SET status=?, descricao=? WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, e.getStatus());
        ps.setString(2, e.getDescricao());
        ps.setLong(3, e.getId());

        ps.execute();
        ps.close();
    }

    @Override
    public void delete(Connection conn, Long id) throws SQLException {
        String sql = "DELETE FROM solicitacao WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, id);
        ps.execute();
        ps.close();
    }

    private void updateHorarioAtendimentoList(Connection conn, Solicitacao e) throws SQLException {
        String sql = "DELETE FROM solicitacao_horario_atendimento WHERE solicitacao_fk=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, e.getId());
        ps.execute();
        ps.close();

        sql = "INSERT INTO solicitacao_horario_atendimento(solicitacao_fk, horario_atendimento_fk) VALUES (?, ?);";
        for (HorarioAtendimento aux : e.getHorarioAtendimentoList()) {
            ps = conn.prepareStatement(sql);
            ps.setLong(1, e.getId());
            ps.setLong(2, aux.getId());
            ps.execute();
        }
        ps.close();
    }

    public List<Solicitacao> getSolicitacoesACancelar(Connection connection, Long id, Long diaAtendimentoFK) throws SQLException {
        String sql = "Select * from\n"
                + "\n"
                + "(SELECT\n"
                + "  s.id solicitacao_fk, \n"
                + "  s.status solicitacao_status,\n"
                + "  es.id execucao_fk, \n"
                + "  es.servico_fk servico_fk, \n"
                + "  es.descricao execucao_descricao, \n"
                + "  es.duracao execucao_duracao, \n"
                + "  es.valor execucao_valor, \n"
                + "  ser.nome servico_nome,\n"
                + "  up.nome profissional_nome, \n"
                + "  up.sobrenome profissional_sobrenome, \n"
                + "  up.nome_usuario profissional_nome_usuario, \n"
                + "  up.telefone profissional_telefone, \n"
                + "  up.email profissional_email, \n"
                + "  up.telefone profissional_nome,\n"
                + "  p.usuario_fk profissional_usuario_fk, \n"
                + "  p.descricao profissional_descricao, \n"
                + "  p.hora_inicio profissional_hora_inicio, \n"
                + "  p.hora_fim profissional_hora_fim, \n"
                + "  p.unidade_tempo profissional_unidade_tempo,\n"
                + "  s.usuario_fk cliente_fk, \n"
                + "  u.nome cliente_nome, \n"
                + "  u.sobrenome cliente_sobrenome, \n"
                + "  u.nome_usuario cliente_nome_usuario, \n"
                + "  u.email cliente_email, \n"
                + "  u.telefone cliente_telefone, \n"
                + "  min(ha.hora_inicio) hora_inicio, \n"
                + "  max(ha.hora_fim) hora_fim, \n"
                + "  da.data_at, da.id dia_atendimento_fk,\n"
                + "\n"
                + "(SELECT  min(ha.hora_inicio) hora_inicio from solicitacao s left join solicitacao_horario_atendimento sh on sh.solicitacao_fk=s.id left join horario_atendimento ha on sh.horario_atendimento_fk=ha.id where s.id =? group by s.id) as inicio_escolhido,\n"
                + "\n"
                + "  (SELECT  max(ha.hora_fim) hora_fim from solicitacao s left join solicitacao_horario_atendimento sh on sh.solicitacao_fk=s.id left join horario_atendimento ha on sh.horario_atendimento_fk=ha.id where s.id =? group by s.id) as fim_escolhido\n"
                + "  \n"
                + "from solicitacao s \n"
                + "left join solicitacao_horario_atendimento sh on sh.solicitacao_fk=s.id\n"
                + "left join horario_atendimento ha on sh.horario_atendimento_fk=ha.id\n"
                + "left join usuario u on s.usuario_fk=u.id\n"
                + "left join execucao_servico es on s.execucao_servico_fk=es.id\n"
                + "left join profissional p on es.profissional_fk=p.usuario_fk\n"
                + "left join usuario up on p.usuario_fk=up.id\n"
                + "left join servico ser on ser.id=es.servico_fk\n"
                + "left join dia_atendimento da on da.id=ha.dia_atendimento_fk\n"
                + "\n"
                + "--where 1=1 and data_at between '01-09-2015' and '02-09-2015' and s.status ilike 'a'\n"
                + "where s.id <> ? and da.id=?  and s.status ilike 'a'\n"
                + "\n"
                + "group by s.id, u.id, es.id, p.usuario_fk, up.id, ser.id, da.id) as a\n"
                + "\n"
                + "\n"
                + "where a.hora_inicio between a.inicio_escolhido and a.fim_escolhido-'00:00:01'::interval or a.hora_fim between a.inicio_escolhido+'00:00:01'::interval and a.fim_escolhido";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setLong(1, id);
        ps.setLong(2, id);
        ps.setLong(3, id);
        ps.setLong(4, diaAtendimentoFK);
        ResultSet rs = ps.executeQuery();

        List<Solicitacao> solicitacaoList = new ArrayList<>();
        while (rs.next()) {

            Solicitacao solicitacao = new Solicitacao();
            solicitacao.setStatus(rs.getString("solicitacao_status"));
            solicitacao.setId(rs.getLong("solicitacao_fk"));
            //usuario
            Usuario usuario = new Usuario();
            usuario.setId(rs.getLong("cliente_fk"));
            usuario.setNome(rs.getString("cliente_nome"));
            usuario.setSobrenome(rs.getString("cliente_sobrenome"));
            usuario.setNomeUsuario(rs.getString("cliente_nome_usuario"));
            usuario.setEmail(rs.getString("cliente_email"));
            usuario.setTelefone(rs.getString("cliente_telefone"));
            solicitacao.setUsuario(usuario);

            //execucao
            Execucao execucao = new Execucao();
            execucao.setId(rs.getLong("execucao_fk"));
            execucao.setDescricao(rs.getString("execucao_descricao"));
            execucao.setDuracao(new DateTime(rs.getTime("execucao_duracao").getTime()));
            execucao.setValor(rs.getFloat("execucao_valor"));
            //servico
            Servico servico = new Servico();
            servico.setId(rs.getLong("servico_fk"));
            servico.setNome(rs.getString("servico_nome"));
            execucao.setServico(servico);
            //profissional
            Profissional profissional = new Profissional();
            profissional.setId(rs.getLong("profissional_usuario_fk"));
            profissional.setDescricao(rs.getString("profissional_descricao"));
            profissional.setEmail(rs.getString("profissional_email"));
            profissional.setHoraFim(new DateTime(rs.getTime("profissional_hora_fim").getTime()));
            profissional.setHoraInicio(new DateTime(rs.getTime("profissional_hora_inicio").getTime()));
            profissional.setUnidadeTempo(new DateTime(rs.getTime("profissional_unidade_tempo").getTime()));
            profissional.setNome(rs.getString("profissional_nome"));
            profissional.setSobrenome(rs.getString("profissional_sobrenome"));
            profissional.setNomeUsuario(rs.getString("profissional_nome_usuario"));
            profissional.setTelefone(rs.getString("profissional_telefone"));

            execucao.setProfissional(profissional);
            solicitacao.setExecucao(execucao);

            //horario_atendimento
            HorarioAtendimento horario = new HorarioAtendimento();
            horario.setHoraInicio(new DateTime(rs.getTime("hora_inicio").getTime()));
            horario.setHoraFim(new DateTime(rs.getTime("hora_fim").getTime()));
            //dia atendimento
            DiaAtendimento diaAtendimento = new DiaAtendimento();
            diaAtendimento.setData(new DateTime(rs.getDate("data_at").getTime()));
            diaAtendimento.setId(rs.getLong("dia_atendimento_fk"));
            horario.setDiaAtendimento(diaAtendimento);

            solicitacao.getHorarioAtendimentoList().add(horario);

            solicitacaoList.add(solicitacao);
        }

        return solicitacaoList;
    }

    public Long counByCriteria(Connection conn, Map<String, Object> criteria) throws SQLException {
        String sql = "SELECT count(distinct s.id) total "
                + "from solicitacao s  "
                + "left join solicitacao_horario_atendimento sh on sh.solicitacao_fk=s.id "
                + "left join horario_atendimento ha on sh.horario_atendimento_fk=ha.id "
                + "left join usuario u on s.usuario_fk=u.id "
                + "left join execucao_servico es on s.execucao_servico_fk=es.id "
                + "left join profissional p on es.profissional_fk=p.usuario_fk "
                + "left join usuario up on p.usuario_fk=up.id "
                + "left join servico ser on ser.id=es.servico_fk "
                + "left join dia_atendimento da on da.id=ha.dia_atendimento_fk "
                + "where 1=1 ";

        if (criteria != null && criteria.size() > 0) {
            Long profissionalFK = (Long) criteria.get(SolicitacaoCriteria.PROFISSIONAL_FK_EQ);
            if (profissionalFK != null) {
                sql += " and p.usuario_fk=" + profissionalFK;
            }

            Long clienteFK = (Long) criteria.get(SolicitacaoCriteria.CLIENTE_FK_EQ);
            if (clienteFK != null) {
                sql += " and s.usuario_fk=" + clienteFK;
            }

            DateTime dataInicio = (DateTime) criteria.get(SolicitacaoCriteria.DIA_ATENDIMENTO_ME);
            if (dataInicio != null) {
                sql += " and da.data_at >='" + dataInicio.toString("dd/MM/yyyy") + "'";
            }
            DateTime dataFim = (DateTime) criteria.get(SolicitacaoCriteria.DIA_ATENDIMENTO_LE);
            if (dataFim != null) {
                sql += " and da.data_at <='" + dataFim.toString("dd/MM/yyyy") + "'";
            }

            String status = (String) criteria.get(SolicitacaoCriteria.STATUS_EQ);
            if (status != null && !status.isEmpty()) {
                sql += " and s.status ilike '" + status + "'";
            }
        }

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        Long count = null;
        if (rs.next()) {
            count = rs.getLong("total");
        }

        return count;
    }

    public DiaAtendimento getDiaAtendimentoFromSolicitacao(Connection connection, Long id) throws SQLException {
        String sql = "SELECT da.* from solicitacao s \n"
                + "left join solicitacao_horario_atendimento sha on sha.solicitacao_fk=s.id\n"
                + "left join horario_atendimento ha on ha.id=sha.horario_atendimento_fk\n"
                + "left join dia_atendimento da on da.id=ha.dia_atendimento_fk\n"
                + "where s.id=?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setLong(1, id);

        ResultSet rs = ps.executeQuery();

        DiaAtendimento diaAtendimento = null;
        if (rs.next()) {
            diaAtendimento = new DiaAtendimento();
            diaAtendimento.setId(rs.getLong("id"));
            diaAtendimento.setData(new DateTime(rs.getDate("data_at")));

            Profissional profissional = new Profissional();
            profissional.setId(rs.getLong("profissional_fk"));
            diaAtendimento.setProfissional(profissional);
        }
        return diaAtendimento;
    }

    public void remarcarSolicitacao(Connection connection, Solicitacao e) throws SQLException {
        update(connection, e);
        updateHorarioAtendimentoList(connection, e);
    }

    public boolean existsSolicitacao(Connection conn, Long execucaoFK, Long clienteFK, Long diaAtendimentoFK, DateTime horaInicio, DateTime horaFim) throws SQLException {
        String sql = "SELECT 	s.id solicitacao_fk, 	\n"
                + "	es.id execucao_fk,\n"
                + "	s.usuario_fk cliente_fk,\n"
                + "	min(ha.hora_inicio) s_hora_inicio, max(ha.hora_fim) s_hora_fim, \n"
                + "	da.id dia_atendimento_fk	\n"
                + "from solicitacao s \n"
                + "left join solicitacao_horario_atendimento sh on sh.solicitacao_fk=s.id\n"
                + "left join horario_atendimento ha on sh.horario_atendimento_fk=ha.id\n"
                + "left join usuario u on s.usuario_fk=u.id\n"
                + "left join execucao_servico es on s.execucao_servico_fk=es.id\n"
                + "left join profissional p on es.profissional_fk=p.usuario_fk\n"
                + "left join usuario up on p.usuario_fk=up.id\n"
                + "left join dia_atendimento da on da.id=ha.dia_atendimento_fk\n"
                + "where es.id=? and s.usuario_fk=? and dia_atendimento_fk=? and s.status ilike 'a'\n"
                + "group by s.id, u.id, es.id, p.usuario_fk, up.id, da.id\n"
                + "having min(ha.hora_inicio)=? and max(ha.hora_fim)=?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, execucaoFK);
        ps.setLong(2, clienteFK);
        ps.setLong(3, diaAtendimentoFK);
        ps.setTime(4, new Time(horaInicio.getMillis()));
        ps.setTime(5, new Time(horaFim.getMillis()));

        ResultSet rs = ps.executeQuery();

        boolean existe = false;
        if (rs.next()) {
            existe = true;
        }
        rs.close();
        ps.close();
        return existe;
    }

    public Solicitacao getSolicitacaoFromHorarioAtendimentoId(Connection conn, Long horarioAtendimentoId, String status) throws SQLException {
        String sql = "select servico.nome servico_nome, usuario.nome usuario_nome from horario_atendimento ha \n"
                + "left join solicitacao_horario_atendimento sha on ha.id=sha.horario_atendimento_fk\n"
                + "left join solicitacao s on s.id=sha.solicitacao_fk\n"
                + "left join execucao_servico es on es.id=s.execucao_servico_fk\n"
                + "left join servico on es.servico_fk=servico.id\n"
                + "left join usuario on s.usuario_fk=usuario.id\n"
                + "where ha.id=? and s.status ilike ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, horarioAtendimentoId);
        ps.setString(2, status);

        ResultSet rs = ps.executeQuery();
        Solicitacao solicitacao = null;
        if (rs.next()) {
            solicitacao = new Solicitacao();

            Execucao execucao = new Execucao();
            Servico servico = new Servico();
            servico.setNome(rs.getString("servico_nome"));
            execucao.setServico(servico);
            solicitacao.setExecucao(execucao);

            Usuario usuario = new Usuario();
            usuario.setNome(rs.getString("usuario_nome"));
            solicitacao.setUsuario(usuario);
        }
        rs.close();
        ps.close();
        return solicitacao;
    }

    public void avaliar(Connection conn, Long id, Float score) throws SQLException {
        String sql = "INSERT INTO solicitacao_avaliacao(solicitacao_fk, pontos) VALUES (?, ?)";
        
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, id);
        ps.setFloat(2, score);
        
        ps.execute();
        ps.close();
    }
}
