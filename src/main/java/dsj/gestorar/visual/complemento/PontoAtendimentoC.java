package dsj.gestorar.visual.complemento;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import dsj.gestorar.modelo.PontoAtendimento;
import dsj.gestorar.visual.buscas.BuscaPontoAtendimento;

public class PontoAtendimentoC extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private String[] colunas = {"Código", "Ponto de Atendimento", "Cidade", "UF"};
	
	private Object[][] dados = new Object[0][colunas.length];
	
	private PontoAtendimento linhaSelecionada;
	
	private Object[] pontoSelecionado;
	
	private BuscaPontoAtendimento bp;
	
	private ArrayList<PontoAtendimento> pontos;
	
	public ArrayList<PontoAtendimento> getGestores() {
		return pontos;
	}

	public void setGestores(ArrayList<PontoAtendimento> pontos) {
		this.pontos = pontos;
	}

	public String[] getColunas() {
		return colunas;
	}

	public void setColunas(String[] colunas) {
		this.colunas = colunas;
	}

	public Object[][] getDados() {
		return dados;
	}

	public void setDados(Object[][] dados) {
		this.dados = dados;
	}
	
	public PontoAtendimento getLinhaSelecionada() {
		return linhaSelecionada;
	}

	public void setLinhaSelecionada(PontoAtendimento linhaSelecionada) {
		this.linhaSelecionada = linhaSelecionada;
	}

	public Object[] getPontoSelecionado() {
		return pontoSelecionado;
	}

	public void setPontoSelecionado(Object[] gestorSelecionado) {
		this.pontoSelecionado = gestorSelecionado;
	}

	public JTable tabelaAtivos(List<PontoAtendimento> dados) {
		
		setColunas(new String[] {"Código", "Apelido", "Cidade", "UF"});
		
		int cLinhas = dados.size();
	
		int cColunas = 4;
		
		this.dados = new Object[cLinhas][cColunas];
		
		for(int i = 0; i < cLinhas; i++) {
			
			PontoAtendimento pa = dados.get(i);
			
			this.dados[i][0] = pa.getCodigo();
			this.dados[i][1] = pa.getApelido();
			this.dados[i][2] = pa.getCidade();
			this.dados[i][3] = pa.getUf();
			
		}
		
		final JTable tabelaAtivos = new JTable(this);
		
		tabelaAtivos.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {

				if(KeyEvent.VK_ENTER == e.getKeyCode()) {
					
					int numaroLinhaSelecionada = tabelaAtivos.getSelectedRow();
					
					pontoSelecionado = PontoAtendimentoC.this.dados[numaroLinhaSelecionada];
					
					linhaSelecionada = new PontoAtendimento((Integer) pontoSelecionado[0],
							(String) pontoSelecionado[1],
							(String) pontoSelecionado[2],
							(String) pontoSelecionado[3]);
					
					bp.abreFormularioPontosAtendimento(linhaSelecionada);
					
				}
				
			}
		});
		
		tabelaAtivos.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(e.getClickCount() == 2) {
					
					int numaroLinhaSelecionada = tabelaAtivos.getSelectedRow();
					
					pontoSelecionado = PontoAtendimentoC.this.dados[numaroLinhaSelecionada];
					
					linhaSelecionada = new PontoAtendimento((Integer) pontoSelecionado[0],
							(String) pontoSelecionado[1],
							(String) pontoSelecionado[2],
							(String) pontoSelecionado[3]);
					
					bp.abreFormularioPontosAtendimento(linhaSelecionada);
					
				}
				
			}
			
		});
		
		tabelaAtivos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		tabelaAtivos.setFont(new Font("Arial",Font.BOLD,12));
		
		tabelaAtivos.setSelectionBackground(Color.orange);
		
		tabelaAtivos.setGridColor(Color.blue);
		
		ajustarLarguraColunas(tabelaAtivos);
		
		tabelaAtivos.requestFocusInWindow();
		
		return tabelaAtivos;
	}

	@Override
	public int getRowCount() {
		
		return dados.length;
		 
	}

	@Override
	public int getColumnCount() {
		
		return colunas.length;
		
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		return dados[rowIndex][columnIndex];
		
	}
	
	@Override
	public String getColumnName(int columnIndex) {
	    return colunas[columnIndex];
	}
	
	public PontoAtendimentoC(BuscaPontoAtendimento bp) {
		
		this.bp = bp;
		
	}
	
	public PontoAtendimentoC(BuscaPontoAtendimento bp, ArrayList<PontoAtendimento> pontos) {
		
		this.bp = bp;
		
	}
	
	private void ajustarLarguraColunas(JTable tabela) {
	    TableColumnModel columnModel = tabela.getColumnModel();
	    int[] larguras = {50, 300, 100, 50};
	    for (int i = 0; i < larguras.length; i++) {
	        TableColumn coluna = columnModel.getColumn(i);
	        coluna.setMinWidth(larguras[i]);
	        coluna.setPreferredWidth(larguras[i]);
	        coluna.setMaxWidth(larguras[i]);
	    }
	}
}
