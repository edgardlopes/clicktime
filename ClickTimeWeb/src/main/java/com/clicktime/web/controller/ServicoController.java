package com.clicktime.web.controller;

import com.clicktime.model.base.service.BaseCategoriaServicoService;
import com.clicktime.model.base.service.BaseExecucaoService;
import com.clicktime.model.base.service.BaseProfissionalService;
import com.clicktime.model.base.service.BaseServicoService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ServicoController {

    @Autowired
    private BaseExecucaoService execucaoService;

    @Autowired
    private BaseServicoService servicoService;

    @Autowired
    private BaseCategoriaServicoService categoriaServicoService;

    @Autowired
    private BaseProfissionalService profissionalService;

    @GetMapping("/servico/getServicos")
    public String readServicos(Model model, String idCategoria) throws Exception {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put(ServicoCriteria.CATEGORIA_SERVICO_FK_EQ, idCategoria);
        List<Servico> servicos = servicoService.readByCriteria(criteria, null);
        model.addAttribute("servicos", servicos);
        return "servico/servico-option";
    }

    @GetMapping("/servicos")
    public String servicos(Model model, HttpSession session) throws Exception {
        final Profissional profissional = (Profissional) getLoggedUser(session);
        List<Execucao> execucaoList = execucaoService.readByProfissional(profissional);
        model.addAttribute("execucaoList", execucaoList);

        return "/servico/list";
    }

    @GetMapping("/servico/novo")
    public String create(Model model, HttpSession session) throws Exception {
        List<CategoriaServico> categorias = categoriaServicoService.readByCriteria(new HashMap<String, Object>(), null);
        model.addAttribute("categorias", categorias);
        model.addAttribute("isNovoServico", true);

        return "/servico/cadastro-servico";
    }

    @PostMapping("/servico/novo")
    public String create(HttpSession session, Model model, String categoriaFK, String servicoFK, String valor, String duracao, String descricao) throws Exception {
        Map<String, Object> fields = new HashMap<>();
        fields.put(ExecucaoServicoFields.CATEGORIA_FK, categoriaFK);
        fields.put(ExecucaoServicoFields.SERVICO_FK, servicoFK);
        fields.put(ExecucaoServicoFields.VALOR, valor);
        fields.put(ExecucaoServicoFields.DURACAO, duracao);
        fields.put(ExecucaoServicoFields.DESCRICAO, descricao);

        Profissional profissional = (Profissional) getLoggedUser(session);
        fields.put(ExecucaoServicoFields.PROFISSIONAL, profissional);
        Map<String, String> errors = execucaoService.validateForCreate(fields);
        if (!errors.isEmpty()) {
            List<CategoriaServico> categorias = categoriaServicoService.readByCriteria(new HashMap<String, Object>(), null);
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
        execucaoService.create(execucao);

        Profissional profissionalAtualizado = profissionalService.readById(profissional.getId());
        setLoggedUser(session, profissionalAtualizado);

        return "redirect:/servico/servicos";
    }

    @GetMapping("/servico/{id}/excluir")
    public String delete(@PathVariable Long id, HttpSession session, Model model) throws Exception {

        Profissional p = (Profissional) getLoggedUser(session);
        execucaoService.delete(id, p);
        Profissional profissionalAtualizado = profissionalService.readById(p.getId());
        setLoggedUser(session, profissionalAtualizado);

        return "redirect:/servico/servicos";
    }

    @GetMapping("/servico/{id}/editar")
    public String update(@PathVariable Long id, Model model, HttpSession session) throws Exception {
        Execucao execucao = execucaoService.readById(id);
        model.addAttribute("execucao", execucao);
        model.addAttribute("isUpdate", true);

        return "/servico/cadastro-servico";
    }

    //FALTA VALIDAR!!
    @PostMapping("/servico/{id}/editar")
    public String update(@PathVariable Long id, Model model, Execucao execucao) throws Exception {
        execucaoService.update(execucao);
        return "redirect:/servico/servicos";
    }

}
