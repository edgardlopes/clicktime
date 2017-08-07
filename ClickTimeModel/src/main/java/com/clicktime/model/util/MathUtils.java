/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clicktime.model.util;

/**
 *
 * @author edgard
 */
public final class MathUtils {
    private MathUtils(){
        
    }
    
    public static int mdc(int a, int b) {
        int resto;

        while (b != 0) {
            resto = a % b;
            a = b;
            b = resto;
        }

        return a;
    }
}
