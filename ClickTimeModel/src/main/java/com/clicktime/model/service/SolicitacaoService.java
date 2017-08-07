package com.clicktime.model.service;

import com.clicktime.model.base.service.BaseSolicitacaoService;
import com.clicktime.model.criteria.HorarioAtendimentoCriteria;
import com.clicktime.model.dao.HorarioAtendimentoDAO;
import com.clicktime.model.dao.SolicitacaoDAO;
import com.clicktime.model.entity.DiaAtendimento;
import com.clicktime.model.entity.HorarioAtendimento;
import com.clicktime.model.entity.Solicitacao;
import com.clicktime.model.entity.Usuario;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public class SolicitacaoService extends BaseSolicitacaoService {

    public SolicitacaoService() {
        super(new SolicitacaoDAO());
    }

    @Override
    public List<Solicitacao> readByCriteriaSemPaginacao(Map<String, Object> criteria) throws Exception {
        return executor.execute(conn -> ((SolicitacaoDAO) dao).readByCriteriaSemPaginacao(conn, criteria));
    }

    @Override
    public void solicitarHorario(Solicitacao solicitacao) throws Exception {
        solicitacao.setStatus(Solicitacao.AGUARDANDO_CONFIRMACAO);
        create(solicitacao);
    }

    @Override
    public List<Solicitacao> aceitarHorario(Solicitacao solicitacao) throws Exception {
        return executor.execute(conn -> {

            Map<String, Object> criteria = new HashMap<>();
            criteria.put(HorarioAtendimentoCriteria.SOLICITACAO_FK_EQ, solicitacao.getId());
            HorarioAtendimentoDAO horarioAtendimentoDAO = new HorarioAtendimentoDAO();
            List<HorarioAtendimento> horarioAtendimentoList = horarioAtendimentoDAO.readByCriteria(conn, criteria, null);

            //reservando horarios
            for (HorarioAtendimento aux : horarioAtendimentoList) {
                aux.setStatus(HorarioAtendimento.HORARIO_RESERVADO);
                horarioAtendimentoDAO.update(conn, aux);
            }

            //cancelando automaticamente os horarios que sobrepoem ao que esta sendo reservado
            DiaAtendimento diaSolicitacao = ((SolicitacaoDAO) dao).getDiaAtendimentoFromSolicitacao(conn, solicitacao.getId());
            List<Solicitacao> solicitacaoCanceladaList = ((SolicitacaoDAO) dao).getSolicitacoesACancelar(conn, solicitacao.getId(), diaSolicitacao.getId());
            for (Solicitacao aux : solicitacaoCanceladaList) {
                aux.cancelar();
                ((SolicitacaoDAO) dao).update(conn, aux);
            }

            solicitacao.setStatus(Solicitacao.SOLICITACAO_ACEITA);
            ((SolicitacaoDAO) dao).update(conn, solicitacao);

            return solicitacaoCanceladaList;
        });
    }

    @Override
    public void recusarHorario(Solicitacao solicitacao) throws Exception {
        executor.execute(conn -> {
            solicitacao.setStatus(Solicitacao.SOLICITACAO_REJEITADA);

            ((SolicitacaoDAO) dao).update(conn, solicitacao);

            Map<String, Object> criteria = new HashMap<>();
            criteria.put(HorarioAtendimentoCriteria.SOLICITACAO_FK_EQ, solicitacao.getId());
            HorarioAtendimentoDAO horarioAtendimentoDAO = new HorarioAtendimentoDAO();
            List<HorarioAtendimento> horarioAtendimentoList = horarioAtendimentoDAO.readByCriteria(conn, criteria, null);

            //liberando horarios
            for (HorarioAtendimento aux : horarioAtendimentoList) {
                aux.setStatus(HorarioAtendimento.HORARIO_LIVRE);
                horarioAtendimentoDAO.update(conn, aux);
            }

            return Void.TYPE;
        });
    }

    @Override
    public Long countByCriteria(Map<String, Object> criteria) throws Exception {
        return executor.execute(conn -> ((SolicitacaoDAO) dao).counByCriteria(conn, criteria));
    }

    @Override
    public void remarcarSolicitacao(Solicitacao e) throws Exception {
        executor.execute(conn -> {
            HorarioAtendimentoDAO dao = new HorarioAtendimentoDAO();
            for (HorarioAtendimento aux : e.getHorarioAtendimentoList()) {
                dao.update(conn, aux);
            }
            return Void.TYPE;
        });
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
        return executor.execute(conn -> ((SolicitacaoDAO) dao).existsSolicitacao(conn, execucaoFK, clienteFK, diaAtendimentoFK, horaInicio, horaFim));
    }

    @Override
    public Solicitacao getSolicitacaoFromHorarioAtendimentoId(Long horarioAtendimentoId, String status) throws Exception {
        return executor.execute(conn -> ((SolicitacaoDAO) dao).getSolicitacaoFromHorarioAtendimentoId(conn, horarioAtendimentoId, status));
    }

    @Override
    public void avaliar(Long id, Float score) throws Exception {
        executor.execute(conn -> {
            ((SolicitacaoDAO) dao).avaliar(conn, id, score);
            return Void.TYPE;
        });
    }

    @Override
    public List<Solicitacao> readByCliente(Usuario usuario) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
