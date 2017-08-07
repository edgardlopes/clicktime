package com.clicktime.model.base.service;

import com.clicktime.model.base.BaseDAO;
import com.clicktime.model.base.BaseService;
import com.clicktime.model.entity.DiaAtendimento;
import com.clicktime.model.entity.Execucao;
import com.clicktime.model.entity.HorarioAtendimento;
import com.clicktime.model.entity.Profissional;
import java.util.List;
import java.util.Map;

public abstract class BaseHorarioAtendimentoService extends BaseService<HorarioAtendimento> {

    public BaseHorarioAtendimentoService(BaseDAO dao) {
        super(dao);
    }

    public abstract void generateHorarioAtendimentoList(DiaAtendimento da) throws Exception;

    public abstract List<HorarioAtendimento> read(DiaAtendimento da) throws Exception;

    public abstract void createHorarioAtendimentoList(List<HorarioAtendimento> horarioAtendimentoList) throws Exception;

    public abstract void updateList(Long[] id, String action) throws Exception;

    public abstract List<Map<String, Object>> agruparHorarios(Execucao execucao, List<HorarioAtendimento> horarioList, Profissional profissional) throws Exception;

    public abstract void updateList(List<HorarioAtendimento> horarioList) throws Exception;

    public abstract List<HorarioAtendimento> readByCriteriaCliente(Map<String, Object> criteria, Integer offset) throws Exception;
}
