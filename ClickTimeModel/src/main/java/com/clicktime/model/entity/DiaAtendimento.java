package com.clicktime.model.entity;

import com.clicktime.model.base.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.joda.time.DateTime;

public class DiaAtendimento extends BaseEntity {

//    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private DateTime data;
    private Profissional profissional;
    private List<HorarioAtendimento> atendimentoList;

    public DiaAtendimento() {
        atendimentoList = new ArrayList<>();
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }

    public DateTime getData() {
        return data;
    }

    public void setData(DateTime data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DiaAtendimento other = (DiaAtendimento) obj;
        if (!Objects.equals(this.data, other.data)) {
            return false;
        }
        if (!Objects.equals(this.profissional, other.profissional)) {
            return false;
        }
        return true;
    }

    public String toJson() {
        String json = "-1";

        if (id != null && id > 0 && profissional != null && profissional.getId() != null && profissional.getId() > 0) {
            json = "{" + "\"id\": " + id + ", \"profissionalFK\": " + profissional.getId() + "}";
        }

        return json;
    }

}
