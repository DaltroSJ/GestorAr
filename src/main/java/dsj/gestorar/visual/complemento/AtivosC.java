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
import dsj.gestorar.modelo.Ativo;
import dsj.gestorar.modelo.Gestor;
import dsj.gestorar.modelo.Maquina;
import dsj.gestorar.visual.buscas.BuscaAtivos;

public class AtivosC extends AbstractTableModel{

	private static final long serialVersionUID = 1L;

	private String[] colunas = {"Código", "Gestor", "Hostname", "Ponto de Atendimento", "Data Vinculação"};
    private Object[][] dados = new Object[0][colunas.length];
	
	private BuscaAtivos ba;
	
	private Object[] ativoSelecionado;
	
	public Object[] getAtivoSelecionado() {
		return ativoSelecionado;
	}

	public void setAtivoSelecionado(Object[] ativoSelecionado) {
		this.ativoSelecionado = ativoSelecionado;
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

	public JTable tabelaAtivos(List<Ativo> dados) {
		
		int cLinhas = dados.size();
		
		int cColunas = 8;
		
		this.dados = new Object[cLinhas][cColunas];
		
		for(int i = 0; i < cLinhas; i++) {
			
			Ativo ativo = dados.get(i);
			
			/*this.dados[i][0] = ativo.getCodigo();
			this.dados[i][1] = ativo.getGestor().getNome();
			this.dados[i][2] = ativo.getAr().getNome();
			this.dados[i][3] = ativo.getMaquina().getNome();
			this.dados[i][4] = ativo.getDataVinculacao();*/
			this.dados[i][0] = ativo.getCodigo();
			this.dados[i][1] = ativo.getGestor().getNome();
			this.dados[i][2] = ativo.getMaquina().getNome();
			this.dados[i][3] = ativo.getMaquina().getPa().getApelido();
			this.dados[i][4] = ativo.getDataVinculacao();
			this.dados[i][5] = ativo.getAr().getCodigo();
			this.dados[i][6] = ativo.getGestor().getCodigo();
			this.dados[i][7] = ativo.getMaquina().getCodigo();
			
			
			
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
					
					ativoSelecionado = AtivosC.this.dados[linhaSelecionada];
					
					Ar ar = new Ar();
					
					ar.setCodigo((Integer)ativoSelecionado[5]);
					
					Maquina maquina = new Maquina();
					
					maquina.setCodigo((Integer)ativoSelecionado[7]);
					
					Gestor gestor = new Gestor();
					
					gestor.setCodigo((Integer)ativoSelecionado[6]);
					
					Ativo ativo = new Ativo();
					
					ativo.setAr(ar);
					ativo.setCodigo((Integer)ativoSelecionado[0]);
					ativo.setGestor(gestor);
					ativo.setMaquina(maquina);
					ativo.setDataVinculacao((String)ativoSelecionado[4]);
					
					ba.abreFormularioAtivos(ativo);
					
					ba.fechaTela();
					
				}
				
				
			}
		});
		
		tabelaAtivos.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(e.getClickCount() == 2) {
					
					int linhaSelecionada = tabelaAtivos.getSelectedRow();
					
					ativoSelecionado = AtivosC.this.dados[linhaSelecionada];
					
					Ar ar = new Ar();
					
					ar.setCodigo((Integer)ativoSelecionado[5]);
					
					Maquina maquina = new Maquina();
					
					maquina.setCodigo((Integer)ativoSelecionado[7]);
					
					Gestor gestor = new Gestor();
					
					gestor.setCodigo((Integer)ativoSelecionado[6]);
					
					Ativo ativo = new Ativo();
					
					ativo.setAr(ar);
					ativo.setCodigo((Integer)ativoSelecionado[0]);
					ativo.setGestor(gestor);
					ativo.setMaquina(maquina);
					ativo.setDataVinculacao((String)ativoSelecionado[4]);
					
					ba.abreFormularioAtivos(ativo);
					
					ba.fechaTela();
					
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
	
	public DefaultComboBoxModel<Gestor> modeloGestores(List<Gestor> listaGestores){
		
		DefaultComboBoxModel<Gestor> gestores = new DefaultComboBoxModel<>();

		
		for(Gestor gestor : listaGestores) {
			
			gestores.addElement(gestor);
			
		}
		
		return gestores;
		
	}
	
	public DefaultComboBoxModel<Maquina> modeloMaquina(List<Maquina> listaGestores){
		
		DefaultComboBoxModel<Maquina> maquinas = new DefaultComboBoxModel<>();
		
		for(Maquina maquina : listaGestores) {
			
			maquinas.addElement(maquina);
			
		}
		
		return maquinas;
		
	}
	
	public AtivosC(BuscaAtivos ba) {
		
		this.ba = ba;
		
	}
	
	public AtivosC() {
		
		
		
	}
	
	private void ajustarLarguraColunas(JTable tabela) {
	    TableColumnModel columnModel = tabela.getColumnModel();
	    int[] larguras = {50, 140, 130, 285, 80};

	    for (int i = 0; i < larguras.length; i++) {
	        TableColumn coluna = columnModel.getColumn(i);
	        coluna.setMinWidth(larguras[i]);
	        coluna.setPreferredWidth(larguras[i]);
	        coluna.setMaxWidth(larguras[i]);
	    }
	}
	
}
