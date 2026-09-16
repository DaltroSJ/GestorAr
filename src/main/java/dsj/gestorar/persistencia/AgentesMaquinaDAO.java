package dsj.gestorar.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dsj.gestorar.modelo.Agente;
import dsj.gestorar.modelo.AgentesMaquina;
import dsj.gestorar.modelo.Auditoria;
import dsj.gestorar.modelo.Maquina;
import dsj.gestorar.modelo.Usuario;

public class AgentesMaquinaDAO implements AutoCloseable{

	private final String tabela = "AGENTES_MAQUINA";
	private final Connection conexao;
	public AgentesMaquinaDAO() throws SQLException{
		
		this.conexao = GerenciarConexoes.getConexao();
		
	}
	
	public List<Agente> listaAgentesMaquina(int codigoMaquina, int codigoPA) {
		
		List<Agente> agentesMaquina = new ArrayList<Agente>();
		
		List<Integer> codigoAgentes = new ArrayList<>();
			
			try(PreparedStatement pstm = conexao.prepareStatement("SELECT codigo_agente FROM agentes_maquina WHERE codigo_ponto_atendimento = ? AND codigo_maquina = ?")) {

		        pstm.setInt(1, codigoPA);
		        pstm.setInt(2, codigoMaquina);

		        ResultSet rs = pstm.executeQuery();

		        while (rs.next()) {
		        	
		            codigoAgentes.add(rs.getInt("codigo_agente"));
		            
		        }
		        
		    }catch(Exception e) {
		    	
		    	System.err.println("Erro ao criar lista de codigos de agentes: " + e);
		    	
		    }
			
		
	    
	    
	    if(codigoAgentes.isEmpty()) {
	    	
	    	return Collections.emptyList();
	    	
	    }
	    
	    try(AgenteDAO aDAO = new AgenteDAO()){
	    
	    	agentesMaquina = aDAO.listaAgentesCodigo(codigoAgentes);
	    	
	    	return agentesMaquina;
	    	
	    } catch (Exception e) {
	    	
	        System.err.println("Erro ao pesquisar agentes: " + e);
	        return null;
	    }
	    
	}
	
	public List<Agente> listaAgentesPonto(int codigoPA){
		
		List<Agente> agentes = new ArrayList<Agente>();
		
		try(AgenteDAO aDAO = new AgenteDAO()){
			
			agentes = aDAO.listaAgentesCodigo(aDAO.listaCodigosAgentesPorPonto(codigoPA));
			return agentes;
		
		}catch(Exception sqle) {
			
			System.err.println("Erro ao pesquisar agentes: " + sqle);
			
			return null;
		}
		
	}
	
	public List<Agente> listaAgentesMaquina(int codigoMaquina) {

	    List<Integer> codigoAgentes = new ArrayList<>();

	    try (PreparedStatement pstm =
	             conexao.prepareStatement("SELECT codigo_agente FROM agentes_maquina WHERE codigo_maquina = ?")) {

	        pstm.setInt(1, codigoMaquina);

	        ResultSet rs = pstm.executeQuery();

	        while (rs.next()) {
	            codigoAgentes.add(rs.getInt("codigo_agente"));
	        }
	        try (AgenteDAO aDAO = new AgenteDAO()) {
	            return aDAO.listaAgentesCodigo(codigoAgentes);
	        }

	    } catch (Exception e) {

	        System.err.println(e);
	        return null;
	    }
	}
	
	public List<AgentesMaquina> listaGeral(){
		
	    List<AgentesMaquina> agentes = new ArrayList<>();

	    try (PreparedStatement pstm =
	             conexao.prepareStatement("SELECT * FROM agentes_maquina")) {
	    	
	        ResultSet rs = pstm.executeQuery();

	        while (rs.next()) {
	        	AgentesMaquina am = new AgentesMaquina();
	        	am.setCodigo(rs.getInt("codigo")); 
	        	am.setMaquina(new Maquina(rs.getInt("codigo_maquina")));
	        	am.setAgente(new Agente(rs.getInt("codigo_agente")));
	        	
	        	agentes.add(am);
	        }
	        
	        return agentes;

	    } catch (Exception e) {

	        System.err.println(e);
	        return null;
	    }
		
	}
	
	public boolean associa(int codigoMaquina, int codigoAgente, int codigoPa, Usuario usuario){
		
		try (PreparedStatement pstm = conexao.prepareStatement("INSERT INTO agentes_maquina (codigo_maquina, codigo_agente, codigo_ponto_atendimento) VALUES (?,?,?)")){

			pstm.setInt(1, codigoMaquina);
			pstm.setInt(2, codigoAgente);
			pstm.setInt(3, codigoPa);
			
			pstm.execute();

			AuditoriaDAO.gravaAuditoria(new Auditoria(tabela,
					"ASSOCIOU AGENTE A MAQUINA",
					"AGENTE: " + codigoAgente +" MAQUINA: "+ codigoMaquina + " PA: "+ codigoPa,
					usuario),
				conexao);
			
			return true;

		}catch(Exception sqle) {
			
			System.err.println("Erro ao inserir dados: " + sqle.getMessage());
			
			return false;
		}
		
	}
	
	public boolean desassocia(int codigoMaquina, int codigoAgente, int codigoPa, Usuario usuario){
		
		try (PreparedStatement pstm = conexao.prepareStatement("DELETE FROM agentes_maquina"
				+ " WHERE codigo_maquina = ? AND"
				+ " codigo_agente = ? AND"
				+ " codigo_ponto_atendimento = ?")){
			
			pstm.setInt(1, codigoMaquina);
			pstm.setInt(2, codigoAgente);
			pstm.setInt(3, codigoPa);
			
			pstm.execute();
			
			AuditoriaDAO.gravaAuditoria(new Auditoria(tabela,
					"DESASSOCIOU AGENTE A MAQUINA",
					"AGENTE: " + codigoAgente +" MAQUINA: "+ codigoMaquina + " PA: "+ codigoPa,
					usuario),
				conexao);
			
			return true;

		}catch(Exception sqle) {
			
			System.err.println("Erro ao deletar dados: " + sqle.getMessage());
			
			return false;
		}
		
	}
	
	public boolean existe(int codigoMaquina, int codigoAgente, int codigoPa){
		
		try(PreparedStatement pstm = conexao.prepareStatement("SELECT * FROM agentes_maquina WHERE codigo_maquina = ? AND codigo_agente = ? AND codigo_ponto_atendimento = ?");) {
			
			pstm.setInt(1, codigoMaquina);
			pstm.setInt(2, codigoAgente);
			pstm.setInt(3, codigoPa);

			if(pstm.executeQuery().next()) {
				
				return true;
				
			}else {

				
				return false;
			}
		
		}catch(SQLException sqle) {
			
			System.err.println("Erro ao pesquisar agentes: " + sqle);
			
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
