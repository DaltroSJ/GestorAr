package dsj.gestorar;

import java.sql.SQLException;

import dsj.gestorar.exportacao.InventarioMaquinas;
import dsj.gestorar.persistencia.AtivoDAO;
import dsj.gestorar.persistencia.UsuarioDAO;

public class TesteInventarioMaquinas {

	public static void main(String args[]) throws SQLException, Exception {
		try(AtivoDAO aDAO = new AtivoDAO();UsuarioDAO udao=new UsuarioDAO();){
		
			new InventarioMaquinas(udao.buscarUsuario(1))
			.exportar("/home/daltrosj/gestorar/teste.pdf", "15.09.2026", "15.09.2026");
			
		}
		
		
		
	}
	
}
