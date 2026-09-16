package dsj.gestorar.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dsj.gestorar.modelo.Agente;
import dsj.gestorar.modelo.Auditoria;
import dsj.gestorar.modelo.Usuario;

public class AgenteDAO implements AutoCloseable{
	
	private final Connection conexao;
	public AgenteDAO() throws SQLException{
		
		this.conexao = GerenciarConexoes.getConexao();
		
	}
	
	private String tabela = "AGENTE";
	
	public boolean verificaCampos(Agente a) {
		
		if(a.getNome().isEmpty() || a.getNome() == "") {
			
			return false;
			
		}
		
		if(a.getCpf().isEmpty() || a.getCpf() == "") {
			
			return false;
			
		}
		
		
		if(a.getSituacao().isEmpty() || a.getSituacao() == "") {
			
			return false;
			
		}
		
		if(a.getPontoAtendimento() == null || a.getPontoAtendimento().getCodigo() <= 0) {
			
			return false;
			
		}
		
		if(a.getDataHabilitacao().isEmpty() || a.getDataHabilitacao() == "") {
			
			return false;
			
		}
		
		if(a.getPontoAtendimento().getCodigo() == 0) {
			
			return false;
			
		}
		return true;
	}
	
	public boolean gravaAgente(Agente a, Usuario usuario) {
		
		if(!verificaCampos(a)) {
			
			return false;
			
		}
		
		boolean inserindo = a.getCodigo() <= 0;
		
		try(PreparedStatement pstm = (inserindo) 
				? conexao.prepareStatement("INSERT INTO agente (nome, cpf, situacao, codigo_ponto_atendimento, data_habilitacao) VALUES (?,?,?,?,?)")
				: conexao.prepareStatement("UPDATE agente SET nome = ?, cpf = ?, situacao = ?, codigo_ponto_atendimento = ?, data_habilitacao = ? WHERE codigo = ?")) {
		
			
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				
				Date datax = sdf.parse(a.getDataHabilitacao());
				
				Timestamp data = new Timestamp(datax.getTime());
				
				pstm.setString(1, a.getNome());
				pstm.setString(2, a.getCpf());
				pstm.setString(3, a.getSituacao());
				pstm.setInt(4, a.getPontoAtendimento().getCodigo());
				pstm.setTimestamp(5, data);
				
				if(!inserindo) {
					
					pstm.setInt(6, a.getCodigo());
				}
				
				pstm.execute();
				AuditoriaDAO.gravaAuditoria(new Auditoria(tabela,
						"GRAVAÇÃO DE REGISTRO",
						String.valueOf((inserindo) ? "NOVO" : a.getCodigo()),
						usuario),
					conexao);
				
				return true;
			
			
		}catch(Exception sqle) {
			
			System.err.println("Erro SQL: " + sqle);
			
			return false;
		}
		
	}
	
	public List<Agente> listaAgentes(){
		
		List<Agente> agentes = new ArrayList<Agente>();
		
		try(PreparedStatement pstm = conexao.prepareStatement("SELECT * FROM agente ORDER BY(nome)")){
			
			agentes = mapaResultados(pstm.executeQuery());
			return agentes;
			
		}catch(Exception sqle) {
			
			System.err.println("Erro SQL: " + sqle);
			
			return null;
			
		}
		
	}
	
	public Agente buscaAgente(int codigo){
		
		Agente agente = new Agente();
		
		try(PreparedStatement pstm = conexao.prepareStatement("SELECT * FROM agente WHERE codigo = ?")) {
			
			pstm.setInt(1, codigo);
			agente = mapaResultado(pstm.executeQuery());
			return agente;
			
		}catch(Exception sqle) {
			
			System.err.println("Erro SQL: " + sqle);
			
			return null;
			
		}
		
	}
	
	public List<Agente> listaAgentesFiltro(int codigo){
		
		List<Agente> agentes = new ArrayList<Agente>();
		
		try(PreparedStatement pstm = conexao.prepareStatement("SELECT * FROM agente WHERE codigo_ponto_atendimento = ?")) {
			
			pstm.setInt(1, codigo);

			agentes = mapaResultados(pstm.executeQuery());
			return agentes;
			
		}catch(Exception sqle) {
			
			System.err.println("Erro SQL: " + sqle);
			
			return null;
			
		}
		
	}
	
