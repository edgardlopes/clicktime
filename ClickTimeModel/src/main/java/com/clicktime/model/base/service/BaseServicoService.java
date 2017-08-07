package com.clicktime.model.base.service;

import com.clicktime.model.base.BaseDAO;
import com.clicktime.model.base.BaseService;
import com.clicktime.model.entity.Servico;

public abstract class BaseServicoService extends BaseService<Servico>{
    
    public BaseServicoService(BaseDAO dao) {
        super(dao);
    }
    
}
