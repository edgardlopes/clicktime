package com.clicktime.model.base.service;

import com.clicktime.model.base.BaseService;
import com.clicktime.model.entity.Avatar;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.entity.Usuario;

public interface BaseUsuarioService extends BaseService<Usuario> {

    public Usuario login(String email, String senha) throws Exception;

    public void setAvatar(Long id, Avatar avatar) throws Exception;

    public Avatar getAvatar(Long id) throws Exception;
    
    public Profissional getProfissionalFavorito(Long id) throws Exception;
}
