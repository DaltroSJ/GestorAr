package dsj.gestorar.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dsj.gestorar.modelo.Auditoria;
import dsj.gestorar.modelo.Maquina;
import dsj.gestorar.modelo.Programas;
import dsj.gestorar.modelo.Usuario;

public class ProgramasDAO implements AutoCloseable{

	private final Connection conexao;

	public ProgramasDAO() throws SQLException{
		
		this.conexao = GerenciarConexoes.getConexao();
		
	}
	private final String tabela = "PROGRAMAS";
	
	public boolean gravaDados(Programas programas, Usuario usuario) {
		
		if(programas.getMaquina() == null) {
			
			return false;			
			
		}
			
		boolean inserindo = programas.getCodigo() <= 0;
		
		try(PreparedStatement pstm = (inserindo) 
				? conexao.prepareStatement("INSERT INTO programas (codigo_maquina, driver_camera,"
						+ " driver_leitor_biometrico, edge, emissao,"
						+ " java, pdf, safesign) VALUES (?,?,?,?,?,?,?,?)") 
				: conexao.prepareStatement("UPDATE programas SET codigo_maquina = ?, driver_camera = ?,"
						+ " driver_leitor_biometrico = ?, edge = ?, emissao = ?,"
						+ " java = ?, pdf = ?, safesign = ? WHERE codigo = ?")) {	
			
			pstm.setInt(1, programas.getMaquina().getCodigo());
			pstm.setString(2, programas.getDriverCamera());
			pstm.setString(3, programas.getDriverLeitorBiometrico());
			pstm.setString(4, programas.getEdge());
			pstm.setString(5, programas.getEmissao());
			pstm.setString(6, programas.getJava());
			pstm.setString(7, programas.getPdf());
			pstm.setString(8, programas.getSafesing());
			
			if(!inserindo) {
			
				pstm.setInt(9, programas.getCodigo());
				
			}
				
			pstm.execute();

			AuditoriaDAO.gravaAuditoria(new Auditoria(tabela,
					"GRAVOU DADOS DE PROGRAMAS",
					"PROGRAMAS: " +  programas.getCodigo(),
					usuario),
				conexao);
			
			return true;
			
		}catch(Exception sqle) {
			
			System.err.println("Erro SQL: " + sqle);
			
		}
		
		
		return false;
		
	}
	
	public List<Programas> listaGeral(){
		
		List<Programas> programasx = new ArrayList<>();
		
		try(PreparedStatement pstm = conexao.prepareStatement("SELECT * FROM programas")) {

			ResultSet rs = pstm.executeQuery();
			
			while(rs.next()) {
				Programas programas = new Programas();
				programas.setCodigo(rs.getInt("codigo"));
				programas.setDriverCamera(rs.getString("driver_camera"));
				programas.setDriverLeitorBiometrico(rs.getString("driver_leitor_biometrico"));
				programas.setEdge(rs.getString("edge"));
				programas.setEmissao(rs.getString("emissao"));
				programas.setJava(rs.getString("java"));
				programas.setPdf(rs.getString("pdf"));
				programas.setSafesing(rs.getString("safesign"));
				Maquina maquina = new Maquina();
				try(MaquinaDAO mDAO = new MaquinaDAO()){
					maquina = mDAO.carregaMaquina(rs.getInt("codigo_maquina"));
				}
				programas.setMaquina(maquina);
				
				programasx.add(programas);
			}
			
			
			return programasx;
			
		}catch(Exception sqle) {
			
			System.err.println("Erro SQL: " + sqle);
			
			return null;
			
		}
		
	}
	
 	public Programas buscaProgramas(Maquina maquina) {
		
		Programas programas = new Programas();
		
		try(PreparedStatement pstm = conexao.prepareStatement("SELECT * FROM programas WHERE codigo_maquina = ?")) {

			pstm.setInt(1, maquina.getCodigo());
			programas = mapaResultado(pstm.executeQuery());
			return programas;
			
		}catch(Exception sqle) {
			
			System.err.println("Erro SQL: " + sqle);
			
			return null;
			
		}
		
	}
	
