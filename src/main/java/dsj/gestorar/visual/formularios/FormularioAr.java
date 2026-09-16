package dsj.gestorar.visual.formularios;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import dsj.gestorar.modelo.Ar;
import dsj.gestorar.modelo.TituloUsuarios;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.persistencia.ArDAO;
import dsj.gestorar.utilitarios.Mascaras;
import dsj.gestorar.visual.X;

import java.awt.Color;
import java.awt.Component;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormularioAr {

	private JFrame frame;
	private JTextField tCodigo;
	private JTextField tNome;
	private JFormattedTextField tTelefone;
	private JTextField tEndereco;
	private JTextField tVinculacao;
	private Usuario usuario;
	private Ar ar;
	private Component cp;
	private Object origem;
	private TituloUsuarios permissoes;
	private JButton bSalvar;
	private JButton bVoltar;
	
	public FormularioAr(Usuario usuario, Ar ar, Component componente, Object origem, TituloUsuarios permissoes) {
		
		this.origem = origem;
		this.cp = componente;
		this.ar = ar;
		this.usuario = usuario;	
		this.permissoes = permissoes;

		
		initialize();
		
		
	}public FormularioAr(){
		
		this.origem = null;
		this.cp = null;
		this.ar = new Ar();
		this.ar.setCodigo(0);
		this.ar.setEndereco("");
		this.ar.setNome("");
		this.ar.setTelefone("");
		this.ar.setVinculacao("");
		this.usuario = new Usuario();
		this.usuario.setAr(ar);
		this.usuario.setCodigo(0);
		this.usuario.setNome("");
		this.permissoes = TituloUsuarios.ADMINISTRADOR;
		
		initialize();
		this.bVoltar.setEnabled(false);
	}
	
	

	private void initialize() {
		
		frame = new JFrame();
		frame.setType(Type.UTILITY);
		frame.getContentPane().setBackground(new Color(0, 0, 0));
		frame.setBounds(100, 100, 550, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(cp);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		Font customFont = new Font("Arial", Font.BOLD, 12);

        UIManager.put("OptionPane.messageFont", customFont);
        UIManager.put("OptionPane.buttonFont", customFont);
        UIManager.put("TextField.font", customFont);
		
		JPanel pPrincipal = new JPanel();
		pPrincipal.setBackground(new Color(0, 64, 128));
		springLayout.putConstraint(SpringLayout.NORTH, pPrincipal, 10, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, pPrincipal, 10, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, pPrincipal, 351, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, pPrincipal, 524, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(pPrincipal);
		SpringLayout sl_pPrincipal = new SpringLayout();
		pPrincipal.setLayout(sl_pPrincipal);
		
		JPanel pTitulo = new JPanel();
		pTitulo.setBackground(new Color(0, 64, 128));
		sl_pPrincipal.putConstraint(SpringLayout.NORTH, pTitulo, 10, SpringLayout.NORTH, pPrincipal);
		sl_pPrincipal.putConstraint(SpringLayout.WEST, pTitulo, 10, SpringLayout.WEST, pPrincipal);
		sl_pPrincipal.putConstraint(SpringLayout.SOUTH, pTitulo, 48, SpringLayout.NORTH, pPrincipal);
		sl_pPrincipal.putConstraint(SpringLayout.EAST, pTitulo, 504, SpringLayout.WEST, pPrincipal);
		pPrincipal.add(pTitulo);
		SpringLayout sl_pTitulo = new SpringLayout();
		pTitulo.setLayout(sl_pTitulo);
		
		JPanel pForm = new JPanel();
		pForm.setBackground(new Color(0, 64, 128));
		sl_pPrincipal.putConstraint(SpringLayout.NORTH, pForm, 18, SpringLayout.SOUTH, pTitulo);
		sl_pPrincipal.putConstraint(SpringLayout.WEST, pForm, 10, SpringLayout.WEST, pPrincipal);
		sl_pPrincipal.putConstraint(SpringLayout.SOUTH, pForm, -78, SpringLayout.SOUTH, pPrincipal);
		sl_pPrincipal.putConstraint(SpringLayout.EAST, pForm, 0, SpringLayout.EAST, pTitulo);
		pPrincipal.add(pForm);
		
		JPanel pBotoes = new JPanel();
		sl_pPrincipal.putConstraint(SpringLayout.WEST, pBotoes, 0, SpringLayout.WEST, pTitulo);
		sl_pPrincipal.putConstraint(SpringLayout.EAST, pBotoes, 0, SpringLayout.EAST, pTitulo);
		pBotoes.setBackground(new Color(0, 64, 128));
		sl_pPrincipal.putConstraint(SpringLayout.NORTH, pBotoes, 6, SpringLayout.SOUTH, pForm);
		SpringLayout sl_pForm = new SpringLayout();
		pForm.setLayout(sl_pForm);
		
		JPanel pCodigo = new JPanel();
		pCodigo.setBackground(new Color(0, 64, 128));
		sl_pForm.putConstraint(SpringLayout.NORTH, pCodigo, 10, SpringLayout.NORTH, pForm);
		sl_pForm.putConstraint(SpringLayout.WEST, pCodigo, 10, SpringLayout.WEST, pForm);
		sl_pForm.putConstraint(SpringLayout.SOUTH, pCodigo, 50, SpringLayout.NORTH, pForm);
		sl_pForm.putConstraint(SpringLayout.EAST, pCodigo, 484, SpringLayout.WEST, pForm);
		pForm.add(pCodigo);
		
		JPanel pNome = new JPanel();
		pNome.setBackground(new Color(0, 64, 128));
		sl_pForm.putConstraint(SpringLayout.NORTH, pNome, 14, SpringLayout.SOUTH, pCodigo);
		sl_pForm.putConstraint(SpringLayout.WEST, pNome, 0, SpringLayout.WEST, pCodigo);
		sl_pForm.putConstraint(SpringLayout.EAST, pNome, 0, SpringLayout.EAST, pCodigo);
		pForm.add(pNome);
		
		JPanel pEmail = new JPanel();
		pEmail.setBackground(new Color(0, 64, 128));
		sl_pForm.putConstraint(SpringLayout.SOUTH, pNome, -6, SpringLayout.NORTH, pEmail);
		SpringLayout sl_pNome = new SpringLayout();
		pNome.setLayout(sl_pNome);
		
		JLabel nome = new JLabel("Nome");
		sl_pNome.putConstraint(SpringLayout.NORTH, nome, 10, SpringLayout.NORTH, pNome);
		sl_pNome.putConstraint(SpringLayout.WEST, nome, 0, SpringLayout.WEST, pNome);
		nome.setForeground(new Color(255, 255, 255));
		nome.setFont(new Font("Arial", Font.BOLD, 12));
		pNome.add(nome);
		
		tNome = new JTextField();
		sl_pNome.putConstraint(SpringLayout.NORTH, tNome, -3, SpringLayout.NORTH, nome);
		sl_pNome.putConstraint(SpringLayout.WEST, tNome, 6, SpringLayout.EAST, nome);
		sl_pNome.putConstraint(SpringLayout.EAST, tNome, -308, SpringLayout.EAST, pNome);
		tNome.setFont(new Font("Arial", Font.BOLD, 12));
		pNome.add(tNome);
		tNome.setColumns(10);
		
		tNome.setText(usuario.getAr().getNome());
		
		JLabel lEndereco = new JLabel("Endereço");
		sl_pNome.putConstraint(SpringLayout.NORTH, lEndereco, 0, SpringLayout.NORTH, nome);
		sl_pNome.putConstraint(SpringLayout.WEST, lEndereco, 6, SpringLayout.EAST, tNome);
		lEndereco.setForeground(new Color(255, 255, 255));
		lEndereco.setFont(new Font("Arial", Font.BOLD, 12));
		pNome.add(lEndereco);
		
		tEndereco = new JTextField();
		sl_pNome.putConstraint(SpringLayout.NORTH, tEndereco, -3, SpringLayout.NORTH, nome);
		sl_pNome.putConstraint(SpringLayout.WEST, tEndereco, 6, SpringLayout.EAST, lEndereco);
		sl_pNome.putConstraint(SpringLayout.EAST, tEndereco, -10, SpringLayout.EAST, pNome);
		tEndereco.setFont(new Font("Arial", Font.BOLD, 12));
		tEndereco.setColumns(10);
		pNome.add(tEndereco);
		
		tEndereco.setText(usuario.getAr().getEndereco());
		
		sl_pForm.putConstraint(SpringLayout.EAST, pEmail, 0, SpringLayout.EAST, pCodigo);
		sl_pForm.putConstraint(SpringLayout.NORTH, pEmail, 112, SpringLayout.NORTH, pForm);
		sl_pForm.putConstraint(SpringLayout.SOUTH, pEmail, 164, SpringLayout.NORTH, pForm);
		sl_pForm.putConstraint(SpringLayout.WEST, pEmail, 0, SpringLayout.WEST, pCodigo);
		SpringLayout sl_pCodigo = new SpringLayout();
		pCodigo.setLayout(sl_pCodigo);
		
		JLabel codigo = new JLabel("Código");
		sl_pCodigo.putConstraint(SpringLayout.NORTH, codigo, 10, SpringLayout.NORTH, pCodigo);
		sl_pCodigo.putConstraint(SpringLayout.WEST, codigo, 0, SpringLayout.WEST, pCodigo);
		sl_pCodigo.putConstraint(SpringLayout.EAST, codigo, 49, SpringLayout.WEST, pCodigo);
		codigo.setForeground(new Color(255, 255, 255));
		codigo.setFont(new Font("Arial", Font.BOLD, 12));
		pCodigo.add(codigo);
		
		tCodigo = new JTextField();
		sl_pCodigo.putConstraint(SpringLayout.NORTH, tCodigo, -3, SpringLayout.NORTH, codigo);
		sl_pCodigo.putConstraint(SpringLayout.WEST, tCodigo, 6, SpringLayout.EAST, codigo);
		sl_pCodigo.putConstraint(SpringLayout.EAST, tCodigo, -370, SpringLayout.EAST, pCodigo);
		tCodigo.setEditable(false);
		tCodigo.setEnabled(false);
		tCodigo.setFont(new Font("Arial", Font.BOLD, 12));
		pCodigo.add(tCodigo);
		tCodigo.setColumns(15);
		
		tCodigo.setText(String.valueOf(usuario.getAr().getCodigo()));
		
		pForm.add(pEmail);
		SpringLayout sl_pEmail = new SpringLayout();
		pEmail.setLayout(sl_pEmail);
		
		JLabel lTelefone = new JLabel("Telefone");
		sl_pEmail.putConstraint(SpringLayout.NORTH, lTelefone, 10, SpringLayout.NORTH, pEmail);
		sl_pEmail.putConstraint(SpringLayout.WEST, lTelefone, 0, SpringLayout.WEST, pEmail);
		lTelefone.setForeground(new Color(255, 255, 255));
		lTelefone.setFont(new Font("Arial", Font.BOLD, 12));
		pEmail.add(lTelefone);
		
		tTelefone = new JFormattedTextField(Mascaras.mascaraTelefoneFixoFormularios());
		sl_pEmail.putConstraint(SpringLayout.NORTH, tTelefone, -3, SpringLayout.NORTH, lTelefone);
		sl_pEmail.putConstraint(SpringLayout.WEST, tTelefone, 6, SpringLayout.EAST, lTelefone);
		sl_pEmail.putConstraint(SpringLayout.EAST, tTelefone, -307, SpringLayout.EAST, pEmail);
		tTelefone.setFont(new Font("Arial", Font.BOLD, 12));
		pEmail.add(tTelefone);
		tTelefone.setColumns(10);
		
		JLabel lblVinculao = new JLabel("Vinculação");
		sl_pEmail.putConstraint(SpringLayout.NORTH, lblVinculao, 0, SpringLayout.NORTH, lTelefone);
		sl_pEmail.putConstraint(SpringLayout.WEST, lblVinculao, 6, SpringLayout.EAST, tTelefone);
		lblVinculao.setForeground(new Color(255, 255, 255));
		lblVinculao.setFont(new Font("Arial", Font.BOLD, 12));
		pEmail.add(lblVinculao);
		
		tVinculacao = new JTextField();
		sl_pEmail.putConstraint(SpringLayout.NORTH, tVinculacao, -3, SpringLayout.NORTH, lTelefone);
		sl_pEmail.putConstraint(SpringLayout.WEST, tVinculacao, 6, SpringLayout.EAST, lblVinculao);
		sl_pEmail.putConstraint(SpringLayout.EAST, tVinculacao, 346, SpringLayout.WEST, pEmail);
		tVinculacao.setFont(new Font("Arial", Font.BOLD, 12));
		tVinculacao.setColumns(10);
		pEmail.add(tVinculacao);
		
		tVinculacao.setText(usuario.getAr().getVinculacao());
		
		sl_pPrincipal.putConstraint(SpringLayout.SOUTH, pBotoes, -10, SpringLayout.SOUTH, pPrincipal);
		
		JLabel lTitulo = new JLabel("Autoridade de Registro");
		sl_pTitulo.putConstraint(SpringLayout.WEST, lTitulo, 10, SpringLayout.WEST, pTitulo);
		sl_pTitulo.putConstraint(SpringLayout.EAST, lTitulo, 484, SpringLayout.WEST, pTitulo);
		lTitulo.setForeground(new Color(255, 255, 255));
		sl_pTitulo.putConstraint(SpringLayout.SOUTH, lTitulo, -9, SpringLayout.SOUTH, pTitulo);
		lTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lTitulo.setFont(new Font("Arial", Font.BOLD, 16));
		pTitulo.add(lTitulo);
		pPrincipal.add(pBotoes);
		SpringLayout sl_pBotoes = new SpringLayout();
		pBotoes.setLayout(sl_pBotoes);
		
		bSalvar = new JButton("Salvar");
		sl_pBotoes.putConstraint(SpringLayout.SOUTH, bSalvar, -11, SpringLayout.SOUTH, pBotoes);
		sl_pBotoes.putConstraint(SpringLayout.EAST, bSalvar, -10, SpringLayout.EAST, pBotoes);
		bSalvar.setFont(new Font("Arial", Font.BOLD, 14));
		bSalvar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(permissoes.getPolitica().podeGravarAR()) {
					
					Ar ar = new Ar(Integer.parseInt(tCodigo.getText()),
							tNome.getText(),
							tVinculacao.getText(),
							tEndereco.getText(),
							tTelefone.getText());
					
					try(ArDAO arDAO = new ArDAO()){
						
						if(arDAO.gravaDados(ar, usuario)) {
							
							FormularioAr.this.ar = ar;
							JOptionPane.showMessageDialog(frame, "Dados gravados", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
							if(ar.getCodigo() == 0) {
								JOptionPane.showMessageDialog(frame, "Dados gravados - por favor reinicie o sistema", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
								System.exit(0);
							}else {
								X pp = new X(usuario, ar);
								
								pp.abrirTela();
								fechaTela();
								
							}
							
						}else {
							
							JOptionPane.showMessageDialog(frame, "Erro ao gravar os dados", "Erro!", JOptionPane.ERROR_MESSAGE);
							
						}
						
					}catch(Exception ee) {
						
						JOptionPane.showMessageDialog(frame, "Erro ao gravar os dados", "Erro!", JOptionPane.ERROR_MESSAGE);
						
						System.err.println(ee);
					}
					
				}else {
					
					JOptionPane.showMessageDialog(frame, "Você não tem permissão para alterar as informações da AR", "Acesso negado", JOptionPane.WARNING_MESSAGE);					
				}
				
			}
		});
		pBotoes.add(bSalvar);
		
		bVoltar = new JButton("Voltar");
		sl_pBotoes.putConstraint(SpringLayout.NORTH, bSalvar, 0, SpringLayout.NORTH, bVoltar);
		sl_pBotoes.putConstraint(SpringLayout.WEST, bSalvar, 174, SpringLayout.EAST, bVoltar);
		sl_pBotoes.putConstraint(SpringLayout.NORTH, bVoltar, 10, SpringLayout.NORTH, pBotoes);
		sl_pBotoes.putConstraint(SpringLayout.WEST, bVoltar, 10, SpringLayout.WEST, pBotoes);
		sl_pBotoes.putConstraint(SpringLayout.SOUTH, bVoltar, -10, SpringLayout.SOUTH, pBotoes);
		sl_pBotoes.putConstraint(SpringLayout.EAST, bVoltar, -334, SpringLayout.EAST, pBotoes);
		bVoltar.setFont(new Font("Arial", Font.BOLD, 14));
		bVoltar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (origem instanceof FormularioAtivo) {
					
					fechaTela();
					
				}else {
					
					X pp = new X(usuario, ar);
					
					pp.abrirTela();
					fechaTela();
					
				}
				
				
			}
		});
		pBotoes.add(bVoltar);
		
		carregarDados();
	}
	
	public void abrirTela() {
		
		frame.setVisible(true);
		
	}
	
	public void fechaTela() {
		
		frame.setVisible(false);
		frame.dispose();
		
	}
	
	public void carregarDados() {
		
		if(ar != null) {
			
			tCodigo.setText(String.valueOf(ar.getCodigo()));
			tTelefone.setText(ar.getTelefone());
			tEndereco.setText(ar.getEndereco());
			tNome.setText(ar.getNome());
			tVinculacao.setText(ar.getVinculacao());
			
			System.out.println(ar.toString());
			
		}
		
	}
}
