package com.clicktime.model.base.service;

import com.clicktime.model.base.BaseService;
import com.clicktime.model.entity.DiaAtendimento;
import com.clicktime.model.entity.Execucao;
import com.clicktime.model.entity.HorarioAtendimento;
import com.clicktime.model.entity.Profissional;
import java.util.List;
import java.util.Map;

public interface BaseHorarioAtendimentoService extends BaseService<HorarioAtendimento> {

    public void generateHorarioAtendimentoList(DiaAtendimento da) throws Exception;

    public List<HorarioAtendimento> read(DiaAtendimento da) throws Exception;

    public void createHorarioAtendimentoList(List<HorarioAtendimento> horarioAtendimentoList) throws Exception;

    public void updateList(Long[] id, String action) throws Exception;

    public List<Map<String, Object>> agruparHorarios(Execucao execucao, List<HorarioAtendimento> horarioList, Profissional profissional) throws Exception;

    public void updateList(List<HorarioAtendimento> horarioList) throws Exception;

    public List<HorarioAtendimento> readByCriteriaCliente(Map<String, Object> criteria, Integer offset) throws Exception;
}
