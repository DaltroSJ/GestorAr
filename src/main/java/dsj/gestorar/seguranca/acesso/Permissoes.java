package dsj.gestorar.seguranca.acesso;

public class Permissoes {

	public static final PoliticasAdministrador ADMIN = new PoliticasAdministrador();
	public static final PoliticasOperador OPERADOR = new PoliticasOperador();
	public static final PoliticasTecnico TECNICO = new PoliticasTecnico();
	public static final PoliticasObservador OBSERVADOR = new PoliticasObservador();
	
	public static class PoliticasAdministrador implements PoliticasPermissao{

		@Override
		public boolean podeAdicionarGestor() {
			
			return true;
		}
		
		@Override
		public boolean podeManipularAgentesMaquina() {
		
			return true;
		}

		@Override
		public boolean podeAdicionarAgente() {
			
			return true;
		}

		@Override
		public boolean podeManipularAgente() {
			
			return true;
		}

		@Override
		public boolean podeGravarAgente() {
			
			return true;
		}

		@Override
		public boolean podeManipularAR() {
			
			return true;
		}

		@Override
		public boolean podeGravarAR() {
			
			return true;
		}

		@Override
		public boolean podeAprovarAtivo() {
			
			return true;
		}

		@Override
		public boolean podeReprovarAtivo() {
			
			return true;
		}

		@Override
		public boolean podeDesativarAtivo() {
			
			return true;
		}

		@Override
		public boolean podeManipularGestor() {
			
			return true;
		}

		@Override
		public boolean podeGravarGestor() {
			
			return true;
		}

		@Override
		public boolean podeExcluirGestor() {
			
			return true;
		}

		@Override
		public boolean podeAdicionarMaquina() {
			
			return true;
		}

		@Override
		public boolean podeManipularMaquina() {
			
			return true;
		}

		@Override
		public boolean podeGravarMaquina() {
			
			return true;
		}

		@Override
		public boolean podeExcluirMaquina() {
			
			return true;
		}

		@Override
		public boolean podeAdicionarPontoAtendimento() {
			
			return true;
		}

		@Override
		public boolean podeManipularPontoAtendimento() {
			
			return true;
		}

		@Override
		public boolean podeGravarPontoAtendimento() {
			
			return true;
		}

		@Override
		public boolean podeManipularProgramas() {
			
			return true;
		}

		@Override
		public boolean podeGravarProgramas() {
			
			return true;
		}

		@Override
		public boolean podeManipularOutrosUsuarios() {
			
			return true;
		}

		@Override
		public boolean podeManipularSeuUsuario() {
			
			return true;
		}

		@Override
		public boolean podeAdicionarHostname() {
			
			return true;
		}

		@Override
		public boolean podeManipularHostnames() {
			
			return true;
		}

		@Override
		public boolean podeDesativarGestor() {
			
			return true;
		}
		
		
		
	}
	
	public static class PoliticasOperador implements PoliticasPermissao{
		
		@Override
		public boolean podeAdicionarGestor() {
			
			return true;
		}
		
		@Override
		public boolean podeManipularAgentesMaquina() {
		
			return false;
		}

		@Override
		public boolean podeAdicionarAgente() {
			
			return true;
		}

		@Override
		public boolean podeManipularAgente() {
			
			return true;
		}

		@Override
		public boolean podeGravarAgente() {
			
			return true;
		}

		@Override
		public boolean podeManipularAR() {
			
			return false;
		}

		@Override
		public boolean podeGravarAR() {
			
			return false;
		}

		@Override
		public boolean podeAprovarAtivo() {
			
			return false;
		}

		@Override
		public boolean podeReprovarAtivo() {
			
			return false;
		}

		@Override
		public boolean podeDesativarAtivo() {
			
			return false;
		}

		@Override
		public boolean podeManipularGestor() {
			
			return true;
		}

		@Override
		public boolean podeGravarGestor() {
			
			return true;
		}

		@Override
		public boolean podeExcluirGestor() {
			
			return true;
		}

		@Override
		public boolean podeAdicionarMaquina() {
			
			return false;
		}

		@Override
		public boolean podeManipularMaquina() {
			
			return false;
		}

		@Override
		public boolean podeGravarMaquina() {
			
			return false;
		}

		@Override
		public boolean podeExcluirMaquina() {
			
			return false;
		}

		@Override
		public boolean podeAdicionarPontoAtendimento() {
			
			return true;
		}

		@Override
		public boolean podeManipularPontoAtendimento() {
			
			return true;
		}

		@Override
		public boolean podeGravarPontoAtendimento() {
			
			return true;
		}

		@Override
		public boolean podeManipularProgramas() {
			
			return false;
		}

		@Override
		public boolean podeGravarProgramas() {
			
			return false;
		}

		@Override
		public boolean podeManipularOutrosUsuarios() {
			
			return false;
		}

		@Override
		public boolean podeManipularSeuUsuario() {
			
			return true;
		}

