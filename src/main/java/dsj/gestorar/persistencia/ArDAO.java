package dsj.gestorar.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dsj.gestorar.modelo.Ar;
import dsj.gestorar.modelo.Auditoria;
import dsj.gestorar.modelo.Usuario;

public class ArDAO implements AutoCloseable{

	private final String tabela = "AR";
	
	private final Connection conexao;
	public ArDAO() throws SQLException{
		
		this.conexao = GerenciarConexoes.getConexao();
		
	}
	
	public boolean gravaDados(Ar ar, Usuario usuario) {
		
	    if (!verificaCampos(ar)) {
	        return false;
	    }
	    
	    try (PreparedStatement pstm = (ar.getCodigo() <= 0)
	            ? conexao.prepareStatement("INSERT INTO ar (nome, endereco, telefone, vinculacao) VALUES (?,?,?,?)")
	            : conexao.prepareStatement("UPDATE ar SET nome = ?, endereco = ?, telefone = ?, vinculacao = ? WHERE codigo = ?")) {

	        pstm.setString(1, ar.getNome());
	        pstm.setString(2, ar.getEndereco());
	        pstm.setString(3, ar.getTelefone());
	        pstm.setString(4, ar.getVinculacao());

	        if (ar.getCodigo() > 0) {
	            pstm.setInt(5, ar.getCodigo());
	        }

	        pstm.execute();

	        AuditoriaDAO.gravaAuditoria(new Auditoria(tabela,
					"GRAVOU DADOS",
					ar.getCodigo() == 0 ? "Nova AR cadastrada" : "Ar: "+ ar.getCodigo(),
					usuario.getCodigo() == 0 ? null : usuario ),
				conexao);
	        
	        return true;

	    } catch (Exception e) {
	        System.err.println("Erro de SQL: " + e);
	        return false;
	    }
	}

	public ArrayList<Ar> listaGestores(){
		
		String sql = "SELECT * FROM ar";
		
		try (PreparedStatement consulta = conexao.prepareStatement(sql)){
			return mapaResultados(consulta.executeQuery());
			
		}catch(SQLException sqle) {
			
			System.err.println("Erro de SQL: " + sqle);
			
			return null;
			
		}
		
	}
	
	public List<Ar> listaArs(){
		
		try(PreparedStatement pstm = conexao.prepareStatement("SELECT * FROM ar")){
			
			return mapaResultados(pstm.executeQuery());
			
			
		}catch(Exception e) {
			
			System.err.println("Erro ao pesquisar Ars: " + e);
			return Collections.emptyList();
		}
		
	}
	
	public Ar buscaAr(int codigo){
		
		Ar ar = new Ar();
		
		try(PreparedStatement consulta = conexao.prepareStatement("SELECT * FROM ar WHERE codigo = ?")){
			
			consulta.setInt(1, codigo);
			
			ar = mapaResultado(consulta.executeQuery());
			return ar;
			
		}catch(SQLException sqle) {
			
			System.err.println("Erro de SQL: " + sqle);
			
			return null;
			
		}
		
	
		
	}
	
	public boolean verificaCampos(Ar ar) {
		
		if(ar == null) {
			
			return false;
			
		}else {
			
			if (ar.getNome() == null || ar.getNome().isEmpty()) {
				
			    return false;
			    
			} else if (ar.getEndereco() == null || ar.getEndereco().isEmpty()) {
				
			    return false;
			    
			} else if (ar.getTelefone() == null || ar.getTelefone().isEmpty()) {
				
			    return false;
			    
			} else if (ar.getVinculacao() == null || ar.getVinculacao().isEmpty()) {
				
			    return false;
			}
				
		}
		
		return true;
		
	}
	
	public Ar mapaResultado(ResultSet resultado) throws SQLException {
	    return resultado.next() ? extrairAr(resultado) : null;
	}
	
	private Ar extrairAr(ResultSet r) throws SQLException {
	    Ar ar = new Ar();
	    ar.setCodigo(r.getInt("codigo"));
	    ar.setNome(r.getString("nome"));
	    ar.setEndereco(r.getString("endereco"));
	    ar.setVinculacao(r.getString("vinculacao"));
	    ar.setTelefone(r.getString("telefone"));
	    return ar;
	}
	
	public ArrayList<Ar> mapaResultados(ResultSet resultados) throws SQLException {
	    ArrayList<Ar> ars = new ArrayList<>();
	    while (resultados.next()) {
	        ars.add(extrairAr(resultados));
	    }
	    return ars;
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
