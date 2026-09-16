package dsj.gestorar.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dsj.gestorar.modelo.Auditoria;
import dsj.gestorar.modelo.PontoAtendimento;
import dsj.gestorar.modelo.Usuario;

public class PontoAtendimentoDAO implements AutoCloseable{

	private final Connection conexao;

	public PontoAtendimentoDAO() throws SQLException{
		
		this.conexao = GerenciarConexoes.getConexao();
		
	}
	private final String tabela = "PONTO_ATENDIMENTO";
	
	public boolean verificaCampos(PontoAtendimento pa) {
		
		if(pa.getApelido().isEmpty() || pa.getApelido() == "") {
			
			return false;
			
		}
		
		if(pa.getCidade().isEmpty() || pa.getCidade() == "") {
			
			return false;
			
		}
		
		
		if(pa.getUf().isEmpty() || pa.getUf() == "") {
			
			return false;
			
		}
		
		return true;
	}
	
	
	public  boolean gravaPonto(PontoAtendimento pa, Usuario usuario) {
		
		if(!verificaCampos(pa)) {
			
			return false;
			
		}
		
		try (PreparedStatement pstm = (pa.getCodigo() <= 0) 
				? conexao.prepareStatement("INSERT INTO ponto_atendimento (apelido, cidade, uf) VALUES (?,?,?)")
				: conexao.prepareStatement("UPDATE ponto_atendimento SET apelido = ?, cidade = ?, uf = ? WHERE codigo = ?")){
				
			pstm.setString(1, pa.getApelido());
			pstm.setString(2, pa.getCidade());
			pstm.setString(3, pa.getUf());
				
			if(pa.getCodigo() > 0) {
					
				pstm.setInt(4, pa.getCodigo());
					
			}
				
			pstm.execute();

			
			AuditoriaDAO.gravaAuditoria(new Auditoria(tabela,
					"GRAVOU DADOS DE PA",
					"PA: " +  pa.getCodigo(),
					usuario),
				conexao);
			
			return true;
			
		}catch(Exception sqle) {
			
			System.err.println("Erro SQL: " + sqle);
			
			return false;
			
		}
		
	}

	public List<PontoAtendimento> listaPontos(){
		
		List<PontoAtendimento> pontos = new ArrayList<PontoAtendimento>();

		String sql = "SELECT * FROM ponto_atendimento ORDER BY(apelido)";
		
		try(PreparedStatement consulta = conexao.prepareStatement(sql)){

			pontos = mapaResultados(consulta.executeQuery()); 

			return pontos;
				
		}catch(Exception sqle) {
			
			System.err.println("Erro SQL: " + sqle);
			
			return null;
			
		}
		
	}
	
	public PontoAtendimento buscaPonto(int codigo){
		
		PontoAtendimento pa= new PontoAtendimento();
		
		String sql = "SELECT * FROM ponto_atendimento WHERE codigo = ?";
		
		try(PreparedStatement pstm = conexao.prepareStatement(sql)){
			
			pstm.setInt(1, codigo);
			
			pa= mapaResultado(pstm.executeQuery());

			return pa;
			
		}catch(Exception e) {
			
			System.err.println("Erro ao buscar o ponto de atendimento: " + e.getStackTrace());
			
			return null;
			
		}
		
	}

	public PontoAtendimento mapaResultado(ResultSet resultado) throws Exception {
		
		PontoAtendimento pa = new PontoAtendimento();
		
		if(resultado.next()) {
			
			pa.setCodigo(resultado.getInt("codigo"));
			pa.setApelido(resultado.getString("apelido"));
			pa.setCidade(resultado.getString("cidade"));
			pa.setUf(resultado.getString("uf"));
			
		}
		
		return pa;
		
		
	}
	
	public List<PontoAtendimento> mapaResultados(ResultSet resultado) throws Exception {
		
		List<PontoAtendimento> pas = new ArrayList<>();
		
		
		
		while(resultado.next()) {
			
			PontoAtendimento pa = new PontoAtendimento();
			pa.setCodigo(resultado.getInt("codigo"));
			pa.setApelido(resultado.getString("apelido"));
			pa.setCidade(resultado.getString("cidade"));
			pa.setUf(resultado.getString("uf"));
			pas.add(pa);
			
			
		}
		
		return pas;
		
		
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