	public List<Programas> listaCompleta(String dataFinal){

		List<Programas> listaProgramas  = new ArrayList<>();
		
		String sql = "";
		List<String> listaMaquinasAtivas = null;
		
		try(AtivoDAO aDAO = new AtivoDAO()){
			
			listaMaquinasAtivas = aDAO.listaInventarioProgramas(dataFinal);
			
			sql = "SELECT programas.codigo_maquina AS cm, "
					+ "programas.codigo as pc,"
					+ "programas.driver_camera AS dc, "
					+ "programas.driver_leitor_biometrico AS dl, "
					+ "programas.edge AS e, "
					+ "programas.emissao AS em, "
					+ "programas.java AS j, "
					+ "programas.pdf AS p, "
					+ "programas.safesign AS safe, "
					+ "maquina.nome AS mn, "
					+ "maquina.mac as mm, "
					+ "maquina.os AS mo, "
					+ "maquina.biometria AS mb, "
					+ "maquina.camera AS mc, "
					+ "maquina.data_ativacao AS mdt, "
					+ "ar.nome, "
					+ "ar.vinculacao AS av,"
					+ "ativo.codigo AS ac "
					+ "FROM ativo "
					+ "LEFT JOIN maquina ON maquina.codigo = ativo.codigo_maquina "
					+ "LEFT JOIN programas ON programas.codigo_maquina = maquina.codigo "
					+ "LEFT JOIN ar ON ar.codigo = ativo.codigo_ar "
					+ "WHERE maquina.nome IN (";
			
			for(int i = 0; i < listaMaquinasAtivas.size(); i++) {
				
				if(i > 0) {
					
					sql+=", ";
					
				}
				
				sql+="?";
				
			}
			
			sql+=") ORDER BY mn";
			
		}catch(Exception e) {
			
			System.err.println(e);
			
		}
		
		if(listaMaquinasAtivas.isEmpty()) {
			
			return Collections.emptyList();
			
		}
		
		try (PreparedStatement pstm = conexao.prepareStatement(sql)){
			
			for(int i = 0; i < listaMaquinasAtivas.size(); i++) {
				
				pstm.setString(i + 1, listaMaquinasAtivas.get(i));
				
			}
			
			
			ResultSet  resultado = pstm.executeQuery();
			
			while (resultado.next()) {
				
				Programas programas = new Programas();
				
				programas.setCodigo(resultado.getInt("pc"));
				programas.setDriverCamera(resultado.getString("dc"));
				programas.setDriverLeitorBiometrico(resultado.getString("dl"));
				programas.setEdge(resultado.getString("e"));
				programas.setEmissao(resultado.getString("em"));
				programas.setJava(resultado.getString("j"));
				programas.setPdf(resultado.getString("p"));
				programas.setSafesing(resultado.getString("safe"));
				programas.setMaquina(new Maquina(resultado.getInt("cm")));
				
				Maquina maquina;
				
				try(MaquinaDAO mDAO = new MaquinaDAO()){
					
					maquina = mDAO.carregaMaquina(programas.getMaquina().getCodigo());
					
					programas.setMaquina(maquina);
					
				}
				
				listaProgramas.add(programas);
				
				
			}
			
			return listaProgramas;
			
		}catch(Exception sqle) {
			
			System.err.println("Erro ao obter lista completa de maquinas: " + sqle);
					
					
			return null;
		}
		
		
	}

	public Programas mapaResultado(ResultSet resultado) throws Exception{
		
		Programas programas = new Programas();
		
		while(resultado.next()) {
			
			programas.setCodigo(resultado.getInt("codigo"));
			programas.setDriverCamera(resultado.getString("driver_camera"));
			programas.setDriverLeitorBiometrico(resultado.getString("driver_leitor_biometrico"));
			programas.setEdge(resultado.getString("edge"));
			programas.setEmissao(resultado.getString("emissao"));
			programas.setJava(resultado.getString("java"));
			programas.setPdf(resultado.getString("pdf"));
			programas.setSafesing(resultado.getString("safesign"));
			Maquina maquina = new Maquina();
			try(MaquinaDAO mDAO = new MaquinaDAO()){
				maquina = mDAO.carregaMaquina(resultado.getInt("codigo_maquina"));
			}
			programas.setMaquina(maquina);
		}
		
		return programas;
		
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
