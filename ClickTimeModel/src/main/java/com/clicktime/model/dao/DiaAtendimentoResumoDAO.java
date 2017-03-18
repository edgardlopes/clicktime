package com.clicktime.model.dao;

import com.clicktime.model.entity.DiaAtendimento;
import com.clicktime.model.entity.DiaAtendimentoResumo;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

public class DiaAtendimentoResumoDAO {

    public List<DiaAtendimentoResumo> readByReportOfMonth(Connection conn, DiaAtendimento da) throws Exception {
        String sql = "select data_at, profissional_fk, sum(livre) as qtde_livre, sum(bloqueado) as qtde_bloqueado from "
                + "(select data_at, profissional_fk,  "
                + "case when upper(status) ilike 'l' then count(status) else 0 end as livre, "
                + "case when upper(status) ilike 'b' then count(status) else 0 end as bloqueado "
                + "from dia_atendimento left join horario_atendimento on dia_atendimento.id = horario_atendimento.dia_atendimento_fk where profissional_fk=? and data_at between ? and ? "
                + "group by data_at, profissional_fk, upper(status) "
                + "order by data_at "
                + ")temp "
                + "group by data_at, profissional_fk";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setLong(1, da.getProfissional().getId());

        Date date = new Date(da.getData().getMillis());
        ps.setDate(2, date);

        
        date = new Date(da.getData().plusMonths(1).minusDays(1).getMillis());
        ps.setDate(3, date);


        ResultSet rs = ps.executeQuery();

        List<DiaAtendimentoResumo> resumoList = new ArrayList<DiaAtendimentoResumo>();
        while (rs.next()) {
            DiaAtendimentoResumo resumo = new DiaAtendimentoResumo();

            DiaAtendimento dia = new DiaAtendimento();
            DateTime dt = new DateTime(rs.getDate("data_at"));
            dia.setData(dt);
            dia.setProfissional(da.getProfissional());

            resumo.setDiaAtendimento(dia);
            resumo.setHorariosBloqueados(rs.getInt("qtde_bloqueado"));
            resumo.setHorariosLivres(rs.getInt("qtde_livre"));

            resumoList.add(resumo);
        }

        return resumoList;
    }
}
