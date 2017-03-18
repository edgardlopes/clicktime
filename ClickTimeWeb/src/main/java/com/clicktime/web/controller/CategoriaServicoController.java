/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clicktime.web.controller;

import com.clicktime.model.ServiceLocator;
import com.clicktime.model.criteria.SolicitacaoCriteria;
import com.clicktime.model.entity.CategoriaServico;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.entity.Servico;
import com.clicktime.model.fields.CategoriaServicoFields;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/*
 * 
 * @author Edgard Lopes <edgard-rodrigo@hotmail.com>
 * controller para categoria de servico ou tipo de profissional
 */
@Controller
public class CategoriaServicoController {

    @RequestMapping(value = "/categoria/novo", method = RequestMethod.GET)
    public String create(HttpSession session, Model model) {
        String url = "/categoria/novo";
        try {
            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(SolicitacaoCriteria.PROFISSIONAL_FK_EQ, ((Profissional) session.getAttribute("usuarioLogado")).getId());
            model.addAttribute("solicitacaoCount", ServiceLocator.getSolicitacaoService().countByCriteria(criteria));
        } catch (Exception exception) {
            exception.printStackTrace();
            url = "/error";
        }
        return url;
    }

    @RequestMapping(value = "/categoria/novo", method = RequestMethod.POST)
    public String create(String nome, Model model) {
        String url = "redirect:/servico/novo";

        try {
            Map<String, Object> fields = new HashMap<>();
            fields.put(CategoriaServicoFields.NOME, nome);
            Map<String, String> errors = ServiceLocator.getCategoriaServicoService().validateForCreate(fields);
            if (errors.isEmpty()) {
                CategoriaServico categoria = new CategoriaServico();
                categoria.setNome(nome);
                ServiceLocator.getCategoriaServicoService().create(categoria);
            } else {
                model.addAttribute("categoria", fields);
                model.addAttribute("errors", errors);
                url = "/categoria/novo";
            }
        } catch (Exception e) {
            e.printStackTrace();
            url = "/error";
        }

        return url;
    }

    @RequestMapping(value = "/tipoServico/novo", method = RequestMethod.GET)
    public String createTipoServico(HttpSession session, Model model) {
        String url = "/categoria/novo_servico";
        try {
            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(SolicitacaoCriteria.PROFISSIONAL_FK_EQ, ((Profissional) session.getAttribute("usuarioLogado")).getId());
            model.addAttribute("solicitacaoCount", ServiceLocator.getSolicitacaoService().countByCriteria(criteria));
            model.addAttribute("categorias", ServiceLocator.getCategoriaServicoService().readByCriteria(new HashMap<String, Object>(), null));
        } catch (Exception exception) {
            exception.printStackTrace();
            url = "/error";
        }
        return url;
    }

    @RequestMapping(value = "/tipoServico/novo", method = RequestMethod.POST)
    public String createTipoServico(Long categoriaFK, String nome, Model model) {
        String url = "redirect:/servico/novo";

        try {

            Servico servico = new Servico();
            servico.setNome(nome);
            CategoriaServico categoriaServico = new CategoriaServico();
            categoriaServico.setId(categoriaFK);
            servico.setCategoria(categoriaServico);
            ServiceLocator.getServicoService().create(servico);
            
        } catch (Exception e) {
            e.printStackTrace();
            url = "/error";
        }

        return url;
    }
}
