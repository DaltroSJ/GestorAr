package dsj.gestorar.visual.formularios;

import javax.swing.*;

import dsj.gestorar.modelo.Ar;
import dsj.gestorar.modelo.TipoCadastroUsuario;
import dsj.gestorar.modelo.TituloUsuarios;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.persistencia.UsuarioDAO;
import dsj.gestorar.visual.Login;
import dsj.gestorar.visual.X;
import dsj.gestorar.visual.buscas.BuscaUsuarios;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormularioUsuario {

	private JDialog frmAlteraoDeUsurio;
	private JTextField textoCodigo;
	private JTextField textoNome;
	private JPasswordField campoSenha;
	private Usuario usuario;
	private Usuario usuario2;
	private JTextField textoAr;
	private TipoCadastroUsuario tipoCadastro;
	private JComboBox<TituloUsuarios> cbTipos;
	private DefaultComboBoxModel<TituloUsuarios> modeloCB = new DefaultComboBoxModel<>();
	private Ar ar;
	private TituloUsuarios permissoes;
	private JButton btnNovoUsuario;
	private JButton btnExcluir;

	public FormularioUsuario(Usuario usuario, TipoCadastroUsuario tipoCadastro, TituloUsuarios permissoes) {
		this.usuario = usuario;
		this.ar = usuario.getAr();
		this.tipoCadastro = tipoCadastro;
		this.permissoes = permissoes;
		initialize();
	}

	public FormularioUsuario(Usuario usuario1, Usuario usuario2, TipoCadastroUsuario tipoCadastro,
			TituloUsuarios permissoes) {
		this.usuario = usuario1;
		this.usuario2 = usuario2;
		this.ar = usuario.getAr();
		this.tipoCadastro = tipoCadastro;
		this.permissoes = permissoes;
		initialize();
		if(usuario1 == usuario2) {
			btnExcluir.setEnabled(false);
		}
	}

	public FormularioUsuario(Ar ar, TipoCadastroUsuario tipoCadastro, TituloUsuarios permissoes) {
		this.ar = ar;
		this.tipoCadastro = tipoCadastro;
		this.permissoes = permissoes;
		initialize();

	}

	private void initialize() {

		frmAlteraoDeUsurio = new JDialog();
		frmAlteraoDeUsurio.setModal(true);
		frmAlteraoDeUsurio.setResizable(false);
		frmAlteraoDeUsurio.setTitle("Usuário");
		frmAlteraoDeUsurio.setType(Window.Type.UTILITY);
		frmAlteraoDeUsurio.getContentPane().setBackground(Color.BLACK);
		frmAlteraoDeUsurio.getContentPane().setFont(new Font("Arial", Font.BOLD, 12));
		frmAlteraoDeUsurio.getContentPane().setLayout(null);

		JPanel painelPrincipal = new JPanel();
		painelPrincipal.setBackground(new Color(0, 64, 128));
		painelPrincipal.setBounds(10, 11, 513, 290);
		frmAlteraoDeUsurio.getContentPane().add(painelPrincipal);
		painelPrincipal.setLayout(null);

		JLabel labelInformativo = new JLabel("Dados do usuário");
		labelInformativo.setHorizontalAlignment(SwingConstants.CENTER);
		labelInformativo.setForeground(SystemColor.text);
		labelInformativo.setFont(new Font("Arial", Font.BOLD, 16));
		labelInformativo.setBounds(10, 11, 497, 20);
		painelPrincipal.add(labelInformativo);

		textoCodigo = new JTextField();
		textoCodigo.setEditable(false);
		textoCodigo.setEnabled(false);
		textoCodigo.setFont(new Font("Arial", Font.BOLD, 12));
		textoCodigo.setBounds(105, 82, 46, 25);
		painelPrincipal.add(textoCodigo);
		textoCodigo.setColumns(10);

		JLabel labelCodigo = new JLabel("Código");
		labelCodigo.setForeground(SystemColor.text);
		labelCodigo.setFont(new Font("Arial", Font.BOLD, 12));
		labelCodigo.setBounds(20, 82, 75, 25);
		painelPrincipal.add(labelCodigo);

		JLabel labelNome = new JLabel("Nome");
		labelNome.setForeground(SystemColor.text);
		labelNome.setFont(new Font("Arial", Font.BOLD, 12));
		labelNome.setBounds(20, 130, 75, 25);
		painelPrincipal.add(labelNome);

		JLabel lblSenha = new JLabel("Senha");
		lblSenha.setForeground(SystemColor.text);
		lblSenha.setFont(new Font("Arial", Font.BOLD, 12));
		lblSenha.setBounds(20, 180, 75, 25);
		painelPrincipal.add(lblSenha);

		textoNome = new JTextField();
		textoNome.setFont(new Font("Arial", Font.BOLD, 12));
		textoNome.setBounds(105, 130, 225, 25);
		painelPrincipal.add(textoNome);

		campoSenha = new JPasswordField();
		campoSenha.setFont(new Font("Arial", Font.BOLD, 14));
		campoSenha.setBounds(105, 181, 225, 25);
		painelPrincipal.add(campoSenha);

		textoAr = new JTextField(ar.getNome());
		textoAr.setFont(new Font("Arial", Font.BOLD, 12));
		textoAr.setEnabled(false);
		textoAr.setEditable(false);
		textoAr.setBounds(161, 42, 169, 25);
		painelPrincipal.add(textoAr);

		JLabel labelAr = new JLabel("Autoridade de registro");
		labelAr.setForeground(SystemColor.text);
		labelAr.setFont(new Font("Arial", Font.BOLD, 12));
		labelAr.setBounds(20, 42, 131, 25);
		painelPrincipal.add(labelAr);

		JLabel lTitulo = new JLabel("Titulo");
		lTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lTitulo.setForeground(SystemColor.text);
		lTitulo.setFont(new Font("Arial", Font.BOLD, 12));
		lTitulo.setBounds(338, 41, 166, 25);
		painelPrincipal.add(lTitulo);

		for (TituloUsuarios x : TituloUsuarios.values())
			modeloCB.addElement(x);
		cbTipos = new JComboBox<>(modeloCB);
		cbTipos.setBounds(348, 72, 155, 25);
		painelPrincipal.add(cbTipos);

		JButton botaoSalvar = new JButton("Salvar");
		botaoSalvar.setFont(new Font("Arial", Font.BOLD, 12));
		botaoSalvar.setBounds(393, 239, 110, 40);
		botaoSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	
				Usuario u = construirUsuario(textoNome.getText(), String.valueOf(campoSenha.getPassword()));

				if(u.getNome() == null || u.getNome().isEmpty()) {
					
					JOptionPane.showMessageDialog(frmAlteraoDeUsurio,
							"O nome de usuario não pode ser em branco !",
							"Verifique os dados", JOptionPane.WARNING_MESSAGE);
					
					return;
					
				}
				
				if(u.getSenha() == null || u.getSenha().isEmpty()) {
					
					JOptionPane.showMessageDialog(frmAlteraoDeUsurio,
							"A senha não pode ficar em branco! ",
							"Verifique os dados", JOptionPane.WARNING_MESSAGE);
					
					return;
					
				}
				
				try (UsuarioDAO uDAO = new UsuarioDAO()) {
					
					if(tipoCadastro == TipoCadastroUsuario.NOVO && uDAO.verificaSeJaExisteUsuarioComNome(u.getNome())) {
						
						JOptionPane.showMessageDialog(frmAlteraoDeUsurio,
								"Esse nome de usuario já consta na base de dados.\nPor favor escolha outro!",
								"Verifique os dados", JOptionPane.WARNING_MESSAGE);
						
						return;
						
					}
					
					boolean sucesso = false;
					boolean senhaAlterada = false;

					if (!u.getSenha().equals("SENHA")) {

						senhaAlterada = true;

					}

					switch (tipoCadastro) {

					case ALETRACAO: {

						sucesso = uDAO.alterarDados(u, usuario, senhaAlterada);
						break;

					}
					case ALTERACAO_ADMINISTRADOR: {

						sucesso = uDAO.alterarDados(u, usuario, senhaAlterada);
						break;

					}
					case NOVO: {

						sucesso = uDAO.cadastrarUsuario(u, usuario);
						break;
					}
					case SEM_USUARIO: {
						sucesso = uDAO.cadastrarUsuario(u, usuario);
						break;
					}

					}

					if (sucesso) {

						if (tipoCadastro == TipoCadastroUsuario.SEM_USUARIO) {
							
							JOptionPane.showMessageDialog(frmAlteraoDeUsurio,
									 "Usuário cadastrado, vc efetuará login no sistema a seguir",
									"Sucesso! ", JOptionPane.INFORMATION_MESSAGE);
							
							new Login(ar).abreTela();
							
							fechaTela();
							
						}else if(tipoCadastro == TipoCadastroUsuario.ALETRACAO) {
							
							JOptionPane.showMessageDialog(frmAlteraoDeUsurio,
									"Dados alterados com sucesso!",
									"Sucesso!", JOptionPane.INFORMATION_MESSAGE);
							campoSenha.setText("SENHA");
							return;
							
						}else if(tipoCadastro == TipoCadastroUsuario.ALTERACAO_ADMINISTRADOR || tipoCadastro == TipoCadastroUsuario.NOVO){
							
							JOptionPane.showMessageDialog(frmAlteraoDeUsurio,
									tipoCadastro == TipoCadastroUsuario.ALTERACAO_ADMINISTRADOR ? "Dados alterados com sucesso!"
											: "Novo usuário cadastrado com sucesso!",
									"Sucesso!", JOptionPane.INFORMATION_MESSAGE);
							
							BuscaUsuarios bu =  new BuscaUsuarios(usuario, permissoes);
							bu.atualizaTabela();
							bu.abreTela();
							fechaTela();
							
						}

						

					} else {

						JOptionPane.showMessageDialog(frmAlteraoDeUsurio,
								tipoCadastro == TipoCadastroUsuario.ALETRACAO ? "Erro, dados não foram alterados!"
										: "Erro, o usuário não foi cadastrado!",
								"Erro", JOptionPane.ERROR_MESSAGE);

					}
				} catch (Exception ex) {

					JOptionPane.showMessageDialog(frmAlteraoDeUsurio, "Ocorreu um erro: " + ex.getMessage(), "Erro",
							JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();

				}
			}
		});
		painelPrincipal.add(botaoSalvar);

		JButton btnSair = new JButton("Voltar");
		btnSair.setFont(new Font("Arial", Font.BOLD, 12));
		btnSair.setBounds(10, 239, 110, 40);
		btnSair.addActionListener(e -> {
			
			switch (tipoCadastro) {

			case ALETRACAO: {

				new X(usuario).abrirTela();
				break;

			}
			case ALTERACAO_ADMINISTRADOR: {

				new BuscaUsuarios(usuario, permissoes).abreTela();
				break;
				
			}
			case NOVO: {

				new BuscaUsuarios(usuario, permissoes).abreTela();
				break;
			}
			default:
				break;

			}
			
			fechaTela();

		});
		painelPrincipal.add(btnSair);

		btnExcluir = new JButton("Excluir");
		btnExcluir.setFont(new Font("Arial", Font.BOLD, 12));
		btnExcluir.setBounds(130, 239, 110, 40);
		btnExcluir.setVisible(false);
		btnExcluir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try(UsuarioDAO uDAO = new UsuarioDAO()){
					
					if(uDAO.apagarUsuario(Integer.parseInt(textoCodigo.getText()))) {
					
						JOptionPane.showMessageDialog(frmAlteraoDeUsurio,
								"Usuario deleta com sucesso !",
								"Sucesso!", JOptionPane.INFORMATION_MESSAGE);

						new BuscaUsuarios(usuario, permissoes).abreTela();
						fechaTela();

					} else {

						JOptionPane.showMessageDialog(frmAlteraoDeUsurio,
								"Erro ao tentar excluir usuario! ",
								"Erro", JOptionPane.ERROR_MESSAGE);

					}
						
					
					
				}catch(Exception ee) {
					
					System.err.println(ee);
					
				}
				
				
			}
		});
		painelPrincipal.add(btnExcluir);

		btnNovoUsuario = new JButton("Novo Usuario");
		btnNovoUsuario.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnNovoUsuario.setBounds(273, 239, 110, 40);
		btnNovoUsuario.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				tipoCadastro = TipoCadastroUsuario.NOVO;
				
				textoCodigo.setText("0");
				textoNome.setText("");
				campoSenha.setText("");
				cbTipos.setSelectedIndex(0);
				textoAr.setText(ar.getNome());
			}
		});
		painelPrincipal.add(btnNovoUsuario);

		frmAlteraoDeUsurio.setBounds(100, 100, 550, 350);
		frmAlteraoDeUsurio.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmAlteraoDeUsurio.setLocationRelativeTo(null);
		deixaFormularioCerto();

		if (permissoes == TituloUsuarios.ADMINISTRADOR) {

			btnExcluir.setVisible(true);
			btnNovoUsuario.setVisible(true);

		}
		
		if(tipoCadastro == TipoCadastroUsuario.NOVO) {
			
			btnSair.setEnabled(false);
			
		}

		campoSenha.setText("SENHA");

	}

	private Usuario construirUsuario(String nome, String senha) {
		TituloUsuarios titulo = cbTipos.getItemAt(cbTipos.getSelectedIndex());
		if (tipoCadastro == TipoCadastroUsuario.SEM_USUARIO || tipoCadastro == TipoCadastroUsuario.NOVO) {

			return new Usuario(0, nome, senha, "", "", titulo.getCodigo(), ar);

		} else if (tipoCadastro == TipoCadastroUsuario.ALETRACAO) {

			return new Usuario(usuario.getCodigo(), nome, senha, usuario.getSal(), usuario.getHash(),
					usuario.getCodigoTitulo(), ar);

		} else if (tipoCadastro == TipoCadastroUsuario.ALTERACAO_ADMINISTRADOR) {

			return new Usuario(usuario2.getCodigo(), nome, senha, usuario2.getSal(), "", titulo.getCodigo(), ar);

		}
		return null;
	}

	public void fechaTela() {
		frmAlteraoDeUsurio.setVisible(false);
		frmAlteraoDeUsurio.dispose();
	}

	public void abrirTela() {
		frmAlteraoDeUsurio.setVisible(true);
	}

	public void deixaFormularioCerto() {
		switch (tipoCadastro.codigo) {
		case 1:
			cbTipos.setEnabled(true);
			cbTipos.setSelectedIndex(0);
			textoCodigo.setText("");
			textoNome.setText("");
			break;
		case 2:
			cbTipos.setEnabled(false);
			cbTipos.setSelectedItem(TituloUsuarios.ADMINISTRADOR);
			textoCodigo.setText("");
			textoNome.setText("");
			break;
		case 3:
			cbTipos.setEnabled(usuario.getCodigoTitulo() == TituloUsuarios.ADMINISTRADOR.getCodigo());
			cbTipos.setSelectedItem(TituloUsuarios.fromCodigo(usuario.getCodigoTitulo()));
			textoCodigo.setText(String.valueOf(usuario.getCodigo()));
			textoNome.setText(usuario.getNome());
			break;
		case 4:
			cbTipos.setEnabled(true);
			cbTipos.setSelectedItem(TituloUsuarios.fromCodigo(usuario2.getCodigoTitulo()));
			textoCodigo.setText(String.valueOf(usuario2.getCodigo()));
			textoNome.setText(usuario2.getNome());
			break;
		}
		textoAr.setText(ar.getNome());
	}
}