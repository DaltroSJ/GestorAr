package dsj.gestorar;

import dsj.gestorar.exportacao.InventarioProgramas;
import dsj.gestorar.modelo.Ar;
import dsj.gestorar.modelo.Usuario;

public class TesteInventarioProgramas {
	
	public static void main(String args[]) {
		
		Usuario u = new Usuario();
		Ar ar = new Ar();
		ar.setNome("AR NATHYELLE");
		ar.setVinculacao("AC VALID");
		u.setAr(ar);
		
		new InventarioProgramas(u).exportaPlanilhaInventarioAtivosNovo("/home/daltrosj/gestorar/",
				"01.01.2000",
				"31.09.2026");
	}
	
}
