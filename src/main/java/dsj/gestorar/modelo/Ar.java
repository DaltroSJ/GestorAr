package dsj.gestorar.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "AR")
public class Ar {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int codigo;
	
	private String nome;
	
	private String vinculacao;
	
	private String endereco;
	
	private String telefone;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getVinculacao() {
		return vinculacao;
	}

	public void setVinculacao(String vinculacao) {
		this.vinculacao = vinculacao;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Ar() {
		
	}
	
	public Ar(int codigo) {
		
		this.codigo = codigo;
		
	}
	
	public Ar(String nome, String vinculacao) {
		
		this.nome = nome;
		this.vinculacao = vinculacao;

	}
	
	public Ar(int codigo, String nome, String vinculacao, String endereco, String telefone) {
		
		this.codigo = codigo;
		this.nome = nome;
		this.vinculacao = vinculacao;
		this.endereco = endereco;
		this.telefone = telefone;

	}
	
	@Override
	public String toString() {
		
		return "codigo: " +getCodigo()
				+ "nome: "+getNome()
				+ "vinculacao: "+getVinculacao()
				+ "endereco: "+getEndereco()
				+ "telefone: "+ getTelefone();
		
	}
	
}
