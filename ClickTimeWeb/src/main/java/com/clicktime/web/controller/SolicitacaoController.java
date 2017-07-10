package com.clicktime.web.controller;

import com.clicktime.model.ServiceLocator;
import com.clicktime.model.criteria.SolicitacaoCriteria;
import com.clicktime.model.dao.SolicitacaoDAO;
import com.clicktime.model.entity.DiaAtendimento;
import com.clicktime.model.entity.Execucao;
import com.clicktime.model.entity.HorarioAtendimento;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.entity.Solicitacao;
import com.clicktime.model.entity.Usuario;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SolicitacaoController {

    @RequestMapping(value = "/reservarHorario", method = RequestMethod.POST)
    public String reservarHorario(String execucaoID, String horarioSelecionado, HttpSession session, Model model) throws Exception {
        String url = "";
        String[] aux = horarioSelecionado.split(", ");

        Long clienteFK = ((Usuario) session.getAttribute("usuarioLogado")).getId();
        DiaAtendimento dia = ServiceLocator.getHorarioAtendimentoService().readById(Long.parseLong(aux[0])).getDiaAtendimento();
        HorarioAtendimento horaInicio = ServiceLocator.getHorarioAtendimentoService().readById(Long.parseLong(aux[0]));
        HorarioAtendimento horaFim = ServiceLocator.getHorarioAtendimentoService().readById(Long.parseLong(aux[aux.length - 1]));

        if (!ServiceLocator.getSolicitacaoService().existsSolicitacao(Long.parseLong(execucaoID), clienteFK, dia.getId(), horaInicio.getHoraInicio(), horaFim.getHoraFim())) {
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

            Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
            solicitacao.setUsuario(usuario);

            ServiceLocator.getSolicitacaoService().solicitarHorario(solicitacao);
            solicitacao = ServiceLocator.getSolicitacaoService().readById(solicitacao.getId());
            model.addAttribute("solicitacao", solicitacao);

            url = "/solicitacao/cliente/sucesso";

            Map<String, Object> criteria = new HashMap<>();
            criteria.put(SolicitacaoCriteria.CLIENTE_FK_EQ, usuario.getId());
            Long count = ServiceLocator.getSolicitacaoService().countByCriteria(criteria);
            model.addAttribute("profissionalFavorito", ServiceLocator.getUsuarioService().getProfissionalFavorito(usuario.getId()));
            model.addAttribute("solicitacaoCount", count);

        } else {
            url = "/solicitacao/ja-reservada";
        }
        return url;
    }

    @RequestMapping(value = "/solicitacoes", method = RequestMethod.GET)
    public String list(HttpSession session, Model model, String dataInicio, String dataFim, String status, Integer pagina) throws Exception {
        String url = "";

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

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        List<Solicitacao> solicitacaoList = null;
//        solicitacaoList.get(0).getHorarioAtendimentoList().get(0).getDiaAtendimento().
        if (usuario instanceof Profissional) {
            criteria.put(SolicitacaoCriteria.PROFISSIONAL_FK_EQ, usuario.getId());
            url = "/solicitacao/profissional/list";
        } else {
            criteria.put(SolicitacaoCriteria.CLIENTE_FK_EQ, usuario.getId());
            model.addAttribute("profissionalFavorito", ServiceLocator.getUsuarioService().getProfissionalFavorito(usuario.getId()));
            url = "/solicitacao/cliente/list";
        }

        solicitacaoList = ServiceLocator.getSolicitacaoService().readByCriteria(criteria, (pagina - 1) * SolicitacaoDAO.LIMIT);
//                solicitacaoList = ServiceLocator.getSolicitacaoService().readByCriteria(criteria, null);
        Long count = ServiceLocator.getSolicitacaoService().countByCriteria(criteria);
        Integer paginas = Math.round(count.floatValue() / SolicitacaoDAO.LIMIT.floatValue());
        model.addAttribute("solicitacaoList", solicitacaoList);
        model.addAttribute("pagina", pagina);
        model.addAttribute("countPaginas", paginas);

        criteria = new HashMap<>();
        if (usuario instanceof Profissional) {
            criteria.put(SolicitacaoCriteria.PROFISSIONAL_FK_EQ, usuario.getId());
        } else {
            criteria.put(SolicitacaoCriteria.CLIENTE_FK_EQ, usuario.getId());
        }
        model.addAttribute("solicitacaoCount", ServiceLocator.getSolicitacaoService().countByCriteria(criteria));

        model.addAttribute("solicitacao", "active");

        return url;
    }

    @RequestMapping(value = "/solicitacao/{id}/aceitar", method = RequestMethod.GET)
    public String aceitarSolicitacao(HttpSession session, @PathVariable Long id, Model m) throws Exception {
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setId(id);
        List<Solicitacao> solicitacoesCanceladas = ServiceLocator.getSolicitacaoService().aceitarHorario(solicitacao);
        if (solicitacoesCanceladas.isEmpty()) {
            return "redirect:/solicitacoes";
        }

        m.addAttribute("solicitacoesCanceladas", solicitacoesCanceladas);
        Map<String, Object> criteria = new HashMap<>();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        criteria.put(SolicitacaoCriteria.PROFISSIONAL_FK_EQ, usuario.getId());
        m.addAttribute("solicitacaoCount", ServiceLocator.getSolicitacaoService().countByCriteria(criteria));
        return "/solicitacao/profissional/solicitacao_cancelada_list";
    }

    @RequestMapping(value = "/solicitacao/rejeitar", method = RequestMethod.POST)
    public String rejeitarSolicitacao(Long solicitacaoID, String idList, String descricao) throws Exception {
        //adicionar justificativa 
        Solicitacao solicitacao = ServiceLocator.getSolicitacaoService().readById(solicitacaoID);
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
            ServiceLocator.getSolicitacaoService().remarcarSolicitacao(solicitacao);
        } else {
            ServiceLocator.getSolicitacaoService().recusarHorario(solicitacao);
        }
        return "redirect:/solicitacoes";
    }

    @RequestMapping(value = "/solicitacao/remarcar", method = RequestMethod.POST)
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

            ServiceLocator.getSolicitacaoService().remarcarSolicitacao(solicitacao);
            response.setStatus(200);
        } catch (Exception exception) {
            exception.printStackTrace();
            response.setStatus(500);
        }
    }

    @RequestMapping(value = "/solicitacao/{id}/aceitarNovo", method = RequestMethod.GET)
    public String aceitarNovo(@PathVariable Long id) throws Exception {
        Solicitacao solicitacao = ServiceLocator.getSolicitacaoService().readById(id);
        ServiceLocator.getSolicitacaoService().aceitarHorario(solicitacao);

        return "redirect:/solicitacoes";
    }

    @RequestMapping(value = "/solicitacao/{id}/recusarNovo", method = RequestMethod.GET)
    public String recusarNovo(@PathVariable Long id) throws Exception {
        Solicitacao solicitacao = ServiceLocator.getSolicitacaoService().readById(id);
        ServiceLocator.getSolicitacaoService().recusarHorario(solicitacao);
        return "redirect:/solicitacoes";
    }

    @RequestMapping(value = "/horarioatendimento/solicitacao", method = RequestMethod.GET)
    public String getSolicitacaoFromHorarioAtendimento(Long horarioAtendimentoID, Model model, String status) throws Exception {
        Solicitacao solicitacao = ServiceLocator.getSolicitacaoService().getSolicitacaoFromHorarioAtendimentoId(horarioAtendimentoID, status);
        model.addAttribute("solicitacao", solicitacao);
        return "/agenda/profissional/solicitacao-resumo";
    }

    @RequestMapping(value = "/avaliacoes", method = RequestMethod.GET)
    public String getAvaliacoes(Model model, HttpSession session) throws Exception {
        Map<String, Object> criteria = new HashMap<>();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        criteria.put(SolicitacaoCriteria.CLIENTE_FK_EQ, usuario.getId());

        DateTime now = new DateTime();
        now = now.minusDays(1);
        criteria.put(SolicitacaoCriteria.DIA_ATENDIMENTO_LE, new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 1, 1));
        criteria.put(SolicitacaoCriteria.STATUS_EQ, Solicitacao.SOLICITACAO_ACEITA);
        criteria.put(SolicitacaoCriteria.IS_AVALIACAO, true);

        List<Solicitacao> solicitacaoList = ServiceLocator.getSolicitacaoService().readByCriteriaSemPaginacao(criteria);

        model.addAttribute("solicitacaoList", solicitacaoList);
        model.addAttribute("isAvaliacao", true);

        criteria = new HashMap<>();
        criteria.put(SolicitacaoCriteria.CLIENTE_FK_EQ, usuario.getId());
        model.addAttribute("solicitacaoCount", ServiceLocator.getSolicitacaoService().countByCriteria(criteria));
        model.addAttribute("profissionalFavorito", ServiceLocator.getUsuarioService().getProfissionalFavorito(usuario.getId()));
        model.addAttribute("avaliacao", "active");
        return "/solicitacao/cliente/list";
    }

    @RequestMapping(value = "/avaliarSolicitacao", method = RequestMethod.POST)
    public void avaliar(Long solicitacaoID, Float score, HttpServletResponse response) {

        try {
            ServiceLocator.getSolicitacaoService().avaliar(solicitacaoID, score);
            response.setStatus(200);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(400);
        }
    }
}
