package com.clicktime.web.controller;

import com.clicktime.model.ServiceLocator;
import com.clicktime.model.criteria.ProfissionalCriteria;
import com.clicktime.model.entity.Avatar;
import com.clicktime.model.entity.Execucao;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.fields.ProfissionalFields;
import com.clicktime.model.fields.UsuarioFields;
import com.clicktime.model.service.calendario.CalendarioService;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ProfissionalController {

    @RequestMapping(value = "/profissional/novo", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("isProfissional", true);
        return "/usuario/cadastro-usuario";
    }

    @RequestMapping(value = "/profissional/novo", method = RequestMethod.POST)
    public String create(Model model, String nome, String sobrenome,
            String nomeUsuario, String email, String telefone, String senha, String senhaConfirm,
            String descricao, String horaInicio, String horaFim, MultipartFile avatar) throws Exception {
        //SETAR NOME DO USUARIO, PARA MOSTRA NA TELA DE SERVICOS
        //u.setSenha(DigestUtils.md5Hex(u.getSenha()));

        String url = "";
        Map<String, Object> fields = new HashMap<>();
        fields.put(UsuarioFields.NOME, nome);
        fields.put(UsuarioFields.SOBRENOME, sobrenome);
        fields.put(UsuarioFields.NOME_USUARIO, nomeUsuario);
        fields.put(UsuarioFields.EMAIL, email);
        fields.put(UsuarioFields.TELEFONE, telefone);
        fields.put(UsuarioFields.SENHA, senha);
        fields.put(UsuarioFields.SENHA_CONFIRM, senhaConfirm);
        fields.put(ProfissionalFields.DESCRICAO, descricao);
        fields.put(ProfissionalFields.HORA_INICIO, horaInicio);
        fields.put(ProfissionalFields.HORA_FIM, horaFim);
        Map<String, String> errors = ServiceLocator.getProfissionalService().validateForCreate(fields);

        if (!errors.isEmpty()) {
            model.addAttribute("isProfissional", true);
            model.addAttribute("usuario", fields);
            model.addAttribute("errors", errors);
            return "/usuario/cadastro-usuario";
        }
        
        Profissional profissional = new Profissional();
        profissional.setNome(nome);
        profissional.setSobrenome(sobrenome);
        profissional.setNomeUsuario(nomeUsuario);
        profissional.setEmail(email);
        profissional.setTelefone(telefone);
        profissional.setSenha(senha);
        profissional.setDescricao(descricao);
        profissional.setHoraInicio(CalendarioService.parseStringToDateTime(horaInicio, "HH:mm"));
        profissional.setHoraFim(CalendarioService.parseStringToDateTime(horaFim, "HH:mm"));
        ServiceLocator.getProfissionalService().create(profissional);

        if (!avatar.isEmpty()) {
            Avatar a = new Avatar();
            a.setImagem(avatar.getBytes());
            ServiceLocator.getUsuarioService().setAvatar(profissional.getId(), a);
        }


        return "redirect:/sucesso";
    }

    @RequestMapping(value = "/profissional/{urlProfissional}/home", method = RequestMethod.GET)
    public String homeProfissional(Model m, @PathVariable String urlProfissional, HttpSession session) throws Exception {
        return "/profissional/home";
    }

    @RequestMapping(value = "/profissional/busca", method = RequestMethod.GET)
    public String selectProfissional(Model m, Long categoriaFK, Long servicoFK, HttpSession session) throws Exception {
        Map<String, Object> criteria = new HashMap<>();
        if (categoriaFK != null && !categoriaFK.equals(-1L)) {
            criteria.put(ProfissionalCriteria.CATEGORIA_SERVICO_ID_EQ, categoriaFK);
            m.addAttribute("categoriaFK", categoriaFK);
        }

        if (servicoFK != null && !servicoFK.equals(-1L)) {
            criteria.put(ProfissionalCriteria.SERVICO_ID_EQ, servicoFK);
            m.addAttribute("servicoFK", servicoFK);
        }

        m.addAttribute("categorias", ServiceLocator.getCategoriaServicoService().readByCriteria(null, null));
        List<Profissional> profissionalList = ServiceLocator.getProfissionalService().readByCriteria(criteria, null);
        m.addAttribute("profissionalList", profissionalList);

        return "/usuario/select-profissional";
    }

    @RequestMapping(value = "/profissional/{urlProfissional}", method = RequestMethod.GET)
    public String readByUrl(@PathVariable String urlProfissional, Model m, HttpSession session) throws Exception {
        Profissional profissional = ServiceLocator.getProfissionalService().readByUserName(urlProfissional);
        m.addAttribute("profissional", profissional);

        if (profissional != null) {
            List<Execucao> execucaoList = ServiceLocator.getExecucaoService().readByProfissional(profissional);
            m.addAttribute("execucaoList", execucaoList);
        }
        return "/profissional/perfil-profissional";
    }

    @RequestMapping(value = "/profissional/{id}/img.jpg", method = RequestMethod.GET)
    public void streamImagem(@PathVariable Long id, HttpServletResponse response) throws Exception {
        Avatar avatar = ServiceLocator.getUsuarioService().getAvatar(id);
        if (avatar != null) {
            response.getOutputStream().write(avatar.getImagem());
        } else {
            InputStream img = UsuarioController.class.getResourceAsStream("/client.png");
            response.getOutputStream().write(IOUtils.toByteArray(img));
        }
        response.setContentType("image/jpg");
        response.flushBuffer();
    }
}
