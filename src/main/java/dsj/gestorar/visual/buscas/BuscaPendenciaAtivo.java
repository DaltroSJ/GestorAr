package dsj.gestorar.visual.buscas;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.UIManager;

import dsj.gestorar.modelo.Maquina;
import dsj.gestorar.modelo.TituloUsuarios;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.persistencia.MaquinaDAO;
import dsj.gestorar.visual.complemento.PendenciaAtivoC;
import dsj.gestorar.visual.formularios.FormularioAtivo;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Window.Type;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class BuscaPendenciaAtivo {

	private JFrame frame;
	private PendenciaAtivoC pc;
	private Usuario usuario;
	private TituloUsuarios permissoes;

	public BuscaPendenciaAtivo(Usuario usuario, TituloUsuarios permissoes) {
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
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
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
		pc = new PendenciaAtivoC(this);
		JTable tabela= new JTable();
		try(MaquinaDAO mDAO = new  MaquinaDAO()){
			
			tabela = pc.tabelaAtivos(mDAO.listaMaquinasPendentes());
			
		}catch(Exception e) {
			
			System.err.println(e);
			
		}
		
		pPrincipal.setLayout(null);
		
		JScrollPane spAtivos = new JScrollPane(tabela);
		spAtivos.setBounds(10, 50, 694, 256);
		pPrincipal.add(spAtivos);
		
		JPanel pBotoes = new JPanel();
		pBotoes.setBounds(10, 326, 694, 54);
		pBotoes.setBackground(new Color(0, 64, 128));
		pPrincipal.add(pBotoes);
		
		JButton bVoltar = new JButton("Voltar");
		bVoltar.setBounds(0, 0, 694, 50);
		bVoltar.setFont(new Font("Arial", Font.BOLD, 12));
		bVoltar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				voltar();
				
			}
		});
		pBotoes.setLayout(null);
		pBotoes.add(bVoltar);
		
		JLabel lPendencias = new JLabel("Pendencias");
		lPendencias.setHorizontalAlignment(SwingConstants.CENTER);
		lPendencias.setForeground(new Color(255, 255, 255));
		lPendencias.setFont(new Font("Arial", Font.BOLD, 20));
		lPendencias.setBounds(10, 11, 694, 28);
		pPrincipal.add(lPendencias);
	}
	
	public void abrirTela() {
		
		frame.setVisible(true);
		
	}
	
	public void fechaTela() {
	
		frame.dispose();
		
	}
	
	public void abreFormularioAtivos(Maquina maquina) {
		
		FormularioAtivo fa = new FormularioAtivo(usuario, maquina, permissoes);
		
		fa.carregaDadosPendencia();
		
		fechaTela();
		
	}
	
	public void voltar() {
		
		fechaTela();
		BuscaAtivos ba = new BuscaAtivos(usuario, permissoes);
		ba.abrirTela();
		
	}
}
