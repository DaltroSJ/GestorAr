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
@Table(name = "ATIVO")
public class Ativo {
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
	@Column(name = "data_vinculacao")
	private String dataVinculacao;

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

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public String getDataVinculacao() {
		return dataVinculacao;
	}

	public void setDataVinculacao(String dataVinculacao) {
		this.dataVinculacao = dataVinculacao;
	}

	public Ativo() {
		
	}
	
	public Ativo(Gestor gestor, Maquina maquina, Ar ar, String dataVinculcao) {
		
		this.gestor = gestor;
		this.ar = ar;
		this.maquina = maquina;
		this.dataVinculacao = dataVinculcao;
		
	}
	
	public Ativo(int codigo, Gestor gestor, Maquina maquina, Ar ar, String dataVinculcao) {
		
		this.codigo = codigo;
		this.gestor = gestor;
		this.ar = ar;
		this.maquina = maquina;
		this.dataVinculacao = dataVinculcao;
		
	}
	
	@Override
	public String toString() {
	
		return "AR:" + getAr()
				+ "MAQUINA:" + getMaquina()
				+ "GESTOR:" + getGestor()
				+ "CODIGO" +getCodigo()
				+ "DATA" + getDataVinculacao();
				
		
	}
}
