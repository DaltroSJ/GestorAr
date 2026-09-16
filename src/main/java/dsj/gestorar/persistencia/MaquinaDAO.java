package dsj.gestorar.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import dsj.gestorar.modelo.Ativo;
import dsj.gestorar.modelo.Auditoria;
import dsj.gestorar.modelo.Maquina;
import dsj.gestorar.modelo.Usuario;

public class MaquinaDAO implements AutoCloseable{
	
	private final String tabela = "MAQUINA";
	private int codigoMaquinaInserida;
	private final Connection conexao;
	
	public MaquinaDAO() throws SQLException{
		
		this.conexao = GerenciarConexoes.getConexao();
		
	}
	public int getCodigoMaquinaInserida() {
		return codigoMaquinaInserida;
	}
	
	public boolean verificaHostnameMaquina(String hostname) {
		
		try(PreparedStatement pstm = conexao.prepareStatement("SELECT 1 FROM maquina WHERE nome = ? AND situacao = 'ATIVA'")){

			pstm.setString(1, hostname);
			
			ResultSet rs = pstm.executeQuery();
			
			return rs.next();
			
		}catch(Exception e) {
			
			return false;
			
		}
		
	}
	
	public boolean gravarMaquina(Maquina maquina, Usuario usuario) {
	    verificaCampos(maquina);

	    String sqlInsert = "INSERT INTO maquina (nome, os, biometria, camera, grafico, impressora, mae, memoria, processador, provedor, ssdhdd, mac, data_ativacao, modelo, dna, criptografia, codigo_ponto_atendimento, situacao, placa_rede, nfe) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	    String sqlUpdate = "UPDATE maquina SET nome = ?, os = ?, biometria = ?, camera = ?, grafico = ?, impressora = ?, mae = ?, memoria = ?, processador = ?, provedor = ?, ssdhdd = ?, mac = ?, data_ativacao = ?, modelo = ?, dna = ?, criptografia = ?, codigo_ponto_atendimento = ?, situacao = ?, placa_rede = ?, nfe = ? WHERE codigo = ?";

	    boolean isInsert = maquina.getCodigo() <= 0;

	    try (PreparedStatement pstm = conexao.prepareStatement(isInsert ? sqlInsert : sqlUpdate, PreparedStatement.RETURN_GENERATED_KEYS)) {

	        pstm.setString(1, maquina.getNome());
	        pstm.setString(2, maquina.getOs());
	        pstm.setString(3, maquina.getBiometria());
	        pstm.setString(4, maquina.getCamera());
	        pstm.setString(5, maquina.getGrafico());
	        pstm.setString(6, maquina.getImpressora());
	        pstm.setString(7, maquina.getMae());
	        pstm.setString(8, maquina.getMemoria());
	        pstm.setString(9, maquina.getProcessador());
	        pstm.setString(10, maquina.getProvedor());
	        pstm.setString(11, maquina.getSsdhdd());
	        pstm.setString(12, maquina.getMac());

	        Timestamp data = new Timestamp(new SimpleDateFormat("dd.MM.yyyy").parse(maquina.getDataAtivacao()).getTime());
	        pstm.setTimestamp(13, data);

	        pstm.setString(14, maquina.getModelo());
	        pstm.setString(15, maquina.getDna());
	        pstm.setString(16, maquina.getModoCriptografia());
	        pstm.setInt(17, maquina.getPa().getCodigo());
	        pstm.setString(18, maquina.getSituacao());
	        pstm.setString(19, maquina.getPlacaRede());
	        pstm.setString(20, maquina.getNfe());

	        if (isInsert) {
	            pstm.executeUpdate();
	            try (ResultSet rs = pstm.getGeneratedKeys()) {
	                if (rs.next()) {
	                    codigoMaquinaInserida = rs.getInt(1);
	                }
	            }
	        } else {
	            pstm.setInt(21, maquina.getCodigo());
	            int codigoPaAntigo = carregaMaquina(maquina.getCodigo()).getPa().getCodigo();
	            if(maquina.getPa().getCodigo() != codigoPaAntigo) {
	            	try(AgentesMaquinaDAO amDAO = new AgentesMaquinaDAO();
	            			AgenteDAO aDAO = new AgenteDAO()){
	            		List<Integer> agentesPA = aDAO.listaCodigosAgentesPorPonto(codigoPaAntigo);
	            		if(agentesPA.size() > 0) {
	            			for(int a : agentesPA) {
		            			amDAO.desassocia(maquina.getCodigo(), a, codigoPaAntigo, usuario);
		            		}
	            		}
	            			
	            	}
	            }
	            pstm.executeUpdate();
	            codigoMaquinaInserida = maquina.getCodigo();
	            
	        }
	        AuditoriaDAO.gravaAuditoria(new Auditoria(tabela,
					"GRAVOU DADOS DE MAQUINA ",
					"MAQUINA: " + codigoMaquinaInserida,
					usuario),
				conexao);

	        return true;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	public boolean deletaMaquina(Maquina maquina, Usuario usuario) {
		
		String sql = "DELETE FROM maquina WHERE codigo = ?";
		
		try(PreparedStatement pstm = conexao.prepareStatement(sql)) {
			
			pstm.setInt(1, maquina.getCodigo());
			
			pstm.execute();
			AuditoriaDAO.gravaAuditoria(new Auditoria(tabela,
					"DELETOU DADOS DE MAQUINA ",
					"MAQUINA: " + maquina.getCodigo(),
					usuario),
				conexao);
			
			return true;
			
		}catch(Exception sqle) {
			
			System.err.println("Erro SQL: " + sqle);
			
			return false;
			
		}
		
	}
	
	public List<Maquina> listaMaquinas(String situacao) {
	    boolean todas = "Todas".equalsIgnoreCase(situacao);

	    String sql = todas 
	        ? "SELECT * FROM maquina ORDER BY nome" 
	        : "SELECT * FROM maquina WHERE situacao = ? ORDER BY nome";

	    try (PreparedStatement pstm = conexao.prepareStatement(sql)) {
	        if (!todas) {
	            pstm.setString(1, situacao);
	        }

	        List<Maquina> maquinas = mapaResultados(pstm.executeQuery());
	        
	        
	        return maquinas;

	    } catch (Exception e) {
	        System.err.println("Erro ao listar maquinas (" + situacao + "): " + e.getMessage());
	        e.printStackTrace();
	        return Collections.emptyList();
	    }
	}
	
	public List<Maquina> listaMaquinasAtivas(){
		
		try(PreparedStatement pstm = conexao.prepareStatement("SELECT * FROM maquina WHERE situacao = 'ATIVA' ORDER BY(nome)")) {
				
			return mapaResultados(pstm.executeQuery());
			
		}catch(Exception sqle) {
			
			System.err.println("Erro de SQL: " + sqle);
			
			return null;
			
		}
		
		
		
	}
	
	public List<Maquina> buscaMaquinas(String pesquisa, List<Maquina> maquinas) {
		
	    if (maquinas == null || maquinas.isEmpty()) {
	    	
	        try (PreparedStatement pstm = conexao.prepareStatement("SELECT * FROM maquina WHERE nome LIKE ? ORDER BY nome")) {
	        	
	            pstm.setString(1, "%" + pesquisa + "%");
	            
	            return mapaResultados(pstm.executeQuery());
	            
	        } catch (Exception sqle) {
	        	
	            System.err.println("Erro de SQL: " + sqle);
	            
	            return new ArrayList<>();
	            
	        }
	        
	    } else {
	    	
	        return maquinas.stream()
	                .filter(m -> m.getNome().toLowerCase().contains(pesquisa.toLowerCase()))
	                .collect(Collectors.toList());
	    }
	}
	
	public Maquina carregaMaquina(int codigo) {

		try(PreparedStatement pstm = conexao.prepareStatement("SELECT * FROM maquina WHERE codigo = ?")) {

			pstm.setInt(1, codigo);
			
			Maquina maquina = mapaResultado(pstm.executeQuery());
			
			return maquina;
			
		}catch(Exception sqle) {
			
			System.err.println("Erro SQL: " + sqle);
			
			return null;
			
		}
	}
	
	public List<Maquina> listaMaquinasPendentes(){
		
		try {
			
			StringBuilder sql = new StringBuilder("SELECT * FROM maquina");
			
			List<Ativo> ativos = new ArrayList<>();
			
			try(AtivoDAO aDAO = new AtivoDAO()){
				
				ativos = aDAO.listaAtivos();
				
			
			}catch(Exception e) {
				
				System.err.println(e);
			}
			
		    if (!ativos.isEmpty()) {
		    	
		        sql.append(" WHERE nome NOT IN (");
		        
		        for (int i = 0; i < ativos.size(); i++) {
		        	
		            sql.append("?");
		            
		            if (i < ativos.size() - 1) {
		            	
		                sql.append(", ");
		                
		            }
		            
		        }
		        
		        sql.append(")");
		    }

		    
		    if (sql.indexOf("WHERE") >= 0) {
		    	
		        sql.append(" AND");
		        
		    } else {
		    	
		        sql.append(" WHERE");
		        
		    }
		    
		    sql.append(" maquina.situacao = 'PENDENTE' ORDER BY nome");
			
			try(PreparedStatement pstm = conexao.prepareStatement(sql.toString())){
				
				for(int i = 0; i < ativos.size(); i++) {
					
					pstm.setString(i + 1, ativos.get(i).getMaquina().getNome());
					
				}
				
				return mapaResultados(pstm.executeQuery());
				
			}
			
		}catch(Exception sqle) {
			
			System.err.println("Erro de SQL: " + sqle + "AQUI: "+ sqle.getMessage());
			
			return null;
			
		}
		
	}
	
	public int contaMaquinasSituacao(String parametro) {

	    String p = parametro.toUpperCase();
	    Integer quantidade = null;
	    try {
	        if (p.equals("TODAS")) {

	            try (PreparedStatement pstm = conexao.prepareStatement(
	                    "SELECT COUNT(*) AS numero FROM maquina")) {

	                ResultSet rs = pstm.executeQuery();
	                if (rs.next()) quantidade = rs.getInt("numero");
	                return quantidade;
	            }

	        } else {

	            try (PreparedStatement pstm = conexao.prepareStatement(
	                    "SELECT COUNT(*) AS numero FROM maquina WHERE situacao = ?")) {

	                pstm.setString(1, p);
	                ResultSet rs = pstm.executeQuery();
	                if (rs.next()) quantidade = rs.getInt("numero");

	                return quantidade;
	            }
	        }

	    } catch (Exception e) {
	        System.err.println("Erro de SQL: " + e);
	        return 0;
	    }
	}
	
	public boolean verificaCampos(Maquina maquina) {
		
		if(maquina == null) {
			
			return false;
			
			
		}else {
			
			if(maquina.getNome().equals(null) || maquina.getNome().isEmpty()) {
				
				return false;
				
			}else if(maquina.getOs().equals(null) || maquina.getOs().isEmpty()){
				
				return false;
				
			}else if(maquina.getBiometria().equals(null) || maquina.getBiometria().isEmpty()){
				
				return false;
				
			}else if(maquina.getCamera().equals(null) || maquina.getCamera().isEmpty()){
				
				return false;
				
			}else if(maquina.getGrafico().equals(null) || maquina.getGrafico().isEmpty()){
				
				return false;
				
			}else if(maquina.getImpressora().equals(null) || maquina.getImpressora().isEmpty()){
				
				return false;
				
			}else if(maquina.getMae().equals(null) || maquina.getMae().isEmpty()){
				
				return false;
				
			}else if(maquina.getMemoria().equals(null) || maquina.getMemoria().isEmpty()){

				
				return false;
				
			}else if(maquina.getProcessador().equals(null) || maquina.getProcessador().isEmpty()){
				

				return false;
				
			}else if(maquina.getProvedor().equals(null) || maquina.getProvedor().isEmpty()){

				return false;
				
			}else if(maquina.getSsdhdd().equals(null) || maquina.getSsdhdd().isEmpty()){

				return false;
				
			}else if(maquina.getMac().equals(null) || maquina.getMac().isEmpty()){
				
				return false;
				
			}else if(maquina.getDataAtivacao().equals(null) || maquina.getDataAtivacao().isEmpty()){
				
				
				return false;
				
			}else if(maquina.getModelo().equals(null) || maquina.getModelo() .isEmpty()){
				
				
				return false;
				
			}else if(maquina.getDna().equals(null) || maquina.getDna() .isEmpty()){
				
				
				return false;
				
			}else if(maquina.getModoCriptografia().equals(null) || maquina.getModoCriptografia() .isEmpty()){
				
				
				return false;
				
			}else if(maquina.getPa().getCodigo() == 0) {
				
				return false;
				
			}
				
		}
		return true;
		
	}
	
	private Maquina mapaResultado(ResultSet resultados) throws Exception {
		
		Maquina maquina = new Maquina();
		
		if(resultados.next()) {
			
			maquina.setCodigo(resultados.getInt("codigo"));
			maquina.setNome(resultados.getString("nome"));
			maquina.setOs(resultados.getString("os"));
			maquina.setBiometria(resultados.getString("biometria"));
			maquina.setCamera(resultados.getString("camera"));
			maquina.setGrafico(resultados.getString("grafico"));
			maquina.setImpressora(resultados.getString("impressora"));
			maquina.setMae(resultados.getString("mae"));
			maquina.setMemoria(resultados.getString("memoria"));
			maquina.setProcessador(resultados.getString("processador"));
			maquina.setProvedor(resultados.getString("provedor"));
			maquina.setSsdhdd(resultados.getString("ssdhdd"));
			maquina.setMac(resultados.getString("mac"));
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			String data = sdf.format(resultados.getTimestamp("data_ativacao"));
			
			maquina.setDataAtivacao(data);
			maquina.setModelo(resultados.getString("modelo"));
			maquina.setDna(resultados.getString("dna"));
			maquina.setModoCriptografia(resultados.getString("criptografia"));
			
			try(PontoAtendimentoDAO pDAO = new PontoAtendimentoDAO()){
				maquina.setPa(pDAO.buscaPonto(resultados.getInt("codigo_ponto_atendimento")));
			}
			
			
			maquina.setPlacaRede(resultados.getString("placa_rede"));
			maquina.setNfe(resultados.getString("nfe"));
			maquina.setSituacao(resultados.getString("situacao"));
			
		}
		
		return maquina;
		
	}
	
	private List<Maquina> mapaResultados(ResultSet resultados) throws Exception {

		
		List<Maquina> maquinas = new ArrayList<>();
		
		while(resultados.next()) {
				
			Maquina maquina = new Maquina();
			
			maquina.setCodigo(resultados.getInt("codigo"));
			maquina.setNome(resultados.getString("nome"));
			maquina.setOs(resultados.getString("os"));
			maquina.setBiometria(resultados.getString("biometria"));
			maquina.setCamera(resultados.getString("camera"));
			maquina.setGrafico(resultados.getString("grafico"));
			maquina.setImpressora(resultados.getString("impressora"));
			maquina.setMae(resultados.getString("mae"));
			maquina.setMemoria(resultados.getString("memoria"));
			maquina.setProcessador(resultados.getString("processador"));
			maquina.setProvedor(resultados.getString("provedor"));
			maquina.setSsdhdd(resultados.getString("ssdhdd"));
			maquina.setMac(resultados.getString("mac"));
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			String data = sdf.format(resultados.getTimestamp("data_ativacao"));
			
			maquina.setDataAtivacao(data);
			maquina.setModelo(resultados.getString("modelo"));
			maquina.setDna(resultados.getString("dna"));
			maquina.setModoCriptografia(resultados.getString("criptografia"));
			
			try(PontoAtendimentoDAO pDAO = new PontoAtendimentoDAO()){
				maquina.setPa(pDAO.buscaPonto(resultados.getInt("codigo_ponto_atendimento")));
			}
			
			
			maquina.setPlacaRede(resultados.getString("placa_rede"));
			maquina.setNfe(resultados.getString("nfe"));
			maquina.setSituacao(resultados.getString("situacao"));
			
			
			maquinas.add(maquina);

		}
		return maquinas;
		
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
