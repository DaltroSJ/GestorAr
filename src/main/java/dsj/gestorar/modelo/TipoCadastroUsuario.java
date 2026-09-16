package dsj.gestorar.modelo;

public enum TipoCadastroUsuario {

	NOVO(1,"Já há usuários cadastrados no sistema, e será cadastrado um novo"),
	SEM_USUARIO(2, "Não existem usuários cadastrados no sistema, e será cadastrado um novo"),
	ALETRACAO(3, "Já há usuários cadastrados, e será alterado o cadastro do usuário que esta logado no sistema"),
	ALTERACAO_ADMINISTRADOR(4,"Usuário administrador vai alterar configurações de um usuario ja cadastrado");
	
	public int codigo;
	public String descricao;
	
	TipoCadastroUsuario(int codigo, String descricao) {
		
		this.codigo = codigo;
		this.descricao = descricao;
		
	}
	
	
}
