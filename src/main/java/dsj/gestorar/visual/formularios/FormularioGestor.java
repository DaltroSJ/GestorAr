package dsj.gestorar.visual.formularios;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import dsj.gestorar.modelo.Gestor;
import dsj.gestorar.modelo.NiveisGestores;
import dsj.gestorar.modelo.Situacao;
import dsj.gestorar.modelo.TituloUsuarios;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.persistencia.GestorDAO;
import dsj.gestorar.visual.buscas.BuscaGestores;

import java.awt.Color;
import java.awt.Component;
import java.awt.Window.Type;
import javax.swing.JComboBox;

public class FormularioGestor {

	private JFrame frame;
	private Usuario usuario;
	private Gestor gestor;
	private Component cp;
	private Object origem;
	private JTextField tCodigo;
	private JTextField tNome;
	private JTextField tEmail;
	private DefaultComboBoxModel<String> modeloCB;
	private JComboBox<String> cbNivel;
	private JComboBox<String> cbSituacao;
	private DefaultComboBoxModel<String> modeloCbSituacao;
	private TituloUsuarios permissoes;
	private JButton bDesativar;
	private JButton bSalvar;
	private JButton bExcluir;
	
	
	public void setEstadoCbNivel(boolean x) {
		
		cbNivel.setEnabled(x);
		
	}
	
	public void setEstadoCbSituacao(boolean x) {
		
		cbSituacao.setEnabled(x);
		
	}
	
	public void setBotaoSalvarNovoCadastro(boolean x) {
		
		bSalvar.setEnabled(x);
		
	}
	
	public FormularioGestor(Usuario usuario, TituloUsuarios permissoes, Gestor gestor, Component componente, Object origem) {
		
		this.origem = origem;
		this.gestor = gestor;
		this.cp = componente;
		this.usuario = usuario;
		this.permissoes = permissoes;
				
		initialize();
		if(gestor.getSituacao().equals(Situacao.Ativo.toString())) {
			
			bDesativar.setEnabled(true);
			bSalvar.setEnabled(true);
			bExcluir.setEnabled(true);
			
		}
		
	}
	
	public FormularioGestor(Usuario usuario, TituloUsuarios permissoes, Object origem) {
		
		this.origem = origem;
		this.usuario = usuario;
		this.permissoes = permissoes;
		initialize();
	
	}

