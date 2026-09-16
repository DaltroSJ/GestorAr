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
@Table(name = "AGENTE")
public class Agente {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int codigo;
	
	private String nome;
	
	private String cpf;
	
	private String situacao;
	@Column(name = "data_habilitacao")
	private String dataHabilitacao;
	
	private String email;
	@ManyToOne
	@JoinColumn(name = "codigo_ponto_atendimento")
	private PontoAtendimento pontoAtendimento;

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public PontoAtendimento getPontoAtendimento() {
		return pontoAtendimento;
	}

	public void setPontoAtendimento(PontoAtendimento pontoAtendimento) {
		this.pontoAtendimento = pontoAtendimento;
	}

	
	
	public String getDataHabilitacao() {
		return dataHabilitacao;
	}

	public void setDataHabilitacao(String dataHabilitacao) {
		this.dataHabilitacao = dataHabilitacao;
	}
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Agente(int codigo) {
		super();
		this.codigo = codigo;
	}
	
	public Agente(int codigo, String nome, String dataHabilitacao, String cpf, String situacao,  String email,   PontoAtendimento pontoAtendimento) {
		super();
		this.codigo = codigo;
		this.nome = nome;
		this.cpf = cpf;
		this.situacao = situacao;
		this.dataHabilitacao = dataHabilitacao;
		this.pontoAtendimento = pontoAtendimento;
		this.email = email;
	}
	
	public Agente(int codigo, String nome, String cpf, PontoAtendimento pontoAtendimento) {
		super();
		this.codigo = codigo;
		this.nome = nome;
		this.cpf = cpf;
		this.pontoAtendimento = pontoAtendimento;
	}
	
	public Agente() {
		
	}
	
	@Override
	public String toString() {
	    return "Agente{" +
	            "codigo=" + codigo +
	            ", nome='" + nome + '\'' +
	            ", cpf='" + cpf + '\'' +
	            ", situacao='" + situacao + '\'' +
	            ", dataHabilitacao='" + dataHabilitacao + '\'' +
	            ", email='" + email + '\'' +
	            ", pontoAtendimento=" + pontoAtendimento +
	            '}';
	}

	
}
