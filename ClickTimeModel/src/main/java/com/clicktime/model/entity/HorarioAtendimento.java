package com.clicktime.model.entity;

import com.clicktime.model.base.BaseEntity;
import java.util.Objects;
import org.joda.time.DateTime;

public class HorarioAtendimento extends BaseEntity implements Comparable<HorarioAtendimento>{
    public static final String HORARIO_BLOQUEADO = "B";
    public static final String HORARIO_LIVRE = "L";
    public static final String HORARIO_RESERVADO = "R";
    public static final String AGUARDANDO_CLIENTE_ACEITAR = "1";
        
    
//    @DateTimeFormat(pattern = "HH:mm")
    private DateTime horaInicio;
//    @DateTimeFormat(pattern = "HH:mm")
    private DateTime horaFim;
    private String status;
    private DiaAtendimento diaAtendimento;
    
    public DiaAtendimento getDiaAtendimento() {
        return diaAtendimento;
    }

    public void setDiaAtendimento(DiaAtendimento diaAtendimento) {
        this.diaAtendimento = diaAtendimento;
    }

    public DateTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(DateTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public DateTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(DateTime horaFim) {
        this.horaFim = horaFim;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HorarioAtendimento other = (HorarioAtendimento) obj;
        if (!Objects.equals(this.horaInicio, other.horaInicio)) {
            return false;
        }
        if (!Objects.equals(this.horaFim, other.horaFim)) {
            return false;
        }
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        if (!Objects.equals(this.diaAtendimento, other.diaAtendimento)) {
            return false;
        }
        return true;
    }


    
    @Override
    public int compareTo(HorarioAtendimento o) {
        if(this.getHoraInicio().getMinuteOfDay() < o.getHoraInicio().getMinuteOfDay()){
            return -1;
        }
        if(this.getHoraInicio().getMinuteOfDay()> o.getHoraInicio().getMinuteOfDay()){
            return 1;
        }
        
        return 0;
    }
    
    

}
