package com.clicktime.model.service;

import com.clicktime.model.base.service.BaseServicoService;
import com.clicktime.model.dao.ServicoDAO;
import java.util.Map;

public class ServicoService extends BaseServicoService {

    public ServicoService() {
        super(new ServicoDAO());
    }

    @Override
    public Map<String, String> validateForCreate(Map<String, Object> fields) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, String> validateForUpdate(Map<String, Object> fields) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
