package dsj.gestorar.persistencia;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class GerenciarConexoes {

	private static final String userHome = System.getProperty("user.home");
    private static final String caminhoPastaPrincipal = userHome + File.separator + "gestorar";
	private static HikariDataSource dataSource;
    protected Connection conexao;
    static {
    	
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:"+caminhoPastaPrincipal+"/gestorar");
        config.setUsername("sa");
        config.setPassword("");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(300_000);
        config.setConnectionTimeout(30_000);
        config.setLeakDetectionThreshold(10_000);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
    }


    private GerenciarConexoes() {
        
    }
    
    public static Connection getConexao() throws SQLException {
    	
        if (dataSource == null || dataSource.isClosed()) {
        	
            reinicializarPool();
            
        }
        return dataSource.getConnection();
    }
    
    private static synchronized void reinicializarPool() {
    	
        if (dataSource != null && !dataSource.isClosed()) return;

        try {
        	
        	HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:h2:"+caminhoPastaPrincipal+"/gestorar");
            config.setUsername("sa");
            config.setPassword("");
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(300_000);
            config.setConnectionTimeout(30_000);
            config.setLeakDetectionThreshold(10_000);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            
        } catch (Exception e) {
        	
           System.err.println(e);
            
        }
    }

	
}
