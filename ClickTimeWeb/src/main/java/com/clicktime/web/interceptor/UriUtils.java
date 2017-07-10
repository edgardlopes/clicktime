/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clicktime.web.interceptor;

/**
 *
 * @author edgard
 */
public final class UriUtils {
    private UriUtils(){
        
    }
    
    public static boolean isStaticResource(String uri){
        return uri.contains("css") || uri.contains("fonts") || uri.contains("img") || uri.contains("js");
    }
}
