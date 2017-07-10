/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clicktime.web.controller;

import com.clicktime.model.ServiceLocator;
import com.clicktime.model.criteria.DiaAtendimentoCriteria;
import com.clicktime.model.criteria.HorarioAtendimentoCriteria;
import com.clicktime.model.entity.DiaAtendimento;
import com.clicktime.model.entity.DiaAtendimentoResumo;
import com.clicktime.model.entity.Execucao;
import com.clicktime.model.entity.HorarioAtendimento;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.service.calendario.CalendarioService;
import com.clicktime.model.service.calendario.Day;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Classe responsavel pela agenda do profissional que o cliente acessa
 *
 * @author Edgard Lopes <edgard-rodrigo@hotmail.com>
 */
@Controller
public class AgendaClienteController {

    @RequestMapping(value = "/{profissionalUrl}/agenda", method = RequestMethod.GET)
    public String getAgendaProfissional(@PathVariable String profissionalUrl, Model m, Long servicoID, HttpSession session) throws Exception {
        Profissional p = ServiceLocator.getProfissionalService().readByUserName(profissionalUrl);
        m.addAttribute("profissional", p);
        m.addAttribute("year", new DateTime().getYear());
        Execucao execucao = null;
        if (servicoID != null && servicoID > 0) {
            execucao = ServiceLocator.getExecucaoService().readById(servicoID);
        } else {
            execucao = ServiceLocator.getExecucaoService().readByProfissional(p).get(0);
        }
        m.addAttribute("execucao", execucao);

        return "/agenda/months";
    }

    @RequestMapping(value = "/{profissionalUrl}/agenda/{year}/{month}", method = RequestMethod.GET)
    public String getCalendario(@PathVariable String profissionalUrl, @PathVariable Integer year,
            @PathVariable Integer month, Model m, HttpSession session, Long servicoID) throws Exception {

        String url = "/agenda/data-ultrapassada";

        DateTime now = new DateTime();
        if (year >= now.getYear() && month >= now.getMonthOfYear()) {
            Profissional p = ServiceLocator.getProfissionalService().readByUserName(profissionalUrl);
            m.addAttribute("profissional", p);
            List<Execucao> execucaoList = ServiceLocator.getExecucaoService().readByProfissional(p);
            Execucao execucao = null;
            if (servicoID != null) {
                execucao = ServiceLocator.getExecucaoService().readById(servicoID);
                m.addAttribute("execucaoSelected", execucao);
            } else {
                execucao = execucaoList.get(0);
            }

            m.addAttribute("execucaoList", execucaoList);

            CalendarioService service = ServiceLocator.getCalendarioService(year, month, p, true);

            List<List> weekList = (List<List>) service.getInformations().get(CalendarioService.DAYS_OF_MONTH);

            for (List week : weekList) {
                for (Object aux : week) {
                    Day day = (Day) aux;

                    Map<String, Object> criteria = new HashMap<>();
                    criteria.put(DiaAtendimentoCriteria.PROFISSIONAL_FK_EQ, p.getId());
                    criteria.put(DiaAtendimentoCriteria.DIA_ATENDIMENTO_EQ, day.getDay());

                    List<DiaAtendimento> diaAtendimentoList = ServiceLocator.getDiaAtendimentoService().readByCriteria(criteria, null);
                    if (diaAtendimentoList.size() > 0) {
                        DiaAtendimento dia = diaAtendimentoList.get(0);

                        criteria.clear();
                        criteria.put(HorarioAtendimentoCriteria.DIA_ATENDIMENTO_FK_EQ, dia.getId());
                        List<HorarioAtendimento> horarioList = ServiceLocator.getHorarioAtendimentoService().read(dia);

                        if (execucao != null) {
                            List<Map<String, Object>> novoHorario = ServiceLocator.getHorarioAtendimentoService().agruparHorarios(execucao, horarioList, p);

                            DiaAtendimentoResumo resumo = new DiaAtendimentoResumo();
                            resumo.setHorariosLivres(novoHorario.size());
                            day.setResumo(resumo);
                        }
                    }
                }
            }

            service.getInformations().remove(CalendarioService.DAYS_OF_MONTH);
            m.addAllAttributes(service.getInformations());
            m.addAttribute(CalendarioService.DAYS_OF_MONTH, weekList);

            url = "/agenda/calendario";
        }

        return url;
    }

    @RequestMapping(value = "/{profissionalURL}/agenda/{year}/{month}/{day}", method = RequestMethod.GET)
    public String getDay(HttpSession session, @PathVariable String profissionalURL, @PathVariable Integer year,
            @PathVariable Integer month, @PathVariable Integer day, Model m, Long servicoID) throws Exception {

        DateTime now = new DateTime();
        DateTime dataParam = new DateTime(year, month, day, 1, 1);

        Profissional p = ServiceLocator.getProfissionalService().readByUserName(profissionalURL);
        DateTime dt = new DateTime(year, month, day, 1, 1);

        Map<String, Object> criteria = new HashMap<>();
        criteria.put(DiaAtendimentoCriteria.DIA_ATENDIMENTO_EQ, dt);
        criteria.put(DiaAtendimentoCriteria.PROFISSIONAL_FK_EQ, p.getId());

        List<DiaAtendimento> diaAtendimentoList = ServiceLocator.getDiaAtendimentoService().readByCriteria(criteria, null);
        DiaAtendimento da = null;
        List<HorarioAtendimento> horarioList = new ArrayList<>();
        List<Execucao> execucaoList = new ArrayList<>();
        List<Map<String, Object>> novaLista = null;
        Execucao execucao = null;

        if (diaAtendimentoList.size() > 0) {

            if (servicoID != null) {
                execucao = ServiceLocator.getExecucaoService().readById(servicoID);
            } else {
                execucaoList = ServiceLocator.getExecucaoService().readByProfissional(p);
                execucao = execucaoList.get(0);
            }

            da = diaAtendimentoList.get(0);
            horarioList = ServiceLocator.getHorarioAtendimentoService().read(da);
            novaLista = ServiceLocator.getHorarioAtendimentoService().agruparHorarios(execucao, horarioList, p);
        }

        m.addAttribute("diaAtendimento", da);
        m.addAttribute("horarioList", novaLista);
        m.addAttribute("execucaoList", execucaoList);
        m.addAttribute("profissional", p);
        m.addAttribute("execucao", execucao);
        m.addAttribute("dataParam", dataParam);
        DateTime nowAux = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 1, 1);
        m.addAttribute("now", nowAux);
        return "/agenda/cliente/horarios";
    }

    @RequestMapping(value = "/atualizarHorarios", method = RequestMethod.GET)
    public String updateHorarios(Long execucaoID, Long diaAtendimentoID, Long profissionalID, Model model) throws Exception {
        DiaAtendimento diaAtendimento = ServiceLocator.getDiaAtendimentoService().readById(diaAtendimentoID);
        List<HorarioAtendimento> horarioAtendimentoList = ServiceLocator.getHorarioAtendimentoService().read(diaAtendimento);
        Profissional profissional = ServiceLocator.getProfissionalService().readById(profissionalID);
        Execucao execucao = ServiceLocator.getExecucaoService().readById(execucaoID);

        List<Map<String, Object>> novaLista = ServiceLocator.getHorarioAtendimentoService().agruparHorarios(execucao, horarioAtendimentoList, profissional);

        model.addAttribute("horarioList", novaLista);
        model.addAttribute("execucao", execucao);
        return "/agenda/cliente/horario-list-frame";
    }

}
