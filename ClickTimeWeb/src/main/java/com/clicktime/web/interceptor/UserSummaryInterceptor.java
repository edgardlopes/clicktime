package com.clicktime.web.interceptor;

import com.clicktime.model.ServiceLocator;
import com.clicktime.model.criteria.SolicitacaoCriteria;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.entity.Usuario;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class UserSummaryInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler, ModelAndView mv) throws Exception {
//
//        HttpSession session = request.getSession();
//        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
//        if (!(usuario instanceof Profissional)) {
//            Map<String, Object> criteria = new HashMap<>();
//            criteria.put(SolicitacaoCriteria.CLIENTE_FK_EQ, usuario.getId());
//            mv.addObject("solicitacaoCount", ServiceLocator.getSolicitacaoService().countByCriteria(criteria));
//            mv.addObject("profissionalFavorito", ServiceLocator.getUsuarioService().getProfissionalFavorito(usuario.getId()));
//        }
    }

}