		@Override
		public boolean podeAdicionarHostname() {
			
			return false;
		}

		@Override
		public boolean podeManipularHostnames() {
			
			return false;
		}
		
		@Override
		public boolean podeDesativarGestor() {
			
			return true;
		}
		
	}
	
	public static class PoliticasTecnico implements PoliticasPermissao{
		
		@Override
		public boolean podeAdicionarGestor() {
			
			return false;
		}
		
		@Override
		public boolean podeManipularAgentesMaquina() {
		
			return true;
		}

		@Override
		public boolean podeAdicionarAgente() {
			
			return true;
		}

		@Override
		public boolean podeManipularAgente() {
			
			return true;
		}

		@Override
		public boolean podeGravarAgente() {
			
			return true;
		}

		@Override
		public boolean podeManipularAR() {
			
			return false;
		}

		@Override
		public boolean podeGravarAR() {
			
			return false;
		}

		@Override
		public boolean podeAprovarAtivo() {
			
			return true;
		}

		@Override
		public boolean podeReprovarAtivo() {
			
			return true;
		}

		@Override
		public boolean podeDesativarAtivo() {
			
			return true;
		}

		@Override
		public boolean podeManipularGestor() {
			
			return false;
		}

		@Override
		public boolean podeGravarGestor() {
			
			return false;
		}

		@Override
		public boolean podeExcluirGestor() {
			
			return false;
		}

		@Override
		public boolean podeAdicionarMaquina() {
			
			return true;
		}

		@Override
		public boolean podeManipularMaquina() {
			
			return true;
		}

		@Override
		public boolean podeGravarMaquina() {
			
			return true;
		}

		@Override
		public boolean podeExcluirMaquina() {
			
			return true;
		}

		@Override
		public boolean podeAdicionarPontoAtendimento() {
			
			return true;
		}

		@Override
		public boolean podeManipularPontoAtendimento() {
			
			return true;
		}

		@Override
		public boolean podeGravarPontoAtendimento() {
			
			return true;
		}

		@Override
		public boolean podeManipularProgramas() {
			
			return true;
		}

		@Override
		public boolean podeGravarProgramas() {
			
			return true;
		}

		@Override
		public boolean podeManipularOutrosUsuarios() {
			
			return false;
		}

		@Override
		public boolean podeManipularSeuUsuario() {
			
			return true;
		}

		@Override
		public boolean podeAdicionarHostname() {
			
			return true;
		}

		@Override
		public boolean podeManipularHostnames() {
			
			return true;
		}
		
		@Override
		public boolean podeDesativarGestor() {
			
			return false;
		}
		
	}
	
	public static class PoliticasObservador implements PoliticasPermissao{
		
		@Override
		public boolean podeAdicionarGestor() {
			
			return false;
		}
		
		@Override
		public boolean podeDesativarGestor() {
			
			return false;
		}
		
		@Override
		public boolean podeManipularAgentesMaquina() {
		
			return false;
		}

		@Override
		public boolean podeAdicionarAgente() {
			
			return false;
		}

		@Override
		public boolean podeManipularAgente() {
			
			return false;
		}

		@Override
		public boolean podeGravarAgente() {
			
			return false;
		}

		@Override
		public boolean podeManipularAR() {
			
			return false;
		}

		@Override
		public boolean podeGravarAR() {
			
			return false;
		}

		@Override
		public boolean podeAprovarAtivo() {
			
			return false;
		}

		@Override
		public boolean podeReprovarAtivo() {
			
			return false;
		}

		@Override
		public boolean podeDesativarAtivo() {
			
			return false;
		}

		@Override
		public boolean podeManipularGestor() {
			
			return false;
		}

		@Override
		public boolean podeGravarGestor() {
			
			return false;
		}

		@Override
		public boolean podeExcluirGestor() {
			
			return false;
		}

		@Override
		public boolean podeAdicionarMaquina() {
			
			return false;
		}

		@Override
		public boolean podeManipularMaquina() {
			
			return false;
		}

		@Override
		public boolean podeGravarMaquina() {
			
			return false;
		}

		@Override
		public boolean podeExcluirMaquina() {
			
			return false;
		}

		@Override
		public boolean podeAdicionarPontoAtendimento() {
			
			return false;
		}

		@Override
		public boolean podeManipularPontoAtendimento() {
			
			return false;
		}

		@Override
		public boolean podeGravarPontoAtendimento() {
			
			return false;
		}

		@Override
		public boolean podeManipularProgramas() {
			
			return false;
		}

		@Override
		public boolean podeGravarProgramas() {
			
			return false;
		}

		@Override
		public boolean podeManipularOutrosUsuarios() {
			
			return false;
		}

		@Override
		public boolean podeManipularSeuUsuario() {
			
			return false;
		}

		@Override
		public boolean podeAdicionarHostname() {
			
			return false;
		}

		@Override
		public boolean podeManipularHostnames() {
			
			return false;
		}
		
		
	}
	
}
