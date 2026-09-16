package dsj.gestorar.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dsj.gestorar.modelo.Auditoria;
import dsj.gestorar.modelo.Gestor;
import dsj.gestorar.modelo.Situacao;
import dsj.gestorar.modelo.Usuario;

public class GestorDAO implements AutoCloseable{
	
	private String tabela = "GESTOR";
	public int codigoGestor = 0;
	private final Connection conexao;

	public GestorDAO() throws SQLException{
		
		this.conexao = GerenciarConexoes.getConexao();
		
	}
	
	public boolean alterarSituacaoGestor(Situacao situacao, int codigo, Usuario usuario) {
		
		try(PreparedStatement pstm = conexao.prepareStatement("UPDATE gestor SET situacao = ? WHERE codigo = ?")) {

			pstm.setString(1, situacao.toString());
			pstm.setInt(2, codigo);
			
			pstm.execute();

			AuditoriaDAO.gravaAuditoria(new Auditoria( tabela, "ALTERAÇÃO DE SITUAÇÃO PARA " + situacao, String.valueOf(codigo), usuario), conexao);
			
			return true;
			
		}catch(Exception sqle) {
			
			System.err.println("Erro ao atualizar situacao gestors " + sqle);
			
			return false;
			
		}
		
	}
	
	public boolean deletaCadastro(int codigo, Usuario usuario) {
		
		boolean existeAtivos = true;
		boolean existeDesativacao = true;
		
		try(AtivoDAO aDAO = new AtivoDAO()){
			
			if(!aDAO.existeResgistroGestor(codigo)) {
				
				existeAtivos = false;
				
			}
			
		}catch(Exception e) {
			
			System.err.println("Erro ao verificar registro de gestor em ativos" + e);
			
		}
		
		try(DesativacaoDAO dDAO = new DesativacaoDAO()){
			
			if(!dDAO.existeRegistroGestor(codigo)) {
				
				existeDesativacao = false;
				
			}
			
		}catch(Exception e) {
			
			System.err.println("Erro ao verificar registro de gestor em desativações" + e);
			
		}
		
		if(!existeAtivos && !existeDesativacao) {
			
			try(PreparedStatement pstm = conexao.prepareStatement("DELETE FROM gestor WHERE codigo = ?")){
				
				pstm.setInt(1, codigo);
				
				pstm.execute();

				AuditoriaDAO.gravaAuditoria(new Auditoria( tabela, "EXCLUSÃO", String.valueOf(codigo), usuario), conexao);
				
				return true;
				
			}catch(Exception e) {
				
				System.err.println("Erro ao deletar gestor: " + e);
				
				return false;
			}
			
		}
		
		return false;
		
	}
	
	public boolean gravaDados(Gestor gestor, Usuario usuario) {
		
		if(gestor.equals(null)) {
			
		}else {
			
			if(gestor.getNome().equals(null) || gestor.getNome().isEmpty()) {
				
				return false;
				
			}else if(gestor.getEmail().equals(null) || gestor.getEmail().isEmpty()){
				
				return false;
				
			}
				
		}
		
		boolean inserindo = gestor.getCodigo() <= 0;

		try(PreparedStatement pstm = (inserindo) 
				? conexao.prepareStatement("INSERT INTO gestor (nome, email, nivel, situacao) VALUES (?,?,?,?)",
						PreparedStatement.RETURN_GENERATED_KEYS)
				: conexao.prepareStatement("UPDATE gestor SET nome = ?, email = ?, nivel = ?, situacao = ? where codigo = ?",
						PreparedStatement.RETURN_GENERATED_KEYS)) {

			pstm.setString(1, gestor.getNome());
			pstm.setString(2, gestor.getEmail());
			pstm.setString(3, gestor.getNivel());
			pstm.setString(4, gestor.getSituacao());
				
			if(!inserindo) {
					
				pstm.setInt(5, gestor.getCodigo());	
					
			}
				
			if(inserindo) {
				
				pstm.executeUpdate();
				
				try(ResultSet rs = pstm.getGeneratedKeys()){
					if(rs.next()) {
						
						codigoGestor = rs.getInt(1);
						
					}
				}
				
			}else {
				
				codigoGestor = gestor.getCodigo();
				
			}

			AuditoriaDAO.gravaAuditoria(new Auditoria( tabela,
					"GRAVAÇÃO DE REGISTRO",
					String.valueOf((gestor.getCodigo() == 0) ? "NOVO" : gestor.getCodigo()),
					usuario),
				conexao);
			
			return true;
				
		}catch(Exception sqle) {
			
			System.err.println("Erro ao inserir ou atualizar dados do gestor " + sqle);
			
			return false;
			
		}
		
	}

	public List<Gestor> listaGestores(){

		List<Gestor> gestores = new ArrayList<Gestor>();
		
		gestores = new ArrayList<>();
		
		try(PreparedStatement pstm = conexao.prepareStatement("SELECT * FROM gestor")) {

			gestores = mapaResultados(pstm.executeQuery());

			return gestores;
			
			
		}catch(Exception sqle) {
			
			System.err.println("Erro ao listar todos gestores " + sqle);
			
			return null;
			
		}
		

	}

	public Gestor buscaGestor(int codigo){
		
		Gestor gestor = new Gestor();
		
		try(PreparedStatement pstm = conexao.prepareStatement("SELECT * FROM gestor WHERE codigo = ?")) {
			
			pstm.setInt(1, codigo);
			
			gestor = mapaResultado(pstm.executeQuery());

			return gestor;
			
		}catch(Exception sqle) {
			
			System.err.println("Erro ao buscar gestor: " + sqle);
			
			return null;
			
		}
		
	}
	
	public Gestor mapaResultado(ResultSet resultados) throws Exception{
		
		Gestor gestor = new Gestor();
		
		if(resultados.next()) {
			
			gestor.setCodigo(resultados.getInt("codigo"));
			gestor.setNome(resultados.getString("nome"));
			gestor.setEmail(resultados.getString("email"));
			gestor.setNivel(resultados.getString("nivel"));
			gestor.setSituacao(resultados.getString("situacao"));
			
		}
		
		return gestor;

	}
	
	public List<Gestor> mapaResultados(ResultSet resultados) throws Exception{
		
		List<Gestor> gestores = new ArrayList<>();
		
		while(resultados.next()) {
			
			Gestor gestor = new Gestor();
			
			gestor.setCodigo(resultados.getInt("codigo"));
			gestor.setNome(resultados.getString("nome"));
			gestor.setEmail(resultados.getString("email"));
			gestor.setNivel(resultados.getString("nivel"));
			gestor.setSituacao(resultados.getString("situacao"));
			
			gestores.add(gestor);
			
		}
		
		return gestores;
		
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
