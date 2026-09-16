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
@Table(name = "USUARIO")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int codigo;
	private String nome;
	private String sal;
	private String hash;
	private String senha;
	@Column(name = "codigo_titulo")
	private int codigoTitulo;
	@ManyToOne
	@JoinColumn(name = "codigo_ar")
	private Ar ar;

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
	public String getSal() {
		return sal;
	}
	public void setSal(String sal) {
		this.sal = sal;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public Ar getAr() {
		return ar;
	}
	public void setAr(Ar ar) {
		this.ar = ar;
	}
	
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public int getCodigoTitulo() {
		return codigoTitulo;
	}

	public void setCodigoTitulo(int codigoTItulo) {
		this.codigoTitulo = codigoTItulo;
	}
	public Usuario(Ar ar) {
		this.ar = ar;
	}
	public Usuario(int codigo, String nome, String senha, String sal, String hash, int codigoTitulo, Ar ar) {
		super();
		this.codigo = codigo;
		this.nome = nome;
		this.sal = sal;
		this.hash = hash;
		this.codigoTitulo = codigoTitulo;
		this.senha = senha;
		this.ar = (ar != null) ? ar : new Ar(0, "", "", "", "");
	}
	public Usuario() {
		super();
	}
	
	public Usuario(int codigo) {
		super();
		this.codigo = codigo;
	}
	
	
}
