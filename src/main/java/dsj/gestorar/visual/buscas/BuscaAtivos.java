package dsj.gestorar.visual.buscas;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.UIManager;

import dsj.gestorar.modelo.Ativo;
import dsj.gestorar.modelo.TituloUsuarios;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.persistencia.AtivoDAO;
import dsj.gestorar.visual.X;
import dsj.gestorar.visual.complemento.AtivosC;
import dsj.gestorar.visual.formularios.FormularioAtivo;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Window.Type;
import javax.swing.JLabel;

public class BuscaAtivos {

	private JFrame frame;
	private JScrollPane spTabela;
	private AtivosC ac;
	private BuscaPendenciaAtivo bpa;
	private Usuario usuario;
	private TituloUsuarios permissoes;
	

	public JFrame getFrame() {
		
		return frame;
		
	}
	
	
	public BuscaAtivos(Usuario usuario, TituloUsuarios permissoes) {
		
		this.usuario = usuario;
		this.permissoes = permissoes;
		
		initialize();
		
	}
	
	
	private void initialize() {
		
		frame = new JFrame();
		frame.setType(Type.UTILITY);
		frame.getContentPane().setBackground(new Color(0, 0, 0));
		frame.setBounds(100, 100, 750, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		Font customFont = new Font("Arial", Font.BOLD, 12);

        UIManager.put("OptionPane.messageFont", customFont);
        UIManager.put("OptionPane.buttonFont", customFont);
        UIManager.put("TextField.font", customFont);
		
		JPanel pPrincipal = new JPanel();
		springLayout.putConstraint(SpringLayout.SOUTH, pPrincipal, 401, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, pPrincipal, 724, SpringLayout.WEST, frame.getContentPane());
		pPrincipal.setBackground(new Color(0, 64, 128));
		springLayout.putConstraint(SpringLayout.NORTH, pPrincipal, 10, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, pPrincipal, 10, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(pPrincipal);
		
		ac =  new AtivosC(this);
		
		JTable tabela = new JTable();
		
		try(AtivoDAO aDAO = new AtivoDAO()){
			
			tabela = ac.tabelaAtivos(aDAO.listaAtivos());
			
			
		}catch(Exception e) {
			
			System.err.println(e);
			
		}
		
		pPrincipal.setLayout(null);
		
		spTabela = new JScrollPane(tabela);
		spTabela.setBounds(10, 55, 694, 265);
		
		spTabela.setPreferredSize(tabela.getPreferredSize());
		pPrincipal.add(spTabela);
		
		JPanel pBusca = new JPanel();
		pBusca.setBounds(10, 10, 694, 35);
		pBusca.setBackground(new Color(0, 64, 128));
		pPrincipal.add(pBusca);
		SpringLayout sl_pBusca = new SpringLayout();
		pBusca.setLayout(sl_pBusca);
		
		JLabel lAtivos = new JLabel("Ativos\r\n");
		sl_pBusca.putConstraint(SpringLayout.WEST, lAtivos, 310, SpringLayout.WEST, pBusca);
		sl_pBusca.putConstraint(SpringLayout.SOUTH, lAtivos, -6, SpringLayout.SOUTH, pBusca);
		lAtivos.setForeground(new Color(255, 255, 255));
		lAtivos.setFont(new Font("Arial", Font.BOLD, 20));
		pBusca.add(lAtivos);
		
		JButton bVoltar = new JButton("Voltar");
		bVoltar.setBounds(10, 331, 150, 49);
		bVoltar.setFont(new Font("Arial", Font.BOLD, 12));
		bVoltar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				voltar();
				
			}
		});
		pPrincipal.add(bVoltar);
		
		JButton bPedencia = new JButton("Pendencias");
		bPedencia.setBounds(554, 331, 150, 49);
		bPedencia.setFont(new Font("Arial", Font.BOLD, 12));
		bPedencia.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				abreBuscaPedencias();
				
			}
		});
		pPrincipal.add(bPedencia);
		
	}
	
	public void abreFormularioAtivos(Ativo ativo) {

		new FormularioAtivo(usuario, ativo, permissoes); 
		
		fechaTela();
	
		
	}
	
	public void abreBuscaPedencias() {
	
		bpa = new BuscaPendenciaAtivo(usuario, permissoes);
		
		bpa.abrirTela();
		
		fechaTela();
		
	}
	
	public void abrirTela() {
		
		frame.setVisible(true);
		
	}
	
	public void fechaTela() {
		
		getFrame().dispose();
		
	}
	
	public void voltar() {
		
		X pp = new X(usuario);
		
		pp.abrirTela();
		
		frame.dispose();
		
	}
	
	
	
}
