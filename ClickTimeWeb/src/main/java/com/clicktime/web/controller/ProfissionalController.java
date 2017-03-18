package com.clicktime.web.controller;

import com.clicktime.model.ServiceLocator;
import com.clicktime.model.criteria.ExecucaoCriteria;
import com.clicktime.model.criteria.ProfissionalCriteria;
import com.clicktime.model.criteria.SolicitacaoCriteria;
import com.clicktime.model.entity.Avatar;
import com.clicktime.model.entity.Execucao;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.entity.Solicitacao;
import com.clicktime.model.entity.Usuario;
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
            String descricao, String horaInicio, String horaFim, MultipartFile avatar) {
        //SETAR NOME DO USUARIO, PARA MOSTRA NA TELA DE SERVICOS
        //u.setSenha(DigestUtils.md5Hex(u.getSenha()));

        String url = "";
        try {
            Map<String, Object> fields = new HashMap<String, Object>();
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
            if (errors.isEmpty()) {
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

                Avatar a = new Avatar();
                if (avatar.isEmpty()) {
                    InputStream img = UsuarioController.class.getResourceAsStream("/com/clicktime/web/res/client.png");
                    a.setImagem(IOUtils.toByteArray(img));
                } else {
                    a.setImagem(avatar.getBytes());
                }
                ServiceLocator.getUsuarioService().setAvatar(profissional.getId(), a);

                url = "redirect:/sucesso";
            } else {
                url = "/usuario/cadastro-usuario";
                model.addAttribute("isProfissional", true);
                model.addAttribute("usuario", fields);
                model.addAttribute("errors", errors);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            url = "/error";
        }

        return url;
    }

    @RequestMapping(value = "/profissional/{urlProfissional}/home", method = RequestMethod.GET)
    public String homeProfissional(Model m, @PathVariable String urlProfissional, HttpSession session) {
        String url = "";
        try {
            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(SolicitacaoCriteria.STATUS_EQ, Solicitacao.AGUARDANDO_CONFIRMACAO);
            criteria.put(SolicitacaoCriteria.PROFISSIONAL_FK_EQ, ((Usuario) session.getAttribute("usuarioLogado")).getId());

            Long count = ServiceLocator.getSolicitacaoService().countByCriteria(criteria);
            m.addAttribute("countSolicitacao", count);
            criteria.remove(SolicitacaoCriteria.STATUS_EQ);
            m.addAttribute("solicitacaoCount", ServiceLocator.getSolicitacaoService().countByCriteria(criteria));
            url = "/profissional/home";
        } catch (Exception exception) {
            exception.printStackTrace();
            url = "/error";
        }

        return url;
    }

    @RequestMapping(value = "/profissional/busca", method = RequestMethod.GET)
    public String selectProfissional(Model m, Long categoriaFK, Long servicoFK, HttpSession session) {
        String url = "";
        try {
            Map<String, Object> criteria = new HashMap<String, Object>();
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

            criteria = new HashMap<String, Object>();
            Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
            criteria.put(SolicitacaoCriteria.CLIENTE_FK_EQ, usuario.getId());
            m.addAttribute("solicitacaoCount", ServiceLocator.getSolicitacaoService().countByCriteria(criteria));
            m.addAttribute("profissionalFavorito", ServiceLocator.getUsuarioService().getProfissionalFavorito(usuario.getId()));

            url = "/usuario/select-profissional";
        } catch (Exception ex) {
            ex.printStackTrace();
            url = "/error";
        }

        return url;
    }

    @RequestMapping(value = "/profissional/{urlProfissional}", method = RequestMethod.GET)
    public String readByUrl(@PathVariable String urlProfissional, Model m, HttpSession session) {
        String url = "";
        try {
            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(ProfissionalCriteria.NOME_USUARIO_EQ, urlProfissional);

            Profissional profissional = null;
            try {
                profissional = ServiceLocator.getProfissionalService().readByCriteria(criteria, null).get(0);
            } catch (IndexOutOfBoundsException ex) {
                ex.printStackTrace();
            }
            m.addAttribute("profissional", profissional);

            if (profissional != null) {
                criteria.clear();
                criteria.put(ExecucaoCriteria.PROFISSIONAL_FK_EQ, profissional.getId());
                
                List<Execucao> execucaoList = ServiceLocator.getExecucaoService().readByCriteria(criteria, null);
                m.addAttribute("execucaoList", execucaoList);
                Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
                if (usuario != null) {
                    criteria = new HashMap<String, Object>();
                    criteria.put(SolicitacaoCriteria.CLIENTE_FK_EQ, usuario.getId());
                    m.addAttribute("solicitacaoCount", ServiceLocator.getSolicitacaoService().countByCriteria(criteria));
                    m.addAttribute("profissionalFavorito", ServiceLocator.getUsuarioService().getProfissionalFavorito(usuario.getId()));
                }
                
            }
            url = "/profissional/perfil-profissional";
        } catch (Exception ex) {
            ex.printStackTrace();
            url = "/error";
        }

        return url;
    }

    @RequestMapping(value = "/profissional/{id}/img.jpg", method = RequestMethod.GET)
    public void streamImagem(@PathVariable Long id, HttpServletResponse response) throws Exception {
        Avatar avatar = ServiceLocator.getUsuarioService().getAvatar(id);
        response.setContentType("image/jpg");
        response.getOutputStream().write(avatar.getImagem());
        response.flushBuffer();
    }
}
