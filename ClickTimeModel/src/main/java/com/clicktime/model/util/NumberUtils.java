/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clicktime.model.util;

import com.clicktime.model.ErrorMessage;
import com.clicktime.model.fields.ExecucaoServicoFields;

/**
 *
 * @author edgard
 */
public final class NumberUtils {

    private NumberUtils() {

    }

    public static boolean isLongValid(String n) {
        try {
            Long.parseLong(n);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isFloatValid(String n) {
        try {
            Float.parseFloat(n);
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }
}
