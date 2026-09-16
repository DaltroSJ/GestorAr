package dsj.gestorar.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "DESATIVACAO")
public class Desativacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int codigo;
	@ManyToOne
	@JoinColumn(name = "codigo_gestor")
	private Gestor gestor;
	@OneToOne
	@JoinColumn(name = "codigo_maquina")
	private Maquina maquina;
	@ManyToOne
	@JoinColumn(name = "codigo_ar")
	private Ar ar;
	@Column(name = "data")
	private String dataDesativacao;
	
	private String motivo;

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Gestor getGestor() {
		return gestor;
	}

	public void setGestor(Gestor gestor) {
		this.gestor = gestor;
	}

	public Maquina getMaquina() {
		return maquina;
	}

	public void setMaquina(Maquina maquina) {
		this.maquina = maquina;
	}

	public Ar getAr() {
		return ar;
	}

	public void setAr(Ar ar) {
		this.ar = ar;
	}

	public String getDataDesativacao() {
		return dataDesativacao;
	}

	public void setDataDesativacao(String dataDesativacao) {
		this.dataDesativacao = dataDesativacao;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Desativacao(int codigo, Gestor gestor, Maquina maquina, Ar ar, String dataDesativacao, String motivo) {
		super();
		this.codigo = codigo;
		this.gestor = gestor;
		this.maquina = maquina;
		this.ar = ar;
		this.dataDesativacao = dataDesativacao;
		this.motivo = motivo;
	}
	
	
	public Desativacao() {
		
	}
	
	@Override
	public String toString() {
	
		return "\nAr: " + getAr().getNome()
				+"\nMaquina nome: " + getMaquina().getNome()
				+"\nMaquina data: " + getMaquina().getDataAtivacao()
				+"\nGestor: " + getGestor().getNome();
		
	}
	
}
