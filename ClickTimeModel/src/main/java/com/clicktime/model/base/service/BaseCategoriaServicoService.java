package com.clicktime.model.base.service;

import com.clicktime.model.base.BaseDAO;
import com.clicktime.model.base.BaseService;
import com.clicktime.model.entity.CategoriaServico;

public abstract class BaseCategoriaServicoService extends BaseService<CategoriaServico>{
    
    public BaseCategoriaServicoService(BaseDAO dao) {
        super(dao);
    }
    
}
