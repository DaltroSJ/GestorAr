package dsj.gestorar.utilitarios;

import java.awt.Component;
import java.io.*;
import java.nio.file.*;
import javax.swing.JOptionPane;

import dsj.gestorar.persistencia.ArDAO;
import dsj.gestorar.persistencia.BancoDados;
import dsj.gestorar.persistencia.UsuarioDAO;

public class Verificacoes {
	
    public static final String userHome = System.getProperty("user.home");
    public static final String caminhoPastaPrincipal = userHome + File.separator + "gestorar";
    public static final String caminhoPastaTemp = caminhoPastaPrincipal + File.separator + "temp";
    public static final String caminhoPastaModelo = caminhoPastaPrincipal + File.separator + "modelos";
    public static final String caminhoPastaArquivoConfiguracao = caminhoPastaPrincipal + File.separator + "config";
    public static final Path diretorioModeloAtivos = Paths.get(caminhoPastaModelo, "modeloPlanilhaAtivo.xlsx");
    public static final Path diretorioModeloInventarioAtivos = Paths.get(caminhoPastaModelo, "modeloInventarioAtivo.xlsx");
    public static final Path diretorioModeloPlanilhaAgentes = Paths.get(caminhoPastaModelo, "modeloPlanilhaAgentes.xlsx");
    public static final Path diretorioArquivoCOnfiguracao = Paths.get(caminhoPastaArquivoConfiguracao, "config.properties");
    
    
	public static boolean verificaSeHaUsuarioCadastrado(int codigoAr) throws Exception {
		
		try(UsuarioDAO uDAO = new UsuarioDAO()){
			
			if(uDAO.verificaSeHaUsuarioCadastrado(codigoAr).isEmpty()) {
				
				return false;
				
			}
			
		}
		
		return true;
		
	}

	public static boolean verificaConexaoBancoDados() throws Exception{

		try(BancoDados bd = new BancoDados()){
			
			if(!bd.abreConexao()) {
				
				return false;
				
			}
			
		}
		
		return true;
			
		
	}
	
	public static boolean verificaSeHaCadastroAR(Component tela) throws Exception{
		
		try(ArDAO aDAO = new ArDAO()){
			
			if(aDAO.listaArs().isEmpty()) {
				
				return false;
				
			}
			
		}
		
		return true;
		
	}
	
	public static void verificacoesInicais() {

		criaDiretorioSeNaoExiste(caminhoPastaPrincipal);
		criaDiretorioSeNaoExiste(caminhoPastaTemp);
		criaDiretorioSeNaoExiste(caminhoPastaModelo);
		criaDiretorioSeNaoExiste(caminhoPastaArquivoConfiguracao);
		if (!Files.exists(diretorioModeloAtivos)) {
			copiaArquivo("/modeloPlanilhaAtivo.xlsx", diretorioModeloAtivos.toString());
		}

		if (!Files.exists(diretorioModeloInventarioAtivos)) {
			copiaArquivo("/modeloInventarioAtivos.xlsx", diretorioModeloInventarioAtivos.toString());
		}
		if (!Files.exists(diretorioModeloPlanilhaAgentes)) {
			copiaArquivo("/modeloPlanilhaAgentes.xlsx", diretorioModeloPlanilhaAgentes.toString());
		}
		if(!Files.exists(diretorioArquivoCOnfiguracao)) {
			Configuracoes.criarArquivoPadrao();
		}
		
		
		
		Configuracoes.criarArquivoPadrao();

	}

	public static void criaDiretorioSeNaoExiste(String caminho) {
		File dir = new File(caminho);
		if (!dir.exists()) {
			if (dir.mkdir()) {
				JOptionPane.showMessageDialog(null, "Diretório criado com sucesso: " + caminho, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Erro ao criar diretório: " + caminho, "Erro", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public static void copiaArquivo(String resourcePath, String destino) {
		try (InputStream is = Verificacoes.class.getResourceAsStream(resourcePath)) {
			if (is == null) {
				System.out.println("Arquivo não encontrado: " + resourcePath);
				return;
			}

			try (OutputStream os = Files.newOutputStream(Paths.get(destino))) {
				byte[] buffer = new byte[1024];
				int tamanho;
				while ((tamanho = is.read(buffer)) != -1 && tamanho > 0) {
					os.write(buffer, 0, tamanho);
				}
			}

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erro ao copiar arquivo: " + resourcePath, "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

}