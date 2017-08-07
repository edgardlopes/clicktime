package com.clicktime.model.service;

import com.clicktime.model.ConnectionManager;
import com.clicktime.model.ErrorMessage;
import com.clicktime.model.ServiceLocator;
import com.clicktime.model.base.BaseDAO;
import com.clicktime.model.base.service.BaseProfissionalService;
import com.clicktime.model.criteria.ProfissionalCriteria;
import com.clicktime.model.dao.ProfissionalDAO;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.fields.ProfissionalFields;
import com.clicktime.model.fields.UsuarioFields;
import com.clicktime.model.service.calendario.CalendarioService;
import com.clicktime.model.util.StringUtils;
import com.clicktime.model.util.TimeUtils;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public class ProfissionalService extends BaseProfissionalService {

    public ProfissionalService() {
        super(new ProfissionalDAO());
    }

    @Override
    public Map<String, String> validateForCreate(Map<String, Object> fields) throws Exception {
        Map<String, String> errors = new HashMap<>();
        errors = ServiceLocator.getUsuarioService().validateForCreate(fields);

        String descricao = (String) fields.get(ProfissionalFields.DESCRICAO);
        if (StringUtils.isBlank(descricao)) {
            errors.put(ProfissionalFields.DESCRICAO, ErrorMessage.NOT_NULL);
        }

        String horaInicio = (String) fields.get(ProfissionalFields.HORA_INICIO);
        DateTime hi = null;
        if (StringUtils.isBlank(horaInicio)) {
            errors.put(ProfissionalFields.HORA_INICIO, ErrorMessage.NOT_NULL);
            fields.remove(ProfissionalFields.HORA_INICIO);
        } else if (!TimeUtils.isValid(horaInicio)) {
            errors.put(ProfissionalFields.HORA_INICIO, ErrorMessage.HORA_INVALIDA);
            fields.remove(ProfissionalFields.HORA_INICIO);
        }

        String horaFim = (String) fields.get(ProfissionalFields.HORA_FIM);
        DateTime hf = null;
        if (StringUtils.isBlank(horaFim)) {
            errors.put(ProfissionalFields.HORA_FIM, ErrorMessage.NOT_NULL);
            fields.remove(ProfissionalFields.HORA_FIM);
        } else if (!TimeUtils.isValid(horaFim)) {
            errors.put(ProfissionalFields.HORA_FIM, ErrorMessage.HORA_INVALIDA);
            fields.remove(ProfissionalFields.HORA_FIM);
        }

        if (hi != null && hi.getMinuteOfDay() % 30 != 0) {
            errors.put(ProfissionalFields.HORA_INICIO, ErrorMessage.HORA_INCORRETA);
            fields.remove(ProfissionalFields.HORA_INICIO);
            hf = null;
        }
        if (hf != null && hf.getMinuteOfDay() % 30 != 0) {
            errors.put(ProfissionalFields.HORA_FIM, ErrorMessage.HORA_INCORRETA);
            fields.remove(ProfissionalFields.HORA_FIM);
            hf = null;
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
