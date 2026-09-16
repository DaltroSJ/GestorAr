package dsj.gestorar.seguranca.acesso;

public interface PoliticasPermissao {

	//AGENTES MAQUINA
	public boolean podeManipularAgentesMaquina();//AgentesMaquina
	
	//AGENTE
	public boolean podeAdicionarAgente();//BuscaAgente
	public boolean podeManipularAgente();//FormularioAgente
	public boolean podeGravarAgente();//FormularioAgente
	
	//AR
	public boolean podeManipularAR();//FormularioAR
	public boolean podeGravarAR();//FormularioAR
	
	//ATIVOS
	public boolean podeAprovarAtivo();//FormularioAtivo
	public boolean podeReprovarAtivo();//FormularioAtivo
	public boolean podeDesativarAtivo();//FormularioAtivo
	
	//GESTORES
	public boolean podeAdicionarGestor();//BuscaGestor
	public boolean podeManipularGestor();//FormularioGestor
	public boolean podeGravarGestor();//FormularioGestor
	public boolean podeExcluirGestor();//FormularioGestor
	public boolean podeDesativarGestor();//FormularioGestor
	
	//MAQUINA
	public boolean podeAdicionarMaquina();//BuscaMaquina e FormularioMaquina
	public boolean podeManipularMaquina();//FormularioMaquina
	public boolean podeGravarMaquina();//FormularioMaquina
	public boolean podeExcluirMaquina();//FormularioMaquina
	
	//PONTO DE ATENDIMENTO
	public boolean podeAdicionarPontoAtendimento();//BuscaPontoAtendimento
	public boolean podeManipularPontoAtendimento();//FormularioPontoAtendimento
	public boolean podeGravarPontoAtendimento();//FormularioPontoAtendimento
	
	//PROGRAMAS
	public boolean podeManipularProgramas();//FormularioProgramas
	public boolean podeGravarProgramas();//FormularioProgramas
	
	//USUARIO
	public boolean podeManipularOutrosUsuarios();//DINAMICO
	public boolean podeManipularSeuUsuario();//FormularioUsuario
	
	//HOSTNAMES
	public boolean podeAdicionarHostname();//BuscaHostname
	public boolean podeManipularHostnames();//BuscaHostname
}
