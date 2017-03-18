package com.clicktime.model.service;

import com.clicktime.model.ConnectionManager;
import com.clicktime.model.base.service.BaseHorarioAtendimentoService;
import com.clicktime.model.criteria.HorarioAtendimentoCriteria;
import com.clicktime.model.dao.HorarioAtendimentoDAO;
import com.clicktime.model.entity.DiaAtendimento;
import com.clicktime.model.entity.Execucao;
import com.clicktime.model.entity.HorarioAtendimento;
import com.clicktime.model.entity.Profissional;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public class HorarioAtendimentoService implements BaseHorarioAtendimentoService {

    public static final String BLOCK_HORARIO_ATENDIMENTO = "B";
    public static final String RELEASE_HORARIO_ATENDIMENTO = "L";
    public static final String KEY_HORARIO_ATENDIMENTO = "horarioAtendimento";
    public static final String KEY_ID_LIST = "IDList";

    @Override
    public void create(HorarioAtendimento entity) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HorarioAtendimento readById(Long id) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();

        HorarioAtendimento horario = null;
        try {
            HorarioAtendimentoDAO dao = new HorarioAtendimentoDAO();
            horario = dao.readById(connection, id);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            connection.close();
        }

        return horario;
    }

    @Override
    public List<HorarioAtendimento> readByCriteria(Map<String, Object> criteria, Integer offset) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();

        List<HorarioAtendimento> horarioList = null;
        try {
            HorarioAtendimentoDAO dao = new HorarioAtendimentoDAO();
            horarioList = dao.readByCriteria(connection, criteria, offset);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            connection.close();
        }

        return horarioList;
    }

    @Override
    public void update(HorarioAtendimento entity) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();

        try {
            HorarioAtendimentoDAO dao = new HorarioAtendimentoDAO();
            dao.update(connection, entity);
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.close();
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void generateHorarioAtendimentoList(DiaAtendimento da) throws Exception {
        //Map<String, Object> criteria = new HashMap<String, Object>();
        //criteria.put(ExecucaoCriteria.PROFISSIONAL_FK_EQ, da.getProfissional().getId());

        //BaseExecucaoService execucaoService = ServiceLocator.getExecucaoService();
        //List<Execucao> execucaoList = execucaoService.readByCriteria(criteria, null);
        //DateTime unidade = execucaoService.calcularUnidadeTempo(execucaoList);

        List<HorarioAtendimento> horarioList = new ArrayList<>();

//        if (unidade != null) {
            horarioList = gerarHorarioList(da);
//        }

        createHorarioAtendimentoList(horarioList);

    }

    @Override
    public List<HorarioAtendimento> read(DiaAtendimento da) throws Exception {
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put(HorarioAtendimentoCriteria.DIA_ATENDIMENTO_FK_EQ, da.getId());

        List<HorarioAtendimento> horarioList = readByCriteriaCliente(criteria, null);

        return horarioList;
    }

    @Override
    public void createHorarioAtendimentoList(List<HorarioAtendimento> horarioAtendimentoList) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();
        HorarioAtendimentoDAO dao = new HorarioAtendimentoDAO();

        try {
            for (HorarioAtendimento aux : horarioAtendimentoList) {
                dao.create(connection, aux);
            }
            connection.commit();
        } catch (Exception ex) {
            connection.rollback();
            ex.printStackTrace();
            throw ex;
        } finally {
            connection.close();
        }

    }

    @Override
    public void updateList(Long[] id, String action) throws Exception {
        if (action.equals(BLOCK_HORARIO_ATENDIMENTO) || action.equals(RELEASE_HORARIO_ATENDIMENTO)) {
            Connection connection = ConnectionManager.getInstance().getConnection();
            try {
                HorarioAtendimentoDAO dao = new HorarioAtendimentoDAO();
                for (Long aux : id) {
                    HorarioAtendimento h = new HorarioAtendimento();
                    h.setId(aux);
                    h.setStatus(action);

                    dao.update(connection, h);
                }

                connection.commit();
            } catch (Exception ex) {
                connection.rollback();
                ex.printStackTrace();
                throw ex;

            } finally {
                connection.close();
            }
        }
    }

    @Override
    public List<Map<String, Object>> agruparHorarios(Execucao execucao, List<HorarioAtendimento> horarioList, Profissional profissional) {
        DateTime duracao = execucao.getDuracao();

        List<Map<String, Object>> informations = new ArrayList<Map<String, Object>>();
        try {
            int qtdeHorarios = duracao.getMinuteOfDay() / profissional.getUnidadeTempo().getMinuteOfDay();
            informations = new ArrayList<Map<String, Object>>();

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

        List<HorarioAtendimento> horarioList = new ArrayList<>();

        DateTime diferenca = diaAtendimento.getProfissional().getUnidadeTempo();
        if (diferenca.getMinuteOfDay() != 0) {
            DateTime dt = new DateTime(diaAtendimento.getProfissional().getHoraInicio());
            while (dt.getHourOfDay() < diaAtendimento.getProfissional().getHoraFim().getHourOfDay()) {
                HorarioAtendimento ha = new HorarioAtendimento();
                ha.setDiaAtendimento(diaAtendimento);
                ha.setHoraInicio(dt);

                dt = dt.plusMinutes(diferenca.getMinuteOfDay());

                ha.setHoraFim(dt);

                horarioList.add(ha);
            }

        } else {
            throw new Exception("Unidade de tempo == 0!");
        }

        return horarioList;
    }

    @Override
    public void updateList(List<HorarioAtendimento> horarioList) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();
        try {
            for (HorarioAtendimento h : horarioList) {
                new HorarioAtendimentoDAO().update(connection, h);
            }

            connection.commit();
        } catch (Exception ex) {
            connection.rollback();
            ex.printStackTrace();
            throw ex;

        } finally {
            connection.close();
        }
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
        Connection connection = ConnectionManager.getInstance().getConnection();

        List<HorarioAtendimento> horarioList = null;
        try {
            HorarioAtendimentoDAO dao = new HorarioAtendimentoDAO();
            horarioList = dao.readByCriteriaCliente(connection, criteria, offset);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            connection.close();
        }

        return horarioList;
    }

}
