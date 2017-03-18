package com.clicktime.model.service;

import com.clicktime.model.ConnectionManager;
import com.clicktime.model.ErrorMessage;
import com.clicktime.model.ServiceLocator;
import com.clicktime.model.base.service.BaseUsuarioService;
import com.clicktime.model.criteria.ProfissionalCriteria;
import com.clicktime.model.criteria.UsuarioCriteria;
import com.clicktime.model.dao.UsuarioDAO;
import com.clicktime.model.entity.Avatar;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.entity.Usuario;
import com.clicktime.model.fields.UsuarioFields;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuarioService implements BaseUsuarioService {

    @Override
    public Usuario login(String email, String senha) throws Exception {
        Usuario usuarioLogado = null;

        if (email != null && !email.isEmpty() && senha != null && !senha.isEmpty()) {
            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(UsuarioCriteria.EMAIL_EQ, email);
            criteria.put(UsuarioCriteria.SENHA_EQ, senha);

            List<Usuario> usuarioList = readByCriteria(criteria, null);
            if (usuarioList != null && usuarioList.size() == 1) {
                usuarioLogado = usuarioList.get(0);

                criteria.clear();
                criteria.put(ProfissionalCriteria.USUARIO_FK_EQ, usuarioLogado.getId());
                List<Profissional> profissionalList = ServiceLocator.getProfissionalService().readByCriteria(criteria, null);
                if (profissionalList != null && profissionalList.size() == 1) {
                    usuarioLogado = profissionalList.get(0);
                }

            }
        }

        return usuarioLogado;
    }

    @Override
    public void create(Usuario entity) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();

        try {
            UsuarioDAO dao = new UsuarioDAO();
            dao.create(connection, entity);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            connection.close();
        }
    }

    @Override
    public Usuario readById(Long id) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();

        Usuario usuario = null;
        try {
            UsuarioDAO dao = new UsuarioDAO();
            usuario = dao.readById(connection, id);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            connection.close();
        }

        return usuario;
    }

    @Override
    public List<Usuario> readByCriteria(Map<String, Object> criteria, Integer offset) throws Exception {
        List<Usuario> usuarios = null;
        Connection connection = ConnectionManager.getInstance().getConnection();

        try {
            UsuarioDAO dao = new UsuarioDAO();
            usuarios = dao.readByCriteria(connection, criteria, offset);
            connection.commit();
        } catch (Exception exception) {
            connection.rollback();
            exception.printStackTrace();
            throw exception;
        } finally {
            connection.close();
        }

        return usuarios;
    }

    @Override
    public void update(Usuario entity) throws Exception {
        Connection connection = ConnectionManager.getInstance().getConnection();

        try {
            new UsuarioDAO().update(connection, entity);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.close();
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, String> validateForCreate(Map<String, Object> fields) throws Exception {
        Map<String, String> errors = new HashMap<String, String>();

        String nome = (String) fields.get(UsuarioFields.NOME);
        if (nome == null || nome.isEmpty()) {
            errors.put(UsuarioFields.NOME, ErrorMessage.NOT_NULL);
        }

        String sobrenome = (String) fields.get(UsuarioFields.SOBRENOME);
        if (sobrenome == null || sobrenome.isEmpty()) {
            errors.put(UsuarioFields.SOBRENOME, ErrorMessage.NOT_NULL);
        }

        String nomeUsuario = (String) fields.get(UsuarioFields.NOME_USUARIO);
        if (nomeUsuario == null || nomeUsuario.isEmpty()) {
            errors.put(UsuarioFields.NOME_USUARIO, ErrorMessage.NOT_NULL);
        } else {
            Map<String, Object> criteria = new HashMap<>();
            criteria.put(UsuarioCriteria.NOME_USUARIO_EQ, nomeUsuario);

            Long id = (Long) fields.get(UsuarioFields.ID);
            if (id != null && id > 0L) {
                criteria.put(UsuarioCriteria.ID_NE, id);
            }

            if (ServiceLocator.getUsuarioService().readByCriteria(criteria, null).size() > 0) {
                errors.put(UsuarioFields.NOME_USUARIO, ErrorMessage.NOME_USUARIO_UNIQUE);
            }
        }

        String telefone = (String) fields.get(UsuarioFields.TELEFONE);
        if (telefone != null && !telefone.isEmpty()) {
            if (!isTelefone(telefone)) {
                errors.put(UsuarioFields.TELEFONE, ErrorMessage.TELEFONE_INVALIDO);
            }
            telefone = telefone.trim().replace(" ", "");
            Map<String, Object> criteria = new HashMap<>();
            criteria.put(UsuarioCriteria.TELEFONE_EQ, telefone);

            Long id = (Long) fields.get(UsuarioFields.ID);
            if (id != null && id > 0L) {
                criteria.put(UsuarioCriteria.ID_NE, id);
            }

            if (ServiceLocator.getUsuarioService().readByCriteria(criteria, null).size() > 0) {
                errors.put(UsuarioFields.TELEFONE, ErrorMessage.TELEFONE_UNIQUE);
            }
        }

        String email = (String) fields.get(UsuarioFields.EMAIL);
        if (email == null || email.isEmpty()) {
            errors.put(UsuarioFields.EMAIL, ErrorMessage.NOT_NULL);
        } else {
            Map<String, Object> criteria = new HashMap<>();
            criteria.put(UsuarioCriteria.EMAIL_EQ, email);
            
            Long id = (Long) fields.get(UsuarioFields.ID);
            if(id != null && id > 0){
                criteria.put(UsuarioCriteria.ID_NE, id);
            }
            
            if (ServiceLocator.getUsuarioService().readByCriteria(criteria, null).size() > 0) {
                errors.put(UsuarioFields.EMAIL, ErrorMessage.EMAIL_UNIQUE);
            }
        }

        String senha = (String) fields.get(UsuarioFields.SENHA);
        if (senha == null || senha.isEmpty()) {
            errors.put(UsuarioFields.SENHA, ErrorMessage.NOT_NULL);
            errors.put(UsuarioFields.SENHA_CONFIRM, ErrorMessage.NOT_NULL);
        } else {
            String senhaConfirm = (String) fields.get(UsuarioFields.SENHA_CONFIRM);
            if (senhaConfirm == null || senhaConfirm.isEmpty()) {
                errors.put(UsuarioFields.SENHA_CONFIRM, ErrorMessage.NOT_NULL);
            } else {
                if (!senha.equals(senhaConfirm)) {
                    errors.put(UsuarioFields.SENHA, ErrorMessage.SENHA_NE);
                    errors.put(UsuarioFields.SENHA_CONFIRM, ErrorMessage.SENHA_NE);
                } else if (senha.length() < ErrorMessage.MIN_LENGTH_SENHA) {
                    errors.put(UsuarioFields.SENHA, ErrorMessage.SENHA_CURTA);
                    errors.put(UsuarioFields.SENHA_CONFIRM, ErrorMessage.SENHA_CURTA);
                }
            }
        }
        return errors;
    }

    @Override
    public Map<String, String> validateForUpdate(Map<String, Object> fields) throws Exception {
        Map<String, String> errors = validateForCreate(fields);
        errors.remove(UsuarioFields.SENHA);
        errors.remove(UsuarioFields.SENHA_CONFIRM);

        return errors;
    }

    public boolean isTelefone(String numeroTelefone) {
        return numeroTelefone.matches(".((10)|([1-9][1-9]).)\\s9?[6-9][0-9]{3}-[0-9]{4}")
                || numeroTelefone.matches(".((10)|([1-9][1-9]).)\\s[2-5][0-9]{3}-[0-9]{4}")
                || numeroTelefone.matches(".((10)|([1-9][1-9]).)9?[6-9][0-9]{3}-[0-9]{4}")
                || numeroTelefone.matches(".((10)|([1-9][1-9]).)[2-5][0-9]{3}-[0-9]{4}");
    }
    
    @Override
    public void setAvatar(Long id, Avatar avatar) throws Exception {
        Connection conn = ConnectionManager.getInstance().getConnection();
        try {
            UsuarioDAO dao = new UsuarioDAO();
            dao.setAvatar(conn, id, avatar);
            conn.commit();
            conn.close();

        } catch (Exception e) {
            conn.rollback();
            conn.close();
            throw e;
        }
    }

    @Override
    public Avatar getAvatar(Long id) throws Exception {
        Connection conn = ConnectionManager.getInstance().getConnection();
        Avatar avatar = null;
        try {
            UsuarioDAO dao = new UsuarioDAO();
            avatar = dao.getAvatar(conn, id);
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        }finally{
            conn.close();
        }
        return avatar;
    }

    @Override
    public Profissional getProfissionalFavorito(Long id) throws Exception {
        Connection conn = ConnectionManager.getInstance().getConnection();
        Profissional profissional = null;
        try {
            UsuarioDAO dao = new UsuarioDAO();
            profissional = dao.getProfissionalFavorito(conn, id);
            conn.commit();

        } catch (Exception e) {
            conn.rollback();
            throw e;
        }finally{
            conn.close();
        }
        return profissional;   
    }
}
