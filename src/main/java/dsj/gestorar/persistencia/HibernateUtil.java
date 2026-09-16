package dsj.gestorar.persistencia;

import java.io.File;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {

    public static final SessionFactory sessionFactory;
    private static final String userHome = System.getProperty("user.home");
    private static final String caminhoPastaPrincipal = userHome + File.separator + "gestorar";
    
    static {
        try {
           
            File pasta = new File(caminhoPastaPrincipal);
            if (!pasta.exists()) {
                pasta.mkdirs();
            }
            
           sessionFactory = buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Erro na inicialização estática do Hibernate: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static SessionFactory buildSessionFactory() {
    	try {
            Configuration configuration = new Configuration();
            
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
            configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
            configuration.setProperty("hibernate.connection.url", "jdbc:h2:"+caminhoPastaPrincipal+"/gestorar;AUTO_SERVER=TRUE;DB_CLOSE_ON_EXIT=TRUE;DB_CLOSE_DELAY=-1");
            configuration.setProperty("hibernate.connection.username", "sa");
            configuration.setProperty("hibernate.connection.password", "");
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");
            configuration.setProperty("hibernate.show_sql", "true");

            configuration.addAnnotatedClass(dsj.gestorar.modelo.Agente.class);
            configuration.addAnnotatedClass(dsj.gestorar.modelo.AgentesMaquina.class);
            configuration.addAnnotatedClass(dsj.gestorar.modelo.Ar.class);
            configuration.addAnnotatedClass(dsj.gestorar.modelo.Ativo.class);
            configuration.addAnnotatedClass(dsj.gestorar.modelo.Auditoria.class);
            configuration.addAnnotatedClass(dsj.gestorar.modelo.Desativacao.class);
            configuration.addAnnotatedClass(dsj.gestorar.modelo.Gestor.class);
            configuration.addAnnotatedClass(dsj.gestorar.modelo.Hostname.class);
            configuration.addAnnotatedClass(dsj.gestorar.modelo.Maquina.class);
            configuration.addAnnotatedClass(dsj.gestorar.modelo.Programas.class);
            configuration.addAnnotatedClass(dsj.gestorar.modelo.Usuario.class);
            configuration.addAnnotatedClass(dsj.gestorar.modelo.PontoAtendimento.class);
            

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            return configuration.buildSessionFactory(serviceRegistry);

        } catch (Throwable ex) {
            System.err.println("Falha ao criar sessão: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}