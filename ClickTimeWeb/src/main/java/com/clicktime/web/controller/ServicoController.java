package com.clicktime.web.controller;

import com.clicktime.model.ServiceLocator;
import com.clicktime.model.criteria.ServicoCriteria;
import com.clicktime.model.entity.CategoriaServico;
import com.clicktime.model.entity.Execucao;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.entity.Servico;
import com.clicktime.model.fields.ExecucaoServicoFields;
import com.clicktime.model.service.calendario.CalendarioService;
import static com.clicktime.web.interceptor.SessionUtils.*;
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
    public String readServicos(Model model, String idCategoria) throws Exception {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put(ServicoCriteria.CATEGORIA_SERVICO_FK_EQ, idCategoria);
        List<Servico> servicos = ServiceLocator.getServicoService().readByCriteria(criteria, null);
        model.addAttribute("servicos", servicos);
        return "servico/servico-option";
    }

    @RequestMapping(value = "/servico/servicos", method = RequestMethod.GET)
    public String servicos(Model model, HttpSession session) throws Exception {
        final Profissional profissional = (Profissional) getLoggedUser(session);
        List<Execucao> execucaoList = ServiceLocator.getExecucaoService().readByProfissional(profissional);
        model.addAttribute("execucaoList", execucaoList);
        model.addAttribute("isServicos", "active");

        return "/servico/servico-list";
    }

    @RequestMapping(value = "/servico/novo", method = RequestMethod.GET)
    public String create(Model model, HttpSession session) throws Exception {
        List<CategoriaServico> categorias = ServiceLocator.getCategoriaServicoService().readByCriteria(new HashMap<String, Object>(), null);
        model.addAttribute("categorias", categorias);
        model.addAttribute("isNovoServico", true);

        return "/servico/cadastro-servico";
    }

    @RequestMapping(value = "/servico/novo", method = RequestMethod.POST)
    public String create(HttpSession session, Model model, String categoriaFK, String servicoFK, String valor, String duracao, String descricao) throws Exception {
        Map<String, Object> fields = new HashMap<>();
        fields.put(ExecucaoServicoFields.CATEGORIA_FK, categoriaFK);
        fields.put(ExecucaoServicoFields.SERVICO_FK, servicoFK);
        fields.put(ExecucaoServicoFields.VALOR, valor);
        fields.put(ExecucaoServicoFields.DURACAO, duracao);
        fields.put(ExecucaoServicoFields.DESCRICAO, descricao);

        Profissional profissional = (Profissional) getLoggedUser(session);
        fields.put(ExecucaoServicoFields.PROFISSIONAL, profissional);
        Map<String, String> errors = ServiceLocator.getExecucaoService().validateForCreate(fields);
        if (!errors.isEmpty()) {
            List<CategoriaServico> categorias = ServiceLocator.getCategoriaServicoService().readByCriteria(new HashMap<String, Object>(), null);
            model.addAttribute("categorias", categorias);
            try {
                fields.put(ExecucaoServicoFields.DURACAO, CalendarioService.parseStringToDateTime(duracao, "HH:mm"));
            } catch (Exception e) {
            }
            model.addAttribute("execucao", fields);
            model.addAttribute("errors", errors);
            return "/servico/cadastro-servico";
        }

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
        setLoggedUser(session, profissionalAtualizado);

        return "redirect:/servico/servicos";
    }

    @RequestMapping(value = "/servico/{id}/excluir", method = RequestMethod.GET)
    public String delete(@PathVariable Long id, HttpSession session, Model model) throws Exception {

        Profissional p = (Profissional) getLoggedUser(session);
        ServiceLocator.getExecucaoService().delete(id, p);
        Profissional profissionalAtualizado = ServiceLocator.getProfissionalService().readById(p.getId());
        setLoggedUser(session, profissionalAtualizado);

        return "redirect:/servico/servicos";
    }

    @RequestMapping(value = "/servico/{id}/editar", method = RequestMethod.GET)
    public String update(@PathVariable Long id, Model model, HttpSession session) throws Exception {
        Execucao execucao = ServiceLocator.getExecucaoService().readById(id);
        model.addAttribute("execucao", execucao);
        model.addAttribute("isUpdate", true);

        return "/servico/cadastro-servico";
    }

    //FALTA VALIDAR!!
    @RequestMapping(value = "/servico/{id}/editar", method = RequestMethod.POST)
    public String update(@PathVariable Long id, Model model, Execucao execucao) throws Exception {
        ServiceLocator.getExecucaoService().update(execucao);
        return "redirect:/servico/servicos";
    }

}
