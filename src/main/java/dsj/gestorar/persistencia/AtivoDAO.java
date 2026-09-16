package dsj.gestorar.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dsj.gestorar.modelo.Ativo;
import dsj.gestorar.modelo.Auditoria;
import dsj.gestorar.modelo.Usuario;

public class AtivoDAO implements AutoCloseable{
	
	private final String tabela = "ATIVO";
	private final Connection conexao;

	public AtivoDAO() throws SQLException{
		
		this.conexao = GerenciarConexoes.getConexao();
		
	}
	
	public boolean existeResgistroGestor(int codigoGestor) {

		try(PreparedStatement pstm = conexao.prepareStatement("SELECT * FROM ativo WHERE codigo_gestor = ?")){
			
			pstm.setInt(1, codigoGestor);
			
			if(pstm.executeQuery().next()) {

				return true;
				
			}else {

				return false;
				
			}
			
		}catch(Exception e) {
			
			System.err.println("Erro ao pesquisar resgistros de gestor: "+ e);
			
			return true;
			
		}
		
	}
	
	public boolean verificaPossibilidadeAtivacao(String nomeMaquina) throws SQLException{
		
		try(PreparedStatement pstm = conexao.prepareStatement("SELECT maquina.codigo FROM ativo "
				+ "LEFT JOIN maquina ON "
				+ "maquina.codigo = ativo.codigo_maquina "
				+ "WHERE maquina.nome = ?")){
			
			
			pstm.setString(1, nomeMaquina);
			
			ResultSet rs = pstm.executeQuery();
			
			
			if(rs.next()) {
				
				return true;
				
			}
			
			return false;
		}
		
			
		
		
	}
	
	public boolean grava(Ativo ativo, Usuario usuario) throws ParseException {
		
		if(ativo.getAr() == null || ativo.getAr().getCodigo() <= 0) {
			
			return false;
			
		}else if(ativo.getGestor() == null || ativo.getGestor().getCodigo() <= 0){
			
			return false;
			
		}else if(ativo.getMaquina() == null || ativo.getMaquina().getCodigo() <= 0) {
			
			return false;
			
		}else if(ativo.getDataVinculacao().equals(null) || ativo.getDataVinculacao() == "") {
			
			return false;
			
		}
		
		try (PreparedStatement insert = conexao.prepareStatement("INSERT INTO ativo (codigo_ar, codigo_gestor, codigo_maquina, data_vinculacao) VALUES (?,?,?,?)")){
			
			insert.setInt(1, ativo.getAr().getCodigo());
			insert.setInt(2, ativo.getGestor().getCodigo());
			insert.setInt(3, ativo.getMaquina().getCodigo());
			insert.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd.MM.yyyy").parse(ativo.getDataVinculacao()).getTime()));
			
			insert.execute();
			AuditoriaDAO.gravaAuditoria(new Auditoria(tabela,
					"GRAVOU DADOS",
					"ATIVO: " + ativo.getCodigo(),
					usuario),
				conexao);
			
			
			return true;
			
			
		}catch(Exception sqle) {
			
			System.err.println("Erro SQL: " + sqle);
			
			return false;
			
		}
		
	}
	
	public boolean deleta(Ativo ativo, Usuario usuario) {
		
		
		try (PreparedStatement pstm = conexao.prepareStatement("DELETE FROM ativo WHERE codigo = ?")){
			
			pstm.setInt(1, ativo.getCodigo());
			
			pstm.execute();
			AuditoriaDAO.gravaAuditoria(new Auditoria(tabela,
					"DELETOU DADOS",
					"ATIVO: " + ativo.getCodigo(),
					usuario),
				conexao);

			return true;
			
		}catch(Exception sqle) {
			
			System.err.println("ERRO SQL: " + sqle);
			
			return false;
			
		}
		
	}
	
