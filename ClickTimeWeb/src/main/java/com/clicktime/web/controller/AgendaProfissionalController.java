package com.clicktime.web.controller;

import com.clicktime.model.base.service.BaseDiaAtendimentoService;
import com.clicktime.model.base.service.BaseHorarioAtendimentoService;
import com.clicktime.model.entity.DiaAtendimento;
import com.clicktime.model.entity.HorarioAtendimento;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.service.calendario.CalendarioService;
import static com.clicktime.web.interceptor.SessionUtils.*;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AgendaProfissionalController {
    
    @Autowired
    private BaseDiaAtendimentoService diaAtendimentoService;

    @Autowired
    private BaseHorarioAtendimentoService horarioAtendimentoService;

    @Autowired
    private CalendarioService calendarioService;

    //url para profissional
    @GetMapping( "/agenda/{year}")
    public String getAgenda(@PathVariable Integer year, Model m, HttpSession session) {
        m.addAttribute("idProfissional", ((Profissional) getLoggedUser(session)).getId());
        m.addAttribute("year", year);
        m.addAttribute("months", Month.values());
        return "/agenda/months";
    }

    @GetMapping( "/agenda/{year}/{month}/{day}")
    public String getAgenda(@PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day, HttpSession session, Model m) throws Exception {
        Profissional p = (Profissional) getLoggedUser(session);
        DateTime dt = new DateTime(year, month, day, 1, 1);
        List<HorarioAtendimento> horarioList = new ArrayList<>();
        DiaAtendimento dia = diaAtendimentoService.readDiaAtendimentoFromDate(dt, p);
        if (dia != null) {
            horarioList = horarioAtendimentoService.read(dia);
        } else {
            dia = new DiaAtendimento();
            dia.setData(dt);
        }
        m.addAttribute("horarioList", horarioList);
        m.addAttribute("dia", dia);

        return "/agenda/profissional/horarios";
    }

    @GetMapping( "/agenda/{year}/{month}")
    public String getCalendario(@PathVariable Integer year, @PathVariable Integer month, Model m, HttpSession session) throws Exception {
        Profissional p = (Profissional) getLoggedUser(session);
        m.addAllAttributes(calendarioService.getInformations(year, month, p, true));

        return "/agenda/calendario";
    }

    @GetMapping( "/validaDia")
    @ResponseBody
    public String validaDia(HttpServletResponse response, HttpSession session, String data) {
        String json = "";

        try {
            DateTime date = CalendarioService.parseStringToDateTime(data, "dd/MM/yyyy");
            Profissional profissional = (Profissional) getLoggedUser(session);
            DiaAtendimento dia = diaAtendimentoService.readDiaAtendimentoFromDate(date, profissional);
            json = "{\"status\":200, \"dia\":";
            json += dia.toJson();
            json += "}";
        } catch (Exception ex) {
            ex.printStackTrace();
            json = "{\"status\":400, \"error\":\"Ocorreu um erro\"}";
        }

        return json;
    }
}
