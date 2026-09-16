package dsj.gestorar.visual;

import javax.swing.JFrame;
import java.awt.Font;
import java.awt.Color;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import dsj.gestorar.modelo.Ar;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.persistencia.UsuarioDAO;
import dsj.gestorar.utilitarios.Arquivos;
import dsj.gestorar.utilitarios.Configuracoes;
import dsj.gestorar.utilitarios.Verificacoes;
import dsj.gestorar.visual.X.Icones;

import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class Login {

	private JFrame frmAcessarSistemaDe;
	private JTextField textoNome;
	private JPasswordField campoSenha;
	private JPanel painelPrincipal;
	private JLabel popup;
	private JCheckBox lembrarSenha;
	private boolean loginPrimario;
	private boolean mesmoUsuario = false;
	private Ar ar;

	public Login(Ar ar) {
		
		this.ar = ar;
		initialize();

	}

	private void carregarDados() {
		
		Configuracoes.carregarPropriedades();
		if(dsj.gestorar.utilitarios.Configuracoes.lembra.equals("NAO")) {
			
			
			loginPrimario = true;
			
		}else {
			
			loginPrimario = false;
			lembrarSenha.setSelected(true);
			campoSenha.setText("LEMBRA");
			textoNome.setText(Configuracoes.usuario);
			
		}
		
	}
	
	private void initialize() {
		
		frmAcessarSistemaDe = new JFrame();
		frmAcessarSistemaDe.setFont(new Font("Segoe UI", Font.BOLD, 12));
		frmAcessarSistemaDe.setResizable(false);
		frmAcessarSistemaDe.setType(Type.UTILITY);
		frmAcessarSistemaDe.setTitle("Acessar sistema de gestão de AR");
		frmAcessarSistemaDe.getContentPane().setBackground(new Color(0, 0, 0));
		frmAcessarSistemaDe.getContentPane().setFont(new Font("Consolas", Font.BOLD, 12));
		frmAcessarSistemaDe.getContentPane().setLayout(null);
		
		painelPrincipal = new JPanel();
		painelPrincipal.setBackground(new Color(0, 64, 128));
		painelPrincipal.setBounds(10, 10, 530, 262);
		painelPrincipal.setLayout(null);
		JButton botaoSair = new JButton("Sair");
		botaoSair.setBounds(410, 206, 110, 45);
		botaoSair.setFont(new Font("Segoe UI", Font.BOLD, 12));
		botaoSair.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				System.exit(0);
				
			}
		});
		painelPrincipal.add(botaoSair);
		JLabel lNome = new JLabel("Nome do usuário");
		lNome.setBounds(10, 100, 100, 25);
		lNome.setForeground(new Color(255, 255, 255));
		lNome.setFont(new Font("Segoe UI", Font.BOLD, 12));
		painelPrincipal.add(lNome);
		
		JLabel lSenha = new JLabel("Senha do usuário");
		lSenha.setBounds(10, 135, 100, 25);
		lSenha.setForeground(Color.WHITE);
		lSenha.setFont(new Font("Segoe UI", Font.BOLD, 12));
		painelPrincipal.add(lSenha);
		
		textoNome = new JTextField();
		textoNome.setBounds(120, 100, 288, 25);
		textoNome.setFont(new Font("Segoe UI", Font.BOLD, 12));
		textoNome.setCaretPosition(textoNome.getText().length());
		textoNome.select(0, 0);
		
		final boolean[] ignorarTecla = {false};
		textoNome.addKeyListener(new KeyAdapter() {
		    @Override
		    public void keyPressed(KeyEvent e) {
		        if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_TAB) {
		            ignorarTecla[0] = true;
		        } else {
		            ignorarTecla[0] = false;
		        }
		    }
		});

		textoNome.getDocument().addDocumentListener(new DocumentListener() {
			
		    private void verificar() {
		        String usuarioSalvo = dsj.gestorar.utilitarios.Configuracoes.usuario;
		        if (!textoNome.getText().equals(usuarioSalvo)) {
		            lembrarSenha.setSelected(false);
		        }
		    }

		    @Override
		    public void insertUpdate(DocumentEvent e) {
		        verificar();
		    }

		    @Override
		    public void removeUpdate(DocumentEvent e) {
		        verificar();
		    }

		    @Override
		    public void changedUpdate(DocumentEvent e) {
		        verificar();
		    }
		});
		
		
		popup = new JLabel("Sem espaços!");
        popup.setOpaque(true);
        popup.setBackground(new Color(255, 220, 220));
        popup.setForeground(Color.RED);
        popup.setBorder(BorderFactory.createLineBorder(Color.RED));
        popup.setVisible(false);
        popup.setBounds(120, 135, 100, 25);
        painelPrincipal.add(popup);
		
		((AbstractDocument) textoNome.getDocument()).setDocumentFilter(new DocumentFilter() {
			
			
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
					throws BadLocationException {
				
				if(string != null && string.contains(" ")) {
					
					mostraPopup();
					
				}else {
					
					super.insertString(fb, offset, string, attr);
				}
				
			}
			
			
			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException {
				
				if(text != null && text.contains(" ")) {
					
					
					mostraPopup();
					
					
					
				}else {
					
					super.replace(fb, offset, length, text, attrs);
					
				}

			}
		});
		painelPrincipal.add(textoNome);
		textoNome.setColumns(10);
		
		campoSenha = new JPasswordField();
		campoSenha.setBounds(120, 135, 288, 25);
		campoSenha.setFont(new Font("Segoe UI", Font.BOLD, 14));
		painelPrincipal.add(campoSenha);
		
		JButton botaoLogin = new JButton("Entrar");
		botaoLogin.setFont(new Font("Segoe UI", Font.BOLD, 12));
		botaoLogin.setBounds(241, 171, 167, 35);
		botaoLogin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String textoNomeUsuario = null;
		
				textoNomeUsuario = dsj.gestorar.utilitarios.Configuracoes.usuario;
				textoNomeUsuario = textoNomeUsuario.replace(" ", "");
				
				if(!textoNomeUsuario.equals(textoNome.getText())) {
					
					mesmoUsuario = false;
					
				}else {
					
					mesmoUsuario = true;
				}
				
				String hash = null;
				
				if(lembrarSenha.isSelected() && !loginPrimario && mesmoUsuario) {
					
					String lembra = String.valueOf(campoSenha.getPassword());
					
					if(lembra.equals("LEMBRA")) {
						
						textoNomeUsuario = dsj.gestorar.utilitarios.Configuracoes.usuario;
						hash = dsj.gestorar.utilitarios.Configuracoes.lembra;
						
						try(UsuarioDAO uDAO = new UsuarioDAO()){
							
							Usuario u = null;
							
							if(hash != null) {
								
								u = uDAO.verificarLoginSalvo(textoNomeUsuario, hash);
								
							}else {
								
								JOptionPane.showMessageDialog(frmAcessarSistemaDe,
										"Erro ao pegar senha",
										"Erro",
										JOptionPane.ERROR_MESSAGE);
								
							}
							
							if(u != null) {
								
								
								new X(u, ar).abrirTela();
								fechaTela();
								
							}else {
								
								JOptionPane.showMessageDialog(frmAcessarSistemaDe, "Usuário não cadastrado", "Aviso!", JOptionPane.INFORMATION_MESSAGE);
								
							}
							
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(frmAcessarSistemaDe, "Erro ao acessar dados.", "Erro!", JOptionPane.ERROR_MESSAGE);
							e1.printStackTrace();
						}
					}
					
				}else {
					
					textoNomeUsuario = textoNome.getText();
					textoNomeUsuario = textoNomeUsuario.replace(" ", "");
					
					
					try(UsuarioDAO uDAO = new UsuarioDAO()){
						
						Usuario u = uDAO.verificarLogin(textoNomeUsuario, campoSenha.getPassword());
						
						if(u != null) {
							System.out.printf("%d - %s - %S", u.getCodigoTitulo(), u.getNome(), u.getSenha());
							if(loginPrimario && lembrarSenha.isSelected()) {

								dsj.gestorar.utilitarios.Configuracoes.atualizarPropriedade("usuario", textoNomeUsuario);
								
								try {
									
									hash = u.getHash();
									dsj.gestorar.utilitarios.Configuracoes.atualizarPropriedade("lembra", hash);
									
								} catch (Exception e1) {
									
									e1.printStackTrace();
								}
							}
							
							new X(u).abrirTela();
							fechaTela();
							
						}else {
							JOptionPane.showMessageDialog(frmAcessarSistemaDe, "Usuário não cadastrado", "Aviso!", JOptionPane.INFORMATION_MESSAGE);
							
						}
						
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(frmAcessarSistemaDe, "Erro ao acessar dados.", "Erro!", JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
					
				}
				
				
				
			}
		});
		painelPrincipal.add(botaoLogin);
		
		lembrarSenha = new JCheckBox("Lembrar senha");
		lembrarSenha.setForeground(new Color(255, 255, 255));
		lembrarSenha.setBackground(new Color(0, 64, 128));
		lembrarSenha.setHorizontalAlignment(SwingConstants.CENTER);
		lembrarSenha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lembrarSenha.setBounds(120, 169, 115, 38);
		painelPrincipal.add(lembrarSenha);
		
		JLabel lInformativo = new JLabel("Gestor de Autoridade de Registro");
		lInformativo.setForeground(new Color(255, 255, 255));
		lInformativo.setHorizontalAlignment(SwingConstants.CENTER);
		lInformativo.setFont(new Font("Segoe UI Black", Font.BOLD, 16));
		lInformativo.setBounds(65, 11, 400, 45);
		painelPrincipal.add(lInformativo);
		frmAcessarSistemaDe.setBounds(100, 100, 566, 323);
		frmAcessarSistemaDe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAcessarSistemaDe.repaint();
		frmAcessarSistemaDe.getContentPane().add(painelPrincipal);
		
		adicionarEnterComoTab(textoNome, campoSenha);
		adicionarEnterComoTab(campoSenha, botaoLogin);
		
		JButton botaoLimpaDados = new JButton();
		botaoLimpaDados.setFont(new Font("Segoe UI", Font.BOLD, 12));
		botaoLimpaDados.setBounds(410, 135, 21, 25);
		botaoLimpaDados.setIcon(Icones.obterImagem("iconeLixeira.png"));
		botaoLimpaDados.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				Arquivos.deletarArquivo(Verificacoes.diretorioArquivoCOnfiguracao.toString());
				JOptionPane.showMessageDialog(frmAcessarSistemaDe, "Dados de login apagados !");
				lembrarSenha.setSelected(false);
				Configuracoes.criarArquivoPadrao();
				
			}
		});
		painelPrincipal.add(botaoLimpaDados);
		
		frmAcessarSistemaDe.setLocationRelativeTo(null);
		frmAcessarSistemaDe.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{textoNome, campoSenha, lembrarSenha, botaoLogin, botaoSair, botaoLimpaDados}));
		
		carregarDados();
	}
	
	public void abreTela() {
		
		frmAcessarSistemaDe.setVisible(true);
		
	}
	
	public void fechaTela() {
		
		frmAcessarSistemaDe.setVisible(false);
		frmAcessarSistemaDe.dispose();
		
	}
	
	private void adicionarEnterComoTab(JComponent componente, JComponent proximo) {
		
	    componente.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ENTER"), "moverFoco");
	    componente.getActionMap().put("moverFoco", new AbstractAction() {
	    	
			private static final long serialVersionUID = 1L;
			
			@Override
	        public void actionPerformed(java.awt.event.ActionEvent e) {
	            proximo.requestFocusInWindow();
	        }
	    });
	}
	
	private void mostraPopup() {
		
		popup.setVisible(true);
		Timer tempo = new Timer(2000, e -> popup.setVisible(false));
		tempo.setRepeats(false);
		tempo.start();
		
	}
}