	public List<Ativo> listaAtivos(){
	
		List<Ativo> ativos = new ArrayList<Ativo>();
		
		try(PreparedStatement pstm = conexao.prepareStatement("SELECT ativo.codigo AS ac, ar.codigo AS arc, ar.nome AS arn,"
				+ "gestor.codigo AS gc, gestor.nome gn,"
				+ "maquina.codigo AS mc, maquina.nome AS mn,"
				+ "ativo.data_vinculacao  as adv "
				+ "FROM ativo "
				+ "LEFT JOIN ar ON ar.codigo = ativo.codigo_ar "
				+ "LEFT JOIN gestor ON gestor.codigo = ativo.codigo_gestor "
				+ "LEFT JOIN maquina ON maquina.codigo = ativo.codigo_maquina "
				+ "ORDER BY(maquina.nome)")) {
			
			ativos = mapaResultados(pstm.executeQuery());
			return ativos;
			
		}catch(Exception e) {
			
			System.err.println("Erro de SQL: " + e);
			
			return null;
			
		}
		
	}
	
	public List<Ativo> listaAtivosFiltradoDataAtivacao(String data1){
		
		List<Ativo> ativos = new ArrayList<>(); 

		try(PreparedStatement pstm = conexao.prepareStatement("SELECT ativo.codigo AS ac, ar.codigo AS arc, ar.nome AS arn,"
				+ "gestor.codigo AS gc, gestor.nome gn,"
				+ "maquina.codigo AS mc, maquina.nome AS mn,"
				+ "ativo.data_vinculacao  as adv "
				+ "FROM ativo "
				+ "LEFT JOIN ar ON ar.codigo = ativo.codigo_ar "
				+ "LEFT JOIN gestor ON gestor.codigo = ativo.codigo_gestor "
				+ "LEFT JOIN maquina ON maquina.codigo = ativo.codigo_maquina "
				+ "WHERE maquina.data_ativacao <= ? "
				+ "ORDER BY(maquina.nome)")){
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");			
			Date datay = sdf.parse(data1);			
			Timestamp dataFinal = new Timestamp(datay.getTime());
			
			pstm.setTimestamp(1, dataFinal);
			
			
			
			for(Ativo a : mapaResultados(pstm.executeQuery())) {
				
				try {
					
					try(ArDAO aDAO = new ArDAO()){
					
						a.setAr(aDAO.buscaAr(a.getAr().getCodigo()));
						
					}
					
					try(GestorDAO gDAO = new GestorDAO()){
					
						a.setGestor(gDAO.buscaGestor(a.getGestor().getCodigo()));
						
					}
					
					try(MaquinaDAO mDAO = new MaquinaDAO()){
					
						a.setMaquina(mDAO.carregaMaquina(a.getMaquina().getCodigo()));
						
					}
					
				}catch(Exception e) {
					
				}
				
				ativos.add(a);
			}

			return ativos;
			
		}catch(SQLException | ParseException e) {
			
			System.err.println("Erro de SQL: " + e);
			
			return null;
			
		}
		
	}
		
	public List<String> listaPendentes(){
		
		List<String> pendentes = new ArrayList<>();
		
		
		
		try(PreparedStatement pstm = conexao.prepareStatement("SELECT maquina.nome AS nm FROM ativo LEFT JOIN maquina ON maquina.codigo = ativo.codigo_maquina")) {
			
			
			ResultSet rs = pstm.executeQuery();
			
			while(rs.next()) {
				
				pendentes.add(rs.getString("nm"));
				
			}

			return pendentes;
			
		}catch(SQLException e) {
			
			System.err.println("Erro de SQL: " + e);
			
			return null;
			
		}
		
	}
	
	public List<String> listaInventarioProgramas(String data1){
		
		List<String> maquinas = new ArrayList<>();
		
		try(PreparedStatement pstm = conexao.prepareStatement("SELECT maquina.nome AS nm FROM ativo"
				+ " LEFT JOIN maquina ON maquina.codigo = ativo.codigo_maquina"
				+ " WHERE maquina.data_ativacao <= ?")) {
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");			
			Date datay = sdf.parse(data1);			
			Timestamp dataFinal = new Timestamp(datay.getTime());
			
			pstm.setTimestamp(1, dataFinal);
			
			ResultSet rs = pstm.executeQuery();
			
			while(rs.next()) {
				
				maquinas.add(rs.getString("nm"));
				
			}
			
			return maquinas;
			
		}catch(SQLException | ParseException e) {
			
			System.err.println("Erro de SQL: " + e);
			
			return null;
			
		}
		
	}
	
