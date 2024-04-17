package br.com.autenticacao.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.autenticacao.model.Usuario;
import br.com.autenticacao.util.ConnectionFactory;

public class UsuarioDAOImpl implements GenericDAO {

	private Connection conn;

	public UsuarioDAOImpl() throws Exception {
		try {
			this.conn = ConnectionFactory.getConnection();
			System.out.println("Conectado com sucesso!");
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}
	
	@Override
	public List<Object> listarTodos() {

		List<Object> lista = new ArrayList<Object>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT id, nome, email FROM usuario ORDER BY nome";

		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Usuario usuario = new Usuario();
				usuario.setId(rs.getInt("id"));
				usuario.setNome(rs.getString("nome"));
				usuario.setEmail(rs.getString("email"));
				lista.add(usuario);
			}
		} catch (SQLException ex) {
			System.out.println("Problemas na DAO ao listar usuario! Erro" + ex.getMessage());
			ex.printStackTrace();
		} finally {
			try {
				ConnectionFactory.closeConnection(conn, stmt, rs);
			} catch (Exception ex) {
				System.out.println("Problemas ao fechar a conexão! Erro:" + ex.getLocalizedMessage());
			}
		}

		return lista;
	}
	

	@Override
	public Object listarPorId(int id) {
		

		Usuario usuario = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT id, nome, email FROM usuario WHERE id = ?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
		
		if(rs.next()) {
			usuario = new Usuario();
			usuario.setId(rs.getInt("id"));
			usuario.setNome(rs.getString("nome"));
			usuario.setEmail(rs.getString("email"));
		}
		}catch (SQLException ex) {
			System.out.print("Problemas na DAO ao carregar usuario! Erro: " + ex.getMessage());
			ex.printStackTrace();
		}finally{
			try {
				ConnectionFactory.closeConnection(conn, stmt, rs);
			}catch(Exception e) {
				System.out.print("Problemas ao fechar conexão! Erro: " + e.getMessage());
				e.printStackTrace();
			}
		}
		return usuario;
		
	}

	@Override
	public Boolean cadastrar(Object object) {
		Usuario usuario = (Usuario) object;
		PreparedStatement stmt = null;
		String sql = "INSERT INTO usuario (nome, email, senha, isAtivo)" + "VALUES (?,?,?,?)";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, usuario.getNome());
			stmt.setString(2, usuario.getEmail());
			stmt.setString(3, usuario.getSenha());
			stmt.setBoolean(4, usuario.getIsAtivo());
			stmt.execute();
			return true;
		} catch (SQLException ex) {
			System.out.print("Problemas na DAO ao cadastrar usuario! Erro: " + ex.getMessage());
			ex.printStackTrace();
			return false;
		} finally {
			try {
				ConnectionFactory.closeConnection(conn, stmt);
			} catch (Exception ex) {
				System.out.print("Problemas ao fechar conexão! Erro:" + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}

	@Override
	
	public Boolean alterar(Object object) {
		Usuario usuario = (Usuario) object;
		PreparedStatement stmt = null;
		String sql = "UPDATE usuario SET "
				+ "nome = ?, email = ?, isAtivo = ?"
				+ "WHERE id = ?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, usuario.getNome());
			stmt.setString(2, usuario.getEmail());
			stmt.setBoolean(3, usuario.getIsAtivo());
			stmt.setInt(4, usuario.getId());
			stmt.execute();
			return true;
		} catch (SQLException ex) {
			System.out.print("Problemas na DAO ao cadastrar usuario! Erro: " + ex.getMessage());
			ex.printStackTrace();
			return false;
		} finally {
			try {
				ConnectionFactory.closeConnection(conn, stmt);
			} catch (Exception ex) {
				System.out.print("Problemas ao fechar conexão! Erro:" + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void excluir(int id) {
		PreparedStatement stmt = null;
		String sql = "DELETE FROM usuario WHERE id = ?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.executeUpdate();
		}catch(SQLException ex){
			System.out.print("Problemas na DAO ao excluir Usuario! Erro: "+ ex.getMessage());
		}finally{
			try {
				ConnectionFactory.closeConnection(conn, stmt);
		}catch(Exception e){
			System.out.print("Problemas ao fechar conexão! Erro:" + e.getMessage());
		}
		
	}

}
	}
