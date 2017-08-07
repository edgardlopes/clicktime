package com.clicktime.model.service;

import com.clicktime.model.ErrorMessage;
import com.clicktime.model.base.service.BaseExecucaoService;
import com.clicktime.model.criteria.ExecucaoCriteria;
import com.clicktime.model.dao.ExecucaoDAO;
import com.clicktime.model.entity.Execucao;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.fields.ExecucaoServicoFields;
import com.clicktime.model.service.calendario.CalendarioService;
import com.clicktime.model.util.MathUtils;
import com.clicktime.model.util.NumberUtils;
import com.clicktime.model.util.StringUtils;
import com.clicktime.model.util.TimeUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public class ExecucaoService extends BaseExecucaoService {

    public ExecucaoService() {
        super(new ExecucaoDAO());
    }

    @Override
    public void create(Execucao entity) throws Exception {
        super.create(entity);
        updateUnidadeTempo(entity.getProfissional());
    }

    @Override
    public DateTime calcularUnidadeTempo(List<Execucao> execucaoList) throws Exception {

        if (execucaoList.isEmpty()) {
            return null;
        }

        if (execucaoList.size() == 1) {
            return execucaoList.get(0).getDuracao();
        }

        int d1 = execucaoList.get(0).getDuracao().getMinuteOfDay();
        int d2 = execucaoList.get(1).getDuracao().getMinuteOfDay();

        //usar reduce
//        int mdcMinutos = MDC(d2, d1);
//        for (int i = 2; i < execucaoList.size(); i++) {
//            mdcMinutos = MDC(mdcMinutos, execucaoList.get(i).getDuracao().getMinuteOfDay());
//        }
        final int mdcMinutos = MathUtils.mdc(d2, d1);
        int mdc = execucaoList.stream().mapToInt(exec -> exec.getDuracao().getMinuteOfDay())
                .reduce(mdcMinutos, (mdcAcumulado, minuteOfDay) -> MathUtils.mdc(mdcAcumulado, minuteOfDay));

        return new DateTime().withMillisOfDay(mdc * 60 * 1000);
    }

    private void updateUnidadeTempo(Profissional profissional) throws Exception {

        List<Execucao> execucaoList = readByProfissional(profissional);

        executor.execute(conn -> {
            DateTime duracao = calcularUnidadeTempo(execucaoList);
            ExecucaoDAO dao = new ExecucaoDAO();
            dao.updateUnidadeTempo(duracao, profissional.getId(), conn);
            return Void.TYPE;
        });

    }

    @Override
    public void delete(Long id, Profissional profissional) throws Exception {
        executor.execute(conn -> {
            new ExecucaoDAO().delete(conn, id);
            return Void.TYPE;
        });
     
        updateUnidadeTempo(profissional);
    }

    @Override
    public Map<String, String> validateForCreate(Map<String, Object> fields) throws Exception {
        Map<String, String> errors = new HashMap<>();

        String categoriaFKStr = (String) fields.get(ExecucaoServicoFields.CATEGORIA_FK);
        if (!NumberUtils.isLongValid(categoriaFKStr)) {
            errors.put(ExecucaoServicoFields.CATEGORIA_FK, ErrorMessage.DEVE_SER_NUMERO);
        } else if (Long.valueOf(categoriaFKStr).equals(-1)) {
            errors.put(ExecucaoServicoFields.CATEGORIA_FK, ErrorMessage.NOT_NULL);
        }

        String servicoFKStr = (String) fields.get(ExecucaoServicoFields.SERVICO_FK);
        if (!NumberUtils.isLongValid(servicoFKStr)) {
            errors.put(ExecucaoServicoFields.SERVICO_FK, ErrorMessage.DEVE_SER_NUMERO);
        } else if (Long.valueOf(servicoFKStr).equals(-1)) {
            errors.put(ExecucaoServicoFields.SERVICO_FK, ErrorMessage.NOT_NULL);
        }

        String valorStr = (String) fields.get(ExecucaoServicoFields.VALOR);
        if (StringUtils.isBlank(valorStr)) {
            errors.put(ExecucaoServicoFields.VALOR, ErrorMessage.NOT_NULL);
        } else if (!NumberUtils.isFloatValid(valorStr.replace(",", "."))) {
            errors.put(ExecucaoServicoFields.VALOR, ErrorMessage.DEVE_SER_NUMERO);
        }

        String descricao = (String) fields.get(ExecucaoServicoFields.DESCRICAO);
        if (StringUtils.isBlank(descricao)) {
            errors.put(ExecucaoServicoFields.DESCRICAO, ErrorMessage.NOT_NULL);
        }

        String duracaoStr = (String) fields.get(ExecucaoServicoFields.DURACAO);
        if (StringUtils.isBlank(duracaoStr)) {
            errors.put(ExecucaoServicoFields.DURACAO, ErrorMessage.NOT_NULL);
            fields.remove(ExecucaoServicoFields.DURACAO);
        } else {
            DateTime duracao = null;
            if (!TimeUtils.isValid(duracaoStr)) {
                errors.put(ExecucaoServicoFields.DURACAO, ErrorMessage.HORA_INVALIDA);
                fields.remove(ExecucaoServicoFields.DURACAO);
            } else {
                duracao = CalendarioService.parseStringToDateTime(duracaoStr, "HH:mm");
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