	public List<Ativo> listaCarregaCompleta(){

		List<Ativo> ativosCarregados = new ArrayList<>();
		
		for(Ativo a : listaAtivos()) {
			
			try {
				
				try(ArDAO aDAO = new ArDAO()){
				
					a.setAr(aDAO.buscaAr(a.getAr().getCodigo()));
					
				}
				
				try(GestorDAO gDAO = new GestorDAO()){
				
					a.setGestor(gDAO.buscaGestor(a.getGestor().getCodigo()));
					
				}
				
				try(MaquinaDAO mDAO = new MaquinaDAO()){
				
					a.setMaquina(mDAO.carregaMaquina(a.getMaquina().getCodigo()));
					
				}
				
			}catch(Exception e) {
				
			}
			ativosCarregados.add(a);
		}
		return ativosCarregados;
		
	}

	public boolean alterarMAquinaPAtiva(int codigo, Usuario usuario) {

		try(PreparedStatement pstm = conexao.prepareStatement("UPDATE maquina SET situacao = 'ATIVA' WHERE codigo = ?")) {
			
			
			pstm.setInt(1, codigo);
			
			pstm.execute();
			
			AuditoriaDAO.gravaAuditoria(new Auditoria(tabela,
					"ALTEROU DADOS MAQUINA PARA ATIVA",
					"MAQUINA: " + codigo,
					usuario),
				conexao);
			
			return true;
			
		}catch(Exception sqle) {
			
			System.err.println("Erro ao tenta fazer alterações no banco: " + sqle);
			
			return false;
		}
		
	}
	
	public boolean verificaMaquinaAtiva(int codigo) throws SQLException{
		
		String sql = "SELECT codigo_maquina FROM ativo "
				+ "WHERE codigo_maquina = ?";
			
			PreparedStatement pstm = conexao.prepareStatement(sql);
			
			pstm.setInt(1, codigo);
			
			ResultSet rs = pstm.executeQuery();
			
			if(rs.next()) {

				return true;
				
			}

			return false;
			
		
	}

	private List<Ativo> mapaResultados(ResultSet rs) throws SQLException{
		
		List<Ativo> ativos = new ArrayList<>();
		
		while(rs.next()) {
			
			Ativo ativo = new Ativo();
			
			ativo.setCodigo(rs.getInt("ac"));
			try(ArDAO arDAO = new ArDAO();
					GestorDAO gestorDAO = new GestorDAO();
					MaquinaDAO maquinaDAO = new MaquinaDAO();){
				ativo.setAr(arDAO.buscaAr(rs.getInt("arc")));
				ativo.setGestor(gestorDAO.buscaGestor(rs.getInt("gc")));
				ativo.setMaquina(maquinaDAO.carregaMaquina(rs.getInt("mc")));
				ativo.setDataVinculacao(rs.getString("adv"));
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
			ativos.add(ativo);
			
		}
		
		return ativos;

	}
	
	public Ativo mapaResultado(ResultSet rs) throws Exception{
		
		Ativo ativo = new Ativo();
		
		if(rs.next()) {
			
			ativo.setCodigo(rs.getInt("ac"));
			try(ArDAO arDAO = new ArDAO();
					GestorDAO gestorDAO = new GestorDAO();
					MaquinaDAO maquinaDAO = new MaquinaDAO();){
				ativo.setAr(arDAO.buscaAr(rs.getInt("arc")));
				ativo.setGestor(gestorDAO.buscaGestor(rs.getInt("gc")));
				ativo.setMaquina(maquinaDAO.carregaMaquina(rs.getInt("mc")));
				ativo.setDataVinculacao(rs.getString("adv"));
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
		}
		
		return ativo;
		
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
