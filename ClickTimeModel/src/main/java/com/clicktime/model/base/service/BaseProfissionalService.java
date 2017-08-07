package com.clicktime.model.base.service;

import com.clicktime.model.base.BaseDAO;
import com.clicktime.model.base.BaseService;
import com.clicktime.model.entity.Profissional;

public abstract class BaseProfissionalService extends BaseService<Profissional> {

    public BaseProfissionalService(BaseDAO dao) {
        super(dao);
    }

    public abstract Profissional readByUserName(String userName) throws Exception;

}
