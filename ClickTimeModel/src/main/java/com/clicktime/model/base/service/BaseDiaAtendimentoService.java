
package com.clicktime.model.base.service;

import com.clicktime.model.base.BaseDAO;
import com.clicktime.model.base.BaseService;
import com.clicktime.model.entity.DiaAtendimento;
import com.clicktime.model.entity.Profissional;
import org.joda.time.DateTime;

public abstract class BaseDiaAtendimentoService extends BaseService<DiaAtendimento>{

    public BaseDiaAtendimentoService(BaseDAO dao) {
        super(dao);
    }
    public abstract void readOrCreate(DiaAtendimento da) throws Exception;
    
    public abstract DiaAtendimento readDiaAtendimentoFromDate(DateTime date, Profissional profissional) throws Exception;
}
