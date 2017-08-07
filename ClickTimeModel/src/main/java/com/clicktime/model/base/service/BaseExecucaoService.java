package com.clicktime.model.base.service;

import com.clicktime.model.base.BaseDAO;
import com.clicktime.model.base.BaseService;
import com.clicktime.model.entity.Execucao;
import com.clicktime.model.entity.Profissional;
import java.util.List;
import org.joda.time.DateTime;

public abstract class BaseExecucaoService extends BaseService<Execucao> {

    public BaseExecucaoService(BaseDAO dao) {
        super(dao);
    }

    public abstract DateTime calcularUnidadeTempo(List<Execucao> execucaoList) throws Exception;

    public abstract void delete(Long id, Profissional profissional) throws Exception;

    public abstract List<Execucao> readByProfissional(Profissional profissional) throws Exception;
}
