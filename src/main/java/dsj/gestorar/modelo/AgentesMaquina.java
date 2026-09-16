package dsj.gestorar.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "AGENTES_MAQUINA")
public class AgentesMaquina {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int codigo;
	@ManyToOne
	@JoinColumn(name = "codigo_maquina")
	private Maquina maquina;
	@ManyToOne
	@JoinColumn(name = "codigo_agente")
	private Agente agente;
	@ManyToOne
	@JoinColumn(name = "codigo_ponto_atendimento")
	private PontoAtendimento pontoAtendimento;
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public Maquina getMaquina() {
		return maquina;
	}
	public void setMaquina(Maquina maquina) {
		this.maquina = maquina;
	}
	public Agente getAgente() {
		return agente;
	}
	public void setAgente(Agente agente) {
		this.agente = agente;
	}
	public AgentesMaquina(int codigo, Maquina maquina, Agente agente) {
		super();
		this.codigo = codigo;
		this.maquina = maquina;
		this.agente = agente;
	}
	
	public AgentesMaquina() {
		
	}
	
}
