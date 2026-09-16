package dsj.gestorar.visual.formularios;

import java.awt.Component;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import dsj.gestorar.modelo.Ar;
import dsj.gestorar.modelo.Ativo;
import dsj.gestorar.modelo.Desativacao;
import dsj.gestorar.modelo.Gestor;
import dsj.gestorar.modelo.Maquina;
import dsj.gestorar.modelo.TituloUsuarios;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.persistencia.ArDAO;
import dsj.gestorar.persistencia.AtivoDAO;
import dsj.gestorar.persistencia.DesativacaoDAO;
import dsj.gestorar.persistencia.GestorDAO;
import dsj.gestorar.persistencia.MaquinaDAO;
import dsj.gestorar.visual.buscas.BuscaAtivos;
import dsj.gestorar.visual.buscas.BuscaPendenciaAtivo;
import dsj.gestorar.visual.complemento.AtivosC;

import javax.swing.JComboBox;
import java.awt.Color;
import java.awt.Window.Type;

public class FormularioAtivo {

	private JFrame frame;
	private JTextField tCodigo;
	private JTextField tDataVinculacao;
	private JButton bVincular;
	private JButton bVoltar;
	private JButton btnDesativar;
	private JComboBox<Maquina> cbMaquina;
	private JComboBox<Ar> cbAr;
	private JComboBox<Gestor> cbGestor;
	private AtivosC ac;
	private String tipo;
	private Maquina maquinaSelecionada;
	private Ar arSelecionada;
	private Gestor gestorSelecionado;
	private JTextField tPa;
	private Usuario usuario;
	private Maquina maquina;
	private Ativo ativo;
	private TituloUsuarios permissoes;

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public JFrame getFrame() {

		return frame;

	}

	public FormularioAtivo(Usuario usuario, Maquina maquina, TituloUsuarios permissoes) {
		this.maquina = maquina;
		this.usuario = usuario;
		this.permissoes = permissoes;
		initialize();
		carregaDadosPendencia();
	}

	public FormularioAtivo(Usuario usuario, Ativo ativo, TituloUsuarios permissoes) {
		this.ativo = ativo;
		this.usuario = usuario;
		this.permissoes = permissoes;
		initialize();
		carregarDados();
	}

	private void initialize() {

		frame = new JFrame();
		frame.setType(Type.UTILITY);
		frame.getContentPane().setBackground(new Color(0, 0, 0));
		frame.setBounds(100, 100, 750, 450);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);

