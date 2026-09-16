package dsj.gestorar.visual.complemento;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import dsj.gestorar.modelo.Ar;
import dsj.gestorar.modelo.Gestor;
import dsj.gestorar.modelo.Maquina;
import dsj.gestorar.visual.buscas.BuscaPendenciaAtivo;

public class PendenciaAtivoC extends AbstractTableModel{

	private static final long serialVersionUID = 1L;

	private String[] colunas = {"Código","Hostname","Ponto de Atendimento","Situação"};
	
	private Object[][] dados = new Object[0][colunas.length];
	
	private BuscaPendenciaAtivo pa;
	
	private Object[] maquinaSelecionada
	;
	
	public Object[] getMaquinaSelecionada() {
		return maquinaSelecionada;
	}

	public void setMaquinaSelecionada(Object[] ativoSelecionado) {
		this.maquinaSelecionada = ativoSelecionado;
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

	public JTable tabelaAtivos(List<Maquina> dados) {
		
		int cLinhas = dados.size();
		
		int cColunas = 4;
		
		this.dados = new Object[cLinhas][cColunas];

		for(int i = 0; i < cLinhas; i++) {
			
			Maquina maquina = dados.get(i);
			
			this.dados[i][0] = maquina.getCodigo();
			this.dados[i][1] = maquina.getNome();
			this.dados[i][2] = maquina.getPa().getApelido();
			this.dados[i][3] = "PENDENTE";
			
			
			
		}
		
		final JTable tabelaAtivos = new JTable(this);
		
		tabelaAtivos.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {

				if(KeyEvent.VK_ENTER == e.getKeyCode()) {
					
					int linhaSelecionada = tabelaAtivos.getSelectedRow();
					
					maquinaSelecionada = PendenciaAtivoC.this.dados[linhaSelecionada];
					
					Maquina maquina = new Maquina();
					
					maquina.setCodigo((Integer)maquinaSelecionada[0]);
									
					pa.abreFormularioAtivos(maquina);
					
					pa.fechaTela();
					
				}
				
				
			}
		});
		
		tabelaAtivos.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(e.getClickCount() == 2) {
					
					int linhaSelecionada = tabelaAtivos.getSelectedRow();
					
					maquinaSelecionada = PendenciaAtivoC.this.dados[linhaSelecionada];
					
					Maquina maquina = new Maquina();
					
					maquina.setCodigo((Integer)maquinaSelecionada[0]);
					
					pa.abreFormularioAtivos(maquina);
					
					pa.fechaTela();
					
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

	public DefaultComboBoxModel<Ar> modeloArs(ArrayList<Ar> listaArs){
		
		DefaultComboBoxModel<Ar> ars = new DefaultComboBoxModel<>();
		
		for(Ar ar : listaArs) {
			
			ars.addElement(ar);
			
		}
		
		return ars;
		
	}
	
	public DefaultComboBoxModel<Gestor> modeloGestores(ArrayList<Gestor> listaGestores){
		
		DefaultComboBoxModel<Gestor> gestores = new DefaultComboBoxModel<>();
		
		for(Gestor gestor : listaGestores) {
			
			gestores.addElement(gestor);
			
		}
		
		return gestores;
		
	}
	
	public DefaultComboBoxModel<Maquina> modeloMaquina(ArrayList<Maquina> listaGestores){
		
		DefaultComboBoxModel<Maquina> maquinas = new DefaultComboBoxModel<>();
		
		for(Maquina maquina : listaGestores) {
			
			maquinas.addElement(maquina);
			
		}
		
		return maquinas;
		
	}
	
	public PendenciaAtivoC(BuscaPendenciaAtivo pa) {
		
		this.pa = pa;
		
	}
	
	private void ajustarLarguraColunas(JTable tabela) {
	    TableColumnModel columnModel = tabela.getColumnModel();
	    int[] larguras = {50, 150, 300, 150};

	    for (int i = 0; i < larguras.length; i++) {
	        TableColumn coluna = columnModel.getColumn(i);
	        coluna.setMinWidth(larguras[i]);
	        coluna.setPreferredWidth(larguras[i]);
	        coluna.setMaxWidth(larguras[i]);
	    }
	}
	
}
