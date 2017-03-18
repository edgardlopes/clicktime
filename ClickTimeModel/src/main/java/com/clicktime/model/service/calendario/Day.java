/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.clicktime.model.service.calendario;

import com.clicktime.model.entity.DiaAtendimentoResumo;
import org.joda.time.DateTime;

/**
 * 
 * @author Edgard Lopes <edgard-rodrigo@hotmail.com>
 */
public class Day {
    private DateTime day;
    private String dayOfWeekShort;
    private DiaAtendimentoResumo resumo;

    public Day() {
    }

    public Day(DateTime dt) {
        this.day = dt;
        this.dayOfWeekShort = dt.dayOfWeek().getAsShortText();
    }
    
    
    
    public DateTime getDay() {
        return day;
    }

    public void setDay(DateTime day) {
        this.day = day;
    }

    public String getDayOfWeekShort() {
        return dayOfWeekShort;
    }

    public void setDayOfWeekShort(String dayOfWeekShort) {
        this.dayOfWeekShort = dayOfWeekShort;
    }

    @Override
    public String toString() {
        return day +" "+ dayOfWeekShort;
    }

    public void setResumo(DiaAtendimentoResumo resumo) {
        this.resumo = resumo;
    }

    public DiaAtendimentoResumo getResumo() {
        return resumo;
    }
    
    
}
