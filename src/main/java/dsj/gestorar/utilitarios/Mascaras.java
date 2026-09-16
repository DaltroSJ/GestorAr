package dsj.gestorar.utilitarios;

import java.text.ParseException;

import javax.swing.text.MaskFormatter;

public class Mascaras {

	
	public static String mascaraTelefone(String telefone) {
		
		
		String regex = "(\\d{2})(\\d{4}(\\d{0}))";

        return telefone.replaceAll(regex, "($1) $2-$3");
		
	}
	
	public static String mascaraCpf(String cpf) {
		
		String regex = "(\\d{3})(\\d{3})(\\d{3})(\\d{2})";

        return cpf.replaceAll(regex, "$1.$2.$3-$4");
		
	}
	
	public static MaskFormatter mascaraDatasFormularios() {
		try {
		
			MaskFormatter mf = new MaskFormatter("##.##.####");
			
			return mf;
			
		}catch(ParseException pe) {
			
			System.out.println("Erro ao passar mascara: " + pe);
			return null;
		}
		
		
	}
	
	public static MaskFormatter mascaraTelefoneFixoFormularios() {
		try {
		
			MaskFormatter mf = new MaskFormatter("(##) ####-####");
			
			return mf;
			
		}catch(ParseException pe) {
			
			System.out.println("Erro ao passar mascara: " + pe);
			return null;
		}
		
		
	}
	
}
