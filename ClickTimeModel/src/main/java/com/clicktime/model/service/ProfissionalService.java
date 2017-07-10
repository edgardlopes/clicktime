package com.clicktime.model.service;

import com.clicktime.model.ConnectionManager;
import com.clicktime.model.ErrorMessage;
import com.clicktime.model.ServiceLocator;
import com.clicktime.model.base.service.BaseProfissionalService;
import com.clicktime.model.criteria.ProfissionalCriteria;
import com.clicktime.model.dao.ProfissionalDAO;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.fields.ProfissionalFields;
import com.clicktime.model.fields.UsuarioFields;
import com.clicktime.model.service.calendario.CalendarioService;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public class ProfissionalService implements BaseProfissionalService {

    @Override
    public void create(Profissional entity) throws Exception {
        ServiceLocator.getUsuarioService().create(entity);

        if (entity.getId() != null) {
            Connection connection = ConnectionManager.getInstance().getConnection();
            try {
                ProfissionalDAO dao = new ProfissionalDAO();
                dao.create(connection, entity);
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                e.printStackTrace();
                throw e;
            } finally {
                connection.close();
            }

        }
    }

    @Override
    public Profissional readById(Long id) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();

        Profissional profissional = null;
        try {
            profissional = new ProfissionalDAO().readById(connection, id);
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.close();
        }

        return profissional;
    }

    @Override
    public List<Profissional> readByCriteria(Map<String, Object> criteria, Integer offset) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();
        List<Profissional> profissionalList;
        try {
            profissionalList = new ProfissionalDAO().readByCriteria(connection, criteria, offset);
            connection.commit();
        } catch (Exception ex) {
            connection.rollback();
            ex.printStackTrace();
            throw ex;
        } finally {
            connection.close();
        }

        return profissionalList;
    }

    @Override
    public void update(Profissional entity) throws Exception {
        ServiceLocator.getUsuarioService().update(entity);

        Connection connection = ConnectionManager.getInstance().getConnection();

        try {
            new ProfissionalDAO().update(connection, entity);
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
    public Map<String, String> validateForCreate(Map<String, Object> fields) throws Exception {
        Map<String, String> errors = new HashMap<String, String>();
        errors = ServiceLocator.getUsuarioService().validateForCreate(fields);

        String descricao = (String) fields.get(ProfissionalFields.DESCRICAO);
        if (descricao == null || descricao.isEmpty()) {
            errors.put(ProfissionalFields.DESCRICAO, ErrorMessage.NOT_NULL);
        }

        String horaInicio = (String) fields.get(ProfissionalFields.HORA_INICIO);
        DateTime hi = null;
        if (horaInicio == null || horaInicio.isEmpty()) {
            errors.put(ProfissionalFields.HORA_INICIO, ErrorMessage.NOT_NULL);
            fields.remove(ProfissionalFields.HORA_INICIO);
        } else {
            try {
                hi = CalendarioService.parseStringToDateTime(horaInicio, "HH:mm");
            } catch (Exception exception) {
                errors.put(ProfissionalFields.HORA_INICIO, ErrorMessage.HORA_INVALIDA);
                fields.remove(ProfissionalFields.HORA_INICIO);
            }
        }

        String horaFim = (String) fields.get(ProfissionalFields.HORA_FIM);
        DateTime hf = null;
        if (horaFim == null || horaFim.isEmpty()) {
            errors.put(ProfissionalFields.HORA_FIM, ErrorMessage.NOT_NULL);
            fields.remove(ProfissionalFields.HORA_FIM);
        } else {
            try {
                hf = CalendarioService.parseStringToDateTime(horaFim, "HH:mm");
            } catch (Exception exception) {
                errors.put(ProfissionalFields.HORA_FIM, ErrorMessage.HORA_INVALIDA);
                fields.remove(ProfissionalFields.HORA_FIM);
            }
        }

        if (hi != null) {
            if (hi.getMinuteOfDay() % 30 != 0) {
                errors.put(ProfissionalFields.HORA_INICIO, ErrorMessage.HORA_INCORRETA);
                fields.remove(ProfissionalFields.HORA_INICIO);
                hf = null;
            }
        }
        if (hf != null) {
            if (hf.getMinuteOfDay() % 30 != 0) {
                errors.put(ProfissionalFields.HORA_FIM, ErrorMessage.HORA_INCORRETA);
                fields.remove(ProfissionalFields.HORA_FIM);
                hf = null;
            }
        }

        if (hi != null && hf != null) {
            if (hi.getMillis() > hf.getMillis()) {
                errors.put(ProfissionalFields.HORA_FIM, ErrorMessage.HORA_INICIO_MAIOR_HORA_FIM);
                errors.put(ProfissionalFields.HORA_INICIO, ErrorMessage.HORA_FIM_MENOR_HORA_INICIO);
                fields.remove(ProfissionalFields.HORA_INICIO);
                fields.remove(ProfissionalFields.HORA_FIM);

            } else {
                int inicio = hi.getMinuteOfDay();
                int fim = hf.getMinuteOfDay();

                if ((fim - inicio) < 60) {
                    errors.put(ProfissionalFields.HORA_INICIO, ErrorMessage.DIFERENCA_MINIMA);
                    fields.remove(ProfissionalFields.HORA_INICIO);
                    errors.put(ProfissionalFields.HORA_FIM, ErrorMessage.DIFERENCA_MINIMA);
                    fields.remove(ProfissionalFields.HORA_FIM);
                }
            }
        } else {

        }

        if (errors.get(ProfissionalFields.HORA_INICIO) == null) {
            fields.put(ProfissionalFields.HORA_INICIO, CalendarioService.parseStringToDateTime(horaInicio, "HH:mm"));
        }

        if (errors.get(ProfissionalFields.HORA_FIM) == null) {
            fields.put(ProfissionalFields.HORA_FIM, CalendarioService.parseStringToDateTime(horaFim, "HH:mm"));
        }

        return errors;
    }

    @Override
    public Map<String, String> validateForUpdate(Map<String, Object> fields) throws Exception {
        Map<String, String> errors = validateForCreate(fields);
        errors.remove(UsuarioFields.SENHA);
        errors.remove(UsuarioFields.SENHA_CONFIRM);

        return errors;
    }

    @Override
    public Profissional readByUserName(String userName) throws Exception {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put(ProfissionalCriteria.NOME_USUARIO_EQ, userName);
        List<Profissional> list = readByCriteria(criteria, null);
        return list.isEmpty() ? null : list.get(0);
    }

}
