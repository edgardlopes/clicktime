package com.clicktime.web.controller;

import com.clicktime.model.ServiceLocator;
import com.clicktime.model.criteria.ExecucaoCriteria;
import com.clicktime.model.criteria.ServicoCriteria;
import com.clicktime.model.criteria.SolicitacaoCriteria;
import com.clicktime.model.entity.CategoriaServico;
import com.clicktime.model.entity.Execucao;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.entity.Servico;
import com.clicktime.model.fields.ExecucaoServicoFields;
import com.clicktime.model.service.calendario.CalendarioService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ServicoController {

    @RequestMapping(value = "/servico/getServicos", method = RequestMethod.GET)
    public String readServicos(Model model, String idCategoria) {
        String url = "";
        try {
            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(ServicoCriteria.CATEGORIA_SERVICO_FK_EQ, idCategoria);
            List<Servico> servicos = ServiceLocator.getServicoService().readByCriteria(criteria, null);
            model.addAttribute("servicos", servicos);
            url = "servico/servico-option";
        } catch (Exception ex) {
            ex.printStackTrace();
            url = "/error";
        }
        return url;
    }

    @RequestMapping(value = "/servico/servicos", method = RequestMethod.GET)
    public String servicos(Model model, HttpSession session) {
        String url = "";
        try {
            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(ExecucaoCriteria.PROFISSIONAL_FK_EQ, (((Profissional) session.getAttribute("usuarioLogado")).getId()));
            List<Execucao> execucaoList = ServiceLocator.getExecucaoService().readByCriteria(criteria, null);
            model.addAttribute("execucaoList", execucaoList);
            model.addAttribute("isServicos", "active");

            criteria = new HashMap<String, Object>();
            criteria.put(SolicitacaoCriteria.PROFISSIONAL_FK_EQ, ((Profissional) session.getAttribute("usuarioLogado")).getId());
            model.addAttribute("solicitacaoCount", ServiceLocator.getSolicitacaoService().countByCriteria(criteria));
            url = "/servico/servico-list";
        } catch (Exception ex) {
            ex.printStackTrace();
            url = "/error";
        }

        return url;
    }

    @RequestMapping(value = "/servico/novo", method = RequestMethod.GET)
    public String create(Model model, HttpSession session) {
        String url = "";
        try {
            List<CategoriaServico> categorias = ServiceLocator.getCategoriaServicoService().readByCriteria(new HashMap<String, Object>(), null);
            model.addAttribute("categorias", categorias);
            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria = new HashMap<String, Object>();
            criteria.put(SolicitacaoCriteria.PROFISSIONAL_FK_EQ, ((Profissional) session.getAttribute("usuarioLogado")).getId());
            model.addAttribute("solicitacaoCount", ServiceLocator.getSolicitacaoService().countByCriteria(criteria));
            url = "/servico/cadastro-servico";
            model.addAttribute("isNovoServico", true);
        } catch (Exception ex) {
            ex.printStackTrace();
            url = "/error";
        }

        return url;
    }

    @RequestMapping(value = "/servico/novo", method = RequestMethod.POST)
    public String create(HttpSession session, Model model, String categoriaFK, String servicoFK, String valor, String duracao, String descricao) {
        String url = "";
        try {
            Map<String, Object> fields = new HashMap<String, Object>();
            fields.put(ExecucaoServicoFields.CATEGORIA_FK, categoriaFK);
            fields.put(ExecucaoServicoFields.SERVICO_FK, servicoFK);
            fields.put(ExecucaoServicoFields.VALOR, valor);
            fields.put(ExecucaoServicoFields.DURACAO, duracao);
            fields.put(ExecucaoServicoFields.DESCRICAO, descricao);

            Profissional profissional = (Profissional) session.getAttribute("usuarioLogado");
            fields.put(ExecucaoServicoFields.PROFISSIONAL, profissional);
            Map<String, String> errors = ServiceLocator.getExecucaoService().validateForCreate(fields);
            if (errors.isEmpty()) {
                Execucao execucao = new Execucao();
                Servico servico = new Servico();
                servico.setId(Long.parseLong(servicoFK));
                execucao.setServico(servico);
                execucao.setValor(Float.parseFloat(valor));
                execucao.setDescricao(descricao);
                execucao.setDuracao(CalendarioService.parseStringToDateTime(duracao, "HH:mm"));
                execucao.setProfissional(profissional);
                ServiceLocator.getExecucaoService().create(execucao);

                Profissional profissionalAtualizado = ServiceLocator.getProfissionalService().readById(profissional.getId());
                session.setAttribute("usuarioLogado", profissionalAtualizado);
                url = "redirect:/servico/servicos";
            } else {
                url = "/servico/cadastro-servico";
                List<CategoriaServico> categorias = ServiceLocator.getCategoriaServicoService().readByCriteria(new HashMap<String, Object>(), null);
                model.addAttribute("categorias", categorias);
                try {
                    fields.put(ExecucaoServicoFields.DURACAO, CalendarioService.parseStringToDateTime(duracao, "HH:mm"));
                } catch (Exception e) {
                }
                model.addAttribute("execucao", fields);
                model.addAttribute("errors", errors);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            url = "/error";
        }
        return url;
    }

    @RequestMapping(value = "/servico/{id}/excluir", method = RequestMethod.GET)
    public String delete(@PathVariable Long id, HttpSession session, Model model) {
        String url = "";

        try {
            Profissional p = (Profissional) session.getAttribute("usuarioLogado");
            ServiceLocator.getExecucaoService().delete(id, p);
            Profissional profissionalAtualizado = ServiceLocator.getProfissionalService().readById(p.getId());
            session.setAttribute("usuarioLogado", profissionalAtualizado);

            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria = new HashMap<String, Object>();
            criteria.put(SolicitacaoCriteria.PROFISSIONAL_FK_EQ, ((Profissional) session.getAttribute("usuarioLogado")).getId());
            model.addAttribute("solicitacaoCount", ServiceLocator.getSolicitacaoService().countByCriteria(criteria));

            url = "redirect:/servico/servicos";

        } catch (Exception ex) {
            ex.printStackTrace();
            url = "/error";
        }

        return url;
    }

    @RequestMapping(value = "/servico/{id}/editar", method = RequestMethod.GET)
    public String update(@PathVariable Long id, Model model, HttpSession session) {
        String url = "";
        Execucao execucao = null;
        try {
            execucao = ServiceLocator.getExecucaoService().readById(id);
            model.addAttribute("execucao", execucao);
            model.addAttribute("isUpdate", true);

            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria = new HashMap<String, Object>();
            criteria.put(SolicitacaoCriteria.PROFISSIONAL_FK_EQ, ((Profissional) session.getAttribute("usuarioLogado")).getId());
            model.addAttribute("solicitacaoCount", ServiceLocator.getSolicitacaoService().countByCriteria(criteria));

            url = "/servico/cadastro-servico";
        } catch (Exception ex) {
            ex.printStackTrace();
            url = "/error";
        }

        return url;
    }

    
    //FALTA VALIDAR!!
    @RequestMapping(value = "/servico/{id}/editar", method = RequestMethod.POST)
    public String update(@PathVariable Long id, Model model, Execucao execucao) {
        String url = "";
        try {
            ServiceLocator.getExecucaoService().update(execucao);
            url = "redirect:/servico/servicos";
        } catch (Exception ex) {
            ex.printStackTrace();
            url = "/error";
        }

        return url;
    }

}
