package dsj.gestorar.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dsj.gestorar.modelo.Auditoria;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.seguranca.senhas.GeradorHahs;

public class UsuarioDAO implements AutoCloseable{

	
	private final String tabela = "USUARIO";
	private final Connection conexao;

	public UsuarioDAO() throws SQLException{
		
		this.conexao = GerenciarConexoes.getConexao();
		
	}
	public List<Usuario> verificaSeHaUsuarioCadastrado(int codigoAr) throws Exception {
	    String sql = "SELECT * FROM usuario WHERE codigo_ar = ?";

	    try (PreparedStatement pstm = conexao.prepareStatement(sql)) {
	    	
	        pstm.setInt(1, codigoAr);
	    	return mapaResultados(pstm.executeQuery());

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return Collections.emptyList();
	    }
	}
	
	
	public boolean alterarDados(Usuario usuario, Usuario usuarioAlterador, boolean senhaModificada) {
		
		String sql = senhaModificada ?
				"UPDATE usuario SET nome = ?, sal = ?, hash = ?, codigo_titulo = ? WHERE codigo = ?" :
					"UPDATE usuario SET nome = ?, codigo_titulo = ? WHERE codigo = ?";
		
		try(PreparedStatement pstm = conexao.prepareStatement(sql)){
			
			if(senhaModificada) {
				
				pstm.setString(1, usuario.getNome());
				usuario.setSal(GeradorHahs.geraSal());
				pstm.setString(2, usuario.getSal());
				usuario.setHash(GeradorHahs.gerarHash(usuario.getSenha().toCharArray(), usuario.getSal()));
				pstm.setString(3, usuario.getHash());
				pstm.setInt(4, usuario.getCodigoTitulo());
				pstm.setInt(5, usuario.getCodigo());
				
				
			}else {
				
				pstm.setString(1, usuario.getNome());
				pstm.setInt(2, usuario.getCodigoTitulo());
				pstm.setInt(3, usuario.getCodigo());
				
			}
			
			pstm.executeUpdate();
			AuditoriaDAO.gravaAuditoria(new Auditoria(tabela,
					"ALTEROU DADOS DE USUARIO",
					"USUARIO ALTERADO: " +  usuario.getCodigo(),
					usuarioAlterador),
				conexao);
			
			return true;
			
		}catch(Exception e) {
			
			System.err.println("Erro ao alterar usuario: " + e);
			
			return false;
			
		}
		
	}
	
	public Usuario verificarLoginSalvo(String nome, String hash) {
		
		String sql = "SELECT * FROM usuario WHERE nome = ?";
		Usuario usuario;
		
		try(PreparedStatement pstm = conexao.prepareStatement(sql)){
			
			pstm.setString(1, nome);
			
			usuario = mapaResultado(pstm.executeQuery());
			
			if(usuario != null) {
				if(hash.equals(usuario.getHash())) {
					
					AuditoriaDAO.gravaAuditoria(new Auditoria(tabela,
							"USUARIO SALVO FEZ LOGIN",
							"USUARIO: " +  usuario.getCodigo(),
							usuario),
						conexao);
					
					return usuario;
					
				}
				
			}
			
			
		}catch(Exception sqle) {
			
			return null;
			
		}
			
		return null;
		
	}
	
	public Usuario verificarLogin(String nome, char[] senhaInput) {
		
		String sql = "SELECT * FROM usuario WHERE nome = ?";
		Usuario usuario;
		
		try(PreparedStatement pstm = conexao.prepareStatement(sql)){
			
			pstm.setString(1, nome);
			
			usuario = mapaResultado(pstm.executeQuery());
			if(usuario != null) {

				if(GeradorHahs.verificarSenha(senhaInput, usuario.getSal(), usuario.getHash())) {
					
					AuditoriaDAO.gravaAuditoria(new Auditoria(tabela,
							"USUARIO FEZ LOGIN",
							"USUARIO: " +  usuario.getCodigo(),
							usuario),
						conexao);
					
					return usuario;
					
				}
				
			}
			
			
		}catch(Exception sqle) {
			System.err.println("Erro ao logar: " + sqle);
			return null;
			
		}
			
		return null;
		
	}
	
