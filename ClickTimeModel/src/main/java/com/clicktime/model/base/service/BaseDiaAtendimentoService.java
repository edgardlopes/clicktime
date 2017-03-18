
package com.clicktime.model.base.service;

import com.clicktime.model.base.BaseService;
import com.clicktime.model.entity.DiaAtendimento;
import com.clicktime.model.entity.Profissional;
import org.joda.time.DateTime;

public interface BaseDiaAtendimentoService extends BaseService<DiaAtendimento>{
    public void readOrCreate(DiaAtendimento da) throws Exception;
    
    public DiaAtendimento readDiaAtendimentoFromDate(DateTime date, Profissional profissional) throws Exception;
}
