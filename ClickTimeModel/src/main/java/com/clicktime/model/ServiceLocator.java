package com.clicktime.model;

import com.clicktime.model.base.service.BaseCategoriaServicoService;
import com.clicktime.model.base.service.BaseDiaAtendimentoService;
import com.clicktime.model.base.service.BaseExecucaoService;
import com.clicktime.model.base.service.BaseHorarioAtendimentoService;
import com.clicktime.model.base.service.BaseProfissionalService;
import com.clicktime.model.base.service.BaseServicoService;
import com.clicktime.model.base.service.BaseSolicitacaoService;
import com.clicktime.model.base.service.BaseUsuarioService;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.service.CategoriaServicoService;
import com.clicktime.model.service.DiaAtendimentoResumoService;
import com.clicktime.model.service.DiaAtendimentoService;
import com.clicktime.model.service.ExecucaoService;
import com.clicktime.model.service.HorarioAtendimentoService;
import com.clicktime.model.service.ProfissionalService;
import com.clicktime.model.service.ServicoService;
import com.clicktime.model.service.SolicitacaoService;
import com.clicktime.model.service.UsuarioService;
import com.clicktime.model.service.calendario.CalendarioService;

public class ServiceLocator {
    public static BaseCategoriaServicoService getCategoriaServicoService(){
        return new CategoriaServicoService();
    }
    
    public static BaseServicoService getServicoService(){
        return new ServicoService();
    }
    
    public static BaseUsuarioService getUsuarioService(){
        return new UsuarioService();
    }
    
    public static BaseProfissionalService getProfissionalService(){
        return new ProfissionalService();
    }    
    
    public static BaseExecucaoService getExecucaoService(){
        return new ExecucaoService();
    }
    
    public static BaseHorarioAtendimentoService getHorarioAtendimentoService(){
        return new HorarioAtendimentoService();
    }
    
    public static BaseSolicitacaoService getSolicitacaoService(){
        return new SolicitacaoService();
    }
    
    public static BaseDiaAtendimentoService getDiaAtendimentoService(){
        return new DiaAtendimentoService();
    }
    
    public static DiaAtendimentoResumoService getDiaAtendimentoResumoService(){
        return new DiaAtendimentoResumoService();
    }
    
    public static CalendarioService getCalendarioService(int year, int month, Profissional profissional, boolean isProfissional){
        return new CalendarioService(year, month, profissional, isProfissional);
    }
}
