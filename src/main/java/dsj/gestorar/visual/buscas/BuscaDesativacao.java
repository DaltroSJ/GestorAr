package dsj.gestorar.visual.buscas;

import javax.swing.JFrame;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import dsj.gestorar.modelo.Desativacao;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.persistencia.DesativacaoDAO;
import dsj.gestorar.visual.X;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;

public class BuscaDesativacao {

	private JFrame frmDesativaes;
	private DesativacaoC dc;
	private Usuario usuario;
	private JTable tabela;

	public BuscaDesativacao(Usuario usuario) {
		this.usuario = usuario;
		initialize();
	}

	private void initialize() {
		frmDesativaes = new JFrame();
		frmDesativaes.setTitle("Desativações");
		frmDesativaes.getContentPane().setBackground(new Color(0, 0, 0));
		frmDesativaes.setType(Type.UTILITY);
		frmDesativaes.setResizable(false);
		frmDesativaes.setBounds(100, 100, 861, 350);
		frmDesativaes.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmDesativaes.getContentPane().setLayout(null);
		frmDesativaes.setLocationRelativeTo(null);

		dc = new DesativacaoC();
		
		try(DesativacaoDAO dDAO = new DesativacaoDAO()){
			
			tabela = dc.tabela(dDAO.listaDesativacoes());
			
		}catch(Exception e) {
			
			
			
		}
		
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 64, 128));
		panel.setBounds(10, 11, 825, 289);
		frmDesativaes.getContentPane().add(panel);
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 45, 805, 182);
		panel.add(scrollPane);
		scrollPane.setViewportView(tabela);
		

		JButton bVoltar = new JButton("Voltar");
		bVoltar.setFont(new Font("Segoe UI", Font.BOLD, 12));
		bVoltar.setBounds(10, 238, 805, 40);
		bVoltar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				X pp = new X(usuario);
				
				pp.abrirTela();
				
				fechaTela();
				
			}
		});
		panel.add(bVoltar);
		
		JLabel lDesativacao = new JLabel("Desativações");
		lDesativacao.setForeground(new Color(255, 255, 255));
		lDesativacao.setFont(new Font("Arial", Font.BOLD, 24));
		lDesativacao.setBounds(329, 11, 168, 28);
		panel.add(lDesativacao);
	}
	
	public void abrirTela() {
		
		frmDesativaes.setVisible(true);
		
	}
	
	public void fechaTela() {
		
		frmDesativaes.setVisible(false);
		frmDesativaes.dispose();
		
	}

	public class DesativacaoC extends AbstractTableModel {


		private static final long serialVersionUID = 1L;
		private String[] colunas = { "Código", "Data", "Máquina", "Motivo" };
		private Object[][] dados = new Object[0][colunas.length];
		private JTable tabela;
		
		public void adicionarPropriedadesTabela(JTable tabela) {

			tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tabela.setFont(new Font("Arial", Font.BOLD, 12));
			tabela.setSelectionBackground(Color.ORANGE);
			tabela.setGridColor(Color.BLUE);
			tabela.requestFocusInWindow();

		}

		public JTable tabela(List<Desativacao> lista) {

			tabela = new JTable(this);
			
			atualizarTabela(lista);
			ajustarLarguraColunas(tabela);
			adicionarPropriedadesTabela(tabela);
			return tabela;

		}

		public void atualizarTabela(List<Desativacao> desativacao) {
		    	
		        if (desativacao == null) throw new IllegalArgumentException("Lista de máquinas não pode ser nula");

		        dados = new Object[desativacao.size()][colunas.length];

		        for (int i = 0; i < desativacao.size(); i++) {
		        	
		            Desativacao d = desativacao.get(i);
		            
		                dados[i][0] = d.getCodigo();
		                dados[i][1] = d.getDataDesativacao();
		                dados[i][2] = d.getMaquina().getNome();
		                dados[i][3] = d.getMotivo();
		                
		                
		                
		        }
		        
		        fireTableDataChanged();

				
		 }
		
		private void ajustarLarguraColunas(JTable tabela) {
		    TableColumnModel columnModel = tabela.getColumnModel();
		    int[] larguras = {50, 150, 200, 400};

		    for (int i = 0; i < larguras.length; i++) {
		        TableColumn coluna = columnModel.getColumn(i);
		        coluna.setMinWidth(larguras[i]);
		        coluna.setPreferredWidth(larguras[i]);
		        coluna.setMaxWidth(larguras[i]);
		    }
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

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

	}
}
