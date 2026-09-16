package dsj.gestorar.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "GESTOR")
public class Gestor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int codigo;
	
	private String nome;
	
	private String email;

	private String nivel;
	
	private String situacao;
	
	

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	} 
	
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Gestor() {
		
	}
	
	public Gestor(int codigo) {
		
		this.codigo = codigo;
		
	}
	
	public Gestor(String nome, String email) {
		
		this.nome = nome;
		this.email = email;
		
	}
	
	public Gestor(int codigo, String nome, String email, String nivel, String situacao) {
		
		this.codigo = codigo;
		this.nome = nome;
		this.email = email;
		this.nivel = nivel;
		this.situacao = situacao;
		
	}
	
	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	@Override
	public String toString() {
		
		return "Codigo: " + getCodigo()
				+ "\nNome: "+ getNome()
				+ "\nEmail: "+ getEmail()
				+ "\nSITUACAO: " + getSituacao()
				+ "NIVEL:" + getNivel();
		
	}
	
}
