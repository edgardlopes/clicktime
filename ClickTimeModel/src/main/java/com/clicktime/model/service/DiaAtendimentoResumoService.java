package com.clicktime.model.service;

import com.clicktime.model.TransactionExecutor;
import com.clicktime.model.dao.DiaAtendimentoResumoDAO;
import com.clicktime.model.entity.DiaAtendimento;
import com.clicktime.model.entity.DiaAtendimentoResumo;
import java.util.List;

public class DiaAtendimentoResumoService {

    public List<DiaAtendimentoResumo> reportFromMonth(DiaAtendimento day) throws Exception {
        TransactionExecutor executor = new TransactionExecutor();
        return executor.execute(conn -> new DiaAtendimentoResumoDAO().readByReportOfMonth(conn, day));
    }
}
