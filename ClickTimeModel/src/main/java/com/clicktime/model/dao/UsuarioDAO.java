package com.clicktime.model.dao;

import com.clicktime.model.base.BaseDAO;
import com.clicktime.model.criteria.UsuarioCriteria;
import com.clicktime.model.entity.Avatar;
import com.clicktime.model.entity.Profissional;
import com.clicktime.model.entity.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public class UsuarioDAO implements BaseDAO<Usuario> {

    @Override
    public void create(Connection conn, Usuario e) throws Exception {
        String sql = "INSERT INTO usuario (nome, sobrenome, nome_usuario, email, telefone, senha) VALUES (?, ?, ?, ?, ?, ?) RETURNING id;";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, e.getNome());
            ps.setString(i++, e.getSobrenome());
            ps.setString(i++, e.getNomeUsuario());
            ps.setString(i++, e.getEmail());
            ps.setString(i++, e.getTelefone());
            ps.setString(i++, e.getSenha());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                e.setId(rs.getLong("id"));
            }
            rs.close();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

    }

    @Override
    public Usuario readById(Connection conn, Long id) throws Exception {
        String sql = "SELECT * from usuario where id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();

        Usuario usuario = null;
        if (rs.next()) {
            usuario = new Usuario();
            usuario.setEmail(rs.getString("email"));
            usuario.setNome(rs.getString("nome"));
            usuario.setSobrenome(rs.getString("sobrenome"));
            usuario.setNomeUsuario(rs.getString("nome_usuario"));
            usuario.setTelefone(rs.getString("telefone"));
        }
        rs.close();
        ps.close();

        return usuario;
    }

    @Override
    public List<Usuario> readByCriteria(Connection conn, Map<String, Object> criteria, Integer offset) throws Exception {
        String sql = "SELECT * from usuario where 1=1";

        if (criteria != null && !criteria.isEmpty()) {
            String criterionEmailEq = (String) criteria.get(UsuarioCriteria.EMAIL_EQ);
            if (criterionEmailEq != null && (!criterionEmailEq.isEmpty())) {
                sql += " AND email = '" + criterionEmailEq + "'";
            }

            String criterionSenhaEq = (String) criteria.get(UsuarioCriteria.SENHA_EQ);
            if (criterionSenhaEq != null && (!criterionSenhaEq.isEmpty())) {
                sql += " AND senha = '" + criterionSenhaEq + "'";
            }

            String nomeUsuario = (String) criteria.get(UsuarioCriteria.NOME_USUARIO_EQ);
            if (nomeUsuario != null && !nomeUsuario.isEmpty()) {
                sql += " AND nome_usuario like '" + nomeUsuario + "'";
            }

            String telefone = (String) criteria.get(UsuarioCriteria.TELEFONE_EQ);
            if (telefone != null && !telefone.isEmpty()) {
                sql += " AND telefone like '" + telefone + "'";
            }

            Long idNe = (Long) criteria.get(UsuarioCriteria.ID_NE);
            if (idNe != null && idNe > 0) {
                sql += " AND id != " + idNe;
            }
        }

        Statement ps = conn.createStatement();
        ResultSet rs = ps.executeQuery(sql);

        List<Usuario> usuarios = new ArrayList<>();
        while (rs.next()) {
            Usuario u = new Usuario();
            u.setNome(rs.getString("nome"));
            u.setSobrenome(rs.getString("sobrenome"));
            u.setNomeUsuario(rs.getString("nome_usuario"));
            u.setEmail(rs.getString("email"));
            u.setTelefone(rs.getString("telefone"));
            u.setSenha(rs.getString("senha"));
            u.setId(rs.getLong("id"));

            usuarios.add(u);
        }

        rs.close();
        ps.close();

        return usuarios;
    }

    @Override
    public void update(Connection conn, Usuario e) throws Exception {
        String sql = "UPDATE usuario SET nome=?, sobrenome=?, nome_usuario=?, email=?, telefone=? WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        int i = 1;
        ps.setString(i++, e.getNome());
        ps.setString(i++, e.getSobrenome());
        ps.setString(i++, e.getNomeUsuario());
        ps.setString(i++, e.getEmail());
        ps.setString(i++, e.getTelefone());
        ps.setLong(i++, e.getId());

        ps.execute();
        ps.close();
    }

    @Override
    public void delete(Connection conn, Long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setAvatar(Connection conn, Long id, Avatar avatar) throws Exception {
        String sql = "DELETE FROM usuario_avatar WHERE usuario_fk=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, id);
        ps.execute();
        ps.close();
        sql = "INSERT INTO usuario_avatar(usuario_fk, imagem) VALUES(?,?);";
        ps = conn.prepareStatement(sql);
        ps.setLong(1, id);
        ps.setBytes(2, avatar.getImagem());
        ps.execute();
        ps.close();
    }

    public Avatar getAvatar(Connection conn, Long id) throws Exception {
        Avatar avatar = null;
        String sql = "SELECT * FROM usuario_avatar WHERE usuario_fk=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            avatar = new Avatar();
            avatar.setImagem(rs.getBytes("imagem"));
        }
        rs.close();
        ps.close();
        return avatar;
    }

    public Profissional getProfissionalFavorito(Connection conn, Long id) throws Exception {
        String sql = "SELECT p.*, u.*, count(*) from solicitacao s \n"
                + "left join execucao_servico ex on ex.id=s.execucao_servico_fk\n"
                + "left join profissional p on ex.profissional_fk=p.usuario_fk\n"
                + "left join usuario u on p.usuario_fk=u.id\n"
                + "where s.usuario_fk=?\n"
                + "group by p.usuario_fk, u.id\n"
                + "order by count desc\n"
                + "limit 1";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, id);

        ResultSet rs = ps.executeQuery();
        Profissional profissional = null;
        if (rs.next()) {
            profissional = new Profissional();
            profissional.setId(rs.getLong("id"));
            profissional.setNome(rs.getString("nome"));
            profissional.setSobrenome(rs.getString("sobrenome"));
            profissional.setNomeUsuario(rs.getString("nome_usuario"));
            profissional.setEmail(rs.getString("email"));
            profissional.setTelefone(rs.getString("telefone"));
            profissional.setDescricao(rs.getString("descricao"));
            profissional.setHoraFim(new DateTime(rs.getTime("hora_fim")));
            profissional.setHoraInicio(new DateTime(rs.getTime("hora_inicio")));
        }

        return profissional;
    }
}
