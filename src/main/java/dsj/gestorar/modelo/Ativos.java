package dsj.gestorar.modelo;

import java.util.ArrayList;

public class Ativos {
	
	private String revisao = "X.X";
	
	private String pagina;
	
	private String codigo = "1234567890";
	
	private String dataInventarioAtivos;
	
	private ArrayList<Ativo> ativos;
	
	public String getRevisao() {
		return revisao;
	}

	public void setRevisao(String revisao) {
		this.revisao = revisao;
	}

	public String getPagina() {
		return pagina;
	}

	public void setPagina(String pagina) {
		this.pagina = pagina;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDataInventarioAtivos() {
		return dataInventarioAtivos;
	}

	public void setDataInventarioAtivos(String data) {
		this.dataInventarioAtivos = data;
	}

	public ArrayList<Ativo> getAtivos() {
		return ativos;
	}

	public void setAtivos(ArrayList<Ativo> ativos) {
		this.ativos = ativos;
	}

	public Ativos(String revisao, String pagina, String codigo, String dataInventarioAtivos, ArrayList<Ativo> ativos) {
		super();
		this.revisao = revisao;
		this.pagina = pagina;
		this.codigo = codigo;
		this.dataInventarioAtivos = dataInventarioAtivos;
		this.ativos = ativos;
	} 
	
	
	public Ativos() {
		
	}
}
