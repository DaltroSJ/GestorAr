package dsj.gestorar.visual.buscas;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SpringLayout;

import dsj.gestorar.modelo.Gestor;
import dsj.gestorar.modelo.TituloUsuarios;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.persistencia.GestorDAO;
import dsj.gestorar.visual.X;
import dsj.gestorar.visual.complemento.GestoresC;
import dsj.gestorar.visual.formularios.FormularioGestor;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Window.Type;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class BuscaGestores {

	private JFrame frame;
	private JScrollPane spTabela;
	private GestoresC gc;
	private JTable tabela;
	private JButton bNovoCadastro;
	private JButton bVoltar;
	private SpringLayout sl_pPrincipal;
	private JPanel pPrincipal;
	private Usuario usuario;
	private JLabel lTitulo;
	private TituloUsuarios permissoes;

	
	public BuscaGestores(Usuario usuario, TituloUsuarios permissoes) {
		this.usuario = usuario;
		this.permissoes = permissoes;
		initialize();
		
	}

	private void initialize() {
		
		frame = new JFrame();
		frame.setType(Type.UTILITY);
		frame.getContentPane().setBackground(new Color(0, 0, 0));
		frame.setBounds(100, 100, 605, 400);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);

		pPrincipal = new JPanel();
		springLayout.putConstraint(SpringLayout.EAST, pPrincipal, 578, SpringLayout.WEST, frame.getContentPane());
		pPrincipal.setBackground(new Color(0, 64, 128));
		springLayout.putConstraint(SpringLayout.NORTH, pPrincipal, 10, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, pPrincipal, 10, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, pPrincipal, 351, SpringLayout.NORTH, frame.getContentPane());
		frame.getContentPane().add(pPrincipal);
		sl_pPrincipal = new SpringLayout();
		pPrincipal.setLayout(sl_pPrincipal);
		
		gc = new GestoresC(this);
		
		try(GestorDAO gDAO = new GestorDAO()){
			
			tabela = gc.tabelaAtivos(gDAO.listaGestores());
			
		}catch(Exception e) {
			
			System.err.println("Erro ao buscar os dados dos gestores");
			
		}
		
		
		spTabela = new JScrollPane(tabela);
		sl_pPrincipal.putConstraint(SpringLayout.WEST, spTabela, 10, SpringLayout.WEST, pPrincipal);
		sl_pPrincipal.putConstraint(SpringLayout.SOUTH, spTabela, -108, SpringLayout.SOUTH, pPrincipal);
		
		spTabela.setPreferredSize(tabela.getPreferredSize());
		pPrincipal.add(spTabela);
		
		JPanel pBusca = new JPanel();
		sl_pPrincipal.putConstraint(SpringLayout.NORTH, spTabela, 6, SpringLayout.SOUTH, pBusca);
		sl_pPrincipal.putConstraint(SpringLayout.EAST, pBusca, -104, SpringLayout.EAST, pPrincipal);
		sl_pPrincipal.putConstraint(SpringLayout.NORTH, pBusca, 10, SpringLayout.NORTH, pPrincipal);
		sl_pPrincipal.putConstraint(SpringLayout.SOUTH, pBusca, -287, SpringLayout.SOUTH, pPrincipal);
		pBusca.setBackground(new Color(0, 64, 128));
		sl_pPrincipal.putConstraint(SpringLayout.WEST, pBusca, 10, SpringLayout.WEST, pPrincipal);
		pPrincipal.add(pBusca);
		SpringLayout sl_pBusca = new SpringLayout();
		pBusca.setLayout(sl_pBusca);
		
		lTitulo = new JLabel("Gestores");
		sl_pBusca.putConstraint(SpringLayout.WEST, lTitulo, 208, SpringLayout.WEST, pBusca);
		sl_pBusca.putConstraint(SpringLayout.SOUTH, lTitulo, -7, SpringLayout.SOUTH, pBusca);
		lTitulo.setForeground(new Color(255, 255, 255));
		lTitulo.setFont(new Font("Arial", Font.BOLD, 24));
		pBusca.add(lTitulo);
		
		bNovoCadastro = new JButton("Novo Cadastro");
		sl_pPrincipal.putConstraint(SpringLayout.EAST, spTabela, 0, SpringLayout.EAST, bNovoCadastro);
		sl_pPrincipal.putConstraint(SpringLayout.NORTH, bNovoCadastro, 287, SpringLayout.NORTH, pPrincipal);
		sl_pPrincipal.putConstraint(SpringLayout.WEST, bNovoCadastro, -150, SpringLayout.EAST, pPrincipal);
		sl_pPrincipal.putConstraint(SpringLayout.EAST, bNovoCadastro, -10, SpringLayout.EAST, pPrincipal);
		bNovoCadastro.setFont(new Font("Arial", Font.BOLD, 12));
		bNovoCadastro.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(permissoes.getPolitica().podeAdicionarGestor()) {
					abreFormularioGestoresLimpo();
				}else {
				
					JOptionPane.showMessageDialog(BuscaGestores.this.frame, "Usuário não tem acesso para esta ação!", "", JOptionPane.INFORMATION_MESSAGE);
					
				}
				
				
				
			}
		});
		pPrincipal.add(bNovoCadastro);
		
		bVoltar = new JButton("Voltar");
		sl_pPrincipal.putConstraint(SpringLayout.NORTH, bVoltar, 287, SpringLayout.NORTH, pPrincipal);
		sl_pPrincipal.putConstraint(SpringLayout.SOUTH, bVoltar, -10, SpringLayout.SOUTH, pPrincipal);
		sl_pPrincipal.putConstraint(SpringLayout.SOUTH, bNovoCadastro, 0, SpringLayout.SOUTH, bVoltar);
		sl_pPrincipal.putConstraint(SpringLayout.WEST, bVoltar, 10, SpringLayout.WEST, pPrincipal);
		sl_pPrincipal.putConstraint(SpringLayout.EAST, bVoltar, 150, SpringLayout.WEST, pPrincipal);
		bVoltar.setFont(new Font("Arial", Font.BOLD, 12));
		bVoltar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				abrePaginaInicial();
				
			}
		});
		pPrincipal.add(bVoltar);
		
	}
	
	public void abreFormularioGestoresLimpo() {
		
		FormularioGestor fg = new FormularioGestor(usuario, permissoes, BuscaGestores.this); 
		
		fg.abrirTela();
		fg.setEstadoCbNivel(true);
		fg.setEstadoCbSituacao(true);
		fg.setBotaoSalvarNovoCadastro(true);
		
		fechaTela();
		
	}
	
	public void abreFormularioGestores(Gestor gestor) {
		
		FormularioGestor fg = new FormularioGestor(usuario, permissoes, gestor, null, BuscaGestores.this); 
		
		fg.abrirTela();
		
		fechaTela();
		
	}
	
	public void fechaTela() {
		
		frame.dispose();
		
	}
	
	public void abrirTela() {
		
		frame.setVisible(true);
		
	}
	
	public void abrePaginaInicial() {
		
		X pp = new X(usuario);
		
		pp.abrirTela();
		
		fechaTela();
		
	}
	
	

	
}
