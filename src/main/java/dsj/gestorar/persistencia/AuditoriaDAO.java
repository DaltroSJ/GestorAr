package dsj.gestorar.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dsj.gestorar.modelo.Auditoria;

public class AuditoriaDAO implements AutoCloseable{

	private final Connection conexao;
	
	public AuditoriaDAO() throws SQLException{
		
		this.conexao = GerenciarConexoes.getConexao();
		
	}
	
	public static void gravaAuditoria(Auditoria auditoria, Connection conexao) throws Exception{
		
		try(PreparedStatement pstm = conexao.prepareStatement("INSERT INTO auditoria (tabela, acao, chave_primaria, codigo_usuario) VALUES (?,?,?,?)")){
			
			pstm.setString(1, auditoria.getTabela());
			pstm.setString(2, auditoria.getAcao());
			pstm.setString(3, auditoria.getChavePrimaria());
			if(auditoria.getUsuario() == null) {
				pstm.setNull(4,  0);
			}else {
				pstm.setInt(4, auditoria.getUsuario().getCodigo());
			}
			
			
			pstm.execute();
			
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
