/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clicktime.web.config;

import com.clicktime.model.ServiceLocator;
import com.clicktime.model.base.service.BaseCategoriaServicoService;
import com.clicktime.model.base.service.BaseDiaAtendimentoService;
import com.clicktime.model.base.service.BaseExecucaoService;
import com.clicktime.model.base.service.BaseHorarioAtendimentoService;
import com.clicktime.model.base.service.BaseProfissionalService;
import com.clicktime.model.base.service.BaseServicoService;
import com.clicktime.model.base.service.BaseSolicitacaoService;
import com.clicktime.model.base.service.BaseUsuarioService;
import com.clicktime.model.service.calendario.CalendarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author edgar
 */
@Configuration
public class BeanConfig {
    
    @Bean
    public BaseProfissionalService getproProfissionalService(){
        return ServiceLocator.getProfissionalService();
    }
    
    @Bean
    public BaseExecucaoService geteExecucaoService(){
        return ServiceLocator.getExecucaoService();
    }
    
    @Bean
    public BaseCategoriaServicoService getCategoriaServicoService(){
        return ServiceLocator.getCategoriaServicoService();
    }
    
    @Bean
    public BaseDiaAtendimentoService getDiaAtendimentoService(){
        return ServiceLocator.getDiaAtendimentoService();
    }
    
    @Bean
    public BaseHorarioAtendimentoService getHorarioAtendimentoService(){
        return ServiceLocator.getHorarioAtendimentoService();
    }
    
    @Bean
    public BaseServicoService getServicoService(){
        return ServiceLocator.getServicoService();
    }
    
    @Bean
    public BaseUsuarioService getUsuarioService(){
        return ServiceLocator.getUsuarioService();
    }
    
    @Bean
    public BaseSolicitacaoService getSolicitacaoService(){
        return ServiceLocator.getSolicitacaoService();
    }
    
    @Bean
    public CalendarioService getCalendarioService(){
        return ServiceLocator.getCalendarioService();
    }
}