		JPanel pPrincipal = new JPanel();
		springLayout.putConstraint(SpringLayout.SOUTH, pPrincipal, 401, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, pPrincipal, 724, SpringLayout.WEST, frame.getContentPane());
		pPrincipal.setBackground(new Color(0, 64, 128));
		springLayout.putConstraint(SpringLayout.NORTH, pPrincipal, 10, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, pPrincipal, 10, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(pPrincipal);
		pPrincipal.setLayout(null);

		JPanel pTitulo = new JPanel();
		pTitulo.setBounds(10, 10, 671, 38);
		pTitulo.setBackground(new Color(0, 64, 128));
		pPrincipal.add(pTitulo);
		SpringLayout sl_pTitulo = new SpringLayout();
		pTitulo.setLayout(sl_pTitulo);

		JPanel pForm = new JPanel();
		pForm.setBounds(10, 66, 694, 245);
		pForm.setBackground(new Color(0, 64, 128));
		pPrincipal.add(pForm);

		JPanel pBotoes = new JPanel();
		pBotoes.setBounds(10, 318, 694, 62);
		pBotoes.setBackground(new Color(0, 64, 128));
		pForm.setLayout(null);

		JPanel pCodigo = new JPanel();
		pCodigo.setBounds(10, 10, 674, 224);
		pCodigo.setBackground(new Color(0, 64, 128));
		pForm.add(pCodigo);
		pCodigo.setLayout(null);

		tCodigo = new JTextField();
		tCodigo.setBounds(615, 193, 49, 20);
		tCodigo.setEditable(false);
		tCodigo.setEnabled(false);
		tCodigo.setFont(new Font("Arial", Font.BOLD, 12));
		pCodigo.add(tCodigo);
		tCodigo.setColumns(15);

		JLabel lAr = new JLabel("AR");
		lAr.setHorizontalAlignment(SwingConstants.CENTER);
		lAr.setBounds(10, 10, 140, 20);
		lAr.setForeground(new Color(255, 255, 255));
		lAr.setFont(new Font("Arial", Font.BOLD, 12));
		pCodigo.add(lAr);

		ac = new AtivosC();

		try (ArDAO aDAO = new ArDAO()) {

			cbAr = new JComboBox<Ar>();
			cbAr.setFont(new Font("Arial", Font.BOLD, 12));
			cbAr.setBounds(160, 10, 250, 20);
			cbAr.setModel(ac.modeloArs(aDAO.listaGestores()));
			cbAr.setRenderer(new DefaultListCellRenderer() {

				private static final long serialVersionUID = 1L;

				@Override
				public Component getListCellRendererComponent(JList<?> list, Object value, int index,
						boolean isSelected, boolean cellHasFocus) {
					super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					if (value instanceof Ar) {
						Ar ar = (Ar) value;
						setText(ar.getNome());
					}
					return this;
				}

			});

		} catch (Exception e) {

			System.out.println(e);

		}

		pCodigo.add(cbAr);

		JLabel lMaquina = new JLabel("Maquina");
		lMaquina.setHorizontalAlignment(SwingConstants.CENTER);
		lMaquina.setBounds(10, 40, 140, 20);
		lMaquina.setForeground(new Color(255, 255, 255));
		lMaquina.setFont(new Font("Arial", Font.BOLD, 12));
		pCodigo.add(lMaquina);

		try (MaquinaDAO mDAO = new MaquinaDAO()) {

			cbMaquina = new JComboBox<Maquina>();
			cbMaquina.setFont(new Font("Arial", Font.BOLD, 12));
			cbMaquina.setBounds(160, 40, 250, 20);
			cbMaquina.setModel(ac.modeloMaquina(mDAO.listaMaquinas("Todas")));
			cbMaquina.setRenderer(new DefaultListCellRenderer() {

				private static final long serialVersionUID = 1L;

				@Override
				public Component getListCellRendererComponent(JList<?> list, Object value, int index,
						boolean isSelected, boolean cellHasFocus) {
					super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					if (value instanceof Maquina) {
						Maquina maquina = (Maquina) value;
						setText(maquina.getNome());
					}
					return this;
				}

			});

		} catch (Exception e) {

			System.out.println(e);

		}

		pCodigo.add(cbMaquina);

		JLabel lGestor = new JLabel("Gestor");
		lGestor.setHorizontalAlignment(SwingConstants.CENTER);
		lGestor.setBounds(10, 70, 140, 20);
		lGestor.setForeground(new Color(255, 255, 255));
		lGestor.setFont(new Font("Arial", Font.BOLD, 12));
		pCodigo.add(lGestor);

		try (GestorDAO gDAO = new GestorDAO()) {

			cbGestor = new JComboBox<Gestor>();
			cbGestor.setFont(new Font("Arial", Font.BOLD, 12));
			cbGestor.setBounds(160, 70, 250, 20);
			cbGestor.setModel(ac.modeloGestores(gDAO.listaGestores()));
			cbGestor.setRenderer(new DefaultListCellRenderer() {

				private static final long serialVersionUID = 1L;

				@Override
				public Component getListCellRendererComponent(JList<?> list, Object value, int index,
						boolean isSelected, boolean cellHasFocus) {
					super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					if (value instanceof Gestor) {
						Gestor gestor = (Gestor) value;
						setText(gestor.getNome());
					}
					return this;
				}

			});
			pCodigo.add(cbGestor);

		} catch (Exception e) {

			System.out.println(e);

		}

		JLabel lblDataVinculao = new JLabel("Data Vinculação");
		lblDataVinculao.setHorizontalAlignment(SwingConstants.CENTER);
		lblDataVinculao.setBounds(10, 130, 140, 20);
		lblDataVinculao.setForeground(new Color(255, 255, 255));
		lblDataVinculao.setFont(new Font("Arial", Font.BOLD, 12));
		pCodigo.add(lblDataVinculao);

		tDataVinculacao = new JTextField();
		tDataVinculacao.setBounds(160, 130, 100, 20);
		tDataVinculacao.setFont(new Font("Arial", Font.BOLD, 12));
		tDataVinculacao.setEnabled(false);
		tDataVinculacao.setEditable(false);
		tDataVinculacao.setColumns(15);
		pCodigo.add(tDataVinculacao);

		JLabel lPonto = new JLabel("Ponto de Atendimento");
		lPonto.setHorizontalAlignment(SwingConstants.CENTER);
		lPonto.setForeground(Color.WHITE);
		lPonto.setFont(new Font("Arial", Font.BOLD, 12));
		lPonto.setBounds(10, 100, 140, 20);
		pCodigo.add(lPonto);

		tPa = new JTextField();
		tPa.setEditable(false);
		tPa.setFont(new Font("Arial", Font.BOLD, 12));
		tPa.setBounds(160, 100, 250, 20);
		pCodigo.add(tPa);
		tPa.setColumns(10);

		JButton bLevaPonto = new JButton("...");
		bLevaPonto.setEnabled(true);
		bLevaPonto.setFont(new Font("Arial", Font.BOLD, 12));
		bLevaPonto.setBounds(420, 100, 20, 20);
		bLevaPonto.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Maquina maquina = (Maquina) cbMaquina.getSelectedItem();

				FormularioPontoAtendimento fpa = new FormularioPontoAtendimento(usuario, maquina.getPa(),
						FormularioAtivo.this.frame, FormularioAtivo.this, permissoes);

				fpa.abrirTela();

			}
		});
		pCodigo.add(bLevaPonto);

		JButton bLevaAr = new JButton("...");
		bLevaAr.setEnabled(true);
		bLevaAr.setFont(new Font("Arial", Font.BOLD, 12));
		bLevaAr.setBounds(420, 9, 20, 20);
		bLevaAr.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Ar ar = (Ar) cbAr.getSelectedItem();

				FormularioAr fa = new FormularioAr(usuario, ar, FormularioAtivo.this.frame, FormularioAtivo.this,
						permissoes);
				fa.abrirTela();

			}
		});
		pCodigo.add(bLevaAr);

		JButton bLevaMaquina = new JButton("...");
		bLevaMaquina.setEnabled(true);
		bLevaMaquina.setFont(new Font("Arial", Font.BOLD, 12));
		bLevaMaquina.setBounds(420, 39, 20, 20);
		bLevaMaquina.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Maquina maquina = (Maquina) cbMaquina.getSelectedItem();

				FormularioMaquina fm = new FormularioMaquina(usuario, maquina, FormularioAtivo.this.frame,
						FormularioAtivo.this, permissoes);
				fm.abrirTela();

			}
		});
		pCodigo.add(bLevaMaquina);

		JButton bLevarGestor = new JButton("...");
		bLevarGestor.setEnabled(true);
		bLevarGestor.setFont(new Font("Arial", Font.BOLD, 12));
		bLevarGestor.setBounds(420, 69, 20, 20);
		bLevarGestor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Gestor gestor = (Gestor) cbGestor.getSelectedItem();

				FormularioGestor fg = new FormularioGestor(usuario, permissoes, gestor, FormularioAtivo.this.frame,
						FormularioAtivo.this);

				fg.abrirTela();

			}
		});
		pCodigo.add(bLevarGestor);

		JLabel lTitulo = new JLabel("Ativo");
		lTitulo.setForeground(new Color(255, 255, 255));
		sl_pTitulo.putConstraint(SpringLayout.WEST, lTitulo, 194, SpringLayout.WEST, pTitulo);
		sl_pTitulo.putConstraint(SpringLayout.SOUTH, lTitulo, -9, SpringLayout.SOUTH, pTitulo);
		sl_pTitulo.putConstraint(SpringLayout.EAST, lTitulo, -191, SpringLayout.EAST, pTitulo);
		lTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lTitulo.setFont(new Font("Arial", Font.BOLD, 16));
		pTitulo.add(lTitulo);
		pPrincipal.add(pBotoes);

		bVoltar = new JButton("Voltar");
		bVoltar.setBounds(10, 11, 150, 50);
		bVoltar.setFont(new Font("Arial", Font.BOLD, 14));
		bVoltar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				voltar();

			}
		});
		pBotoes.add(bVoltar);

		bVincular = new JButton("Aprovar");
		bVincular.setBounds(170, 11, 150, 50);
		bVincular.setFont(new Font("Arial", Font.BOLD, 14));
		bVincular.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (permissoes.getPolitica().podeAprovarAtivo()) {

					Ar ar = (Ar) cbAr.getSelectedItem();
					Gestor gestor = (Gestor) cbGestor.getSelectedItem();
					Maquina maquina = (Maquina) cbMaquina.getSelectedItem();
					Ativo ativo = new Ativo(gestor, maquina, ar, tDataVinculacao.getText());

					try (AtivoDAO atDAO = new AtivoDAO()) {
						if (atDAO.verificaPossibilidadeAtivacao(maquina.getNome())) {
							JOptionPane.showMessageDialog(null, "Já há uma maquina com esse HOSTNAME habilitada",
									"Aviso", JOptionPane.INFORMATION_MESSAGE);
						} else {
							if (atDAO.grava(ativo, usuario)) {
								if (atDAO.alterarMAquinaPAtiva(maquina.getCodigo(), usuario)) {
									JOptionPane.showMessageDialog(FormularioAtivo.this.frame, "Vinculado com sucesso",
											"Sucesso!", JOptionPane.INFORMATION_MESSAGE);
									voltar();
								} else {
									JOptionPane.showMessageDialog(FormularioAtivo.this.frame,
											"Erro ao alterar situação", "Erro!", JOptionPane.ERROR_MESSAGE);
									voltar();
								}
							} else {
								JOptionPane.showMessageDialog(FormularioAtivo.this.frame, "Erro ao vincular", "Erro!",
										JOptionPane.ERROR_MESSAGE);
								voltar();
							}
						}
					} catch (Exception sqle) {
						System.err.println(sqle);
					}
				} else {

					JOptionPane.showMessageDialog(frame, "Você não tem permissão para aprovar.", "Acesso negado",
							JOptionPane.WARNING_MESSAGE);

				}

			}
		});
		pBotoes.setLayout(null);
		pBotoes.add(bVincular);

		bVoltar = new JButton("Voltar");
		bVoltar.setBounds(10, 11, 150, 50);
		bVoltar.setFont(new Font("Arial", Font.BOLD, 14));
		bVoltar.addActionListener(e -> voltar());
		pBotoes.add(bVoltar);

		btnDesativar = new JButton("Desativar");
		btnDesativar.setBounds(490, 11, 150, 50);
		btnDesativar.setFont(new Font("Arial", Font.BOLD, 14));
		btnDesativar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (permissoes.getPolitica().podeDesativarAtivo()) {

					Desativacao d = new Desativacao();
					Ativo ativo = new Ativo();
					int clonarMaquina = JOptionPane.NO_OPTION;

					ativo.setCodigo(Integer.parseInt(tCodigo.getText()));
					ativo.setAr((Ar) cbAr.getSelectedItem());
					ativo.setGestor((Gestor) cbGestor.getSelectedItem());
					ativo.setMaquina((Maquina) cbMaquina.getSelectedItem());

					String motivo = null;
					while (motivo == null || motivo.trim().isEmpty()) {
						motivo = JOptionPane.showInputDialog(FormularioAtivo.this.frame,
								"Qual o motivo da desativação?", "Motivo", JOptionPane.QUESTION_MESSAGE);
						if (motivo == null)
							return;
					}

					clonarMaquina = JOptionPane.showConfirmDialog(FormularioAtivo.this.frame,
							"Deseja clonar os dados dessa máquina para um novo registro?\nIsso é ideal para desativação de uma máquina que foi formatada e terá que ser reativada com um novo DNA.",
							"Clonagem", JOptionPane.YES_NO_OPTION);

					d.setAr(ativo.getAr());
					d.setGestor(ativo.getGestor());
					d.setMaquina(ativo.getMaquina());
					d.setMotivo(motivo);

					try (DesativacaoDAO dDAO = new DesativacaoDAO()) {
						if (dDAO.aletraSituacaoMaquina(d.getMaquina().getCodigo(), usuario)) {
							if (!dDAO.grava(d, usuario)) {
								JOptionPane.showMessageDialog(FormularioAtivo.this.frame, "Erro ao gravar desativação",
										"Erro!", JOptionPane.ERROR_MESSAGE);
								return;
							}
							try (AtivoDAO atDAO = new AtivoDAO()) {
								if (atDAO.deleta(ativo, usuario)) {
									JOptionPane.showMessageDialog(FormularioAtivo.this.frame,
											"Ativo deletado com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
									if (clonarMaquina == JOptionPane.YES_OPTION) {
										d.getMaquina().setDataAtivacao(dataAtual());
										d.getMaquina().setDna("");
										d.getMaquina().setCodigo(0);
										d.getMaquina().setSituacao("PENDENTE");
										try (MaquinaDAO mDAO = new MaquinaDAO()) {
											if (mDAO.gravarMaquina(d.getMaquina(), usuario)) {
												JOptionPane.showMessageDialog(FormularioAtivo.this.frame,
														"Máquina clonada para um novo registro com sucesso!",
														"Sucesso!", JOptionPane.INFORMATION_MESSAGE);
											} else {
												JOptionPane.showMessageDialog(FormularioAtivo.this.frame,
														"Erro ao clonar a máquina para um novo registro", "Erro!",
														JOptionPane.ERROR_MESSAGE);
											}
										}
									}
									voltar();
								} else {
									JOptionPane.showMessageDialog(FormularioAtivo.this.frame, "Erro ao deletar ativo!",
											"Erro!", JOptionPane.ERROR_MESSAGE);
								}
							}
						} else {
							JOptionPane.showMessageDialog(FormularioAtivo.this.frame,
									"Erro ao alterar situação da máquina.", "Erro!", JOptionPane.ERROR_MESSAGE);
						}
					} catch (Exception ex) {
						System.err.println("Erro geral: " + ex);
					}

				} else {

					JOptionPane.showMessageDialog(frame, "Você não tem permissão para desativar maquinas.",
							"Acesso negado", JOptionPane.WARNING_MESSAGE);

				}
			}

		});
		pBotoes.add(btnDesativar);

	}

	public void carregarDados() {

		abrirTela();
		tCodigo.setText(String.valueOf(ativo.getCodigo()));
		tDataVinculacao.setText(ativo.getDataVinculacao());
		bVincular.setVisible(false);
		tDataVinculacao.setEnabled(false);

		Maquina maquina;
		Ar ar;
		Gestor gestor;

		try {

			try (MaquinaDAO mDAO = new MaquinaDAO()) {

				maquina = mDAO.carregaMaquina(ativo.getMaquina().getCodigo());

			}

			for (int i = 0; i < cbMaquina.getItemCount(); i++) {

				Maquina x = new Maquina();

				x = (Maquina) cbMaquina.getItemAt(i);

				if (x.getCodigo() == maquina.getCodigo()) {

					maquinaSelecionada = x;
				}
			}

			try (ArDAO aDAO = new ArDAO()) {

				ar = aDAO.buscaAr(ativo.getAr().getCodigo());

			}

			for (int i = 0; i < cbAr.getItemCount(); i++) {

				Ar x = new Ar();

				x = (Ar) cbAr.getItemAt(i);

				if (x.getCodigo() == ar.getCodigo()) {

					arSelecionada = ar;
				}
			}

			try (GestorDAO gDAO = new GestorDAO()) {

				gestor = gDAO.buscaGestor(ativo.getGestor().getCodigo());

			}

			for (int i = 0; i < cbGestor.getItemCount(); i++) {

				Gestor x = new Gestor();

				x = (Gestor) cbGestor.getItemAt(i);

				if (x.getCodigo() == gestor.getCodigo()) {

					gestorSelecionado = x;
				}
			}

		} catch (Exception e) {
			System.err.println(e);
		}

		tPa.setText(maquinaSelecionada.getPa().getApelido());

		setTipo("ativo");

		cbAr.setSelectedItem(arSelecionada);
		cbAr.setEnabled(false);

		cbMaquina.setSelectedItem(maquinaSelecionada);
		cbMaquina.setEnabled(false);

		cbGestor.setSelectedItem(gestorSelecionado);
		cbGestor.setEnabled(false);

	}

	public void carregaDadosPendencia() {

		try (MaquinaDAO mDAO = new MaquinaDAO()) {

			maquina = mDAO.carregaMaquina(maquina.getCodigo());

		} catch (Exception e) {

			System.err.println(e);

		}

		for (int i = 0; i < cbMaquina.getItemCount(); i++) {

			Maquina x = new Maquina();

			x = (Maquina) cbMaquina.getItemAt(i);

			if (x.getCodigo() == maquina.getCodigo()) {

				maquinaSelecionada = x;
			}
		}

		cbMaquina.setSelectedItem(maquinaSelecionada);
		cbMaquina.setEnabled(false);

		tDataVinculacao.setText(dataAtual());
		tDataVinculacao.setEditable(false);
		btnDesativar.setVisible(false);

		tPa.setText(maquinaSelecionada.getPa().getApelido());

		setTipo("pendencia");
		abrirTela();

	}

	public void voltar() {

		if (getTipo() == "ativo") {

			BuscaAtivos ba = new BuscaAtivos(usuario, permissoes);

			ba.abrirTela();

			fechaTela();

		} else if (getTipo() == "pendencia") {

			BuscaPendenciaAtivo pa = new BuscaPendenciaAtivo(usuario, permissoes);

			pa.abrirTela();

			fechaTela();

		}

	}

	public void abrirTela() {

		frame.setVisible(true);

	}

	public void fechaTela() {

		frame.dispose();

	}

	public String dataAtual() {

		Date dataCompleta = new Date();

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

		return sdf.format(dataCompleta);

	}
}
