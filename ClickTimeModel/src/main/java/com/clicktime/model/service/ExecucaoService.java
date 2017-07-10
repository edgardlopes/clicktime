package com.clicktime.model.service;

import com.clicktime.model.ConnectionManager;
import com.clicktime.model.ErrorMessage;
import com.clicktime.model.ServiceLocator;
import com.clicktime.model.base.service.BaseExecucaoService;
import com.clicktime.model.criteria.ExecucaoCriteria;
import com.clicktime.model.dao.ExecucaoDAO;
import com.clicktime.model.entity.Execucao;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.fields.ExecucaoServicoFields;
import com.clicktime.model.service.calendario.CalendarioService;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public class ExecucaoService implements BaseExecucaoService {

    @Override
    public void create(Execucao entity) throws Exception {
        ExecucaoDAO dao = new ExecucaoDAO();
        Connection connection = ConnectionManager.getInstance().getConnection();
        try {
            dao.create(connection, entity);
            connection.commit();

        } catch (Exception ex) {
            connection.rollback();
            ex.printStackTrace();
            throw ex;
        } finally {
            connection.close();
        }

        updateUnidadeTempo(entity.getProfissional());
    }

    @Override
    public Execucao readById(Long id) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();
        Execucao execucao = null;
        try {
            ExecucaoDAO dao = new ExecucaoDAO();
            execucao = dao.readById(connection, id);

            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.close();
        }

        return execucao;
    }

    @Override
    public List<Execucao> readByCriteria(Map<String, Object> criteria, Integer offset) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();
        ExecucaoDAO dao = new ExecucaoDAO();
        List<Execucao> execucaoList = null;
        try {
            execucaoList = dao.readByCriteria(connection, criteria, offset);
            connection.commit();
        } catch (Exception ex) {
            connection.rollback();
            ex.printStackTrace();
            throw ex;
        } finally {
            connection.close();
        }

        return execucaoList;
    }

    @Override
    public void update(Execucao entity) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();

        try {
            new ExecucaoDAO().update(connection, entity);
            connection.commit();
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
    public DateTime calcularUnidadeTempo(List<Execucao> execucaoList) throws Exception {

        DateTime unidade = null;
        if (execucaoList.size() != 0) {
            Profissional p = execucaoList.get(0).getProfissional();
            DateTime hf = p.getHoraFim();
            DateTime hi = p.getHoraInicio();
            DateTime inter = hf.minusMinutes(hi.getMinuteOfDay());

            if (execucaoList.size() == 1) {
                unidade = execucaoList.get(0).getDuracao();
            } else {
                int mdcMinutos = 0;

                int d1 = execucaoList.get(0).getDuracao().getMinuteOfDay();
                int d2 = execucaoList.get(1).getDuracao().getMinuteOfDay();

                mdcMinutos = MDC(d2, d1);

                for (int i = 2; i < execucaoList.size(); i++) {
                    mdcMinutos = MDC(mdcMinutos, execucaoList.get(i).getDuracao().getMinuteOfDay());
                }

                unidade = new DateTime();
                if (mdcMinutos > 60) {
                    unidade = unidade.withMillisOfDay(mdcMinutos * 60 * 1000);
                } else {
                    unidade = unidade.withMillisOfDay(mdcMinutos * 60 * 1000);
                }
            }
        }

        return unidade;
    }

    private int MDC(int a, int b) {
        int resto;

        while (b != 0) {
            resto = a % b;
            a = b;
            b = resto;
        }

        return a;
    }

    public void updateUnidadeTempo(Profissional profissional) throws Exception {
        List<Execucao> execucaoList = readByProfissional(profissional);

        Connection connection = ConnectionManager.getInstance().getConnection();

        try {
            ExecucaoDAO dao = new ExecucaoDAO();

            DateTime duracao = calcularUnidadeTempo(execucaoList);

            dao.updateUnidadeTempo(duracao, profissional.getId(), connection);

            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.close();
        }
    }

    @Override
    public void delete(Long id, Profissional profissional) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();
        ExecucaoDAO dao = new ExecucaoDAO();
        try {
            dao.delete(connection, id);
            connection.commit();

        } catch (Exception ex) {
            connection.rollback();
            ex.printStackTrace();
            throw ex;
        } finally {
            connection.close();
        }

        updateUnidadeTempo(profissional);
    }

    @Override
    public Map<String, String> validateForCreate(Map<String, Object> fields) throws Exception {
        Map<String, String> errors = new HashMap<String, String>();

        String categoriaFKStr = (String) fields.get(ExecucaoServicoFields.CATEGORIA_FK);
        Long categoriaFk = -1L;
        try {
            categoriaFk = Long.parseLong(categoriaFKStr);
        } catch (NumberFormatException numberFormatException) {
            errors.put(ExecucaoServicoFields.CATEGORIA_FK, ErrorMessage.DEVE_SER_NUMERO);
        }
        if (categoriaFk.equals(-1L)) {
            errors.put(ExecucaoServicoFields.CATEGORIA_FK, ErrorMessage.NOT_NULL);
        }

        String servicoFKStr = (String) fields.get(ExecucaoServicoFields.SERVICO_FK);
        Long servicoFk = -1L;
        try {
            servicoFk = Long.parseLong(servicoFKStr);
        } catch (NumberFormatException numberFormatException) {
            errors.put(ExecucaoServicoFields.SERVICO_FK, ErrorMessage.DEVE_SER_NUMERO);
        }
        if (servicoFk.equals(-1L)) {
            errors.put(ExecucaoServicoFields.SERVICO_FK, ErrorMessage.NOT_NULL);
        }

        String valorStr = (String) fields.get(ExecucaoServicoFields.VALOR);
        if (valorStr == null || valorStr.isEmpty()) {
            errors.put(ExecucaoServicoFields.VALOR, ErrorMessage.NOT_NULL);
        } else {
            valorStr = valorStr.replace(",", ".");
            try {
                Float valor = Float.parseFloat(valorStr);
            } catch (NumberFormatException numberFormatException) {
                errors.put(ExecucaoServicoFields.VALOR, ErrorMessage.DEVE_SER_NUMERO);
            }
        }

        String descricao = (String) fields.get(ExecucaoServicoFields.DESCRICAO);
        if (descricao == null || descricao.isEmpty()) {
            errors.put(ExecucaoServicoFields.DESCRICAO, ErrorMessage.NOT_NULL);
        }

        String duracaoStr = (String) fields.get(ExecucaoServicoFields.DURACAO);
        if (duracaoStr == null || duracaoStr.isEmpty()) {
            errors.put(ExecucaoServicoFields.DURACAO, ErrorMessage.NOT_NULL);
            fields.remove(ExecucaoServicoFields.DURACAO);
        } else {
            DateTime duracao = null;
            try {
                duracao = CalendarioService.parseStringToDateTime(duracaoStr, "HH:mm");
            } catch (Exception exception) {
                errors.put(ExecucaoServicoFields.DURACAO, ErrorMessage.HORA_INVALIDA);
                fields.remove(ExecucaoServicoFields.DURACAO);
            }

            if (duracao != null) {
                Profissional profissional = (Profissional) fields.get(ExecucaoServicoFields.PROFISSIONAL);
                DateTime inicio = profissional.getHoraInicio();
                DateTime fim = profissional.getHoraFim();

                int diff = fim.getMinuteOfDay() - inicio.getMinuteOfDay();
                if (duracao.getMinuteOfDay() > diff) {
                    errors.put(ExecucaoServicoFields.DURACAO, ErrorMessage.DURACAO_MAIOR_DIA_ATENDIMENTO);
                    fields.remove(ExecucaoServicoFields.DURACAO);
                } else if (duracao.getMinuteOfDay() % 15 != 0) {
                    errors.put(ExecucaoServicoFields.DURACAO, ErrorMessage.DURACAO_INCORRETA);
                    fields.remove(ExecucaoServicoFields.DURACAO);
                }
            }
        }

        return errors;
    }

    @Override
    public Map<String, String> validateForUpdate(Map<String, Object> fields) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Execucao> readByProfissional(Profissional profissional) throws Exception {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put(ExecucaoCriteria.PROFISSIONAL_FK_EQ, profissional.getId());
        return readByCriteria(criteria, null);
    }

}
