package com.clicktime.model.base.service;

import com.clicktime.model.base.BaseService;
import com.clicktime.model.entity.Execucao;
import com.clicktime.model.entity.Profissional;
import java.util.List;
import org.joda.time.DateTime;

public interface BaseExecucaoService extends BaseService<Execucao>{
    public DateTime calcularUnidadeTempo(List<Execucao> execucaoList) throws Exception;
    
    public void delete(Long id, Profissional profissional) throws Exception;
}
