package com.clicktime.model.service;

import com.clicktime.model.base.service.BaseHorarioAtendimentoService;
import com.clicktime.model.criteria.HorarioAtendimentoCriteria;
import com.clicktime.model.dao.HorarioAtendimentoDAO;
import com.clicktime.model.entity.DiaAtendimento;
import com.clicktime.model.entity.Execucao;
import com.clicktime.model.entity.HorarioAtendimento;
import com.clicktime.model.entity.Profissional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public class HorarioAtendimentoService extends BaseHorarioAtendimentoService {

    public static final String BLOCK_HORARIO_ATENDIMENTO = "B";
    public static final String RELEASE_HORARIO_ATENDIMENTO = "L";
    public static final String KEY_HORARIO_ATENDIMENTO = "horarioAtendimento";
    public static final String KEY_ID_LIST = "IDList";

    public HorarioAtendimentoService() {
        super(new HorarioAtendimentoDAO());
    }

    @Override
    public void generateHorarioAtendimentoList(DiaAtendimento da) throws Exception {
        List<HorarioAtendimento> horarioList = gerarHorarioList(da);
        createHorarioAtendimentoList(horarioList);
    }

    @Override
    public List<HorarioAtendimento> read(DiaAtendimento da) throws Exception {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put(HorarioAtendimentoCriteria.DIA_ATENDIMENTO_FK_EQ, da.getId());
        return readByCriteriaCliente(criteria, null);
    }

    @Override
    public void createHorarioAtendimentoList(List<HorarioAtendimento> horarioAtendimentoList) throws Exception {
        executor.execute(conn -> {
            for (HorarioAtendimento aux : horarioAtendimentoList) {
                dao.create(conn, aux);
            }
            return Void.TYPE;
        });
    }

    @Override
    public void updateList(Long[] id, String action) throws Exception {
        if (action.equals(BLOCK_HORARIO_ATENDIMENTO) || action.equals(RELEASE_HORARIO_ATENDIMENTO)) {

            executor.execute(conn -> {
                for (Long aux : id) {
                    HorarioAtendimento h = new HorarioAtendimento();
                    h.setId(aux);
                    h.setStatus(action);

                    dao.update(conn, h);
                }
                return Void.TYPE;
            });
        }
    }

    @Override
    public List<Map<String, Object>> agruparHorarios(Execucao execucao, List<HorarioAtendimento> horarioList, Profissional profissional) {
        DateTime duracao = execucao.getDuracao();

        List<Map<String, Object>> informations = new ArrayList<>();
        try {
            int qtdeHorarios = duracao.getMinuteOfDay() / profissional.getUnidadeTempo().getMinuteOfDay();
            informations = new ArrayList<>();

            Map<String, Object> novoHorarioAtendimento;

            for (int i = 0; i < horarioList.size() - (qtdeHorarios - 1); i++) {
                int j = i;
                String ids = horarioList.get(j).getId() + ", ";
                while (j < (i + qtdeHorarios - 1) && horarioList.get(j).getStatus().toLowerCase().equals(HorarioAtendimento.HORARIO_LIVRE.toLowerCase())) {
                    j++;
                    ids += horarioList.get(j).getId() + ", ";
                }

                if (horarioList.get(j).getStatus().toLowerCase().equals(HorarioAtendimento.HORARIO_LIVRE.toLowerCase())) {
                    novoHorarioAtendimento = new HashMap<>();

                    HorarioAtendimento ha = new HorarioAtendimento();
                    ha.setHoraInicio(horarioList.get(i).getHoraInicio());
                    ha.setHoraFim(horarioList.get(j).getHoraFim());
                    novoHorarioAtendimento.put(KEY_HORARIO_ATENDIMENTO, ha);
                    novoHorarioAtendimento.put(KEY_ID_LIST, ids);

                    informations.add(novoHorarioAtendimento);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return informations;
    }

    public List<HorarioAtendimento> gerarHorarioList(DiaAtendimento diaAtendimento) throws Exception {

        DateTime diferenca = diaAtendimento.getProfissional().getUnidadeTempo();
        if (diferenca.getMinuteOfDay() == 0) {
            throw new Exception("Unidade de tempo == 0!");
        }

        DateTime dt = new DateTime(diaAtendimento.getProfissional().getHoraInicio());
        List<HorarioAtendimento> horarioList = new ArrayList<>();
        while (dt.getHourOfDay() < diaAtendimento.getProfissional().getHoraFim().getHourOfDay()) {
            HorarioAtendimento ha = new HorarioAtendimento();
            ha.setDiaAtendimento(diaAtendimento);
            ha.setHoraInicio(dt);

            dt = dt.plusMinutes(diferenca.getMinuteOfDay());

            ha.setHoraFim(dt);

            horarioList.add(ha);
        }
        return horarioList;
    }

    @Override
    public void updateList(List<HorarioAtendimento> horarioList) throws Exception {
        executor.execute(conn -> {
            for (HorarioAtendimento h : horarioList) {
                dao.update(conn, h);
            }
            return Void.TYPE;
        });

    }

    @Override
    public Map<String, String> validateForCreate(Map<String, Object> fields) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, String> validateForUpdate(Map<String, Object> fields) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<HorarioAtendimento> readByCriteriaCliente(Map<String, Object> criteria, Integer offset) throws Exception {
        return executor.execute(conn -> ((HorarioAtendimentoDAO) dao).readByCriteriaCliente(conn, criteria, offset));
    }

}
