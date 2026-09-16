package dsj.gestorar.utilitarios;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.swing.JOptionPane;

public class Configuracoes {

	public static String tipoAmbiente;
	public static String tipoBanco;
	public static String lembra;
	public static String usuario;
	private static final String userhome = System.getProperty("user.home");
	private static final String caminhoPastaPrincipal = userhome + File.separator + "gestorar" + File.separator + "config";
    private static final Path arquivoConfiguracao = Paths.get(caminhoPastaPrincipal + File.separator + "config.properties");


    public static Properties carregarConfiguracoes() {
    	
        Properties props = new Properties();

        try (InputStream input = Files.newInputStream(arquivoConfiguracao.toFile().toPath())) {
            props.load(input);
            
        } catch (IOException e) {
           
        	JOptionPane.showMessageDialog(null, "Erro ao ler propriedades", "Erro", JOptionPane.ERROR_MESSAGE);
        	
        }

        return props;
    }
	
    public static void atualizarPropriedade(String chave, String valor) {
    	
        Properties props = carregarConfiguracoes();
        props.setProperty(chave, valor);

        try (OutputStream output = Files.newOutputStream(arquivoConfiguracao.toFile().toPath())) {
        	
            props.store(output, "Configurações do Sistema Atualizadas");
            
        } catch (IOException e) {
        	
        	JOptionPane.showMessageDialog(null, "Erro ao alterar propriedades", "Erro", JOptionPane.ERROR_MESSAGE);
            
        }
    }
	
	public static void salvarPropriedades() {

		Properties props = new Properties();

		props.setProperty("ambiente", tipoAmbiente);
		props.setProperty("banco", tipoBanco);
		props.setProperty("lembra", lembra);
		props.setProperty("usuario", usuario);
	}

	
	public Configuracoes() {
		
	
	    
	}
	
	public static void carregarPropriedades() {
		
		Properties propriedades = carregarConfiguracoes();

	    if (propriedades == null) {
	       tipoAmbiente = "";
	       tipoBanco= "";
	       lembra = "NAO";
	       usuario = "";
	       return;
	    }

	    String ambiente = propriedades.getProperty("ambiente");
	    if (ambiente == null) {
	       tipoAmbiente = "";
	    } else {
	       tipoAmbiente= ambiente;
	    }

	    String banco = propriedades.getProperty("banco");
	    if (banco == null) {
	       tipoBanco= "";
	    } else {
	       tipoBanco= banco;
	    }
	    
	    String lembra = propriedades.getProperty("lembra");
	    if(lembra == null) {
	    	lembra = "NAO";
	    }else {
	    	Configuracoes.lembra = lembra;
	    }
	    
	    String usuario = propriedades.getProperty("usuario");
	    if(usuario == null) {
	    	usuario = "NAO";
	    }else {
	    	Configuracoes.usuario = usuario;
	    }
	    
		
	}
	
	public static void criarArquivoPadrao() {
	    try {
	        
	        if (!Files.exists(arquivoConfiguracao.getParent())) {
	            Files.createDirectories(arquivoConfiguracao.getParent());
	        }

	        
	        if (!Files.exists(arquivoConfiguracao)) {
	        	
	            Properties props = new Properties();
	            props.setProperty("ambiente", "PRODUCAO");
	            props.setProperty("banco", "H2");
	            props.setProperty("lembra", "NAO");
	            props.setProperty("usuario", "NAO");

	            try (OutputStream output = Files.newOutputStream(arquivoConfiguracao)) {
	                props.store(output, "Configurações Padrão do Sistema");
	            }
	            
	        }

	    } catch (IOException e) {
	    	
	        JOptionPane.showMessageDialog(null,
	                "Erro ao criar arquivo de configuração: " + e.getMessage(),
	                "Erro",
	                JOptionPane.ERROR_MESSAGE);
	    }
	}

	
	
}
