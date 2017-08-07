package com.clicktime.model.service;

import com.clicktime.model.util.StringUtils;
import com.clicktime.model.ErrorMessage;
import com.clicktime.model.base.service.BaseCategoriaServicoService;
import com.clicktime.model.criteria.CategoriaServicoCriteria;
import com.clicktime.model.dao.CategoriaServicoDAO;
import com.clicktime.model.fields.CategoriaServicoFields;
import java.util.HashMap;
import java.util.Map;

public class CategoriaServicoService extends BaseCategoriaServicoService {

    public CategoriaServicoService() {
        super(new CategoriaServicoDAO());
    }


    @Override
    public Map<String, String> validateForCreate(Map<String, Object> fields) throws Exception {
        Map<String, String> errors = new HashMap<>();
        String nome = (String) fields.get(CategoriaServicoFields.NOME);
        if (StringUtils.isBlank(nome)) {
            errors.put(CategoriaServicoFields.NOME, ErrorMessage.NOT_NULL);
        } else {
            Map<String, Object> criteria = new HashMap<>();
            criteria.put(CategoriaServicoCriteria.NOME_EQ, nome);
            if (!readByCriteria(criteria, null).isEmpty()) {
                errors.put(CategoriaServicoFields.NOME, ErrorMessage.NOME_CATEGORIA_UNIQUE);
            }
        }
        return errors;
    }

    @Override
    public Map<String, String> validateForUpdate(Map<String, Object> fields) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
