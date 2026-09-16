package dsj.gestorar.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PONTO_ATENDIMENTO")
public class PontoAtendimento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int codigo;
	
	private String apelido;
	
	private String uf;
	
	private String cidade;

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getApelido() {
		return apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public PontoAtendimento(int codigo, String apelido, String cidade, String uf) {
		super();
		this.codigo = codigo;
		this.apelido = apelido;
		this.uf = uf;
		this.cidade = cidade;
	}
	
	public PontoAtendimento() {
		
	}
	
	public PontoAtendimento(String apelido) {
		
		this.apelido = apelido;
	}
			
	public PontoAtendimento(int codigo) {
		
		this.codigo = codigo;
	}
	
	@Override
	public String toString() {
	    return "PontoAtendimento{" +
	            "codigo=" + codigo +
	            ", apelido='" + apelido + '\'' +
	            ", uf='" + uf + '\'' +
	            ", cidade='" + cidade + '\'' +
	            '}';
	}

}
