/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clicktime.web.controller;

import com.clicktime.model.base.service.BaseCategoriaServicoService;
import com.clicktime.model.base.service.BaseServicoService;
import com.clicktime.model.entity.CategoriaServico;
import com.clicktime.model.entity.Servico;
import com.clicktime.model.fields.CategoriaServicoFields;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/*
 * 
 * @author Edgard Lopes <edgard-rodrigo@hotmail.com>
 * controller para categoria de servico ou tipo de profissional
 */
@Controller
public class CategoriaServicoController {
    
    @Autowired
    private BaseCategoriaServicoService categoriaServicoService;
    
    @Autowired
    private BaseServicoService servicoService;

    @GetMapping("/categoria/novo")
    public String create(HttpSession session, Model model) throws Exception {
        return "/categoria/novo";
    }

    @PostMapping( "/categoria/novo")
    public String create(String nome, Model model) throws Exception {

        Map<String, Object> fields = new HashMap<>();
        fields.put(CategoriaServicoFields.NOME, nome);
        Map<String, String> errors = categoriaServicoService.validateForCreate(fields);
        if (!errors.isEmpty()) {
            model.addAttribute("categoria", fields);
            model.addAttribute("errors", errors);
            return "/categoria/novo";
        }
        
        CategoriaServico categoria = new CategoriaServico();
        categoria.setNome(nome);
        categoriaServicoService.create(categoria);

        return "redirect:/servico/novo";
    }

    @GetMapping("/tipoServico/novo")
    public String createTipoServico(HttpSession session, Model model) throws Exception {
        model.addAttribute("categorias", categoriaServicoService.readByCriteria(null, null));
        return "/categoria/novo_servico";
    }

    @PostMapping( "/tipoServico/novo")
    public String createTipoServico(Long categoriaFK, String nome, Model model) throws Exception {

        Servico servico = new Servico();
        servico.setNome(nome);
        CategoriaServico categoriaServico = new CategoriaServico();
        categoriaServico.setId(categoriaFK);
        servico.setCategoria(categoriaServico);
        servicoService.create(servico);

        return "redirect:/servico/novo";
    }
}