	private Agente mapaResultado(ResultSet resultado) throws Exception {
		
		Agente a = new Agente();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		
		if(resultado.next()) {
			
			a.setCodigo(resultado.getInt("codigo"));
			a.setNome(resultado.getString("nome"));
			a.setCpf(resultado.getString("cpf"));
			a.setDataHabilitacao(sdf.format(resultado.getTimestamp("data_habilitacao")));
			a.setEmail(resultado.getString("EMAIL"));
			a.setSituacao(resultado.getString("situacao"));
			try(PontoAtendimentoDAO pDAO = new PontoAtendimentoDAO()){
				
				a.setPontoAtendimento(pDAO.buscaPonto(resultado.getInt("codigo_ponto_atendimento")));
				
			}
			
			
		}
		
		return a;
		
	}
	
	private List<Agente> mapaResultados(ResultSet resultado) throws Exception {
		
		List<Agente> agentes = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		
		while(resultado.next()) {
			
			Agente a = new Agente();
			a.setCodigo(resultado.getInt("codigo"));
			a.setNome(resultado.getString("nome"));
			a.setCpf(resultado.getString("cpf"));
			a.setDataHabilitacao(sdf.format(resultado.getTimestamp("data_habilitacao")));
			a.setEmail(resultado.getString("EMAIL"));
			a.setSituacao(resultado.getString("situacao"));
			try(PontoAtendimentoDAO pDAO = new PontoAtendimentoDAO()){
				
				a.setPontoAtendimento(pDAO.buscaPonto(resultado.getInt("codigo_ponto_atendimento")));
				
			}
			agentes.add(a);
			
			
		}
		
		return agentes;
		
	}
	
	public List<Agente> listaAgentesCodigo(List<Integer> codigoAgentes){
		
		List<Agente> agentes = new ArrayList<Agente>();
		
		StringBuilder sql = new StringBuilder();
		
		if (!codigoAgentes.isEmpty()) {
	           
			sql.append("SELECT * FROM agente WHERE codigo IN (");
            
            for (int i = 0; i < codigoAgentes.size(); i++) {
            	
            	sql.append("?");
                
                if (i < codigoAgentes.size() - 1) {
                	
                	sql.append(",");
                    
                }
                
            }
            
            sql.append(")");
	    }else {
	    	
	    	new Exception("Erro, a lista de codigo esta vazia");
	    	
	    }
		
		try(PreparedStatement pstm = conexao.prepareStatement(sql.toString())){
			
			for (int i = 0; i < codigoAgentes.size(); i++) {
	    		
                pstm.setInt(i + 1, codigoAgentes.get(i));
                
            }
			
			agentes = mapaResultados(pstm.executeQuery());

			
			return agentes;
			
		}catch(Exception e) {
			
			System.err.println("Erro na pesquisa de agentes: "+ e);
			
			return null;
			
		}
		
	}

	public List<Integer> listaCodigosAgentesPorPonto(int codigoPa){
		
		List<Integer> codigos = new ArrayList<Integer>();
		try(PreparedStatement pstm = conexao.prepareStatement("SELECT codigo FROM agente WHERE codigo_ponto_atendimento = ?")){
			
			pstm.setInt(1, codigoPa);
			
			ResultSet rs = pstm.executeQuery();
			codigos = new ArrayList<Integer>();
			while(rs.next()) {
				
				codigos.add(rs.getInt("codigo"));
				
			}
			return codigos;
			
		}catch(Exception e) {
			
			System.err.println(e);
			return null;
			
		}
					
		
	}

	
	public List<Agente> listaAgentesExportacaoCompleta(){
		
		String sql = "SELECT * FROM AGENTE";
		
		try(PreparedStatement consulta = conexao.prepareStatement(sql);
				ResultSet resultado = consulta.executeQuery();){
			
			return mapaResultados(resultado);
			
		} catch (Exception e) {
			
			System.err.println("Erro na consulta de agentes: " + e);
			return null;
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
