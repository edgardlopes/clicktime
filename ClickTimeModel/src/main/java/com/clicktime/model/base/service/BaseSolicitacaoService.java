package com.clicktime.model.base.service;

import com.clicktime.model.base.BaseService;
import com.clicktime.model.entity.Solicitacao;
import com.clicktime.model.entity.Usuario;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;


public interface BaseSolicitacaoService extends BaseService<Solicitacao>{
    public void solicitarHorario(Solicitacao solicitacao) throws Exception;
    public List<Solicitacao> aceitarHorario(Solicitacao solicitacao) throws Exception;
    public void recusarHorario(Solicitacao solicitacao) throws Exception;
    public Long countByCriteria(Map<String, Object> criteria) throws Exception;
    public List<Solicitacao> readByCriteriaSemPaginacao(Map<String, Object> criteria) throws Exception;
    public void remarcarSolicitacao(Solicitacao e) throws Exception;
    public Boolean existsSolicitacao(Long execucaoFK, Long clienteFK, Long diaAtendimentoFK, DateTime horaInicio, DateTime horaFim) throws Exception;
    public Solicitacao getSolicitacaoFromHorarioAtendimentoId(Long horarioAtendimentoId, String status) throws Exception;
    public void avaliar(Long id, Float score) throws Exception;
    
    List<Solicitacao> readByCliente(Usuario usuario) throws Exception;
}
