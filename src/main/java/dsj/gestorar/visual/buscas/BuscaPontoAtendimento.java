package dsj.gestorar.visual.buscas;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SpringLayout;

import dsj.gestorar.modelo.PontoAtendimento;
import dsj.gestorar.modelo.TituloUsuarios;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.persistencia.PontoAtendimentoDAO;
import dsj.gestorar.visual.X;
import dsj.gestorar.visual.complemento.PontoAtendimentoC;
import dsj.gestorar.visual.formularios.FormularioPontoAtendimento;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Window.Type;
import javax.swing.JLabel;

public class BuscaPontoAtendimento {

	private JFrame frame;
	private JScrollPane spTabela;
	private PontoAtendimentoC pc;
	private JTable tabela;
	private JButton bNovoCadastro;
	private JButton bVoltar;
	private JPanel pPrincipal;
	private Usuario usuario;
	private JLabel lPontos;
	private TituloUsuarios permissoes;

	public JFrame getFrame() {
		
		return frame;
		
	}
	
	
	public BuscaPontoAtendimento(Usuario usuario, TituloUsuarios permissoes) {
		this.usuario = usuario;
		this.permissoes = permissoes;
		initialize();
		
	}
	
	
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	private void initialize() {
		
		frame = new JFrame();
		frame.setType(Type.UTILITY);
		frame.getContentPane().setBackground(new Color(0, 0, 0));
		frame.setBounds(100, 100, 750, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		
		
		pc = new PontoAtendimentoC(this);
		
		try(PontoAtendimentoDAO pDAO = new PontoAtendimentoDAO()){
			
			tabela = pc.tabelaAtivos(pDAO.listaPontos());
		}catch(Exception e) {
			
			System.err.println(e);
		}
		
		pPrincipal.setLayout(null);
		
		spTabela = new JScrollPane(tabela);
		spTabela.setBounds(20, 60, 684, 265);
		
		spTabela.setPreferredSize(tabela.getPreferredSize());
		pPrincipal.add(spTabela);
		
		bNovoCadastro = new JButton("Novo Cadastro");
		bNovoCadastro.setBounds(554, 336, 150, 50);
		bNovoCadastro.setFont(new Font("Arial", Font.BOLD, 12));
		bNovoCadastro.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				abreFormularioPontoAtendiemntoLimpo();
				
				fechaTela();
			}
		});
		pPrincipal.add(bNovoCadastro);
		
		bVoltar = new JButton("Voltar");
		bVoltar.setBounds(20, 336, 150, 50);
		bVoltar.setFont(new Font("Arial", Font.BOLD, 12));
		bVoltar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				abrePaginaInicial();
				
			}
		});
		pPrincipal.add(bVoltar);
		
		lPontos = new JLabel("Pontos de Atendimento");
		lPontos.setForeground(new Color(255, 255, 255));
		lPontos.setFont(new Font("Arial", Font.BOLD, 24));
		lPontos.setBounds(227, 11, 276, 38);
		pPrincipal.add(lPontos);
		
	}
	
	public void abreFormularioPontoAtendiemntoLimpo() {
		
		FormularioPontoAtendimento fpa = new FormularioPontoAtendimento(usuario, null, permissoes); 
		
		fpa.abrirTela();
		fechaTela();
		
	}
	
	public void abreFormularioPontosAtendimento(PontoAtendimento pa) {
		
		FormularioPontoAtendimento fpa = new FormularioPontoAtendimento(usuario, pa, null, BuscaPontoAtendimento.this, permissoes);
		
		fpa.abrirTela();
		
		fechaTela();
		
	}
	
	public void fechaTela() {
		
		getFrame().dispose();
		
	}
	
	public void abrirTela() {
		
		getFrame().setVisible(true);
		
	}
	
	public void abrePaginaInicial() {
		
		X pp = new X(usuario);
		
		pp.abrirTela();
		
		fechaTela();
		
	}
	
	public JTable retornatabela(String pesquisa) {
		
		spTabela = new JScrollPane(tabela);

		pPrincipal.add(spTabela);
		
		return null;
		
	}
	
}
