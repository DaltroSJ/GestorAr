package dsj.gestorar.visual.formularios;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dsj.gestorar.modelo.Agente;
import dsj.gestorar.modelo.TituloUsuarios;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.persistencia.AgenteDAO;
import dsj.gestorar.persistencia.AgentesMaquinaDAO;

import java.awt.Font;
import java.util.List;
import javax.swing.JScrollPane;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AgentesMaquina extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JScrollPane scrollPane;
	private int codigoPonto;
	private int codigoMaquina;
	private Usuario usuario;
	public TituloUsuarios permissoes;

	public int getCodigoMaquina() {
		return codigoMaquina;
	}

	public void setCodigoMaquina(int codigoMaquina) {
		this.codigoMaquina = codigoMaquina;
	}

	public int getCodigoPonto() {
		return codigoPonto;
	}

	public void setCodigoPonto(int codigoPonto) {
		this.codigoPonto = codigoPonto;
	}

	
	public AgentesMaquina(int codigoPonto, int codigoMaquina, TituloUsuarios permissoes, Usuario usuario) {
		
		this.permissoes = permissoes;
		setCodigoMaquina(codigoMaquina);
		setCodigoPonto(codigoPonto);
		inicializaFrame();
		this.usuario = usuario;
		
	}
	
	public AgentesMaquina(Usuario usuario, TituloUsuarios permissoes) {
		this.usuario = usuario;
		this.permissoes = permissoes;
		inicializaFrame();
	}
	
	public void inicializaFrame() {
		
		setType(Type.UTILITY);
		setFont(new Font("Arial", Font.BOLD, 12));
		setTitle("Agentes Maquina");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 414, 200);
		
		try(AgenteDAO aDAO = new AgenteDAO()){
			
			List<Agente> agentes = aDAO.listaAgentesFiltro(getCodigoPonto()); 
			criaJcbs(agentes);
			
		}catch(Exception e) {
			
			System.err.println(e);
			
		}
		
		contentPane.add(scrollPane);
		
		JPanel painelBotoes = new JPanel();
		painelBotoes.setBackground(new Color(0, 64, 128));
		painelBotoes.setBounds(10, 212, 414, 38);
		contentPane.add(painelBotoes);
		painelBotoes.setLayout(null);
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				fechaTela();
				
			}
		});
		btnVoltar.setFont(new Font("Arial", Font.BOLD, 12));
		btnVoltar.setBounds(0, 0, 120, 38);
		painelBotoes.add(btnVoltar);
		
	}

	public void criaJcbs(List<Agente> agentes) {
		
		int espacamento = 20;
		
		 JPanel panel = new JPanel();
	     panel.setLayout(null);
		
		for(Agente a : agentes) {
			
			JCheckBox jcb = new JCheckBox(a.getNome());
			jcb.setBounds(10, espacamento, 350, 20);
			if(existe(a)) {
				jcb.setSelected(true);
			}
			panel.add(jcb);
			jcb.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					if(permissoes.getPolitica().podeManipularAgentesMaquina()) {
						System.out.println(a.getCodigo());
						if(jcb.isSelected()) {
							associa(a);
							
							
						}else {
							
							desassocia(a);
							
						}
						
					}else {
						
						JOptionPane.showMessageDialog(AgentesMaquina.this,
								"Você não tem permissão para manipular agentes!",
								"Acesso negado",
								JOptionPane.WARNING_MESSAGE);
						
					}
					
					
				}
			});
			espacamento += 20;
			panel.setPreferredSize(new java.awt.Dimension(350, espacamento));
		    scrollPane.setViewportView(panel);
		}
		
		
	}
	
	public void abrirTela() {
		
		setVisible(true);
		
	}
	
	public void fechaTela() {
		
		this.dispose();
		
	}
	
	public void associa(Agente a) {
		
		try(AgentesMaquinaDAO amDAO = new AgentesMaquinaDAO()){
			
			amDAO.associa(getCodigoMaquina(), a.getCodigo(), getCodigoPonto(), usuario);
			
		}catch(Exception e) {
			
			System.err.println(e);
			
		}
		
		
		
	}
	
	public void desassocia(Agente a) {
		
		try(AgentesMaquinaDAO amDAO = new AgentesMaquinaDAO()){
			
			amDAO.desassocia(getCodigoMaquina(), a.getCodigo(), getCodigoPonto(), usuario);
			
		}catch(Exception e) {
			
			System.err.println(e);
			
		}
		
		
		
	}
	
	public boolean existe(Agente a) {
		
		try(AgentesMaquinaDAO amDAO = new AgentesMaquinaDAO()){
			
			return amDAO.existe(getCodigoMaquina(), a.getCodigo(), getCodigoPonto());
			
		}catch(Exception e) {
			
			System.err.println(e);
			return false;
			
		}
		
		
		
	}
}
