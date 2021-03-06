package com.clicktime.web.controller;

import com.clicktime.model.ServiceLocator;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AgendaProfissionalController {

    //url para profissional
    @RequestMapping(value = "/agenda/{year}", method = RequestMethod.GET)
    public String getAgenda(@PathVariable Integer year, Model m, HttpSession session) {
        m.addAttribute("idProfissional", ((Profissional) getLoggedUser(session)).getId());
        m.addAttribute("year", year);
        m.addAttribute("months", Month.values());
        return "/agenda/months";
    }

    @RequestMapping(value = "/agenda/{year}/{month}/{day}", method = RequestMethod.GET)
    public String getAgenda(@PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day, HttpSession session, Model m) throws Exception {
        Profissional p = (Profissional) getLoggedUser(session);
        DateTime dt = new DateTime(year, month, day, 1, 1);
        List<HorarioAtendimento> horarioList = new ArrayList<>();
        DiaAtendimento dia = ServiceLocator.getDiaAtendimentoService().readDiaAtendimentoFromDate(dt, p);
        if (dia != null) {
            horarioList = ServiceLocator.getHorarioAtendimentoService().read(dia);
        } else {
            dia = new DiaAtendimento();
            dia.setData(dt);
        }
        m.addAttribute("horarioList", horarioList);
        m.addAttribute("dia", dia);

        return "/agenda/profissional/horarios";
    }

    @RequestMapping(value = "/agenda/{year}/{month}", method = RequestMethod.GET)
    public String getCalendario(@PathVariable Integer year, @PathVariable Integer month, Model m, HttpSession session) throws Exception {
        Profissional p = (Profissional) getLoggedUser(session);
        CalendarioService service = ServiceLocator.getCalendarioService(year, month, p, true);
        m.addAllAttributes(service.getInformations());

        return "/agenda/calendario";
    }

    @RequestMapping(value = "/validaDia", method = RequestMethod.GET)
    @ResponseBody
    public String validaDia(HttpServletResponse response, HttpSession session, String data) {
        String json = "";

        try {
            DateTime date = CalendarioService.parseStringToDateTime(data, "dd/MM/yyyy");
            Profissional profissional = (Profissional) getLoggedUser(session);
            DiaAtendimento dia = ServiceLocator.getDiaAtendimentoService().readDiaAtendimentoFromDate(date, profissional);
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
