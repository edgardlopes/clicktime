package com.clicktime.web.controller;

import com.clicktime.model.ServiceLocator;
import com.clicktime.model.criteria.SolicitacaoCriteria;
import com.clicktime.model.entity.DiaAtendimento;
import com.clicktime.model.entity.HorarioAtendimento;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.entity.Usuario;
import com.clicktime.model.service.calendario.CalendarioService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        String url = "";
        Usuario u = (Usuario) session.getAttribute("usuarioLogado");
        m.addAttribute("idProfissional", ((Profissional) session.getAttribute("usuarioLogado")).getId());
        m.addAttribute("year", year);
        url = "/agenda/months";
        return url;
    }
    
    @RequestMapping(value = "/agenda/{year}/{month}/{day}", method = RequestMethod.GET)
    public String getAgenda(@PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day, HttpSession session, Model m) {
        String url = "";
        try {
            Profissional p = (Profissional) session.getAttribute("usuarioLogado");
            DateTime dt = new DateTime(year, month, day, 1, 1);
            List<HorarioAtendimento> horarioList = new ArrayList<HorarioAtendimento>();
            DiaAtendimento dia = ServiceLocator.getDiaAtendimentoService().readDiaAtendimentoFromDate(dt, p);
            if (dia != null) {
                horarioList = ServiceLocator.getHorarioAtendimentoService().read(dia);
            } else {
                dia = new DiaAtendimento();
                dia.setData(dt);
            }
            m.addAttribute("horarioList", horarioList);
            m.addAttribute("dia", dia);
            
            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria = new HashMap<String, Object>();
            criteria.put(SolicitacaoCriteria.PROFISSIONAL_FK_EQ, ((Profissional) session.getAttribute("usuarioLogado")).getId());
            m.addAttribute("solicitacaoCount", ServiceLocator.getSolicitacaoService().countByCriteria(criteria));
            
            url = "/agenda/profissional/horarios";
            
        } catch (Exception ex) {
            ex.printStackTrace();
            url = "/error";
        }
        return url;
    }
    
    @RequestMapping(value = "/agenda/{year}/{month}", method = RequestMethod.GET)
    public String getCalendario(@PathVariable Integer year, @PathVariable Integer month, Model m, HttpSession session) {
        String url = "";
        try {
            Profissional p = (Profissional) session.getAttribute("usuarioLogado");
            CalendarioService service = ServiceLocator.getCalendarioService(year, month, p, true);
            m.addAllAttributes(service.getInformations());
            
            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria = new HashMap<String, Object>();
            criteria.put(SolicitacaoCriteria.PROFISSIONAL_FK_EQ, ((Profissional) session.getAttribute("usuarioLogado")).getId());
            m.addAttribute("solicitacaoCount", ServiceLocator.getSolicitacaoService().countByCriteria(criteria));
            
            url = "/agenda/calendario";
            
        } catch (Exception exception) {
            exception.printStackTrace();
            url = "/error";
        }
        return url;
    }
    
    @RequestMapping(value = "/validaDia", method = RequestMethod.GET)
    @ResponseBody
    public String validaDia(HttpServletResponse response, HttpSession session, String data) {
        String json = "";
        
        try {
            DateTime date = CalendarioService.parseStringToDateTime(data, "dd/MM/yyyy");
            Profissional profissional = (Profissional) session.getAttribute("usuarioLogado");
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
