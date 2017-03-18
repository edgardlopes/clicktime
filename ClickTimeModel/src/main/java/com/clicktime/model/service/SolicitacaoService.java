package com.clicktime.model.service;

import com.clicktime.model.ConnectionManager;
import com.clicktime.model.base.service.BaseSolicitacaoService;
import com.clicktime.model.criteria.HorarioAtendimentoCriteria;
import com.clicktime.model.dao.HorarioAtendimentoDAO;
import com.clicktime.model.dao.SolicitacaoDAO;
import com.clicktime.model.entity.DiaAtendimento;
import com.clicktime.model.entity.HorarioAtendimento;
import com.clicktime.model.entity.Solicitacao;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public class SolicitacaoService implements BaseSolicitacaoService {

    @Override
    public void create(Solicitacao entity) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();

        try {
            SolicitacaoDAO dao = new SolicitacaoDAO();
            dao.create(connection, entity);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.close();
        }
    }

    @Override
    public Solicitacao readById(Long id) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();
        Solicitacao solicitacao = null;
        try {
            SolicitacaoDAO dao = new SolicitacaoDAO();
            solicitacao = dao.readById(connection, id);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.close();
        }

        return solicitacao;
    }

    @Override
    public List<Solicitacao> readByCriteria(Map<String, Object> criteria, Integer offset) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();

        List<Solicitacao> solicitacaoList = new ArrayList<>();
        try {
            SolicitacaoDAO dao = new SolicitacaoDAO();
            solicitacaoList = dao.readByCriteria(connection, criteria, offset);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.close();
        }

        return solicitacaoList;
    }

    @Override
    public List<Solicitacao> readByCriteriaSemPaginacao(Map<String, Object> criteria) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();

        List<Solicitacao> solicitacaoList = new ArrayList<>();
        try {
            SolicitacaoDAO dao = new SolicitacaoDAO();
            solicitacaoList = dao.readByCriteriaSemPaginacao(connection, criteria);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.close();
        }

        return solicitacaoList;

    }

    @Override
    public void update(Solicitacao entity) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();

        try {
            SolicitacaoDAO dao = new SolicitacaoDAO();
            dao.update(connection, entity);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.close();
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();

        try {
            SolicitacaoDAO dao = new SolicitacaoDAO();
            dao.delete(connection, id);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.close();
        }
    }

    @Override
    public void solicitarHorario(Solicitacao solicitacao) throws Exception {
        solicitacao.setStatus(Solicitacao.AGUARDANDO_CONFIRMACAO);
        create(solicitacao);
    }

    @Override
    public List<Solicitacao> aceitarHorario(Solicitacao solicitacao) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();
        List<Solicitacao> solicitacaoCanceladaList = new ArrayList<Solicitacao>();
        try {

            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();

            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(HorarioAtendimentoCriteria.SOLICITACAO_FK_EQ, solicitacao.getId());
            HorarioAtendimentoDAO horarioAtendimentoDAO = new HorarioAtendimentoDAO();
            List<HorarioAtendimento> horarioAtendimentoList = horarioAtendimentoDAO.readByCriteria(connection, criteria, null);

            //reservando horarios
            if (horarioAtendimentoList.size() > 0) {
                for (HorarioAtendimento aux : horarioAtendimentoList) {
                    aux.setStatus(HorarioAtendimento.HORARIO_RESERVADO);
                    horarioAtendimentoDAO.update(connection, aux);
                }
            }

            //cancelando automaticamente os horarios que sobrepoem ao que esta sendo reservado
            DiaAtendimento diaSolicitacao = solicitacaoDAO.getDiaAtendimentoFromSolicitacao(connection, solicitacao.getId());
            solicitacaoCanceladaList = solicitacaoDAO.getSolicitacoesACancelar(connection, solicitacao.getId(), diaSolicitacao.getId());
            for (Solicitacao aux : solicitacaoCanceladaList) {
                aux.setStatus(Solicitacao.SOLICITACAO_REJEITADA);
                aux.setDescricao("Cancelado devido a sobreposição de horários.");
                solicitacaoDAO.update(connection, aux);
            }

            solicitacao.setStatus(Solicitacao.SOLICITACAO_ACEITA);
            solicitacaoDAO.update(connection, solicitacao);

            connection.commit();
        } catch (Exception exception) {
            connection.rollback();
            throw exception;

        } finally {
            connection.close();
        }

        return solicitacaoCanceladaList;
    }

    @Override
    public void recusarHorario(Solicitacao solicitacao) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();
        try {
            solicitacao.setStatus(Solicitacao.SOLICITACAO_REJEITADA);

            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
            solicitacaoDAO.update(connection, solicitacao);

            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(HorarioAtendimentoCriteria.SOLICITACAO_FK_EQ, solicitacao.getId());
            HorarioAtendimentoDAO horarioAtendimentoDAO = new HorarioAtendimentoDAO();
            List<HorarioAtendimento> horarioAtendimentoList = horarioAtendimentoDAO.readByCriteria(connection, criteria, null);

            //liberando horarios
            if (horarioAtendimentoList.size() > 0) {
                for (HorarioAtendimento aux : horarioAtendimentoList) {
                    aux.setStatus(HorarioAtendimento.HORARIO_LIVRE);
                    horarioAtendimentoDAO.update(connection, aux);
                }
            }

            connection.commit();
        } catch (Exception exception) {
            connection.rollback();
            throw exception;

        } finally {
            connection.close();
        }

    }

    @Override
    public Long countByCriteria(Map<String, Object> criteria) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();

        Long count = null;
        try {
            count = new SolicitacaoDAO().counByCriteria(connection, criteria);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.close();
        }

        return count;
    }

    @Override
    public void remarcarSolicitacao(Solicitacao e) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();

        try {
            new SolicitacaoDAO().remarcarSolicitacao(connection, e);

            HorarioAtendimentoDAO dao = new HorarioAtendimentoDAO();
            for (HorarioAtendimento aux : e.getHorarioAtendimentoList()) {
                dao.update(connection, aux);
            }
            connection.commit();
        } catch (Exception ex) {
            connection.rollback();
            throw ex;
        } finally {
            connection.close();
        }

    }

    @Override
    public Map<String, String> validateForCreate(Map<String, Object> fields) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, String> validateForUpdate(Map<String, Object> fields) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean existsSolicitacao(Long execucaoFK, Long clienteFK, Long diaAtendimentoFK, DateTime horaInicio, DateTime horaFim) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();
        Boolean existe = null;
        try {
            existe = new SolicitacaoDAO().existsSolicitacao(connection, execucaoFK, clienteFK, diaAtendimentoFK, horaInicio, horaFim);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.close();
        }

        return existe;
    }

    @Override
    public Solicitacao getSolicitacaoFromHorarioAtendimentoId(Long horarioAtendimentoId, String status) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();
        Solicitacao solicitacao = null;

        try {
            solicitacao = new SolicitacaoDAO().getSolicitacaoFromHorarioAtendimentoId(connection, horarioAtendimentoId, status);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.close();
        }
        return solicitacao;
    }

    @Override
    public void avaliar(Long id, Float score) throws Exception {

        Connection connection = ConnectionManager.getInstance().getConnection();

        try {
            new SolicitacaoDAO().avaliar(connection, id, score);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.close();
        }
    }
}