	public boolean cadastrarUsuario(Usuario usuario, Usuario usuarioCadatrou) {
	    String sql = "INSERT INTO usuario (nome, sal, hash, codigo_titulo, codigo_ar) VALUES (?, ?, ?, ?, ?)";

	    try (PreparedStatement pstm = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
	    	
	        usuario.setSal(GeradorHahs.geraSal());
	        usuario.setHash(GeradorHahs.gerarHash(usuario.getSenha().toCharArray(), usuario.getSal()));
	        usuario.setNome(usuario.getNome().replace(" ", ""));
	        pstm.setString(1, usuario.getNome());
	        pstm.setString(2, usuario.getSal());
	        pstm.setString(3, usuario.getHash());
	        pstm.setInt(4, usuario.getCodigoTitulo());
	        pstm.setInt(5, usuario.getAr().getCodigo());

	        pstm.executeUpdate();
	        
	        ResultSet rs = pstm.getGeneratedKeys();
	        if(rs.next()) {
	        
	        	int codigoUsuarioCadastrado = rs.getInt(1);
	        	
	        	if(usuarioCadatrou == null) {
		        	usuarioCadatrou = new Usuario(codigoUsuarioCadastrado);
		        	
		        }
	        }
	        
	        AuditoriaDAO.gravaAuditoria(new Auditoria(tabela,
					"CADASTROU UM USUARIO",
					"USUARIO CADASTRADO: " +  usuario.getCodigo(),
					usuarioCadatrou),
				conexao);
	        
	        return true;
	        
	    } catch (Exception e) {
	        System.err.println("Erro ao cadastrar usuario: " + e);
	        return false;
	    }
	}
	
	
	public List<Usuario> listarUsuarios(){
		
		try(PreparedStatement pstm = conexao.prepareStatement("SELECT * FROM usuario")){

			
			ResultSet rs = pstm.executeQuery();
			
			return mapaResultados(rs);
			
		}catch(Exception e) {
			System.err.println(e);
			return null;
			
		}
		
	}
	
	public Usuario buscarUsuario(int codigo) {
		
		try(PreparedStatement pstm = conexao.prepareStatement("SELECT * FROM usuario WHERE CODIGO = ?")){
			
			pstm.setInt(1, codigo);
			
			ResultSet rs = pstm.executeQuery();
			
			return mapaResultado(rs);
			
		}catch(Exception e) {
			System.err.println(e);
			return null;
			
		}
		
	}
	
	public Usuario mapaResultado(ResultSet resultado) throws Exception{
		
		if(resultado.next()) {
			
			Usuario usuario = new Usuario();
			
			usuario.setCodigo(resultado.getInt("codigo"));
			usuario.setNome(resultado.getString("nome"));
			usuario.setSal(resultado.getString("sal"));
			usuario.setHash(resultado.getString("hash"));
			usuario.setCodigoTitulo(resultado.getInt("codigo_titulo"));
			try(ArDAO arDAO = new ArDAO()){
				usuario.setAr(arDAO.buscaAr(resultado.getInt("codigo_ar")));
				
			}

			return usuario;
		}
		
		return null;
		
	}
	
	public List<Usuario> mapaResultados(ResultSet resultado) throws Exception{
		
		List<Usuario> usuarios = new ArrayList<>();
		
		while(resultado.next()) {
			
			Usuario usuario = new Usuario();
			
			usuario.setCodigo(resultado.getInt("codigo"));
			usuario.setNome(resultado.getString("nome"));
			usuario.setSal(resultado.getString("sal"));
			usuario.setHash(resultado.getString("hash"));
			usuario.setCodigoTitulo(resultado.getInt("codigo_titulo"));
			try(ArDAO arDAO = new ArDAO()){
				usuario.setAr(arDAO.buscaAr(resultado.getInt("codigo_ar")));
				
			}
			
			usuarios.add(usuario);
			
		}
		
		return usuarios;
		
	}
	
	public boolean verificaSeJaExisteUsuarioComNome(String nome) {
		
		try(PreparedStatement pstm = conexao.prepareStatement("SELECT 1 FROM usuario WHERE NOME = ?")){
			
			pstm.setString(1, nome);
			
			ResultSet rs = pstm.executeQuery();
			
			if(rs.next()) {return true;}else {return false;}
			
		}catch(Exception e) {
			
			System.err.println(e);
			return true;
			
		}
		
	}
	
	public boolean apagarUsuario(int codigo) {
		
		try(PreparedStatement pstm = conexao.prepareStatement("DELETE FROM usuario WHERE codigo = ?")){
			
			pstm.setInt(1, codigo);
			pstm.executeUpdate();
			return true;
			
		} catch (SQLException e) {
			
			System.err.println(e);
			return false;
		}
		
	}
	

	@Override
	public void close() throws Exception {

		try { 
			
			if (conexao != null && !conexao.isClosed()) conexao.close();
			
		}catch (SQLException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
}
