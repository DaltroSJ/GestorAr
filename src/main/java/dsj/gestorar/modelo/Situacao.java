package dsj.gestorar.modelo;

public enum Situacao {

	Ativo(1,"Cadastro ativo"),
	Desativado(2, "Cadastro desativado");
	
	public int codigo;
	public String descricao;
	
	Situacao(int codigo, String descricao){
		
		this.codigo = codigo;
		this.descricao = descricao;
		
	}
	
}
