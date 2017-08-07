/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clicktime.model.util;

import com.clicktime.model.ErrorMessage;
import com.clicktime.model.fields.ProfissionalFields;
import com.clicktime.model.service.calendario.CalendarioService;

/**
 *
 * @author edgard
 */
public final class TimeUtils {

    private TimeUtils() {
    }

    public static boolean isValid(String time) {
        try {
            CalendarioService.parseStringToDateTime(time, "HH:mm");
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

}
