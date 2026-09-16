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
@Table(name = "AUDITORIA")
public class Auditoria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int codigo;
	private String tabela;
	private String acao;
	@Column(name = "chave_primaria")
	private String chavePrimaria;
	@ManyToOne
	@JoinColumn(name = "codigo_usuario", nullable = true)
	private Usuario usuario;
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getTabela() {
		return tabela;
	}
	public void setTabela(String tabela) {
		this.tabela = tabela;
	}
	public String getAcao() {
		return acao;
	}
	public void setAcao(String acao) {
		this.acao = acao;
	}
	public String getChavePrimaria() {
		return chavePrimaria;
	}
	public void setChavePrimaria(String chavePrimaria) {
		this.chavePrimaria = chavePrimaria;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Auditoria (String tabela, String acao, String chavePrimaria, Usuario usuario) {

		this.tabela = tabela;
		this.acao = acao;
		this.chavePrimaria = chavePrimaria;
		this.usuario = usuario;
	}
	public Auditoria() {
		super();
	}
	
	
	
}
