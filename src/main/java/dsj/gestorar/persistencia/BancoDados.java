package dsj.gestorar.persistencia;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BancoDados implements AutoCloseable{

	public Connection conexao;
	private static final String userHome = System.getProperty("user.home");
    private static final String caminhoPastaPrincipal = userHome + File.separator + "gestorar";
	private static String URL = "jdbc:h2:"+caminhoPastaPrincipal+"/gestorar";
	private static String USER = "sa";
	private static String PASSWORD = "";


	public boolean abreConexao() {
		
		try {
			
			Class.forName("org.h2.Driver");
			conexao = DriverManager.getConnection(URL, USER, PASSWORD);

			return true;
			
		}catch(SQLException sqle) {
			
			System.err.println("Erro SQL: " + sqle);
			
			return false;
			
		}catch(Exception cnfe) {
			
			System.err.println("Erro ao registrar o Driver: " + cnfe);
			
			return false;
			
		}
		
	}
	
	
	public boolean fechaConexao() {
		
		if (conexao != null) {
			
		    try {
		    	
		        conexao.close();
		        return true;
		        
		    } catch (SQLException e) {
		    	
		        System.err.println("Erro SQL: " + e);
		        return false;
		        
		    }
		    
		} else {
			
		    return true;
		}
		
	}


	@Override
	public void close() throws Exception {
		
		fechaConexao();
		
	}
	
}
