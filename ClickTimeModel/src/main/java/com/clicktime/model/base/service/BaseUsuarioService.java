package com.clicktime.model.base.service;

import com.clicktime.model.base.BaseDAO;
import com.clicktime.model.base.BaseService;
import com.clicktime.model.entity.Avatar;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.entity.Usuario;

public abstract class BaseUsuarioService extends BaseService<Usuario> {

    public BaseUsuarioService(BaseDAO dao) {
        super(dao);
    }

    public abstract Usuario login(String email, String senha) throws Exception;
 
    public abstract void setAvatar(Long id, Avatar avatar) throws Exception;
 
    public abstract Avatar getAvatar(Long id) throws Exception;
     
    public abstract Profissional getProfissionalFavorito(Long id) throws Exception;
}
