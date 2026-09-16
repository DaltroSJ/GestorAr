package dsj.gestorar.visual;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Window.Type;
import javax.swing.SwingConstants;

import dsj.gestorar.modelo.TiposAmbiente;
import dsj.gestorar.modelo.TituloUsuarios;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.visual.buscas.BuscaAgente;
import dsj.gestorar.visual.buscas.BuscaGestores;
import dsj.gestorar.visual.buscas.BuscaMaquinas;
import dsj.gestorar.visual.exportacao.Exportacoes;

import javax.swing.JSeparator;

public class PaginaPrincipal {

	private JFrame frame;
	private JLabel homologacao;
	private final Usuario usuario;
	private TituloUsuarios permissoes;
	
	public PaginaPrincipal(Usuario usuario) {
		
		this.usuario = usuario;
		permissoes = TituloUsuarios.fromCodigo(usuario.getCodigoTitulo());
		initialize();
		
		/*Exception e = new Exception();
        StackTraceElement[] stackTrace = e.getStackTrace();

        System.out.println("Objeto criado em:");
        for (int i = 1; i < stackTrace.length; i++) {  // Começa no índice 1 para ignorar este construtor
            System.out.println("\tat " + stackTrace[i]);
        }*/
	}

	private void initialize() {

		//valid.nathyelle.dsj.utilitarios.Configuracoes.carregarPropriedades();
		homologacao = new JLabel("HOMOLOGAÇÃO");
		homologacao.setFont(new Font("Arial", Font.BOLD, 16));
		homologacao.setBounds(0, 0, 129, 20);
		homologacao.setVisible(false);

		if (TiposAmbiente.HOMOLOGAÇÃO.toString().equals(dsj.gestorar.utilitarios.Configuracoes.tipoAmbiente)) {
		    homologacao.setVisible(true);
		    Thread thread = new Thread(() -> {
		        try {
		            while (true) {
		                homologacao.setForeground(Color.RED);
		                Thread.sleep(500);
		                homologacao.setForeground(Color.WHITE);
		                Thread.sleep(500);
		            }
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    });
		    thread.start();
		}

		frame = new JFrame();
		frame.setType(Type.UTILITY);
		frame.getContentPane().setBackground(new Color(0, 0, 0));
		frame.setBounds(100, 100, 750, 450);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		
		JPanel pPrincipal = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, pPrincipal, 10, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, pPrincipal, 10, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, pPrincipal, 401, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, pPrincipal, 724, SpringLayout.WEST, frame.getContentPane());
		pPrincipal.setBackground(new Color(0, 64, 124));
		frame.getContentPane().add(pPrincipal);
		
		JButton bGestor = new JButton("Gestores");
		bGestor.setBounds(504, 62, 200, 60);
		bGestor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				fechaTela();
				new BuscaGestores(usuario, permissoes).abrirTela();
			}
		});
		pPrincipal.setLayout(null);
		bGestor.setFont(new Font("Arial", Font.BOLD, 12));
		pPrincipal.add(bGestor);
		
		JButton bMaquina = new JButton("Maquinas");
		bMaquina.setBounds(10, 62, 200, 60);
		bMaquina.setFont(new Font("Arial", Font.BOLD, 12));
		bMaquina.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				fechaTela();
				new BuscaMaquinas(usuario, permissoes).abrirTela();;
				
				
				
			}
		});
		pPrincipal.add(bMaquina);
		
		JButton btnAtivardesativarMaquinas = new JButton("Ativar ou Desativar Maquinas");
		btnAtivardesativarMaquinas.setBounds(220, 62, 274, 60);
		btnAtivardesativarMaquinas.setFont(new Font("Arial", Font.BOLD, 12));
		btnAtivardesativarMaquinas.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
		
				fechaTela();
				//BuscaAtivos ba = new BuscaAtivos(usuario);
				//ba.abrirTela();

			}
		});
		pPrincipal.add(btnAtivardesativarMaquinas);
		
		JButton btnSair = new JButton("Sair");
		btnSair.setBounds(504, 301, 200, 57);
		btnSair.setFont(new Font("Arial", Font.BOLD, 12));
		btnSair.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				 frame.dispose();
				 System.exit(0);
				
			}
		});
		
		pPrincipal.add(btnSair);
		
		JLabel lversao = new JLabel("Versão: 1.7.0");
		lversao.setBackground(new Color(255, 255, 255));
		lversao.setHorizontalAlignment(SwingConstants.CENTER);
		lversao.setBounds(558, 361, 146, 20);
		pPrincipal.add(lversao);
		lversao.setLabelFor(frame.getContentPane());
		lversao.setFont(new Font("Arial", Font.BOLD, 16));
		lversao.setForeground(new Color(0, 0, 0));
		
		JButton bAgente = new JButton("Agentes");
		bAgente.setBounds(504, 132, 200, 60);
		bAgente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				fechaTela();
				BuscaAgente ba = new BuscaAgente(usuario, permissoes);
				ba.abrirTela();
				
				
			}
		});
		bAgente.setFont(new Font("Arial", Font.BOLD, 12));
		pPrincipal.add(bAgente);
		
		JButton bPontos = new JButton("Pontos de Atendimento");
		bPontos.setBounds(10, 133, 200, 59);
		bPontos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				fechaTela();
				//BuscaPontoAtendimento bpa = new BuscaPontoAtendimento(usuario);
				//.abrirTela();
				
				
				
			}
		});
		bPontos.setFont(new Font("Arial", Font.BOLD, 12));
		pPrincipal.add(bPontos);

		JLabel labelTitulo = new JLabel("Bem vindo " + usuario.getNome() + " - AR: " + usuario.getAr().getNome());
		labelTitulo.setForeground(new Color(255, 255, 255));
		labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		labelTitulo.setFont(new Font("Arial", Font.BOLD, 30));
		labelTitulo.setBounds(107, 11, 528, 40);
		pPrincipal.add(labelTitulo);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 288, 694, 2);
		pPrincipal.add(separator);
		
		JButton btnConfiguraes = new JButton("Configurações");
		btnConfiguraes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				fechaTela();
				//Configuracoes config = new Configuracoes(usuario);
				//config.abrirTela();

			}
		});
		btnConfiguraes.setFont(new Font("Arial", Font.BOLD, 12));
		btnConfiguraes.setBounds(220, 301, 274, 57);
		pPrincipal.add(btnConfiguraes);
		
		pPrincipal.add(homologacao);
		
		JButton bExportacoes = new JButton("Exportações");
		bExportacoes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Exportacoes ex = new Exportacoes(usuario);
				
				ex.abrirTela();
				
				fechaTela();
				
			}
		});
		bExportacoes.setFont(new Font("Arial", Font.BOLD, 12));
		bExportacoes.setBounds(504, 203, 200, 60);
		pPrincipal.add(bExportacoes);
		
		JButton btnHostnames = new JButton("Hostnames");
		btnHostnames.setFont(new Font("Arial", Font.BOLD, 12));
		btnHostnames.setBounds(10, 203, 200, 60);
		btnHostnames.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//BuscaHostnames bh = new BuscaHostnames(usuario);
				//bh.abrirTela();
				fechaTela();
				
			}
		});
		pPrincipal.add(btnHostnames);
		
		JButton botaoUsuario = new JButton("Dados do Usuário");
		botaoUsuario.setFont(new Font("Arial", Font.BOLD, 12));
		botaoUsuario.setBounds(10, 298, 200, 60);
		botaoUsuario.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				fechaTela();
				//fu.abrirTela();
				
				
			}
		});
		pPrincipal.add(botaoUsuario);
		
		JButton botaoAr = new JButton("Autoridade de Registro");
		botaoAr.setFont(new Font("Arial", Font.BOLD, 12));
		botaoAr.setBounds(220, 133, 274, 60);
		botaoAr.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				fechaTela();
				//FormularioAr fa = new FormularioAr(usuario, usuario.getAr(), null, PaginaPrincipal.class);
				//fa.abrirTela();
				
			}
		});;
		pPrincipal.add(botaoAr);
		
		JButton btnDesativacao = new JButton("Hostnames");
		btnDesativacao.setFont(new Font("Arial", Font.BOLD, 12));
		btnDesativacao.setBounds(220, 203, 274, 60);
		btnDesativacao.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//BuscaDesativacao bd = new BuscaDesativacao(usuario);
				
				//bd.abrirTela();
				
				fechaTela();
				
				
			}
		});
		pPrincipal.add(btnDesativacao);
		
	}
	
	public void fechaTela() {
		
		frame.setVisible(false);
		frame.dispose();
		
	}
	
	public void abreTela() {
		
		frame.setVisible(true);
		
	}
	
	
}
