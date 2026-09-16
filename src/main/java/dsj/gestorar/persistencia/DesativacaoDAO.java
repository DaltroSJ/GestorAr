package dsj.gestorar.persistencia;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import dsj.gestorar.modelo.*;

public class DesativacaoDAO implements AutoCloseable {

	private final Connection conexao;

	public DesativacaoDAO() throws SQLException{
		
		this.conexao = GerenciarConexoes.getConexao();
		
	}
	
	private final String tabela = "DESATIVACAO";

    public boolean existeRegistroGestor(int codigoGestor) {
    	
    	
    	try(PreparedStatement pstm = conexao.prepareStatement("SELECT * FROM desativacao where codigo_gestor = ?")){
    		
    		pstm.setInt(1, codigoGestor);
    		
    		if(pstm.executeQuery().next()) {
    			
    			return true;
    			
    		}else {
    			
    			return false;
    			
    		}
    		
    	}catch(Exception e) {
    		
    		System.err.println("Erro ao verificar se há registros de gestor nos desativados: " + e);
    		return true;
    	}
    	
    }
    
    public boolean grava(Desativacao d, Usuario usuario) {
        String sql = "INSERT INTO desativacao (codigo_maquina, codigo_ar, codigo_gestor, data, motivo) VALUES (?,?,?,?,?)";
        try (PreparedStatement pstm = conexao.prepareStatement(sql)) {

            Timestamp data = new Timestamp(new SimpleDateFormat("dd.MM.yyyy").parse(
                    new SimpleDateFormat("dd.MM.yyyy").format(new Date())).getTime());

            pstm.setInt(1, d.getMaquina().getCodigo());
            pstm.setInt(2, d.getAr().getCodigo());
            pstm.setInt(3, d.getGestor().getCodigo());
            pstm.setTimestamp(4, data);
            pstm.setString(5, d.getMotivo());

            pstm.execute();
            
            AuditoriaDAO.gravaAuditoria(new Auditoria(tabela,
					"GRAVOU DADOS DE DESATIVAÇÃO",
					"DESATIVAÇÃO: " + d.getCodigo(),
					usuario),
				conexao);
            
            return true;

        } catch (Exception e) {
            System.err.println("Erro ao gravar desativacao: " + e);
            return false;
        }
    }

