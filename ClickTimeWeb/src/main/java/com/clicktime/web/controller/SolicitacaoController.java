package com.clicktime.web.controller;

import com.clicktime.model.base.service.BaseHorarioAtendimentoService;
import com.clicktime.model.base.service.BaseSolicitacaoService;
import com.clicktime.model.criteria.SolicitacaoCriteria;
import com.clicktime.model.dao.SolicitacaoDAO;
import com.clicktime.model.entity.DiaAtendimento;
import com.clicktime.model.entity.Execucao;
import com.clicktime.model.entity.HorarioAtendimento;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.entity.Solicitacao;
import com.clicktime.model.entity.Usuario;
import static com.clicktime.web.interceptor.SessionUtils.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SolicitacaoController {

    @Autowired
    private BaseHorarioAtendimentoService horarioAtendimentoService;

    @Autowired
    private BaseSolicitacaoService solicitacaoService;

    @PostMapping("/reservarHorario")
    public String reservarHorario(String execucaoID, String horarioSelecionado, HttpSession session, Model model) throws Exception {
        String[] aux = horarioSelecionado.split(", ");
        final Usuario usuarioLogado = getLoggedUser(session);

        Long clienteFK = usuarioLogado.getId();
        DiaAtendimento dia = horarioAtendimentoService.readById(Long.parseLong(aux[0])).getDiaAtendimento();
        HorarioAtendimento horaInicio = horarioAtendimentoService.readById(Long.parseLong(aux[0]));
        HorarioAtendimento horaFim = horarioAtendimentoService.readById(Long.parseLong(aux[aux.length - 1]));

        if (solicitacaoService.existsSolicitacao(Long.parseLong(execucaoID), clienteFK, dia.getId(), horaInicio.getHoraInicio(), horaFim.getHoraFim())) {
            return "/solicitacao/ja-reservada";
        }

        List<HorarioAtendimento> horarioAtendimentoList = new ArrayList<>();

        for (String id : aux) {
            HorarioAtendimento horarioAtendimento = new HorarioAtendimento();
            horarioAtendimento.setId(Long.parseLong(id));
            horarioAtendimentoList.add(horarioAtendimento);
        }

        Execucao execucao = new Execucao();
        execucao.setId(Long.parseLong(execucaoID));

        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setExecucao(execucao);
        solicitacao.setHorarioAtendimentoList(horarioAtendimentoList);

        solicitacao.setUsuario(usuarioLogado);

        solicitacaoService.solicitarHorario(solicitacao);
        solicitacao = solicitacaoService.readById(solicitacao.getId());
        model.addAttribute("solicitacao", solicitacao);

        return "/solicitacao/cliente/sucesso";
    }

    @GetMapping("/solicitacoes")
    public String list(HttpSession session, Model model, String dataInicio, String dataFim, String status, Integer pagina) throws Exception {
        if (pagina == null) {
            pagina = 1;
        }

        Map<String, Object> criteria = new HashMap<>();

        if (dataInicio != null && !dataInicio.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
            try {
                DateTime dt = formatter.parseDateTime(dataInicio);
                criteria.put(SolicitacaoCriteria.DIA_ATENDIMENTO_ME, dt);
                model.addAttribute("dataInicio", dataInicio);
            } catch (Exception e) {
            }
        }

        if (dataFim != null && !dataFim.isEmpty()) {

            DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
            try {
                DateTime dt = formatter.parseDateTime(dataFim);
                criteria.put(SolicitacaoCriteria.DIA_ATENDIMENTO_LE, dt);
                model.addAttribute("dataFim", dataFim);
            } catch (Exception e) {
            }
        }

        if (status != null && !status.isEmpty()) {
            if (status.toLowerCase().equals("recusados")) {
                criteria.put(SolicitacaoCriteria.STATUS_EQ, Solicitacao.SOLICITACAO_REJEITADA);
            }
            if (status.toLowerCase().equals("aguardando")) {
                criteria.put(SolicitacaoCriteria.STATUS_EQ, Solicitacao.AGUARDANDO_CONFIRMACAO);
            }
            if (status.toLowerCase().equals("aceitos")) {
                criteria.put(SolicitacaoCriteria.STATUS_EQ, Solicitacao.SOLICITACAO_ACEITA);
            }
            if (status.toLowerCase().equals("remarcado")) {
                criteria.put(SolicitacaoCriteria.STATUS_EQ, Solicitacao.SOLICITACAO_REMARCADA);
            }
            model.addAttribute("status", status);
        }

        Usuario usuario = getLoggedUser(session);

        criteria.put(usuario instanceof Profissional
                ? SolicitacaoCriteria.PROFISSIONAL_FK_EQ : SolicitacaoCriteria.CLIENTE_FK_EQ, usuario.getId());

        List<Solicitacao> solicitacaoList = solicitacaoService.readByCriteria(criteria, (pagina - 1) * SolicitacaoDAO.LIMIT);
        Long count = solicitacaoService.countByCriteria(criteria);
        Integer paginas = Math.round(count.floatValue() / SolicitacaoDAO.LIMIT.floatValue());
        model.addAttribute("solicitacaoList", solicitacaoList);
        model.addAttribute("pagina", pagina);
        model.addAttribute("countPaginas", paginas);


        return usuario instanceof Profissional
                ? "/solicitacao/profissional/list" : "/solicitacao/cliente/list";
    }

    @GetMapping("/solicitacao/{id}/aceitar")
    public String aceitarSolicitacao(HttpSession session, @PathVariable Long id, Model m) throws Exception {
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setId(id);
        List<Solicitacao> solicitacoesCanceladas = solicitacaoService.aceitarHorario(solicitacao);
        if (solicitacoesCanceladas.isEmpty()) {
            return "redirect:/solicitacoes";
        }

        m.addAttribute("solicitacoesCanceladas", solicitacoesCanceladas);
        return "/solicitacao/profissional/solicitacao_cancelada_list";
    }

    @PostMapping("/solicitacao/rejeitar")
    public String rejeitarSolicitacao(Long solicitacaoID, String idList, String descricao) throws Exception {
        //adicionar justificativa 
        Solicitacao solicitacao = solicitacaoService.readById(solicitacaoID);
        if (idList != null && !idList.isEmpty()) {
            String[] aux = idList.split(", ");
            List<HorarioAtendimento> horarioAtendimentoList = new ArrayList<>();

            for (String id : aux) {
                HorarioAtendimento horarioAtendimento = new HorarioAtendimento();
                horarioAtendimento.setId(Long.parseLong(id));
                horarioAtendimentoList.add(horarioAtendimento);
                horarioAtendimento.setStatus(HorarioAtendimento.AGUARDANDO_CLIENTE_ACEITAR);
            }

            solicitacao.setHorarioAtendimentoList(horarioAtendimentoList);
            solicitacao.setDescricao(descricao);
            solicitacao.setStatus(Solicitacao.SOLICITACAO_REMARCADA);
            solicitacaoService.remarcarSolicitacao(solicitacao);
        } else {
            solicitacaoService.recusarHorario(solicitacao);
        }
        return "redirect:/solicitacoes";
    }

    @PostMapping("/solicitacao/remarcar")
    public void remarcar(HttpServletResponse response, Long solicitacaoID, String horarioAtendimentoID, String descricao) {
        try {
            String[] aux = horarioAtendimentoID.split(", ");
            List<HorarioAtendimento> horarioAtendimentoList = new ArrayList<>();

            for (String id : aux) {
                HorarioAtendimento horarioAtendimento = new HorarioAtendimento();
                horarioAtendimento.setId(Long.parseLong(id));
                horarioAtendimentoList.add(horarioAtendimento);
                horarioAtendimento.setStatus(HorarioAtendimento.AGUARDANDO_CLIENTE_ACEITAR);
            }

            Solicitacao solicitacao = new Solicitacao();
            solicitacao.setId(solicitacaoID);
            solicitacao.setHorarioAtendimentoList(horarioAtendimentoList);
            solicitacao.setDescricao(descricao);
            solicitacao.setStatus(Solicitacao.SOLICITACAO_REMARCADA);

            solicitacaoService.remarcarSolicitacao(solicitacao);
            response.setStatus(200);
        } catch (Exception exception) {
            exception.printStackTrace();
            response.setStatus(500);
        }
    }

    @GetMapping("/solicitacao/{id}/aceitarNovo")
    public String aceitarNovo(@PathVariable Long id) throws Exception {
        Solicitacao solicitacao = solicitacaoService.readById(id);
        solicitacaoService.aceitarHorario(solicitacao);

        return "redirect:/solicitacoes";
    }

    @GetMapping("/solicitacao/{id}/recusarNovo")
    public String recusarNovo(@PathVariable Long id) throws Exception {
        Solicitacao solicitacao = solicitacaoService.readById(id);
        solicitacaoService.recusarHorario(solicitacao);
        return "redirect:/solicitacoes";
    }

    @GetMapping("/horarioatendimento/solicitacao")
    public String getSolicitacaoFromHorarioAtendimento(Long horarioAtendimentoID, Model model, String status) throws Exception {
        Solicitacao solicitacao = solicitacaoService.getSolicitacaoFromHorarioAtendimentoId(horarioAtendimentoID, status);
        model.addAttribute("solicitacao", solicitacao);
        return "/agenda/profissional/solicitacao-resumo";
    }

    @GetMapping("/avaliacoes")
    public String getAvaliacoes(Model model, HttpSession session) throws Exception {
        Map<String, Object> criteria = new HashMap<>();
        Usuario usuario = getLoggedUser(session);
        criteria.put(SolicitacaoCriteria.CLIENTE_FK_EQ, usuario.getId());

        DateTime now = new DateTime();
        now = now.minusDays(1);
        criteria.put(SolicitacaoCriteria.DIA_ATENDIMENTO_LE, new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 1, 1));
        criteria.put(SolicitacaoCriteria.STATUS_EQ, Solicitacao.SOLICITACAO_ACEITA);
        criteria.put(SolicitacaoCriteria.IS_AVALIACAO, true);

        List<Solicitacao> solicitacaoList = solicitacaoService.readByCriteriaSemPaginacao(criteria);

        model.addAttribute("solicitacaoList", solicitacaoList);
        model.addAttribute("isAvaliacao", true);

        return "/solicitacao/cliente/list";
    }

    @PostMapping("/avaliarSolicitacao")
    public void avaliar(Long solicitacaoID, Float score, HttpServletResponse response) {

        try {
            solicitacaoService.avaliar(solicitacaoID, score);
            response.setStatus(200);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(400);
        }
    }
}
