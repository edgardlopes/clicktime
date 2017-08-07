package com.clicktime.model.service;

import com.clicktime.model.base.service.BaseDiaAtendimentoService;
import com.clicktime.model.criteria.DiaAtendimentoCriteria;
import com.clicktime.model.dao.DiaAtendimentoDAO;
import com.clicktime.model.entity.DiaAtendimento;
import com.clicktime.model.entity.Profissional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public class DiaAtendimentoService extends BaseDiaAtendimentoService {

    public DiaAtendimentoService() {
        super(new DiaAtendimentoDAO());
    }

    @Override
    public void readOrCreate(DiaAtendimento da) throws Exception {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put(DiaAtendimentoCriteria.PROFISSIONAL_FK_EQ, da.getProfissional().getId());
        criteria.put(DiaAtendimentoCriteria.DIA_ATENDIMENTO_EQ, da.getData());

        List<DiaAtendimento> daList = readByCriteria(criteria, null);
        if (daList.isEmpty()) {
            create(da);
        } else {
            da.setId(daList.get(0).getId());
        }
    }

    @Override
    public DiaAtendimento readDiaAtendimentoFromDate(DateTime date, Profissional profissional) throws Exception {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put(DiaAtendimentoCriteria.PROFISSIONAL_FK_EQ, profissional.getId());
        criteria.put(DiaAtendimentoCriteria.DIA_ATENDIMENTO_EQ, date);

        List<DiaAtendimento> daList = readByCriteria(criteria, null);
        if (daList.size() == 1) {
            return daList.get(0);
        }

        return null;
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
