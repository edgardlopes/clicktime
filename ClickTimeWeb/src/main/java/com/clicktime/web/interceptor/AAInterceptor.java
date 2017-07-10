package com.clicktime.web.interceptor;

import com.sun.jndi.toolkit.url.Uri;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AAInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

//        System.out.println(request.getRequestURI());

        Set<String> free = new HashSet<String>();
        String context = "/ClickTimeWeb";
        
        free.add(context + "/profissional/novo");
        free.add(context + "/usuario/novo");
        free.add(context + "/escolha/usuario");
        free.add(context + "/sucesso");
        free.add(context + "/erro");
        free.add(context + "/login");
        free.add(context + "/logout");

        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (pathVariables != null) {
            free.add(context + "/profissional/" + pathVariables.get("urlProfissional"));

            free.add(context + "/profissional/" + pathVariables.get("id") + "/img.jpg");
        }

        boolean ok = false;
        boolean redirectErrorUsuario = false;
        String uri = request.getRequestURI();

        if (UriUtils.isStaticResource(uri)) {
            ok = true;
        } else if (!free.contains(request.getRequestURI())) {
            if (SessionUtils.hasLoggedUser(request.getSession())) {
                ok = true;
            } else {
                redirectErrorUsuario = true;
            }
        } else {
            ok = true;
        }
        if (!ok && !redirectErrorUsuario) {
            response.sendRedirect(context + "/index.jsp");
        } else if (redirectErrorUsuario) {
            response.sendRedirect(context + "/erro");
        }
        return ok;
    }
}
