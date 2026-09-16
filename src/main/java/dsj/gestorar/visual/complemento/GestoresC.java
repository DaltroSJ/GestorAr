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

import dsj.gestorar.modelo.Gestor;
import dsj.gestorar.visual.buscas.BuscaGestores;

public class GestoresC extends AbstractTableModel{

	private static final long serialVersionUID = 1L;

	private String[] colunas;
	
	private Object[][] dados;
	
	private Gestor linhaSelecionada;
	
	private Object[] gestorSelecionado;
	
	private BuscaGestores bg;
	
	private ArrayList<Gestor> gestores;
	
	public ArrayList<Gestor> getGestores() {
		return gestores;
	}

	public void setGestores(ArrayList<Gestor> gestores) {
		this.gestores = gestores;
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
	
	public Gestor getLinhaSelecionada() {
		return linhaSelecionada;
	}

	public void setLinhaSelecionada(Gestor linhaSelecionada) {
		this.linhaSelecionada = linhaSelecionada;
	}

	public Object[] getGestorSelecionado() {
		return gestorSelecionado;
	}

	public void setGestorSelecionado(Object[] gestorSelecionado) {
		this.gestorSelecionado = gestorSelecionado;
	}

	public JTable tabelaAtivos(List<Gestor> dados) {
		
		setColunas(new String[] {"Código", "Nome Gestor", "Email Gestor", "Nível", "Situacao"});
		
		int cLinhas = dados.size();
	
		int cColunas = 5;
		
		this.dados = new Object[cLinhas][cColunas];
		
		for(int i = 0; i < cLinhas; i++) {
			
			Gestor gestor = dados.get(i);
			
			this.dados[i][0] = gestor.getCodigo();
			this.dados[i][1] = gestor.getNome();
			this.dados[i][2] = gestor.getEmail();
			this.dados[i][3] = gestor.getNivel();
			this.dados[i][4] = gestor.getSituacao();
			
		}
		
		final JTable tabelaAtivos = new JTable(this);
		
		tabelaAtivos.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {

				if(KeyEvent.VK_ENTER == e.getKeyCode()) {
					
					int numaroLinhaSelecionada = tabelaAtivos.getSelectedRow();
					
					gestorSelecionado = GestoresC.this.dados[numaroLinhaSelecionada];
					
					linhaSelecionada = new Gestor((Integer) gestorSelecionado[0],
							(String) gestorSelecionado[1],
							(String) gestorSelecionado[2],
							(String) gestorSelecionado[3],
							(String) gestorSelecionado[4]);
					
					bg.abreFormularioGestores(linhaSelecionada);
					
				}
				
			}
		});
		
		tabelaAtivos.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(e.getClickCount() == 2) {
					
					int numaroLinhaSelecionada = tabelaAtivos.getSelectedRow();
					
					gestorSelecionado = GestoresC.this.dados[numaroLinhaSelecionada];
					
					linhaSelecionada = new Gestor((Integer) gestorSelecionado[0],
							(String) gestorSelecionado[1],
							(String) gestorSelecionado[2],
							(String) gestorSelecionado[3],
							(String) gestorSelecionado[4]);
					
					bg.abreFormularioGestores(linhaSelecionada);
					
				}
				
			}
			
		});
		
		tabelaAtivos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		tabelaAtivos.setFont(new Font("Arial",Font.BOLD,12));
		
		tabelaAtivos.setSelectionBackground(Color.orange);
		
		tabelaAtivos.setGridColor(Color.blue);
		
		/*JTableHeader jth = new JTableHeader();
		
		jth.setFont(new Font("Arial",Font.BOLD,12));
		
		jth.setTable(tabelaAtivos);
		
		jth.setColumnModel(new DefaultTableColumnModel(){

			private static final long serialVersionUID = 1L;

			{
			
			for(int i = 0; i < getColunas().length; i++) {

				addColumn(new TableColumn(i, 164));
				getColumn(i).setHeaderValue(colunas[i]);
				
			}
			
			}
		}); 
		
		tabelaAtivos.setTableHeader(jth);*/
		
		tabelaAtivos.requestFocusInWindow();
		ajustarLarguraColunas(tabelaAtivos);
		
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
	
	public GestoresC(BuscaGestores bg) {
		
		this.bg = bg;
		
	}
	
	public GestoresC(BuscaGestores bg, ArrayList<Gestor> gestores) {
		
		this.bg = bg;
		
	}
	
	private void ajustarLarguraColunas(JTable tabela) {
    	
        TableColumnModel model = tabela.getColumnModel();
        
        int[] larguras = {50, 150, 150, 100, 100};
        
        for (int i = 0; i < larguras.length; i++) {
        	
            TableColumn coluna = model.getColumn(i);
            coluna.setMinWidth(larguras[i]);
            coluna.setPreferredWidth(larguras[i]);
            coluna.setMaxWidth(larguras[i]);
            
        }
    }

	
}
