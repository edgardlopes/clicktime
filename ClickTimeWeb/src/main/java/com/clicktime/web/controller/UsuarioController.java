package com.clicktime.web.controller;

import com.clicktime.model.base.service.BaseProfissionalService;
import com.clicktime.model.base.service.BaseSolicitacaoService;
import com.clicktime.model.base.service.BaseUsuarioService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UsuarioController {

    @Autowired
    private BaseProfissionalService profissionalService;

    @Autowired
    private BaseUsuarioService usuarioService;

    @Autowired
    private BaseSolicitacaoService solicitacaoService;

    //menu de escolha de usuario ao cadastrar...
    @GetMapping("/escolha/usuario")
    public String selectUser(Model m) {
        return "/usuario/escolha-tipo-usuario";
    }

    @PostMapping("/login")
    public String autentica(String email, String senha, HttpSession session, Model m) throws Exception {
        //u.setSenha(DigestUtils.md5Hex(u.getSenha()));

        Usuario usuarioLogado = usuarioService.login(email, senha);

        if (usuarioLogado != null) {
            setLoggedUser(session, usuarioLogado);
            return "redirect:/home";
        }

        return "redirect:/";
    }

    //direciona o usuario para a home correta
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {

        Usuario usuarioLogado = getLoggedUser(session);
        if (usuarioLogado instanceof Profissional) {
            //sugestao de url interativa profissional/{nome}/home
            return "redirect:/profissional/" + ((Profissional) usuarioLogado).getNomeUsuario() + "/home";
        }
        //sugestao de url interativa usuario/{nome}/home
        return "redirect:/usuario/home";
    }

    //formulario de cadastro
    @GetMapping("/usuario/novo")
    public String create(Model m) {
        return "/usuario/cadastro-usuario";
    }

    //formulario de cadastro POST
    @PostMapping("/usuario/novo")
    public String create(Model model, String nome, String sobrenome, String nomeUsuario,
            String email, String telefone, String senha, String senhaConfirm, MultipartFile avatar) throws Exception {
        //usuario.setSenha(DigestUtils.md5Hex(usuario.getSenha()));
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
        Map<String, String> errors = usuarioService.validateForCreate(fields);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("usuario", fields);
            return "/usuario/cadastro-usuario";
        }

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setSobrenome(sobrenome);
        usuario.setNomeUsuario(nomeUsuario);
        usuario.setEmail(email);
        usuario.setTelefone(telefone);
        usuario.setSenha(senha);
        usuarioService.create(usuario);

        if (!avatar.isEmpty()) {
            Avatar a = new Avatar();
            a.setImagem(avatar.getBytes());
            usuarioService.setAvatar(usuario.getId(), a);
        }

        return "redirect:/";
    }

    //home cliente
    @GetMapping("/usuario/home")
    public String home(Model m, HttpSession session) throws Exception {
        Usuario usuario = getLoggedUser(session);

        Map<String, Object> criteria = new HashMap<>();
        criteria.put(SolicitacaoCriteria.CLIENTE_FK_EQ, usuario.getId());
        DateTime now = new DateTime();
        now = now.minusDays(1);
        criteria.put(SolicitacaoCriteria.DIA_ATENDIMENTO_LE, new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 1, 1));
        criteria.put(SolicitacaoCriteria.STATUS_EQ, Solicitacao.SOLICITACAO_ACEITA);
        criteria.put(SolicitacaoCriteria.IS_AVALIACAO, true);
        List<Solicitacao> solicitacaoList = solicitacaoService.readByCriteriaSemPaginacao(criteria);
        m.addAttribute("avaliacaoCount", solicitacaoList.size());
        return "/usuario/home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "/error_usuario";
    }

    @GetMapping("/sucesso")
    public String sucesso() {
        return "/usuario/sucesso";
    }

    @GetMapping("/minhaConta")
    public String conta(Model model, HttpSession session) throws Exception {
        Usuario usuario = getLoggedUser(session);
        if (usuario instanceof Profissional) {
            model.addAttribute("isProfissional", true);
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("isUpdate", true);
        return "/usuario/update";
    }

    @PostMapping("/minhaConta")
    public String conta(Model model, HttpSession session, String nome, String sobrenome,
            String nomeUsuario, String email, String telefone, String descricao,
            String horaInicio, String horaFim, Long id, MultipartFile avatar) throws Exception {

        model.addAttribute("isUpdate", true);

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
            Map<String, String> errors = profissionalService.validateForUpdate(fields);
            if (!errors.isEmpty()) {
                model.addAttribute("errors", errors);
                model.addAttribute("usuario", fields);
                model.addAttribute("isProfissional", true);
                return "/usuario/cadastro-usuario";
            }

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
            profissionalService.update(profissional);
            setLoggedUser(session, profissional);

            if (!avatar.isEmpty()) {
                Avatar a = new Avatar();
                a.setImagem(avatar.getBytes());
                usuarioService.setAvatar(profissional.getId(), a);
            }

            return "redirect:/home";
        }

        Map<String, String> errors = usuarioService.validateForUpdate(fields);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("usuario", fields);
            return "/usuario/cadastro-usuario";
        }
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome(nome);
        usuario.setSobrenome(sobrenome);
        usuario.setNomeUsuario(nomeUsuario);
        usuario.setEmail(email);
        usuario.setTelefone(telefone);
        usuarioService.update(usuario);
        setLoggedUser(session, usuario);

        Avatar a = new Avatar();

        if (avatar.isEmpty()) {
            InputStream img = UsuarioController.class.getResourceAsStream("/client.png");
            a.setImagem(IOUtils.toByteArray(img));
        } else {
            a.setImagem(avatar.getBytes());
        }
        usuarioService.setAvatar(usuario.getId(), a);

        return "redirect:/home";
    }

    @GetMapping("/usuarioLogado/img.jpg")
    public void streamImagem(HttpSession session, HttpServletResponse response) throws Exception {
        Usuario usuario = getLoggedUser(session);
        Avatar avatar = usuarioService.getAvatar(usuario.getId());
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
