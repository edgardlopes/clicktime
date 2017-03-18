package com.clicktime.model.entity;

import com.clicktime.model.base.BaseEntity;
import java.util.ArrayList;
import java.util.Objects;

public class CategoriaServico extends BaseEntity {

    private String nome;
    private ArrayList<Servico> servicos;

    public CategoriaServico() {
        servicos = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Servico> getServicos() {
        return servicos;
    }

    public void setServicos(ArrayList<Servico> servicos) {
        this.servicos = servicos;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CategoriaServico other = (CategoriaServico) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        return true;
    }
    
    

}
