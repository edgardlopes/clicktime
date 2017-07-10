package com.clicktime.model.base.service;

import com.clicktime.model.base.BaseService;
import com.clicktime.model.entity.Profissional;

public interface BaseProfissionalService extends BaseService<Profissional> {

    Profissional readByUserName(String userName) throws Exception;

}
