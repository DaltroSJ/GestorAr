package dsj.gestorar.visual.buscas;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import dsj.gestorar.modelo.TipoCadastroUsuario;
import dsj.gestorar.modelo.TituloUsuarios;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.persistencia.UsuarioDAO;
import dsj.gestorar.visual.X;
import dsj.gestorar.visual.formularios.FormularioUsuario;

import java.awt.Window.Type;

public class BuscaUsuarios {

	public class UsuariosC extends AbstractTableModel{
		
		private static final long serialVersionUID = 1L;
		private String[] colunas = {"Código", "Nome", "Titulo"};
		private Object[][] dados = new Object[0][0];
		private BuscaUsuarios bu;
		
		public UsuariosC(BuscaUsuarios bu) {
			
			this.bu = bu;
			
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
	    
	    public void ObtemLinha(JTable tabela) {
	    	
	    	int linha = tabela.getSelectedRow();

			Usuario usuario = null;
			
			if (linha > -1) {

				try (UsuarioDAO uDAO = new UsuarioDAO()) {

					usuario = uDAO.buscarUsuario((Integer) dados[linha][0]);
					
				} catch (Exception ee) {

					System.err.println(ee);

				}

			}

			if (usuario != null) {

				bu.abrirFormularioUsuariosCadastro(usuario);

			}
	    	
	    }
	    
	    public void adicionarStilo(JTable tabela) {
	    	
	    	 tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	         tabela.setFont(new Font("Arial", Font.BOLD, 12));
	         tabela.setSelectionBackground(Color.ORANGE);
	         tabela.setGridColor(Color.BLUE);
	         tabela.requestFocusInWindow();
	    	
	    }
	    
	    public void atualizarTabela(List<Usuario> novosUsuarios) {

			int cLinhas = novosUsuarios.size();
			dados = new Object[cLinhas][colunas.length];

			for (int i = 0; i < cLinhas; i++) {

				Usuario u = novosUsuarios.get(i);

				dados[i][0] = u.getCodigo();
				dados[i][1] = u.getNome();
				dados[i][2] = TituloUsuarios.fromCodigo(u.getCodigoTitulo());
			}

			fireTableDataChanged();
		}
	   
		
		
	}
	
	private JFrame frame;
	private JTable tabela;
	private UsuariosC uc;
	private Usuario usuario;
	private TituloUsuarios permissoes;
	
	public BuscaUsuarios(Usuario usuario, TituloUsuarios permissoes) {
		this.usuario = usuario;
		this.permissoes = permissoes;
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setType(Type.UTILITY);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(new Color(0, 64, 128));
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 282, 337);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JScrollPane painelTabela = new JScrollPane();
		painelTabela.setBounds(10, 11, 262, 236);
		panel.add(painelTabela);;
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.setFont(new Font("Arial", Font.BOLD, 14));
		btnVoltar.setBounds(10, 278, 262, 48);
		btnVoltar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				new X(usuario).abrirTela();
				fechaTela();
				
			}
		});
		panel.add(btnVoltar);
		frame.setBounds(100, 100, 318, 398);
		
		
		tabela = new JTable();
        uc = new UsuariosC(this);
        tabela.setModel(uc);
        painelTabela.setViewportView(tabela);
        uc.adicionaFuncionalidadeTabela(tabela);
        uc.adicionarStilo(tabela);
        atualizaTabela();
        
        frame.setLocationRelativeTo(null);
	}
	
	public void atualizaTabela() {

        try (UsuarioDAO uDAO = new UsuarioDAO()) {
        	
           uc.atualizarTabela(uDAO.listarUsuarios());
           
        } catch (Exception e) {
        	
            e.printStackTrace();
            
        }

    }
	
	public void abrirFormularioUsuariosCadastro(Usuario modificado) {
		
		FormularioUsuario fu = new FormularioUsuario(usuario,
				modificado,
				TipoCadastroUsuario.ALTERACAO_ADMINISTRADOR,
				permissoes);
		
		fu.abrirTela();
		
		fechaTela();
		
	}
	
	public void fechaTela() {
		
		frame.setVisible(false);
        frame.dispose();
		
	}
	
	public void abreTela() {
		
		frame.setVisible(true);
	}
}
