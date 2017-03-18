package com.clicktime.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.joda.time.DateTime;

/**
 * horaInicio e horaFim refere-se ao horario de expediente do profissional é
 * util para gerar horarios mais consistentes
 */
public class Profissional extends Usuario {

    private String descricao;
    private DateTime horaInicio;
    private DateTime horaFim;
    private List<Execucao> execucao;
    private List<HorarioAtendimento> horarios;
    private DateTime unidadeTempo;
    private Float pontos;

    public Profissional() {
        execucao = new ArrayList<>();
        horarios = new ArrayList<>();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Execucao> getExecucao() {
        return execucao;
    }

    public void setExecucao(List<Execucao> execucao) {
        this.execucao = execucao;
    }

    public List<HorarioAtendimento> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<HorarioAtendimento> horarios) {
        this.horarios = horarios;
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

    public DateTime getUnidadeTempo() {
        return unidadeTempo;
    }

    public void setUnidadeTempo(DateTime unidadeTempo) {
        this.unidadeTempo = unidadeTempo;
    }

    public Float getPontos() {
        return pontos;
    }

    public void setPontos(Float pontos) {
        this.pontos = pontos;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Profissional other = (Profissional) obj;
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }

        if (!Objects.equals(this.horaInicio, other.horaInicio)) {
            return false;
        }
        if (!Objects.equals(this.horaFim, other.horaFim)) {
            return false;
        }
        return true;
    }

}
