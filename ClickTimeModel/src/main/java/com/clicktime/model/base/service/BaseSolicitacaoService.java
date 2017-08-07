package com.clicktime.model.base.service;

import com.clicktime.model.base.BaseDAO;
import com.clicktime.model.base.BaseService;
import com.clicktime.model.entity.Solicitacao;
import com.clicktime.model.entity.Usuario;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public abstract class BaseSolicitacaoService extends BaseService<Solicitacao> {

    public BaseSolicitacaoService(BaseDAO dao) {
        super(dao);
    }

    public abstract void solicitarHorario(Solicitacao solicitacao) throws Exception;

    public abstract List<Solicitacao> aceitarHorario(Solicitacao solicitacao) throws Exception;

    public abstract void recusarHorario(Solicitacao solicitacao) throws Exception;

    public abstract Long countByCriteria(Map<String, Object> criteria) throws Exception;

    public abstract List<Solicitacao> readByCriteriaSemPaginacao(Map<String, Object> criteria) throws Exception;

    public abstract void remarcarSolicitacao(Solicitacao e) throws Exception;

    public abstract Boolean existsSolicitacao(Long execucaoFK, Long clienteFK, Long diaAtendimentoFK, DateTime horaInicio, DateTime horaFim) throws Exception;

    public abstract Solicitacao getSolicitacaoFromHorarioAtendimentoId(Long horarioAtendimentoId, String status) throws Exception;

    public abstract void avaliar(Long id, Float score) throws Exception;
    
    public abstract List<Solicitacao> readByCliente(Usuario usuario) throws Exception;
}
