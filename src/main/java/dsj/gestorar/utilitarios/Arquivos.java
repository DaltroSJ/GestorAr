package dsj.gestorar.utilitarios;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Arquivos {

	
	public static void copiarArquivo(String origem, String destino) {
		
		try(InputStream is = Files.newInputStream(Paths.get(origem));
            OutputStream os = Files.newOutputStream(Paths.get(destino))) {
			
			byte[] buffer = new byte[1024];
			int tamanho;
			
			while((tamanho = is.read(buffer)) > 0) {
				
				os.write(buffer, 0, tamanho);
				
			}
			
		}catch(IOException ioe) {
			
			System.err.println("Erro ao copiar arquivo: " + ioe.getMessage());
			
		}
		
	}
	
	public static void deletarArquivo(String caminhoArquivo) {
		
			File arquivo = new File(caminhoArquivo);
			
			if(arquivo.exists()) {

				arquivo.delete();

			}

		
	}
	
}
