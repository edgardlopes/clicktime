package com.clicktime.web.interceptor;

import com.clicktime.model.base.service.BaseSolicitacaoService;
import com.clicktime.model.base.service.BaseUsuarioService;
import com.clicktime.model.criteria.SolicitacaoCriteria;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.entity.Usuario;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class UserSummaryInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private BaseUsuarioService usuarioService;

    @Autowired
    private BaseSolicitacaoService solicitacaoService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler, ModelAndView mv) throws Exception {

        if (UriUtils.isStaticResourceRequest(request.getRequestURI())) {
            return;
        }

        HttpSession session = request.getSession();
        if (!SessionUtils.hasLoggedUser(session)) {
            return;
        }

        Usuario usuario = SessionUtils.getLoggedUser(session);
        mv.addObject("date", DateTime.now());
        if (usuario instanceof Profissional) {
            Map<String, Object> criteria = new HashMap<>();
            criteria.put(SolicitacaoCriteria.PROFISSIONAL_FK_EQ, usuario.getId());
            mv.addObject("solicitacaoCount", solicitacaoService.countByCriteria(criteria));

        } else {
            Map<String, Object> criteria = new HashMap<>();
            criteria.put(SolicitacaoCriteria.CLIENTE_FK_EQ, usuario.getId());
            mv.addObject("solicitacaoCount", solicitacaoService.countByCriteria(criteria));
            mv.addObject("profissionalFavorito", usuarioService.getProfissionalFavorito(usuario.getId()));
        }
    }

}
