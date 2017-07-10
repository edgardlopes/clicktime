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
import static com.clicktime.web.interceptor.SessionUtils.*;
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
    public String autentica(String email, String senha, HttpSession session, Model m) throws Exception {
        //u.setSenha(DigestUtils.md5Hex(u.getSenha()));

        Usuario usuarioLogado = ServiceLocator.getUsuarioService().login(email, senha);

        if (usuarioLogado != null) {
            setLoggedUser(session, usuarioLogado);
            return "redirect:/home";
        }

        return "redirect:/";

    }

    //direciona o usuario para a home correta
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(HttpSession session, Model model) {

        model.addAttribute("isHome", true);

        Usuario usuarioLogado = getLoggedUser(session);
        if (usuarioLogado instanceof Profissional) {
            //sugestao de url interativa profissional/{nome}/home
            return "redirect:/profissional/" + ((Profissional) usuarioLogado).getNomeUsuario() + "/home";
        }
        //sugestao de url interativa usuario/{nome}/home
        return "redirect:/usuario/home";
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
            String email, String telefone, String senha, String senhaConfirm, MultipartFile avatar) throws Exception {
        //usuario.setSenha(DigestUtils.md5Hex(usuario.getSenha()));
        String url = "";
        Map<String, Object> fields = new HashMap<>();
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
                InputStream img = UsuarioController.class.getResourceAsStream("/client.png");
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

        return url;
    }

    //home cliente
    @RequestMapping(value = "/usuario/home", method = RequestMethod.GET)
    public String home(Model m, HttpSession session) throws Exception {
        Usuario usuario = getLoggedUser(session);

        Map<String, Object> criteria = new HashMap<>();
        criteria.put(SolicitacaoCriteria.CLIENTE_FK_EQ, usuario.getId());
        DateTime now = new DateTime();
        now = now.minusDays(1);
        criteria.put(SolicitacaoCriteria.DIA_ATENDIMENTO_LE, new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 1, 1));
        criteria.put(SolicitacaoCriteria.STATUS_EQ, Solicitacao.SOLICITACAO_ACEITA);
        criteria.put(SolicitacaoCriteria.IS_AVALIACAO, true);
        List<Solicitacao> solicitacaoList = ServiceLocator.getSolicitacaoService().readByCriteriaSemPaginacao(criteria);
        m.addAttribute("avaliacaoCount", solicitacaoList.size());
        return "/usuario/home";
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
    public String conta(Model model, HttpSession session) throws Exception {
        Usuario usuario = getLoggedUser(session);
        if (usuario instanceof Profissional) {
            model.addAttribute("isProfissional", true);
        } else {
            model.addAttribute("isCliente", true);
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("isUpdate", true);
        model.addAttribute("minhaConta", "active");
        return "/usuario/cadastro-usuario";
    }

    @RequestMapping(value = "/minhaConta", method = RequestMethod.POST)
    public String conta(Model model, HttpSession session, String nome, String sobrenome,
            String nomeUsuario, String email, String telefone, String descricao,
            String horaInicio, String horaFim, Long id, MultipartFile avatar) throws Exception {

        String url = "";

        Map<String, Object> fields = new HashMap<>();
        fields.put(UsuarioFields.NOME, nome);
        fields.put(UsuarioFields.SOBRENOME, sobrenome);
        fields.put(UsuarioFields.NOME_USUARIO, nomeUsuario);
        fields.put(UsuarioFields.EMAIL, email);
        fields.put(UsuarioFields.TELEFONE, telefone);
        fields.put(UsuarioFields.ID, id);
        fields.put(ProfissionalFields.DESCRICAO, descricao);
        fields.put(ProfissionalFields.HORA_INICIO, horaInicio);
        fields.put(ProfissionalFields.HORA_FIM, horaFim);

        Usuario usuarioLogado = getLoggedUser(session);
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
                setLoggedUser(session, profissional);

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
                setLoggedUser(session, usuario);

                Avatar a = new Avatar();

                if (avatar.isEmpty()) {
                    InputStream img = UsuarioController.class.getResourceAsStream("/client.png");
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

        return url;
    }

    @RequestMapping(value = "/usuarioLogado/img.jpg", method = RequestMethod.GET)
    public void streamImagem(HttpSession session, HttpServletResponse response) throws Exception {
        Usuario usuario = getLoggedUser(session);
        Avatar avatar = ServiceLocator.getUsuarioService().getAvatar(usuario.getId());
        response.setContentType("image/jpg");
        response.getOutputStream().write(avatar.getImagem());
        response.flushBuffer();
    }

}