    public ArrayList<Desativacao> listaAtivosData(String data1, String data2) {
        String sql = "SELECT desativacao.codigo AS dc, ar.codigo AS arc, ar.nome AS arn, " +
                     "gestor.codigo AS gc, gestor.nome AS gn, " +
                     "maquina.codigo AS mc, maquina.nome AS mn " +
                     "FROM desativacao " +
                     "LEFT JOIN ar ON ar.codigo = desativacao.codigo_ar " +
                     "LEFT JOIN gestor ON gestor.codigo = desativacao.codigo_gestor " +
                     "LEFT JOIN maquina ON maquina.codigo = desativacao.codigo_maquina " +
                     "WHERE maquina.data_ativacao >= ? AND desativacao.data <= ? " +
                     "ORDER BY(maquina.nome)";

        try (PreparedStatement pstm = conexao.prepareStatement(sql)) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            pstm.setTimestamp(1, new Timestamp(sdf.parse(data1).getTime()));
            pstm.setTimestamp(2, new Timestamp(sdf.parse(data2).getTime()));

            ArrayList<Desativacao> ativosCarregados = new ArrayList<>();
            for (Desativacao d : mapaResultados(pstm.executeQuery())) {
                try (ArDAO aDAO = new ArDAO()) {
                    d.setAr(aDAO.buscaAr(d.getAr().getCodigo()));
                }
                try (GestorDAO gDAO = new GestorDAO()) {
                    d.setGestor(gDAO.buscaGestor(d.getGestor().getCodigo()));
                }
                try (MaquinaDAO mDAO = new MaquinaDAO()) {
                    d.setMaquina(mDAO.carregaMaquina(d.getMaquina().getCodigo()));
                }
                ativosCarregados.add(d);
            }
            return ativosCarregados;

        } catch (Exception e) {
            System.err.println("Erro de SQL ou Parse: " + e);
            return null;
        }
    }

    public List<Programas> listaAtivosInventarioProgramas(String data1, String data2) {
    	
        List<Programas> p = new ArrayList<>();
        List<Integer> codigos = new ArrayList<>();

        String selectCodigos = "SELECT maquina.codigo AS mc FROM desativacao " +
                                "LEFT JOIN maquina ON maquina.codigo = desativacao.codigo_maquina " +
                                "WHERE maquina.data_ativacao >= ? AND desativacao.data <= ? ORDER BY(maquina.nome)";

        try (PreparedStatement pstm1 = conexao.prepareStatement(selectCodigos)) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            pstm1.setTimestamp(1, new Timestamp(sdf.parse(data1).getTime()));
            pstm1.setTimestamp(2, new Timestamp(sdf.parse(data2).getTime()));

            try (ResultSet rs = pstm1.executeQuery()) {
                while (rs.next()) {
                    codigos.add(rs.getInt("mc"));
                }
            }

            if (codigos.isEmpty()) return null;

            String placeholders = String.join(",", Collections.nCopies(codigos.size(), "?"));
            String sqlProgramas = "SELECT * FROM programas WHERE codigo_maquina IN (" + placeholders + ")";

            try (PreparedStatement pstm2 = conexao.prepareStatement(sqlProgramas)) {
                for (int i = 0; i < codigos.size(); i++) {
                    pstm2.setInt(i + 1, codigos.get(i));
                }

                try (ResultSet rs = pstm2.executeQuery()) {
                	
                    while (rs.next()) {
                    	
                        Programas prog = new Programas();
                        
                        prog.setCodigo(rs.getInt("codigo"));
                        prog.setDriverCamera(rs.getString("driver_camera"));
                        prog.setDriverLeitorBiometrico(rs.getString("driver_leitor_biometrico"));
                        prog.setEdge(rs.getString("edge"));
                        prog.setEmissao(rs.getString("emissao"));
                        prog.setJava(rs.getString("java"));
                        prog.setPdf(rs.getString("pdf"));
                        prog.setSafesing(rs.getString("safesign"));
                        prog.setMaquina(new Maquina(rs.getInt("codigo_maquina")));
                        
                        try(MaquinaDAO mDAO = new MaquinaDAO()){
                        	
                        	prog.setMaquina(mDAO.carregaMaquina(prog.getMaquina().getCodigo()));
                        	
                        }
                        
                        p.add(prog);
                    }
                    
                }catch(Exception e ) {
                	System.err.println(e);
                }
            }

            return p;

        } catch (SQLException | ParseException e) {
            System.err.println("Erro de SQL: " + e);
            return null;
        }
    }

    public boolean aletraSituacaoMaquina(int codigo, Usuario usuario) {
        String sql = "UPDATE maquina SET situacao = 'DESATIVADA' WHERE codigo = ?";
        try (PreparedStatement pstm = conexao.prepareStatement(sql)) {
            pstm.setInt(1, codigo);
            pstm.execute();
            AuditoriaDAO.gravaAuditoria(new Auditoria(tabela,
					"ALTEROU MAQUINA PARA DESATIVADA",
					"DESATIVAÇÃO: " + codigo,
					usuario),
				conexao);
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao alterar situacao da maquina: " + e);
            return false;
        }
    }

    public List<Desativacao> mapaResultados(ResultSet rs) throws Exception {
        List<Desativacao> ativosData = new ArrayList<>();
        while (rs.next()) {
            Desativacao d = new Desativacao();
            d.setCodigo(rs.getInt("dc"));
            d.setAr(new Ar(rs.getInt("arc"), rs.getString("arn"), "", "", ""));
            d.setGestor(new Gestor(rs.getInt("gc"), rs.getString("gn"), "","",""));
            d.setMaquina(new Maquina(rs.getInt("mc"), rs.getString("mn"), "", "", ""));
            ativosData.add(d);
        }
        return ativosData;
    }

    public List<Desativacao> mapaResultadosCompleto(ResultSet rs) throws Exception{
    	
    	List<Desativacao> ativosData = new ArrayList<>();
    	
        while (rs.next()) {
        	
            Desativacao d = new Desativacao();
            d.setCodigo(rs.getInt("codigo"));
            d.setDataDesativacao(rs.getTimestamp("data").toString());
            d.setMotivo(rs.getString("motivo"));
            
            try(ArDAO arDAO = new ArDAO()){
            	
            	d.setAr(arDAO.buscaAr(rs.getInt("codigo_ar")));
            	
            }
            
            try(GestorDAO gDAO = new GestorDAO()){
            	
            	d.setGestor(gDAO.buscaGestor(new Gestor(rs.getInt("codigo_gestor")).getCodigo()));
            	
            }
            
            try(MaquinaDAO mDAO = new MaquinaDAO()){
            	
            	
            	d.setMaquina(mDAO.carregaMaquina(rs.getInt("codigo_maquina")));
            	
            }
            
            ativosData.add(d);
        }
        return ativosData;
    	
    }
    
    public List<Desativacao> listaDesativacoes(){
    	
    	List<Desativacao> desativadas = new ArrayList<>();
    	
    	try(PreparedStatement pstm = conexao.prepareStatement("SELECT * FROM desativacao order by (data)")){
    		
    		
    		desativadas = mapaResultadosCompleto(pstm.executeQuery());
    		return desativadas;
    		
    	}catch(Exception e) {
    		
    		System.err.println("Erro ao buscas desativações: " + e);
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