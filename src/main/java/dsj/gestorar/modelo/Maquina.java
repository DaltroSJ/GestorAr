package dsj.gestorar.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "MAQUINA")
public class Maquina {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int codigo;
	
	private String os = "WINDOWS 10 PRO 64";
	
	private String nome = "NATHYELLE";
	
	private String processador;
	
	private String memoria;
	
	private String ssdhdd;
	
	private String provedor;
	
	private String grafico;
	
	private String impressora;
	
	private String biometria = "Fultronic FS-80H";
	
	private String camera = "Logitech C920 PRO";
	
	private String mae;
	@Column(name = "data_ativacao")
	private String dataAtivacao;
	
	private String mac; 
	
	private String dna;
	
	private String modelo;
	@Column(name = "criptografia")
	private String modoCriptografia;
	@ManyToOne
	@JoinColumn(name = "codigo_ponto_atendimento")
	private PontoAtendimento pa;
	
	private String situacao;
	
	private String nfe;
	@Column(name = "placa_rede")
	private String placaRede;

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getProcessador() {
		return processador;
	}

	public void setProcessador(String processador) {
		this.processador = processador;
	}

	public String getMemoria() {
		return memoria;
	}

	public void setMemoria(String memoria) {
		this.memoria = memoria;
	}

	public String getSsdhdd() {
		return ssdhdd;
	}

	public void setSsdhdd(String ssdhdd) {
		this.ssdhdd = ssdhdd;
	}
	

	public String getGrafico() {
		return grafico;
	}

	public void setGrafico(String grafico) {
		this.grafico = grafico;
	}

	public String getImpressora() {
		return impressora;
	}

	public void setImpressora(String impressora) {
		this.impressora = impressora;
	}

	public String getBiometria() {
		return biometria;
	}

	public void setBiometria(String biometria) {
		this.biometria = biometria;
	}

	public String getCamera() {
		return camera;
	}

	public void setCamera(String camera) {
		this.camera = camera;
	}

	public String getMae() {
		return mae;
	}

	public void setMae(String mae) {
		this.mae = mae;
	}
	
	
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public String getDataAtivacao() {
		return dataAtivacao;
	}

	public void setDataAtivacao(String dataAtivacao) {
		this.dataAtivacao = dataAtivacao;
	}

	
	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getDna() {
		return dna;
	}

	public void setDna(String dna) {
		this.dna = dna;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getModoCriptografia() {
		return modoCriptografia;
	}

	public void setModoCriptografia(String modoCriptografia) {
		this.modoCriptografia = modoCriptografia;
	}
	
	public PontoAtendimento getPa() {
		return pa;
	}

	public void setPa(PontoAtendimento pa) {
		this.pa = pa;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	

	public String getNfe() {
		return nfe;
	}

	public void setNfe(String nfe) {
		this.nfe = nfe;
	}
	
	public String getProvedor() {
		return provedor;
	}

	public void setProvedor(String provedor) {
		this.provedor = provedor;
	}

	public String getPlacaRede() {
		return placaRede;
	}

	public void setPlacaRede(String placaRede) {
		this.placaRede = placaRede;
	}

	public Maquina() {
		
	}

	public Maquina(int codigo, String os, String nome, String processador, String memoria, String ssdhdd, String provedor,
			String grafico, String impressora, String biometria, String camera, String mae, String dataAtivacao, String mac,
			String dna, String modelo, String modoCriptografia, PontoAtendimento pa, String situacao, String nfe, String placaRede) {
		
		super();
		this.codigo = codigo;
		this.os = os;
		this.nome = nome;
		this.processador = processador;
		this.memoria = memoria;
		this.ssdhdd = ssdhdd;
		this.provedor = provedor;
		this.grafico = grafico;
		this.impressora = impressora;
		this.biometria = biometria;
		this.camera = camera;
		this.mae = mae;
		this.dataAtivacao = dataAtivacao;
		this.mac = mac;
		this.dna = dna;
		this.modelo = modelo;
		this.modoCriptografia = modoCriptografia;
		this.pa = pa;
		this.situacao = situacao;
		this.nfe = nfe;
		this.placaRede = placaRede;
	}
	
	public Maquina(int codigo, String nome, String situacao, String nomePonto, String nomeAgentes) {
		
		this.codigo = codigo;
		this.nome = nome;
		
	}
	
	public Maquina(int codigo) {
		
		this.codigo = codigo;
		
	}
	
	@Override
	public String toString() {
	    return "Ativo{" +
	            "codigo=" + codigo +
	            ", os='" + os + '\'' +
	            ", nome='" + nome + '\'' +
	            ", processador='" + processador + '\'' +
	            ", memoria='" + memoria + '\'' +
	            ", ssdhdd='" + ssdhdd + '\'' +
	            ", provedor='" + provedor + '\'' +
	            ", grafico='" + grafico + '\'' +
	            ", impressora='" + impressora + '\'' +
	            ", biometria='" + biometria + '\'' +
	            ", camera='" + camera + '\'' +
	            ", mae='" + mae + '\'' +
	            ", dataAtivacao='" + dataAtivacao + '\'' +
	            ", mac='" + mac + '\'' +
	            ", dna='" + dna + '\'' +
	            ", modelo='" + modelo + '\'' +
	            ", modoCriptografia='" + modoCriptografia + '\'' +
	            ", pa=" + pa +
	            ", situacao='" + situacao + '\'' +
	            ", nfe='" + nfe + '\'' +
	            ", placaRede='" + placaRede + '\'' +
	            '}';
	}

	
	
}
