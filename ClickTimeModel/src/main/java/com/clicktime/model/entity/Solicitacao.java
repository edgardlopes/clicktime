package com.clicktime.model.entity;

import com.clicktime.model.base.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Solicitacao extends BaseEntity {
    public static final String AGUARDANDO_CONFIRMACAO = "A";
    public static final String SOLICITACAO_ACEITA = "S";
    public static final String SOLICITACAO_REJEITADA = "R";
    public static final String SOLICITACAO_REMARCADA = "1";
    
    private String status;
    private Execucao execucao;
    private Usuario usuario;
    private List<HorarioAtendimento> horarioAtendimentoList;
    private String descricao;
    public Float pontos;

    public Solicitacao() {
        horarioAtendimentoList = new ArrayList<HorarioAtendimento>();
    }
    
    
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Execucao getExecucao() {
        return execucao;
    }

    public void setExecucao(Execucao execucao) {
        this.execucao = execucao;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public List<HorarioAtendimento> getHorarioAtendimentoList() {
        return horarioAtendimentoList;
    }

    public void setHorarioAtendimentoList(List<HorarioAtendimento> horarioAtendimentoList) {
        this.horarioAtendimentoList = horarioAtendimentoList;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setPontos(Float pontos) {
        this.pontos = pontos;
    }

    public Float getPontos() {
        return pontos;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }
    
    

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Solicitacao other = (Solicitacao) obj;
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        if (!Objects.equals(this.execucao, other.execucao)) {
            return false;
        }
        return true;
    }

    
}
