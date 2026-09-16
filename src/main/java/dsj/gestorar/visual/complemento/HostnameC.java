package dsj.gestorar.visual.complemento;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import dsj.gestorar.modelo.Hostname;
import dsj.gestorar.persistencia.HostnameDAO;
import dsj.gestorar.visual.buscas.BuscaHostnames;

public class HostnameC extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private String[] colunas = { "Código", "Hostname", "Requisitado", "Disponível", "Liberado", "AR", "Requisitante" };

	private Object[][] dados = new Object[0][0];

	private BuscaHostnames bh;

	public HostnameC(BuscaHostnames bh) {
		this.bh = bh;
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
	public String getColumnName(int coluna) {
		return colunas[coluna];
	}

	@Override
	public Object getValueAt(int linha, int coluna) {
		return dados[linha][coluna];
	}

	public void atualizarTabela(List<Hostname> novosHostnames) {

		int cLinhas = novosHostnames.size();
		dados = new Object[cLinhas][colunas.length];

		for (int i = 0; i < cLinhas; i++) {

			Hostname h = novosHostnames.get(i);

			dados[i][0] = h.getCodigo();
			dados[i][1] = h.getHostname();
			dados[i][2] = h.isRequisitado() ? "SIM" : "NÃO";
			dados[i][3] = h.isDisponivel() ? "SIM" : "NÃO";
			dados[i][4] = h.isLiberado() ? "SIM" : "NÃO";
			dados[i][5] = h.getAr().getNome();
			dados[i][6] = h.getNomeRequisitante();
		}

		fireTableDataChanged();
	}

	public void ObtemLinha(JTable tabela) {

		int linha = tabela.getSelectedRow();

		Hostname h = null;
		if (linha > -1) {

			try (HostnameDAO hDAO = new HostnameDAO()) {

				h = hDAO.buscaHostname(new Hostname((Integer) dados[linha][0]));
				
			} catch (Exception ee) {

				System.err.println(ee);

			}

		}

		if (h != null) {

			bh.abreFrameInterno(h, bh);

		}

	}

	public void adicionaFuncionalidadeTabela(JTable tabela) {

		tabela.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
					
				if (KeyEvent.VK_ENTER == e.getKeyCode()) {
					ObtemLinha(tabela);
				}

			}
		});

		tabela.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {

					ObtemLinha(tabela);

				}
			}

		});

	}
	
	public void adicionarPropriedadesTabela(JTable tabela) {
    	
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setFont(new Font("Arial", Font.BOLD, 12));
        tabela.setSelectionBackground(Color.ORANGE);
        tabela.setGridColor(Color.BLUE);
        tabela.requestFocusInWindow();
        
    }
	
	 public void ajustarLarguraColunas(JTable tabela) {
	    	
	        TableColumnModel model = tabela.getColumnModel();
	        
	        int[] larguras = {50, 130, 75, 75, 75, 100, 150};
	        
	        for (int i = 0; i < larguras.length; i++) {
	        	
	            TableColumn coluna = model.getColumn(i);
	            coluna.setMinWidth(larguras[i]);
	            coluna.setPreferredWidth(larguras[i]);
	            coluna.setMaxWidth(larguras[i]);
	            
	        }
	    }

}
