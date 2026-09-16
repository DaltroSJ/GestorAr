package dsj.gestorar.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dsj.gestorar.modelo.Ar;
import dsj.gestorar.modelo.Auditoria;
import dsj.gestorar.modelo.Hostname;
import dsj.gestorar.modelo.Maquina;
import dsj.gestorar.modelo.Usuario;

public class HostnameDAO implements AutoCloseable{

	private final String tabela = "HOSTNAME";
	private final Connection conexao;
	
	public HostnameDAO() throws SQLException{
		
		this.conexao = GerenciarConexoes.getConexao();
		
	}
	
	public boolean resetarHostname(Hostname hostname, Usuario usuario) {
		
		String sql = "UPDATE hostname SET disponivel = ?, liberado = ?, requisitado = ?, nome_requisitante = '', codigo_maquina = null WHERE codigo = ?";
		
		try(PreparedStatement pstm = conexao.prepareStatement(sql)){
			
			
			pstm.setBoolean(1, true);
			pstm.setBoolean(2, true);
			pstm.setBoolean(3, false);
			pstm.setInt(4, hostname.getCodigo());
			
			pstm.executeUpdate();

			AuditoriaDAO.gravaAuditoria(new Auditoria(tabela,
					"RESETOU O HOSTNAME",
					"HOSTNAME: " + hostname.getCodigo(),
					usuario),
				conexao);
			return true;
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean vincularMaquina(Maquina maquina, Hostname hostname, Usuario usuario) {
		
		String sql = "UPDATE hostname SET codigo_maquina = ? WHERE codigo = ?";
		
		try(PreparedStatement pstm = conexao.prepareStatement(sql)){
			
			pstm.setInt(1, maquina.getCodigo());
			pstm.setInt(2, hostname.getCodigo());

			
			pstm.execute();

			AuditoriaDAO.gravaAuditoria(new Auditoria(tabela,
					"VINCULOU MAQUINA AO HOSTNAME",
					"HOSTNAME: " + hostname.getCodigo() + " MAQUINA: "+maquina.getCodigo(),
					usuario),
				conexao);
			
			return true;
			
			
		}catch (Exception e) {
		
			return false;
			
		}
	}
	
	public boolean inserir(Hostname hostname, Usuario usuario) {
		
		String sql = "INSERT INTO hostname (hostname, disponivel, requisitado, liberado, codigo_ar) VALUES (?,?,?,?,?)";
		
		try(PreparedStatement pstm = conexao.prepareStatement(sql)){
			
			pstm.setString(1, hostname.getHostname());
			pstm.setBoolean(2, hostname.isDisponivel());
			pstm.setBoolean(3, hostname.isRequisitado());
			pstm.setBoolean(4, hostname.isLiberado());
			pstm.setInt(5, hostname.getAr().getCodigo());
			
			pstm.executeUpdate();

			AuditoriaDAO.gravaAuditoria(new Auditoria(tabela,
					"INSERIU HOSTNAME "+ hostname.getHostname(),
					"HOSTNAME: " + hostname.getCodigo(),
					usuario),
				conexao);
			
			return true;
			
			
		}catch (Exception e) {
		
			return false;
			
		}
	}
	
	public boolean alteraPropriedade(String propriedade, boolean valor, Hostname hostname, Usuario usuario) {
		
		if (!propriedade.matches("^(requisitado|disponivel|liberado)$")) {
	        throw new IllegalArgumentException("Propriedade inválida");
	    }
		
		String sql = "UPDATE hostname SET " + propriedade +" = ? WHERE codigo = ?";  
		
		try(PreparedStatement pstm = conexao.prepareStatement(sql)){
			
			
			pstm.setBoolean(1, valor);
			pstm.setInt(2, hostname.getCodigo());
			
			pstm.execute();
		
			AuditoriaDAO.gravaAuditoria(new Auditoria(tabela,
					"ALTEROU HOSTNAME "+ hostname.getHostname(),
					"HOSTNAME: " + hostname.getCodigo(),
					usuario),
				conexao);
			
			return true;
			
		}catch(Exception sqle){
			
			return false;
			
		}
		
	}
	
	public Hostname buscaHostname(Hostname hostname) {
	
		Hostname hostnm = new Hostname();
		
		hostnm = new Hostname();
		
		String sql = "SELECT * FROM hostname WHERE codigo = ?";

		try(PreparedStatement pstm = conexao.prepareStatement(sql)) {
			
			pstm.setInt(1, hostname.getCodigo());
			
			ResultSet rs = pstm.executeQuery();
			hostnm = mapaResultado(rs);

			return hostnm;
			
			
		}catch(Exception sqle){
			
			return null;
			
		}
		
	}
	
	public List<Hostname> buscaHostnames(){
		
		List<Hostname> hostnames = new ArrayList<Hostname>();

		String sql = "SELECT * FROM hostname";
		
		try(PreparedStatement pstm = conexao.prepareStatement(sql)) {
			
			ResultSet rs = pstm.executeQuery();
			hostnames = mapaResultados(rs);
			return hostnames;
			
			
		}catch(SQLException sqle){
			
			return null;
			
		}
	}
	
	public List<Hostname> mapaResultados(ResultSet resultados) throws SQLException{
		
		ArrayList<Hostname> lista = new ArrayList<>();
		while(resultados.next()) {
			
			Hostname hostname = new Hostname();
			
			hostname.setCodigo(resultados.getInt("codigo"));
			hostname.setHostname(resultados.getString("hostname"));
			hostname.setRequisitado(resultados.getBoolean("requisitado"));
			hostname.setDisponivel(resultados.getBoolean("disponivel"));
			hostname.setLiberado(resultados.getBoolean("liberado"));
			hostname.setAr(new Ar(resultados.getInt("codigo_ar")));
			hostname.setMaquina(new Maquina(resultados.getInt("codigo_maquina")));
			
			lista.add(hostname);
			
		}
		
		return lista;
	}
	
	public Hostname mapaResultado(ResultSet resultados) throws Exception{
		
		if(resultados.next()) {
			
			Hostname hostname = new Hostname();
			
			hostname.setCodigo(resultados.getInt("codigo"));
			hostname.setHostname(resultados.getString("hostname"));
			hostname.setRequisitado(resultados.getBoolean("requisitado"));
			hostname.setDisponivel(resultados.getBoolean("disponivel"));
			hostname.setLiberado(resultados.getBoolean("liberado"));
			try(ArDAO arDAO = new ArDAO()){
				
				hostname.setAr(arDAO.buscaAr(resultados.getInt("codigo_ar")));
				
			}
			hostname.setMaquina(new Maquina(resultados.getInt("codigo_maquina")));
			
			return hostname;
			
		}
		
		return null;
		
		
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
