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

    private SessionUtils() {

    }

    public static boolean hasLoggedUser(HttpSession session) {
        return session.getAttribute("usuarioLogado") != null;
    }

    public static Usuario getLoggedUser(HttpSession session) {
        return (Usuario) session.getAttribute("usuarioLogado");
    }
}
