package dsj.gestorar.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "HOSTNAME")
public class Hostname {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int codigo;
	private String hostname;
	private boolean requisitado;
	private boolean disponivel;
	private boolean liberado;
	@ManyToOne
	@JoinColumn(name = "codigo_ar")
	private Ar ar;
	@OneToOne
	@JoinColumn(name = "codigo_maquina")
	private Maquina maquina;
	private String nomeRequisitante;
	
	
	public String getNomeRequisitante() {
		return nomeRequisitante;
	}
	public void setNomeRequisitante(String nomeRequisitante) {
		this.nomeRequisitante = nomeRequisitante;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public boolean isRequisitado() {
		return requisitado;
	}
	public void setRequisitado(boolean requisitado) {
		this.requisitado = requisitado;
	}
	public boolean isDisponivel() {
		return disponivel;
	}
	public void setDisponivel(boolean disponivel) {
		this.disponivel = disponivel;
	}
	public boolean isLiberado() {
		return liberado;
	}
	public void setLiberado(boolean liberado) {
		this.liberado = liberado;
	}
	public Ar getAr() {
		return ar;
	}
	public void setAr(Ar ar) {
		this.ar = ar;
	}
	public Maquina getMaquina() {
		return maquina;
	}
	public void setMaquina(Maquina maquina) {
		this.maquina = maquina;
	}
	public Hostname(int codigo, String hostname, boolean requisitado, boolean disponivel, boolean liberado, Ar ar,
			Maquina maquina, String nomeRequisitante) {
		super();
		this.codigo = codigo;
		this.hostname = hostname;
		this.requisitado = requisitado;
		this.disponivel = disponivel;
		this.liberado = liberado;
		this.ar = ar;
		this.maquina = maquina;
		this.nomeRequisitante = nomeRequisitante;
	}
	public Hostname(int codigo, String hostname, boolean requisitado, boolean disponivel, boolean liberado, Ar ar, String nomeRequisitante) {
		super();
		this.codigo = codigo;
		this.hostname = hostname;
		this.requisitado = requisitado;
		this.disponivel = disponivel;
		this.liberado = liberado;
		this.ar = ar;
		this.nomeRequisitante = nomeRequisitante;
	}
	
	public Hostname(int codigo) {
		
		super();
		this.codigo = codigo;
		
	}
	
	public Hostname() {
		super();
	}
	
	
	
}
