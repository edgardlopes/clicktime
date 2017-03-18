/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clicktime.web.controller;

import com.clicktime.model.ServiceLocator;
import com.clicktime.model.criteria.DiaAtendimentoCriteria;
import com.clicktime.model.criteria.ExecucaoCriteria;
import com.clicktime.model.criteria.HorarioAtendimentoCriteria;
import com.clicktime.model.criteria.ProfissionalCriteria;
import com.clicktime.model.criteria.SolicitacaoCriteria;
import com.clicktime.model.entity.DiaAtendimento;
import com.clicktime.model.entity.DiaAtendimentoResumo;
import com.clicktime.model.entity.Execucao;
import com.clicktime.model.entity.HorarioAtendimento;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.entity.Usuario;
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
    public String getAgendaProfissional(@PathVariable String profissionalUrl, Model m, Long servicoID, HttpSession session) {
        String url = "";
        try {
            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(ProfissionalCriteria.NOME_USUARIO_EQ, profissionalUrl);
            Profissional p = null;
            p = ServiceLocator.getProfissionalService().readByCriteria(criteria, null).get(0);
            m.addAttribute("profissional", p);
            m.addAttribute("year", new DateTime().getYear());
            Execucao execucao = null;
            if (servicoID != null && servicoID > 0) {
                execucao = ServiceLocator.getExecucaoService().readById(servicoID);
            } else {
                criteria = new HashMap<String, Object>();
                criteria.put(ExecucaoCriteria.PROFISSIONAL_FK_EQ, p.getId());
                execucao = ServiceLocator.getExecucaoService().readByCriteria(criteria, null).get(0);
            }
            m.addAttribute("execucao", execucao);

            criteria = new HashMap<String, Object>();
            criteria.put(SolicitacaoCriteria.CLIENTE_FK_EQ, ((Usuario) session.getAttribute("usuarioLogado")).getId());
            m.addAttribute("solicitacaoCount", ServiceLocator.getSolicitacaoService().countByCriteria(criteria));
            m.addAttribute("profissionalFavorito", ServiceLocator.getUsuarioService().getProfissionalFavorito(((Usuario) session.getAttribute("usuarioLogado")).getId()));

            url = "/agenda/months";
        } catch (Exception ex) {
            ex.printStackTrace();
            url = "/error";
        }

        return url;
    }

    @RequestMapping(value = "/{profissionalUrl}/agenda/{year}/{month}", method = RequestMethod.GET)
    public String getCalendario(@PathVariable String profissionalUrl, @PathVariable Integer year,
            @PathVariable Integer month, Model m, HttpSession session, Long servicoID) {

        String url = "/agenda/data-ultrapassada";
        try {
            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(ProfissionalCriteria.NOME_USUARIO_EQ, profissionalUrl);
            Profissional p = null;

            DateTime now = new DateTime();
            if (year >= now.getYear() && month >= now.getMonthOfYear()) {
                p = ServiceLocator.getProfissionalService().readByCriteria(criteria, null).get(0);
                m.addAttribute("profissional", p);
                criteria.clear();
                criteria.put(ExecucaoCriteria.PROFISSIONAL_FK_EQ, p.getId());
                List<Execucao> execucaoList = ServiceLocator.getExecucaoService().readByCriteria(criteria, null);
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

                        criteria.clear();
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

                criteria = new HashMap<String, Object>();
                criteria.put(SolicitacaoCriteria.CLIENTE_FK_EQ, ((Usuario) session.getAttribute("usuarioLogado")).getId());
                m.addAttribute("solicitacaoCount", ServiceLocator.getSolicitacaoService().countByCriteria(criteria));
                m.addAttribute("profissionalFavorito", ServiceLocator.getUsuarioService().getProfissionalFavorito(((Usuario) session.getAttribute("usuarioLogado")).getId()));

                url = "/agenda/calendario";
            }
        } catch (Exception e) {
            e.printStackTrace();
            url = "/error";
        }

        return url;
    }

    @RequestMapping(value = "/{profissionalURL}/agenda/{year}/{month}/{day}", method = RequestMethod.GET)
    public String getDay(HttpSession session, @PathVariable String profissionalURL, @PathVariable Integer year,
            @PathVariable Integer month, @PathVariable Integer day, Model m, Long servicoID) {

        String url = "/agenda/cliente/horarios";
        try {
            DateTime now = new DateTime();
            DateTime dataParam = new DateTime(year, month, day, 1, 1);
            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(ProfissionalCriteria.NOME_USUARIO_EQ, profissionalURL);

            Profissional p = ServiceLocator.getProfissionalService().readByCriteria(criteria, null).get(0);
            DateTime dt = new DateTime(year, month, day, 1, 1);

            criteria = new HashMap<String, Object>();
            criteria.put(DiaAtendimentoCriteria.DIA_ATENDIMENTO_EQ, dt);
            criteria.put(DiaAtendimentoCriteria.PROFISSIONAL_FK_EQ, p.getId());

            List<DiaAtendimento> diaAtendimentoList = ServiceLocator.getDiaAtendimentoService().readByCriteria(criteria, null);
            DiaAtendimento da = null;
            List<HorarioAtendimento> horarioList = new ArrayList<HorarioAtendimento>();
            List<Execucao> execucaoList = new ArrayList<Execucao>();
            List<Map<String, Object>> novaLista = null;
            Execucao execucao = null;

            if (diaAtendimentoList.size() > 0) {
                da = diaAtendimentoList.get(0);

                horarioList = ServiceLocator.getHorarioAtendimentoService().read(da);

                criteria.clear();
                criteria.put(ExecucaoCriteria.PROFISSIONAL_FK_EQ, p.getId());
                execucaoList = ServiceLocator.getExecucaoService().readByCriteria(criteria, null);

                if (servicoID != null) {
                    execucao = ServiceLocator.getExecucaoService().readById(servicoID);
                } else {
                    execucao = execucaoList.get(0);
                }

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

            criteria = new HashMap<String, Object>();
            criteria.put(SolicitacaoCriteria.CLIENTE_FK_EQ, ((Usuario) session.getAttribute("usuarioLogado")).getId());
            m.addAttribute("solicitacaoCount", ServiceLocator.getSolicitacaoService().countByCriteria(criteria));
            m.addAttribute("profissionalFavorito", ServiceLocator.getUsuarioService().getProfissionalFavorito( ((Usuario) session.getAttribute("usuarioLogado")).getId()));

            url = "/agenda/cliente/horarios";
        } catch (Exception ex) {
            ex.printStackTrace();
            url = "/error";
        }

        return url;
    }

    @RequestMapping(value = "/atualizarHorarios", method = RequestMethod.GET)
    public String updateHorarios(Long execucaoID, Long diaAtendimentoID, Long profissionalID, Model model) {
        String url = "";
        try {
            DiaAtendimento diaAtendimento = ServiceLocator.getDiaAtendimentoService().readById(diaAtendimentoID);
            List<HorarioAtendimento> horarioAtendimentoList = ServiceLocator.getHorarioAtendimentoService().read(diaAtendimento);
            Profissional profissional = ServiceLocator.getProfissionalService().readById(profissionalID);
            Execucao execucao = ServiceLocator.getExecucaoService().readById(execucaoID);

            List<Map<String, Object>> novaLista = ServiceLocator.getHorarioAtendimentoService().agruparHorarios(execucao, horarioAtendimentoList, profissional);

            url = "/agenda/cliente/horario-list-frame";
            model.addAttribute("horarioList", novaLista);
            model.addAttribute("execucao", execucao);
        } catch (Exception exception) {
            exception.printStackTrace();
            url = "/error";
        }

        return url;
    }

}