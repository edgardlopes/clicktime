package com.clicktime.model.entity;

import com.clicktime.model.base.BaseEntity;
import java.util.Objects;
import org.joda.time.DateTime;

public class Execucao extends BaseEntity {

    private String descricao;
//    @DateTimeFormat(pattern = "HH:mm")
    private DateTime duracao;
    private Float valor;
    private Servico servico;
    private Profissional profissional;
    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public DateTime getDuracao() {
        return duracao;
    }

    public void setDuracao(DateTime duracao) {
        this.duracao = duracao;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Execucao other = (Execucao) obj;
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        if (!Objects.equals(this.duracao, other.duracao)) {
            return false;
        }
        if (Float.floatToIntBits(this.valor) != Float.floatToIntBits(other.valor)) {
            return false;
        }
        if (!Objects.equals(this.servico, other.servico)) {
            return false;
        }
        return true;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }
    
    
    
    

}
