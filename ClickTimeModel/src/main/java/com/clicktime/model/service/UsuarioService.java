package com.clicktime.model.service;

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
import com.clicktime.model.util.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuarioService extends BaseUsuarioService {

    public UsuarioService() {
        super(new UsuarioDAO());
    }

    @Override
    public Usuario login(String email, String senha) throws Exception {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(senha)) {
            return null;
        }

        Map<String, Object> criteria = new HashMap<>();
        criteria.put(UsuarioCriteria.EMAIL_EQ, email);
        criteria.put(UsuarioCriteria.SENHA_EQ, senha);
        List<Usuario> usuarioList = readByCriteria(criteria, null);

        if (usuarioList.size() != 1) {
            return null;
        }

        Usuario usuarioLogado = usuarioList.get(0);

        criteria.clear();
        criteria.put(ProfissionalCriteria.USUARIO_FK_EQ, usuarioLogado.getId());
        List<Profissional> profissionalList = ServiceLocator.getProfissionalService().readByCriteria(criteria, null);
        if (profissionalList.size() == 1) {
            usuarioLogado = profissionalList.get(0);
        }

        return usuarioLogado;
    }

    @Override
    public Map<String, String> validateForCreate(Map<String, Object> fields) throws Exception {
        Map<String, String> errors = new HashMap<>();

        String nome = (String) fields.get(UsuarioFields.NOME);
        if (StringUtils.isBlank(nome)) {
            errors.put(UsuarioFields.NOME, ErrorMessage.NOT_NULL);
        }

        String sobrenome = (String) fields.get(UsuarioFields.SOBRENOME);
        if (StringUtils.isBlank(sobrenome)) {
            errors.put(UsuarioFields.SOBRENOME, ErrorMessage.NOT_NULL);
        }

        String nomeUsuario = (String) fields.get(UsuarioFields.NOME_USUARIO);
        if (StringUtils.isBlank(nomeUsuario)) {
            errors.put(UsuarioFields.NOME_USUARIO, ErrorMessage.NOT_NULL);
        } else {
            Map<String, Object> criteria = new HashMap<>();
            criteria.put(UsuarioCriteria.NOME_USUARIO_EQ, nomeUsuario);

            Long id = (Long) fields.get(UsuarioFields.ID);
            if (id != null && id > 0L) {
                criteria.put(UsuarioCriteria.ID_NE, id);
            }

            if (!ServiceLocator.getUsuarioService().readByCriteria(criteria, null).isEmpty()) {
                errors.put(UsuarioFields.NOME_USUARIO, ErrorMessage.NOME_USUARIO_UNIQUE);
            }
        }

        String telefone = (String) fields.get(UsuarioFields.TELEFONE);
        if (!StringUtils.isBlank(telefone)) {
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

            if (!ServiceLocator.getUsuarioService().readByCriteria(criteria, null).isEmpty()) {
                errors.put(UsuarioFields.TELEFONE, ErrorMessage.TELEFONE_UNIQUE);
            }
        }

        String email = (String) fields.get(UsuarioFields.EMAIL);
        if (StringUtils.isBlank(email)) {
            errors.put(UsuarioFields.EMAIL, ErrorMessage.NOT_NULL);
        } else {
            Map<String, Object> criteria = new HashMap<>();
            criteria.put(UsuarioCriteria.EMAIL_EQ, email);

            Long id = (Long) fields.get(UsuarioFields.ID);
            if (id != null && id > 0) {
                criteria.put(UsuarioCriteria.ID_NE, id);
            }

            if (!ServiceLocator.getUsuarioService().readByCriteria(criteria, null).isEmpty()) {
                errors.put(UsuarioFields.EMAIL, ErrorMessage.EMAIL_UNIQUE);
            }
        }

        String senha = (String) fields.get(UsuarioFields.SENHA);
        if (StringUtils.isBlank(senha)) {
            errors.put(UsuarioFields.SENHA, ErrorMessage.NOT_NULL);
            errors.put(UsuarioFields.SENHA_CONFIRM, ErrorMessage.NOT_NULL);
        } else {
            String senhaConfirm = (String) fields.get(UsuarioFields.SENHA_CONFIRM);
            if (StringUtils.isBlank(senhaConfirm)) {
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
        executor.execute(conn -> {
            ((UsuarioDAO) dao).setAvatar(conn, id, avatar);
            return Void.TYPE;
        });
    }

    @Override
    public Avatar getAvatar(Long id) throws Exception {
        return executor.execute(conn -> ((UsuarioDAO) dao).getAvatar(conn, id));
    }

    @Override
    public Profissional getProfissionalFavorito(Long id) throws Exception {
        return executor.execute(conn -> ((UsuarioDAO) dao).getProfissionalFavorito(conn, id));
    }
}
