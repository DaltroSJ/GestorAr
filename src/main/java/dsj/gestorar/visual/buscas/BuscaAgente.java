package dsj.gestorar.visual.buscas;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;

import dsj.gestorar.modelo.Agente;
import dsj.gestorar.modelo.TituloUsuarios;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.persistencia.AgenteDAO;
import dsj.gestorar.visual.X;
import dsj.gestorar.visual.complemento.AgenteC;
import dsj.gestorar.visual.formularios.FormularioAgente;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Window.Type;
import javax.swing.JLabel;

public class BuscaAgente {

	private JFrame frame;
	private JScrollPane spTabela;
	private AgenteC ac = new AgenteC(this);;
	private JTable tabela;
	private JButton bNovoCadastro;
	private JButton bVoltar;
	private JPanel pPrincipal;
	private Usuario usuario;
	private TituloUsuarios permissoes;
	private JLabel lAgentes;
	
	public BuscaAgente(Usuario usuario, TituloUsuarios permissoes) {
		
		this.usuario = usuario;
		this.permissoes = permissoes;
		initialize();
		
	}
	

	private void initialize() {
		
		frame = new JFrame();
		frame.setType(Type.UTILITY);
		frame.getContentPane().setBackground(new Color(0, 0, 0));
		frame.setBounds(100, 100, 750, 450);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		
		pPrincipal = new JPanel();
		springLayout.putConstraint(SpringLayout.SOUTH, pPrincipal, 401, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, pPrincipal, 724, SpringLayout.WEST, frame.getContentPane());
		pPrincipal.setBackground(new Color(0, 64, 128));
		springLayout.putConstraint(SpringLayout.NORTH, pPrincipal, 10, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, pPrincipal, 10, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(pPrincipal);

		try(AgenteDAO aDAO = new AgenteDAO()){
			
			tabela = ac.tabelaAtivos(aDAO.listaAgentes());
			pPrincipal.setLayout(null);
			spTabela = new JScrollPane(tabela);
			spTabela.setBounds(10, 60, 694, 257);
			
			spTabela.setPreferredSize(tabela.getPreferredSize());
			pPrincipal.add(spTabela);
			
		}catch(Exception e) {System.err.println(e);}
		
		
		
		
		JPanel pBusca = new JPanel();
		pBusca.setBounds(10, 10, 694, 44);
		pBusca.setBackground(new Color(0, 64, 128));
		pPrincipal.add(pBusca);
		pBusca.setLayout(null);
		
		lAgentes = new JLabel("Agentes");
		lAgentes.setForeground(new Color(255, 255, 255));
		lAgentes.setFont(new Font("Arial", Font.BOLD, 24));
		lAgentes.setBounds(279, 11, 143, 33);
		pBusca.add(lAgentes);
		
		bNovoCadastro = new JButton("Novo Cadastro");
		bNovoCadastro.setBounds(552, 337, 152, 44);
		bNovoCadastro.setFont(new Font("Segoe UI", Font.BOLD, 12));
		bNovoCadastro.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(permissoes.getPolitica().podeAdicionarAgente()) {
					
					abreFormularioAgenteLimpo();
					fechaTela();
					
				}else {
					
					JOptionPane.showMessageDialog(BuscaAgente.this.frame, "Usuário não tem acesso para esta ação!", "", JOptionPane.INFORMATION_MESSAGE);
					
				}
				
				
			}
		});
		pPrincipal.add(bNovoCadastro);
		
		bVoltar = new JButton("Voltar");
		bVoltar.setBounds(10, 337, 121, 44);
		bVoltar.setFont(new Font("Segoe UI", Font.BOLD, 12));
		bVoltar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				abrePaginaInicial();
				
			}
		});
		pPrincipal.add(bVoltar);
		
	}
	
	public void abreFormularioAgenteLimpo() {
		
		FormularioAgente fa = new FormularioAgente(usuario, null, permissoes, this);
		fa.limpaDados();
		
		fa.abrirTela();
		
	}
	
	public void abreFormularioAgente(Agente a) {
		
		try(AgenteDAO aDAO = new AgenteDAO() ){
			
			Agente agente = aDAO.buscaAgente(a.getCodigo());
			
			FormularioAgente fa = new FormularioAgente(usuario, agente, permissoes, this);
			
			fa.abrirTela();
			
			fechaTela();
			
		}catch(Exception e) {
			
			System.err.println(e);
			
		}
		
		
		
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
	
	public void chamaPesquisa() {
		
		tabela.removeAll();
		
		try(AgenteDAO aDAO = new AgenteDAO()){
			
			tabela = ac.tabelaAtivos(aDAO.listaAgentes());
			
		}catch(Exception e) {
			
			System.err.println("Erro ao criar a tebela de agentes");
			
		}
		
		
		
	}
}
