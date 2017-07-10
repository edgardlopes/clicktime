package com.clicktime.web.controller;

import com.clicktime.model.ServiceLocator;
import com.clicktime.model.criteria.HorarioAtendimentoCriteria;
import com.clicktime.model.entity.DiaAtendimento;
import com.clicktime.model.entity.HorarioAtendimento;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.service.HorarioAtendimentoService;
import com.clicktime.model.service.calendario.CalendarioService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HorarioAtendimentoController {

    @RequestMapping(value = "/agenda/{year}/{month}/{day}/bloquearHorarios", method = RequestMethod.POST)
    public String block(HttpSession session, @PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day, Long[] horario,
            String aplicar, Boolean aplicarSabado, Boolean aplicarDomingo, Boolean aplicarFeriados, String jsonFeriados) throws Exception {
        DateTime dt = new DateTime(year, month, day, 0, 0);
        if (horario != null) {
            ServiceLocator.getHorarioAtendimentoService().updateList(horario, HorarioAtendimentoService.BLOCK_HORARIO_ATENDIMENTO);
        }
        //replicarHorarios(dt, session, aplicar, aplicarDomingo, aplicarSabado, aplicarFeriados, jsonFeriados);

        return "redirect:/agenda/" + dt.toString("yyyy/MM/dd/");
    }

    @RequestMapping(value = "/agenda/{year}/{month}/{day}/liberarHorarios", method = RequestMethod.POST)
    public String release(HttpSession session, @PathVariable Integer year, @PathVariable Integer month,
            @PathVariable Integer day, Long[] horario, String aplicar,
            Boolean aplicarSabado, Boolean aplicarDomingo, Boolean aplicarFeriados, String jsonFeriados) throws Exception {

        DateTime dt = new DateTime(year, month, day, 0, 0);
        if (horario != null) {
            ServiceLocator.getHorarioAtendimentoService().updateList(horario, HorarioAtendimentoService.RELEASE_HORARIO_ATENDIMENTO);
        }
        //verificar..
        //replicarHorarios(dt, session, aplicar, aplicarDomingo, aplicarSabado, aplicarFeriados, jsonFeriados);

        return "redirect:/agenda/" + dt.toString("yyyy/MM/dd/");
    }

    @RequestMapping(value = "/agenda/{year}/{month}/{day}/cadastrarHorarios")
    public String create(@PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day, HttpSession session) throws Exception {
        Profissional p = (Profissional) session.getAttribute("usuarioLogado");
        DiaAtendimento da = new DiaAtendimento();

        DateTime dt = new DateTime(year, month, day, 1, 1);

        da.setProfissional(p);
        da.setData(dt);
        ServiceLocator.getDiaAtendimentoService().readOrCreate(da);
        ServiceLocator.getHorarioAtendimentoService().generateHorarioAtendimentoList(da);

        return "redirect:/agenda/" + dt.toString("yyyy/MM/dd/");
    }

    @RequestMapping(value = "/agenda/{year}/{month}/{day}/replicarHorarios", method = RequestMethod.POST)
    public String replicar(HttpSession session, @PathVariable Integer year, @PathVariable Integer month,
            @PathVariable Integer day, Long diaAtendimentoID, String aplicar,
            Boolean aplicarSabado, Boolean aplicarDomingo, Boolean aplicarFeriados, String jsonFeriados, Boolean bloquearReservados) throws Exception {
        DateTime dt = new DateTime(year, month, day, 1, 1);
        DiaAtendimento diaAtendimento = ServiceLocator.getDiaAtendimentoService().readById(diaAtendimentoID);
        replicarHorarios(diaAtendimento, session, aplicar, aplicarDomingo, aplicarSabado, aplicarFeriados, jsonFeriados, bloquearReservados);
        return "redirect:/agenda/" + dt.toString("yyyy/MM/dd/");
    }

    public void gerarDias(DiaAtendimento dia,
            String aplicar, Boolean aplicarDomingo, Boolean aplicarSabado, Boolean aplicarFeriados,
            String jsonFeriados, HttpSession session, List<HorarioAtendimento> horarioList, Boolean bloquarReservados) {

        Map<String, String> feriadoMap = ServiceLocator.getCalendarioService(dia.getData().getYear(), dia.getData().getMonthOfYear(), null, false).parseJsonToFeriadoMap(jsonFeriados);

        if (aplicarDomingo == null) {
            aplicarDomingo = false;
        }
        if (aplicarSabado == null) {
            aplicarSabado = false;
        }
        if (aplicarFeriados == null) {
            aplicarFeriados = false;
        }

        DateTime comeco = new DateTime(dia.getData().getYear(), dia.getData().getMonthOfYear(), 1, 1, 1);
        DateTime fim = dia.getData().dayOfMonth().withMaximumValue();

        if (aplicar.equals("semana")) {
            comeco = CalendarioService.getFirstDayOfWeek(dia.getData());
            fim = CalendarioService.getLastDayOfWeek(dia.getData());
        }

        while (comeco.getMillis() <= fim.getMillis()) {
            if ((aplicarDomingo || comeco.getDayOfWeek() != 7)
                    && (aplicarSabado || comeco.getDayOfWeek() != 6)
                    && (aplicarFeriados || (feriadoMap.get(comeco.toString("yyyy-MM-dd")) == null))) {

                DiaAtendimento diaAtendimento = new DiaAtendimento();
                diaAtendimento.setData(comeco);
                diaAtendimento.setProfissional((Profissional) session.getAttribute("usuarioLogado"));
                try {
                    ServiceLocator.getDiaAtendimentoService().readOrCreate(diaAtendimento);
                    if (!diaAtendimento.getId().equals(dia.getId())) {
                        Map<String, Object> criteria = new HashMap<>();
                        criteria.put(HorarioAtendimentoCriteria.DIA_ATENDIMENTO_FK_EQ, diaAtendimento.getId());
                        List<HorarioAtendimento> listaAux = ServiceLocator.getHorarioAtendimentoService().readByCriteriaCliente(criteria, null);
                        if (listaAux.size() == 0) {
                            for (HorarioAtendimento horario : horarioList) {
                                horario.setDiaAtendimento(diaAtendimento);
                                if (horario.getStatus().toLowerCase().equals(HorarioAtendimento.HORARIO_RESERVADO.toLowerCase())) {
                                    if (bloquarReservados) {
                                        horario.setStatus(HorarioAtendimento.HORARIO_BLOQUEADO);
                                    } else {
                                        horario.setStatus(HorarioAtendimento.HORARIO_LIVRE);
                                    }
                                }
                                if (horario.getStatus().equals(HorarioAtendimento.AGUARDANDO_CLIENTE_ACEITAR)) {
                                    horario.setStatus(HorarioAtendimento.HORARIO_LIVRE);
                                }
                            }
                            ServiceLocator.getHorarioAtendimentoService().createHorarioAtendimentoList(horarioList);
                        } else {
                            for (int i = 0; i < listaAux.size(); i++) {
                                if (horarioList.get(i).getStatus().equals(HorarioAtendimento.HORARIO_RESERVADO)) {
                                    //caso o horario da lista a ser copiada esteja reservado...
                                    if (bloquarReservados) {
                                        listaAux.get(i).setStatus(HorarioAtendimento.HORARIO_BLOQUEADO);
                                    } else {
                                        listaAux.get(i).setStatus(HorarioAtendimento.HORARIO_LIVRE);
                                    }
                                } else if (horarioList.get(i).getStatus().equals(HorarioAtendimento.AGUARDANDO_CLIENTE_ACEITAR)) {
                                    //caso o horario da lista a ser copiada esteja aguaradando resposta do cliente...
                                    listaAux.get(i).setStatus(HorarioAtendimento.HORARIO_LIVRE);
                                } else {
                                    if (!listaAux.get(i).getStatus().equals(HorarioAtendimento.HORARIO_RESERVADO)) {
                                        if (!listaAux.get(i).getStatus().equals(HorarioAtendimento.AGUARDANDO_CLIENTE_ACEITAR)) {
                                            listaAux.get(i).setStatus(horarioList.get(i).getStatus());
                                        }
                                    }
                                }
                            }

                            ServiceLocator.getHorarioAtendimentoService().updateList(listaAux);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            comeco = comeco.plusDays(1);
        }

    }

    public void replicarHorarios(DiaAtendimento diaAtendimento, HttpSession session, String aplicar,
            Boolean aplicarDomingo, Boolean aplicarSabado, Boolean aplicarFeriados, String jsonFeriados, Boolean bloquearReservados) throws Exception {
        diaAtendimento.setProfissional((Profissional) session.getAttribute("usuarioLogado"));

        Map<String, Object> criteria = new HashMap<>();
        criteria.put(HorarioAtendimentoCriteria.DIA_ATENDIMENTO_FK_EQ, diaAtendimento.getId());
        List<HorarioAtendimento> horarioList = ServiceLocator.getHorarioAtendimentoService().readByCriteriaCliente(criteria, null);

        //passa o dia do profissional, nao a data apenas
        gerarDias(diaAtendimento, aplicar, aplicarDomingo, aplicarSabado, aplicarFeriados, jsonFeriados, session, horarioList, bloquearReservados);
    }
}
