/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clicktime.web.interceptor;

import com.clicktime.model.entity.Usuario;
import javax.servlet.http.HttpSession;

/**
 *
 * @author edgard
 */
public final class SessionUtils {

    public static final String USUARIO_LOGADO = "usuarioLogado";

    private SessionUtils() {

    }

    public static boolean hasLoggedUser(HttpSession session) {
        return getLoggedUser(session) != null;
    }

    public static Usuario getLoggedUser(HttpSession session) {
        return (Usuario) session.getAttribute(USUARIO_LOGADO);
    }
    
    public static void setLoggedUser(HttpSession session, Usuario usuario){
        session.setAttribute(USUARIO_LOGADO, usuario);
    }
}
