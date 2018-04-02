package com.clicktime.web.controller;

import com.clicktime.model.base.service.BaseDiaAtendimentoService;
import com.clicktime.model.base.service.BaseHorarioAtendimentoService;
import com.clicktime.model.criteria.HorarioAtendimentoCriteria;
import com.clicktime.model.entity.DiaAtendimento;
import com.clicktime.model.entity.HorarioAtendimento;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.service.HorarioAtendimentoService;
import com.clicktime.model.service.calendario.CalendarioService;
import static com.clicktime.web.interceptor.SessionUtils.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HorarioAtendimentoController {
    
    @Autowired
    private BaseDiaAtendimentoService diaAtendimentoService;

    @Autowired
    private BaseHorarioAtendimentoService horarioAtendimentoService;

    @Autowired
    private CalendarioService calendarioService;

    @PostMapping( "/agenda/{year}/{month}/{day}/bloquearHorarios")
    public String block(HttpSession session, @PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day, Long[] horario,
            String aplicar, Boolean aplicarSabado, Boolean aplicarDomingo, Boolean aplicarFeriados, String jsonFeriados) throws Exception {
        DateTime dt = new DateTime(year, month, day, 0, 0);
        if (horario != null) {
            horarioAtendimentoService.updateList(horario, HorarioAtendimentoService.BLOCK_HORARIO_ATENDIMENTO);
        }
        //replicarHorarios(dt, session, aplicar, aplicarDomingo, aplicarSabado, aplicarFeriados, jsonFeriados);

        return "redirect:/agenda/" + dt.toString("yyyy/MM/dd/");
    }

    @PostMapping( "/agenda/{year}/{month}/{day}/liberarHorarios")
    public String release(HttpSession session, @PathVariable Integer year, @PathVariable Integer month,
            @PathVariable Integer day, Long[] horario, String aplicar,
            Boolean aplicarSabado, Boolean aplicarDomingo, Boolean aplicarFeriados, String jsonFeriados) throws Exception {

        DateTime dt = new DateTime(year, month, day, 0, 0);
        if (horario != null) {
            horarioAtendimentoService.updateList(horario, HorarioAtendimentoService.RELEASE_HORARIO_ATENDIMENTO);
        }
        //verificar..
        //replicarHorarios(dt, session, aplicar, aplicarDomingo, aplicarSabado, aplicarFeriados, jsonFeriados);

        return "redirect:/agenda/" + dt.toString("yyyy/MM/dd/");
    }

    @GetMapping("/agenda/{year}/{month}/{day}/cadastrarHorarios")
    public String create(@PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day, HttpSession session) throws Exception {
        Profissional p = (Profissional) getLoggedUser(session);
        DiaAtendimento da = new DiaAtendimento();

        DateTime dt = new DateTime(year, month, day, 1, 1);

        da.setProfissional(p);
        da.setData(dt);
        diaAtendimentoService.readOrCreate(da);
        horarioAtendimentoService.generateHorarioAtendimentoList(da);

        return "redirect:/agenda/" + dt.toString("yyyy/MM/dd/");
    }

    @PostMapping( "/agenda/{year}/{month}/{day}/replicarHorarios")
    public String replicar(HttpSession session, @PathVariable Integer year, @PathVariable Integer month,
            @PathVariable Integer day, Long diaAtendimentoID, String aplicar,
            Boolean aplicarSabado, Boolean aplicarDomingo, Boolean aplicarFeriados, String jsonFeriados, Boolean bloquearReservados) throws Exception {
        DateTime dt = new DateTime(year, month, day, 1, 1);
        DiaAtendimento diaAtendimento = diaAtendimentoService.readById(diaAtendimentoID);
        replicarHorarios(diaAtendimento, session, aplicar, aplicarDomingo, aplicarSabado, aplicarFeriados, jsonFeriados, bloquearReservados);
        return "redirect:/agenda/" + dt.toString("yyyy/MM/dd/");
    }

    public void gerarDias(DiaAtendimento dia,
            String aplicar, Boolean aplicarDomingo, Boolean aplicarSabado, Boolean aplicarFeriados,
            String jsonFeriados, HttpSession session, List<HorarioAtendimento> horarioList, Boolean bloquarReservados) {

        Map<String, String> feriadoMap = calendarioService.parseJsonToFeriadoMap(jsonFeriados);

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
                diaAtendimento.setProfissional((Profissional) getLoggedUser(session));
                try {
                    diaAtendimentoService.readOrCreate(diaAtendimento);
                    if (!diaAtendimento.getId().equals(dia.getId())) {
                        Map<String, Object> criteria = new HashMap<>();
                        criteria.put(HorarioAtendimentoCriteria.DIA_ATENDIMENTO_FK_EQ, diaAtendimento.getId());
                        List<HorarioAtendimento> listaAux = horarioAtendimentoService.readByCriteriaCliente(criteria, null);
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
                            horarioAtendimentoService.createHorarioAtendimentoList(horarioList);
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

                            horarioAtendimentoService.updateList(listaAux);
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
        diaAtendimento.setProfissional((Profissional) getLoggedUser(session));

        Map<String, Object> criteria = new HashMap<>();
        criteria.put(HorarioAtendimentoCriteria.DIA_ATENDIMENTO_FK_EQ, diaAtendimento.getId());
        List<HorarioAtendimento> horarioList = horarioAtendimentoService.readByCriteriaCliente(criteria, null);

        //passa o dia do profissional, nao a data apenas
        gerarDias(diaAtendimento, aplicar, aplicarDomingo, aplicarSabado, aplicarFeriados, jsonFeriados, session, horarioList, bloquearReservados);
    }
}
