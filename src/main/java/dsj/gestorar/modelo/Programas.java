package dsj.gestorar.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "PROGRAMAS")
public class Programas {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int codigo;
	@OneToOne
	@JoinColumn(name = "codigo_maquina")
	private Maquina maquina;
	
	private String emissao = "VALID-SAR";
	
	private String java = "291 32";
	
	private String edge = "138.0.3351.65";
	
	private String safesign = "3.0.124";
	
	private String pdf = "Adobe Reader";
	@Column(name = "driver_leitor_biometrico")
	private String driverLeitorBiometrico = "8.0.2307";
	@Column(name = "driver_camera")
	private String driverCamera = "2.80";

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

	public String getEmissao() {
		return emissao;
	}

	public void setEmissao(String emissao) {
		this.emissao = emissao;
	}

	public String getJava() {
		return java;
	}

	public void setJava(String java) {
		this.java = java;
	}

	public String getEdge() {
		return edge;
	}

	public void setEdge(String edge) {
		this.edge = edge;
	}

	public String getSafesing() {
		return safesign;
	}

	public void setSafesing(String safesing) {
		this.safesign = safesing;
	}

	public String getPdf() {
		return pdf;
	}

	public void setPdf(String pdf) {
		this.pdf = pdf;
	}

	public String getDriverLeitorBiometrico() {
		return driverLeitorBiometrico;
	}

	public void setDriverLeitorBiometrico(String driverLeitorBiometrico) {
		this.driverLeitorBiometrico = driverLeitorBiometrico;
	}

	public String getDriverCamera() {
		return driverCamera;
	}

	public void setDriverCamera(String driverCamera) {
		this.driverCamera = driverCamera;
	}

	public Programas(int codigo, Maquina maquina, String emissao, String java, String edge, String safesing, String pdf,
			String driverLeitorBiometrico, String driverCamera) {
		super();
		this.codigo = codigo;
		this.maquina = maquina;
		this.emissao = emissao;
		this.java = java;
		this.edge = edge;
		this.safesign = safesing;
		this.pdf = pdf;
		this.driverLeitorBiometrico = driverLeitorBiometrico;
		this.driverCamera = driverCamera;
	}
	
	
	public Programas() {
	
	}
	
	public Programas(Maquina maquina) {
		
		this.maquina = maquina;
		
	}
	
	@Override
	public String toString() {
		return "Codigo: " +getCodigo()+
		"Nome de algo" + getEdge();
	}
	
}
