package com.clicktime.model.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author edgard
 */
public final class StringUtils {
    private StringUtils(){}
    
    public static boolean isBlank(String s){
        return s == null || s.isEmpty();
    }
}
