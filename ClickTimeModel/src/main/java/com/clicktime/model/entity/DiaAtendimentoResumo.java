
package com.clicktime.model.entity;
//colocar cor diferente para destacar classe transiente
public class DiaAtendimentoResumo{
    private DiaAtendimento diaAtendimento;
    private int horariosLivres;
    private int horariosBloqueados;

    public DiaAtendimento getDiaAtendimento() {
        return diaAtendimento;
    }

    public void setDiaAtendimento(DiaAtendimento diaAtendimento) {
        this.diaAtendimento = diaAtendimento;
    }

    public int getHorariosLivres() {
        return horariosLivres;
    }

    public void setHorariosLivres(int horariosLivres) {
        this.horariosLivres = horariosLivres;
    }

    public int getHorariosBloqueados() {
        return horariosBloqueados;
    }

    public void setHorariosBloqueados(int horariosBloqueados) {
        this.horariosBloqueados = horariosBloqueados;
    }

    
}
