/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clicktime.web.interceptor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.HandlerMapping;

/**
 *
 * @author edgard
 */
public final class UriUtils {
    public static final String BASE_APP_CONTEXT =  "/ClickTimeWeb";
    
    private UriUtils(){
        
    }
    
    public static boolean isStaticResourceRequest(String uri){
        return uri.contains("css") || uri.contains("fonts") || uri.contains("img") || uri.contains("js");
    }
    
    public static boolean isFreeRequest(HttpServletRequest request) {
        Set<String> free = new HashSet<>();

        free.add(BASE_APP_CONTEXT + "/profissional/novo");
        free.add(BASE_APP_CONTEXT + "/usuario/novo");
        free.add(BASE_APP_CONTEXT + "/escolha/usuario");
        free.add(BASE_APP_CONTEXT + "/sucesso");
        free.add(BASE_APP_CONTEXT + "/erro");
        free.add(BASE_APP_CONTEXT + "/login");
        free.add(BASE_APP_CONTEXT + "/logout");

        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (pathVariables != null) {
            free.add(BASE_APP_CONTEXT + "/profissional/" + pathVariables.get("urlProfissional"));

            free.add(BASE_APP_CONTEXT + "/profissional/" + pathVariables.get("id") + "/img.jpg");
        }
        
        return free.contains(request.getRequestURI());
    }
}
