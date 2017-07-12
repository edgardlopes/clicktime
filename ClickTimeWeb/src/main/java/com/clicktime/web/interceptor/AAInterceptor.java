package com.clicktime.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AAInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI();

        if (UriUtils.isStaticResourceRequest(uri)) {
            return true;
        }

        if (UriUtils.isFreeRequest(request)) {
            return true;
        }

        if (SessionUtils.hasLoggedUser(request.getSession())) {
            return true;
        }

        response.sendRedirect(UriUtils.BASE_APP_CONTEXT + "/erro");
        return false;
    }

}
