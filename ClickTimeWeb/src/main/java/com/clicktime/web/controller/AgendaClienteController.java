package com.clicktime.web.controller;

import com.clicktime.model.base.service.BaseDiaAtendimentoService;
import com.clicktime.model.base.service.BaseExecucaoService;
import com.clicktime.model.base.service.BaseHorarioAtendimentoService;
import com.clicktime.model.base.service.BaseProfissionalService;
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
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Classe responsavel pela agenda do profissional que o cliente acessa
 *
 * @author Edgard Lopes <edgard-rodrigo@hotmail.com>
 */
@Controller
public class AgendaClienteController {

    @Autowired
    private BaseExecucaoService execucaoService;

    @Autowired
    private BaseProfissionalService profissionalService;

    @Autowired
    private BaseDiaAtendimentoService diaAtendimentoService;

    @Autowired
    private BaseHorarioAtendimentoService horarioAtendimentoService;

    @Autowired
    private CalendarioService service;

    @GetMapping("/{profissionalUrl}/agenda")
    public String getAgendaProfissional(@PathVariable String profissionalUrl, Model m, long servicoID, HttpSession session) throws Exception {
        Profissional p = profissionalService.readByUserName(profissionalUrl);
        m.addAttribute("profissional", p);
        m.addAttribute("year", new DateTime().getYear());

        Execucao execucao = (servicoID > 0) ? execucaoService.readById(servicoID) : execucaoService.readByProfissional(p).get(0);

        m.addAttribute("execucao", execucao);

        return "/agenda/months";
    }

    @GetMapping("/{profissionalUrl}/agenda/{year}/{month}")
    public String getCalendario(@PathVariable String profissionalUrl, @PathVariable Integer year,
            @PathVariable Integer month, Model m, HttpSession session, Long servicoID) throws Exception {

        if (year < LocalDate.now().getYear() || month < LocalDate.now().getMonthOfYear()) {
            return "/agenda/data-ultrapassada";
        }

        Profissional p = profissionalService.readByUserName(profissionalUrl);
        m.addAttribute("profissional", p);
        List<Execucao> execucaoList = execucaoService.readByProfissional(p);
        Execucao execucao = (servicoID != null) ? execucaoService.readById(servicoID) : execucaoList.get(0);

        m.addAttribute("execucaoSelected", execucao);
        m.addAttribute("execucaoList", execucaoList);

        List<List> weekList = (List<List>) service.getInformations(year, month, p, true).get(CalendarioService.DAYS_OF_MONTH);

        for (List week : weekList) {
            for (Object aux : week) {
                Day day = (Day) aux;

                Map<String, Object> criteria = new HashMap<>();
                criteria.put(DiaAtendimentoCriteria.PROFISSIONAL_FK_EQ, p.getId());
                criteria.put(DiaAtendimentoCriteria.DIA_ATENDIMENTO_EQ, day.getDay());

                List<DiaAtendimento> diaAtendimentoList = diaAtendimentoService.readByCriteria(criteria, null);
                if (diaAtendimentoList.size() > 0) {
                    DiaAtendimento dia = diaAtendimentoList.get(0);

                    criteria.clear();
                    criteria.put(HorarioAtendimentoCriteria.DIA_ATENDIMENTO_FK_EQ, dia.getId());
                    List<HorarioAtendimento> horarioList = horarioAtendimentoService.read(dia);

                    if (execucao != null) {
                        List<Map<String, Object>> novoHorario = horarioAtendimentoService.agruparHorarios(execucao, horarioList, p);

                        DiaAtendimentoResumo resumo = new DiaAtendimentoResumo();
                        resumo.setHorariosLivres(novoHorario.size());
                        day.setResumo(resumo);
                    }
                }
            }
        }

        service.getInformations(year, month, p, true).remove(CalendarioService.DAYS_OF_MONTH);
        m.addAllAttributes(service.getInformations(year, month, p, true));
        m.addAttribute(CalendarioService.DAYS_OF_MONTH, weekList);

        return "/agenda/calendario";

    }

    @GetMapping("/{profissionalURL}/agenda/{year}/{month}/{day}")
    public String getDay(HttpSession session, @PathVariable String profissionalURL, @PathVariable Integer year,
            @PathVariable Integer month, @PathVariable Integer day, Model m, Long servicoID) throws Exception {

        DateTime now = new DateTime();
        DateTime dataParam = new DateTime(year, month, day, 1, 1);

        Profissional p = profissionalService.readByUserName(profissionalURL);
        DateTime dt = new DateTime(year, month, day, 1, 1);

        Map<String, Object> criteria = new HashMap<>();
        criteria.put(DiaAtendimentoCriteria.DIA_ATENDIMENTO_EQ, dt);
        criteria.put(DiaAtendimentoCriteria.PROFISSIONAL_FK_EQ, p.getId());

        List<DiaAtendimento> diaAtendimentoList = diaAtendimentoService.readByCriteria(criteria, null);
        DiaAtendimento da = null;
        List<HorarioAtendimento> horarioList = new ArrayList<>();
        List<Execucao> execucaoList = execucaoService.readByProfissional(p);
        List<Map<String, Object>> novaLista = null;
        Execucao execucao = null;

        if (diaAtendimentoList.size() > 0) {

            if (servicoID != null) {
                execucao = execucaoService.readById(servicoID);
            } else {
                execucao = execucaoList.get(0);
            }

            da = diaAtendimentoList.get(0);
            horarioList = horarioAtendimentoService.read(da);
            novaLista = horarioAtendimentoService.agruparHorarios(execucao, horarioList, p);
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

    @GetMapping("/atualizarHorarios")
    public String updateHorarios(Long execucaoID, Long diaAtendimentoID, Long profissionalID, Model model) throws Exception {
        DiaAtendimento diaAtendimento = diaAtendimentoService.readById(diaAtendimentoID);
        List<HorarioAtendimento> horarioAtendimentoList = horarioAtendimentoService.read(diaAtendimento);
        Profissional profissional = profissionalService.readById(profissionalID);
        Execucao execucao = execucaoService.readById(execucaoID);

        List<Map<String, Object>> novaLista = horarioAtendimentoService.agruparHorarios(execucao, horarioAtendimentoList, profissional);

        model.addAttribute("horarioList", novaLista);
        model.addAttribute("execucao", execucao);
        return "/agenda/cliente/horario-list-frame";
    }

}
