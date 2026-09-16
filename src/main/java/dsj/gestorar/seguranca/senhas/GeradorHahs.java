package dsj.gestorar.seguranca.senhas;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class GeradorHahs {

	
	private static final int INTERACOES = 1000;
	private static final int TAMANHO_CHAVE = 128;
	
	public static String geraSal() {
		
		SecureRandom sr = new SecureRandom();
		byte[] sal = new byte[8];
		sr.nextBytes(sal);
		return Base64.getEncoder().encodeToString(sal);
		
		
	}
	
	public static String gerarHash(char[] senha, String sal) throws Exception{
		
		char[] chars = senha;
		byte[] salBytes = Base64.getDecoder().decode(sal);
		
		PBEKeySpec pbeks = new PBEKeySpec(chars, salBytes, INTERACOES, TAMANHO_CHAVE);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		byte[] hash = skf.generateSecret(pbeks).getEncoded();
		
		return Base64.getEncoder().encodeToString(hash);
	}
	
	public static boolean verificarSenha(char[] senha, String sal, String hash) throws Exception{
		String hashNovo = gerarHash(senha, sal);
		return hashNovo.equals(hash);
		
		
	}
	
}