	private void initialize() {
		
		frame = new JFrame();
		frame.setType(Type.UTILITY);
		frame.getContentPane().setBackground(new Color(0, 0, 0));
		frame.setBounds(100, 100, 550, 367);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(cp);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		JPanel pPrincipal = new JPanel();
		springLayout.putConstraint(SpringLayout.SOUTH, pPrincipal, 318, SpringLayout.NORTH, frame.getContentPane());
		pPrincipal.setBackground(new Color(0, 64, 128));
		springLayout.putConstraint(SpringLayout.NORTH, pPrincipal, 10, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, pPrincipal, 10, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, pPrincipal, 524, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(pPrincipal);
		pPrincipal.setLayout(null);
		
		JPanel pTitulo = new JPanel();
		pTitulo.setBounds(10, 10, 494, 38);
		pTitulo.setBackground(new Color(0, 64, 128));
		pPrincipal.add(pTitulo);
		SpringLayout sl_pTitulo = new SpringLayout();
		pTitulo.setLayout(sl_pTitulo);
		
		JPanel pForm = new JPanel();
		pForm.setBounds(10, 66, 494, 158);
		pForm.setBackground(new Color(0, 64, 128));
		pPrincipal.add(pForm);
		
		JPanel pBotoes = new JPanel();
		pBotoes.setBounds(10, 235, 494, 62);
		pBotoes.setBackground(new Color(0, 64, 128));
		pForm.setLayout(null);
		
		JLabel lCodigo = new JLabel("Código");
		lCodigo.setBounds(10, 17, 49, 25);
		lCodigo.setForeground(Color.WHITE);
		lCodigo.setFont(new Font("Segoe UI", Font.BOLD, 12));
		pForm.add(lCodigo);
		
		tCodigo = new JTextField();
		tCodigo.setBounds(75, 17, 49, 25);
		tCodigo.setFont(new Font("Segoe UI", Font.BOLD, 12));
		tCodigo.setEnabled(false);
		tCodigo.setEditable(false);
		tCodigo.setColumns(15);
		pForm.add(tCodigo);
		
		JLabel lNome = new JLabel("Nome");
		lNome.setBounds(10, 86, 49, 25);
		lNome.setForeground(Color.WHITE);
		lNome.setFont(new Font("Segoe UI", Font.BOLD, 12));
		pForm.add(lNome);
		
		tNome = new JTextField();
		tNome.setBounds(75, 86, 409, 25);
		tNome.setFont(new Font("Segoe UI", Font.BOLD, 12));
		tNome.setColumns(10);
		pForm.add(tNome);
		
		JLabel lEMail = new JLabel("E-Mail");
		lEMail.setBounds(10, 122, 49, 25);
		lEMail.setForeground(Color.WHITE);
		lEMail.setFont(new Font("Segoe UI", Font.BOLD, 12));
		pForm.add(lEMail);
		
		tEmail = new JTextField();
		tEmail.setBounds(75, 122, 409, 25);
		tEmail.setFont(new Font("Segoe UI", Font.BOLD, 12));
		tEmail.setColumns(10);
		pForm.add(tEmail);
		
		JLabel lNivel = new JLabel("Nível");
		lNivel.setBounds(224, 19, 49, 25);
		lNivel.setForeground(Color.WHITE);
		lNivel.setFont(new Font("Segoe UI", Font.BOLD, 12));
		pForm.add(lNivel);
		
		modeloCB = new DefaultComboBoxModel<>();
		
		NiveisGestores[] niveis = NiveisGestores.values();
		for(NiveisGestores x : niveis) {
			
			modeloCB.addElement(x.toString());
			
		}
		
		cbNivel = new JComboBox<String>();
		cbNivel.setBounds(282, 18, 202, 25);
		cbNivel.setFont(new Font("Segoe UI", Font.BOLD, 12));
		cbNivel.setModel(modeloCB);
		cbNivel.setEnabled(false);
		pForm.add(cbNivel);
		
		JLabel lSituacao = new JLabel("Situação");
		lSituacao.setForeground(Color.WHITE);
		lSituacao.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lSituacao.setBounds(224, 50, 49, 25);
		pForm.add(lSituacao);
		
		modeloCbSituacao = new DefaultComboBoxModel<>();
		
		Situacao[] situacaoes = Situacao.values();
		
		for(Situacao x : situacaoes) {
			
			modeloCbSituacao.addElement(x.toString());
			
		}
		
		cbSituacao = new JComboBox<String>();
		cbSituacao.setFont(new Font("Segoe UI", Font.BOLD, 12));
		cbSituacao.setBounds(282, 50, 202, 25);
		cbSituacao.setModel(modeloCbSituacao);
		cbSituacao.setEnabled(false);
		pForm.add(cbSituacao);
		
		
		
		JLabel lTitulo = new JLabel("Gestores");
		sl_pTitulo.putConstraint(SpringLayout.WEST, lTitulo, 189, SpringLayout.WEST, pTitulo);
		sl_pTitulo.putConstraint(SpringLayout.SOUTH, lTitulo, -1, SpringLayout.SOUTH, pTitulo);
		sl_pTitulo.putConstraint(SpringLayout.EAST, lTitulo, 298, SpringLayout.WEST, pTitulo);
		lTitulo.setForeground(new Color(255, 255, 255));
		lTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
		pTitulo.add(lTitulo);
		pPrincipal.add(pBotoes);
		
		bSalvar = new JButton("Salvar");
		bSalvar.setBounds(130, 10, 107, 42);
		bSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
		bSalvar.setEnabled(false);
		bSalvar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				if(permissoes.getPolitica().podeGravarGestor()) {
					
					int codigo = 0;
					
					if(!tCodigo.getText().isEmpty()) {
						
						codigo = Integer.parseInt(tCodigo.getText());
						
					}
					
					Gestor g = new Gestor(codigo, tNome.getText(), tEmail.getText(), cbNivel.getSelectedItem().toString(), cbSituacao.getSelectedItem().toString());
					
					try(GestorDAO gDAO = new GestorDAO()){
						
						if(tNome.getText().isEmpty() || tNome.getText().equals(" ")) {
							
							JOptionPane.showMessageDialog(frame,
									"Campo nome não pode ser em branco!",
									"Verefique os campos",
									JOptionPane.WARNING_MESSAGE);
							return;
							
						}else if(tEmail.getText().isEmpty() || tEmail.getText().equals(" ")) {
							
							JOptionPane.showMessageDialog(frame,
									"Campo e-mail não pode ser em branco!",
									"Verefique os campos",
									JOptionPane.WARNING_MESSAGE);
							
							return;
						}
						
						if(gDAO.gravaDados(g, usuario)) {
							
							JOptionPane.showMessageDialog(frame,
									"Gestor " + tNome.getText() + " gravado com sucesso.",
									"Sucesso!",
									JOptionPane.INFORMATION_MESSAGE);
						
							fechaTela();
							new FormularioGestor(usuario,
									permissoes,
									gDAO.buscaGestor(gDAO.codigoGestor),
									cp,
									origem).abrirTela();
							
							
						}else {
							
							JOptionPane.showMessageDialog(frame,
									"Problema ao salvar o gestor, campos inválidos",
									"Erro!",
									JOptionPane.ERROR_MESSAGE);
							
						}
						
						
					}catch(Exception ex) {
						
						
						System.err.println(ex);
						
					}
					
					}else {
					
						JOptionPane.showMessageDialog(FormularioGestor.this.frame, "Usuário não tem acesso para esta ação!", "", JOptionPane.INFORMATION_MESSAGE);
						
					}
					
				
				
			}});
		pBotoes.setLayout(null);
		pBotoes.add(bSalvar);
		
		JButton bVoltar = new JButton("Voltar");
		bVoltar.setBounds(10, 10, 110, 42);
		bVoltar.setFont(new Font("Segoe UI", Font.BOLD, 14));
		bVoltar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				
				if(origem instanceof FormularioAtivo) {
					
					fechaTela();
				}
				
				if (origem instanceof BuscaGestores) {
					
					new BuscaGestores(usuario, permissoes).abrirTela();;
					fechaTela();
					
				}
			}
		});
		pBotoes.add(bVoltar);
		
		bDesativar = new JButton("Desativar");
		bDesativar.setBounds(247, 10, 120, 42);
		pBotoes.add(bDesativar);
		bDesativar.setFont(new Font("Segoe UI", Font.BOLD, 14));
		bDesativar.setEnabled(false);
		bDesativar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				if(permissoes.getPolitica().podeDesativarGestor()) {
					
					
					int selecao = JOptionPane.showConfirmDialog(frame, "Desaja alterar a situação do gestor ? \nLembrando que esse procedimento é ireversivel", "Duvida", JOptionPane.YES_NO_OPTION);
					
					if(selecao == JOptionPane.YES_OPTION) {
						
						alterarSituacaoGestor();
						
					}else {
						
						return;
						
					}
					
				}else {
					
					JOptionPane.showMessageDialog(FormularioGestor.this.frame, "Usuário não tem acesso para esta ação!", "", JOptionPane.INFORMATION_MESSAGE);
						
					
				}
				
				
			}});
		bExcluir = new JButton("Excluir");
		bExcluir.setBounds(377, 10, 107, 42);
		pBotoes.add(bExcluir);
		sl_pTitulo.putConstraint(SpringLayout.WEST, bExcluir, 0, SpringLayout.EAST, lTitulo);
		bExcluir.setFont(new Font("Segoe UI", Font.BOLD, 14));
		bExcluir.setEnabled(false);
		bExcluir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				if(permissoes.getPolitica().podeExcluirGestor()) {
					
					
					int selecao = JOptionPane.showConfirmDialog(frame, "Desaja realmente excluir o registro ?", "Duvida", JOptionPane.YES_NO_OPTION);
					
					if(selecao == JOptionPane.YES_OPTION) {
						
						excluirCadastroGestor();
						
					}else {
						
						return;
						
					}
				}else {
					

					JOptionPane.showMessageDialog(FormularioGestor.this.frame, "Usuário não tem acesso para esta ação!", "", JOptionPane.INFORMATION_MESSAGE);
					
				}
				
				
				
			}});
		
		
		carregarDados();
		
	}
	
	public void carregarDados() {
		
		if(gestor != null) {
			
			tCodigo.setText(String.valueOf(gestor.getCodigo()));
			tNome.setText(gestor.getNome());
			tEmail.setText(gestor.getEmail());
			cbNivel.setSelectedItem(gestor.getNivel());
			cbSituacao.setSelectedItem(gestor.getSituacao());

			
		}
		
		
	}
	
	public void limpaDados() {
		
		this.tCodigo.setText("");
		this.tNome.setText("");
		this.tEmail.setText("");
		this.cbNivel.setSelectedIndex(0);
		this.cbSituacao.setSelectedIndex(0);
		
	}
	
	public void abrirTela() {
		
		frame.setVisible(true);
		
	}
	
	public void fechaTela() {
		
		frame.dispose();
		
	}
	
	private void alterarSituacaoGestor() {
		
		try(GestorDAO gDAO = new GestorDAO()){
			
			if(gDAO.alterarSituacaoGestor(Situacao.Desativado, gestor.getCodigo(), usuario)) {
				
				JOptionPane.showMessageDialog(frame, "Gestor " + gestor.getNome() + " desativado.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
				
			}else {
				
				JOptionPane.showMessageDialog(frame, "Erro ao desativar gestor", "Erro",JOptionPane.ERROR_MESSAGE);
				
			}
			
		}catch(Exception e) {
			
			JOptionPane.showMessageDialog(frame, "Erro ao alterar gestor", "Erro",JOptionPane.ERROR_MESSAGE);
			
		}
			
		
	}
	
	public void excluirCadastroGestor() {
		
		
		try(GestorDAO gDAO = new GestorDAO()){
			
			if(gDAO.deletaCadastro(gestor.getCodigo(), usuario)) {
				
				JOptionPane.showMessageDialog(frame, "Dados do gestor " + gestor.getNome() + " foram excluidos.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
				
				new BuscaGestores(usuario, permissoes).abrirTela();
				fechaTela();
				
			}else {
				
				JOptionPane.showMessageDialog(frame, "Erro ao excluir dados do gestor\n O gestor possivelmente já tem registros vinculados.\n Nesse contexto será necessario faz a desativação do memso.", "Erro",JOptionPane.ERROR_MESSAGE);
				
			}
			
		}catch(Exception e) {
			
			JOptionPane.showMessageDialog(frame, "Erro ao excluir dados do gestor", "Erro",JOptionPane.ERROR_MESSAGE);
			
		}
		
	}
}
