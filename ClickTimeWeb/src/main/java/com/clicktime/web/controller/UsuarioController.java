package com.clicktime.web.controller;

import com.clicktime.model.ServiceLocator;
import com.clicktime.model.criteria.SolicitacaoCriteria;
import com.clicktime.model.entity.Avatar;
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
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UsuarioController {

    //menu de escolha de usuario ao cadastrar...
    @RequestMapping(value = "/escolha/usuario", method = RequestMethod.GET)
    public String selectUser(Model m) {
        m.addAttribute("isIndex", true);
        return "/usuario/escolha-tipo-usuario";
    }

    //RM que trata os dados do login
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String autentica(String email, String senha, HttpSession session, Model m) {
        //u.setSenha(DigestUtils.md5Hex(u.getSenha()));

        String url = "";
        try {
            Usuario usuarioLogado = ServiceLocator.getUsuarioService().login(email, senha);

            if (usuarioLogado != null) {
                session.setAttribute("usuarioLogado", usuarioLogado);
                url = "redirect:/home";
            } else {
                url = "redirect:/";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            url = "/error";
        }

        return url;
    }

    //direciona o usuario para a home correta
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(HttpSession session, Model model) {
        String url = null;

        model.addAttribute("isHome", true);

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado instanceof Profissional) {
            //sugestao de url interativa profissional/{nome}/home
            url = "redirect:/profissional/" + ((Profissional) usuarioLogado).getNomeUsuario() + "/home";
        } else {
            //sugestao de url interativa usuario/{nome}/home
            url = "redirect:/usuario/home";
        }

        return url;
    }

    //formulario de cadastro
    @RequestMapping(value = "/usuario/novo", method = RequestMethod.GET)
    public String create(Model m) {
        m.addAttribute("isUsuario", true);
        return "/usuario/cadastro-usuario";
    }

    //formulario de cadastro POST
    @RequestMapping(value = "/usuario/novo", method = RequestMethod.POST)
    public String create(Model model, String nome, String sobrenome, String nomeUsuario,
            String email, String telefone, String senha, String senhaConfirm, MultipartFile avatar) {
        //usuario.setSenha(DigestUtils.md5Hex(usuario.getSenha()));
        String url = "";
        try {
            Map<String, Object> fields = new HashMap<String, Object>();
            fields.put(UsuarioFields.NOME, nome);
            fields.put(UsuarioFields.SOBRENOME, sobrenome);
            fields.put(UsuarioFields.NOME_USUARIO, nomeUsuario);
            fields.put(UsuarioFields.EMAIL, email);
            if (telefone == null || telefone.isEmpty()) {
                telefone = null;
            }
            fields.put(UsuarioFields.TELEFONE, telefone);
            fields.put(UsuarioFields.SENHA, senha);
            fields.put(UsuarioFields.SENHA_CONFIRM, senhaConfirm);
            Map<String, String> errors = ServiceLocator.getUsuarioService().validateForCreate(fields);
            if (errors.isEmpty()) {
                Usuario usuario = new Usuario();
                usuario.setNome(nome);
                usuario.setSobrenome(sobrenome);
                usuario.setNomeUsuario(nomeUsuario);
                usuario.setEmail(email);
                usuario.setTelefone(telefone);
                usuario.setSenha(senha);
                ServiceLocator.getUsuarioService().create(usuario);

                Avatar a = new Avatar();
                if (avatar.isEmpty()) {
                    InputStream img = UsuarioController.class.getResourceAsStream("/com/clicktime/web/res/client.png");
                    a.setImagem(IOUtils.toByteArray(img));
                } else {
                    a.setImagem(avatar.getBytes());
                }
                ServiceLocator.getUsuarioService().setAvatar(usuario.getId(), a);

                url = "redirect:/sucesso";
            } else {
                model.addAttribute("errors", errors);
                model.addAttribute("usuario", fields);
                url = "/usuario/cadastro-usuario";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            url = "/error";
        }

        return url;
    }

    //home cliente
    @RequestMapping(value = "/usuario/home", method = RequestMethod.GET)
    public String home(Model m, HttpSession session) {
        String url = "/usuario/home";
        try {
            Map<String, Object> criteria = new HashMap<String, Object>();
            Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
            criteria.put(SolicitacaoCriteria.CLIENTE_FK_EQ, usuario.getId());
            m.addAttribute("solicitacaoCount", ServiceLocator.getSolicitacaoService().countByCriteria(criteria));
            m.addAttribute("profissionalFavorito", ServiceLocator.getUsuarioService().getProfissionalFavorito(usuario.getId()));

            criteria.put(SolicitacaoCriteria.CLIENTE_FK_EQ, usuario.getId());
            DateTime now = new DateTime();
            now = now.minusDays(1);
            criteria.put(SolicitacaoCriteria.DIA_ATENDIMENTO_LE, new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 1, 1));
            criteria.put(SolicitacaoCriteria.STATUS_EQ, Solicitacao.SOLICITACAO_ACEITA);
            criteria.put(SolicitacaoCriteria.IS_AVALIACAO, true);
            List<Solicitacao> solicitacaoList = ServiceLocator.getSolicitacaoService().readByCriteriaSemPaginacao(criteria);
            m.addAttribute("avaliacaoCount", solicitacaoList.size());

        } catch (Exception exception) {
            exception.printStackTrace();
            url = "/error";
        }
        return url;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        session.invalidate();

        return "/usuario/usuario-desconectado";
    }

    @RequestMapping(value = "/sucesso", method = RequestMethod.GET)
    public String sucesso() {
        return "/usuario/sucesso";
    }

    @RequestMapping(value = "/minhaConta", method = RequestMethod.GET)
    public String conta(Model model, HttpSession session) {
        String url = "";

        try {
            Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
            Map<String, Object> criteria = new HashMap<String, Object>();
            if (usuario instanceof Profissional) {
                model.addAttribute("isProfissional", true);
                Profissional profissional = (Profissional) usuario;
                model.addAttribute("usuario", usuario);
                criteria.put(SolicitacaoCriteria.PROFISSIONAL_FK_EQ, usuario.getId());
            } else {
                model.addAttribute("isCliente", true);
                model.addAttribute("usuario", usuario);
                criteria.put(SolicitacaoCriteria.CLIENTE_FK_EQ, usuario.getId());
                model.addAttribute("profissionalFavorito", ServiceLocator.getUsuarioService().getProfissionalFavorito(usuario.getId()));

            }
            model.addAttribute("isUpdate", true);
            model.addAttribute("solicitacaoCount", ServiceLocator.getSolicitacaoService().countByCriteria(criteria));
            model.addAttribute("minhaConta", "active");
            url = "/usuario/cadastro-usuario";
        } catch (Exception e) {
            e.printStackTrace();
            url = "/error";
        }

        return url;
    }

    @RequestMapping(value = "/minhaConta", method = RequestMethod.POST)
    public String conta(Model model, HttpSession session, String nome, String sobrenome,
            String nomeUsuario, String email, String telefone, String descricao,
            String horaInicio, String horaFim, Long id, MultipartFile avatar) {

        String url = "";

        try {
            Map<String, Object> fields = new HashMap<String, Object>();
            fields.put(UsuarioFields.NOME, nome);
            fields.put(UsuarioFields.SOBRENOME, sobrenome);
            fields.put(UsuarioFields.NOME_USUARIO, nomeUsuario);
            fields.put(UsuarioFields.EMAIL, email);
            fields.put(UsuarioFields.TELEFONE, telefone);
            fields.put(UsuarioFields.ID, id);
            fields.put(ProfissionalFields.DESCRICAO, descricao);
            fields.put(ProfissionalFields.HORA_INICIO, horaInicio);
            fields.put(ProfissionalFields.HORA_FIM, horaFim);

            Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
            if (usuarioLogado instanceof Profissional) {
                Map<String, String> errors = ServiceLocator.getProfissionalService().validateForUpdate(fields);
                if (errors.isEmpty()) {
                    Profissional profissional = new Profissional();
                    profissional.setId(id);
                    profissional.setNome(nome);
                    profissional.setSobrenome(sobrenome);
                    profissional.setNomeUsuario(nomeUsuario);
                    profissional.setEmail(email);
                    profissional.setTelefone(telefone);
                    profissional.setDescricao(descricao);
                    profissional.setHoraInicio(CalendarioService.parseStringToDateTime(horaInicio, "HH:mm"));
                    profissional.setHoraFim(CalendarioService.parseStringToDateTime(horaFim, "HH:mm"));
                    ServiceLocator.getProfissionalService().update(profissional);
                    session.removeAttribute("usuarioLogado");
                    session.setAttribute("usuarioLogado", profissional);

                    Avatar a = new Avatar();
                    if (avatar.isEmpty()) {
                        InputStream img = UsuarioController.class.getResourceAsStream("/com/clicktime/web/res/client.png");
                        a.setImagem(IOUtils.toByteArray(img));
                    } else {
                        a.setImagem(avatar.getBytes());
                    }
                    ServiceLocator.getUsuarioService().setAvatar(profissional.getId(), a);

                    url = "redirect:/home";
                } else {
                    url = "/usuario/cadastro-usuario";
                    model.addAttribute("errors", errors);
                    model.addAttribute("usuario", fields);
                    model.addAttribute("isProfissional", true);
                }
            } else {
                Map<String, String> errors = ServiceLocator.getUsuarioService().validateForUpdate(fields);
                if (errors.isEmpty()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(id);
                    usuario.setNome(nome);
                    usuario.setSobrenome(sobrenome);
                    usuario.setNomeUsuario(nomeUsuario);
                    usuario.setEmail(email);
                    usuario.setTelefone(telefone);
                    ServiceLocator.getUsuarioService().update(usuario);
                    session.removeAttribute("usuarioLogado");
                    session.setAttribute("usuarioLogado", usuario);

                    Avatar a = new Avatar();

                    if (avatar.isEmpty()) {
                        InputStream img = UsuarioController.class.getResourceAsStream("/com/clicktime/web/res/client.png");
                        a.setImagem(IOUtils.toByteArray(img));
                    } else {
                        a.setImagem(avatar.getBytes());
                    }
                    ServiceLocator.getUsuarioService().setAvatar(usuario.getId(), a);

                    url = "redirect:/home";
                } else {
                    url = "/usuario/cadastro-usuario";
                    model.addAttribute("errors", errors);
                    model.addAttribute("usuario", fields);
                }
            }
            model.addAttribute("isUpdate", true);
        } catch (Exception exception) {
            exception.printStackTrace();
            url = "/error";
        }

        return url;
    }

    @RequestMapping(value = "/usuarioLogado/img.jpg", method = RequestMethod.GET)
    public void streamImagem(HttpSession session, HttpServletResponse response) throws Exception {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        Avatar avatar = ServiceLocator.getUsuarioService().getAvatar(usuario.getId());
//        if (avatar == null) {
//            InputStream img = UsuarioController.class.getResourceAsStream("/com/clicktime/web/res/client.png");
//            avatar = new Avatar();
//            avatar.setImagem(IOUtils.toByteArray(img));
//        }

        response.setContentType("image/jpg");
        response.getOutputStream().write(avatar.getImagem());
        response.flushBuffer();
    }

}