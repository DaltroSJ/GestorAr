package dsj.gestorar.visual.formularios;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import dsj.gestorar.modelo.Maquina;
import dsj.gestorar.modelo.ModelosMaquina;
import dsj.gestorar.modelo.ModosCriptografiaMaquina;
import dsj.gestorar.modelo.PontoAtendimento;
import dsj.gestorar.modelo.Programas;
import dsj.gestorar.modelo.TituloUsuarios;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.persistencia.AtivoDAO;
import dsj.gestorar.persistencia.MaquinaDAO;
import dsj.gestorar.persistencia.PontoAtendimentoDAO;
import dsj.gestorar.persistencia.ProgramasDAO;
import dsj.gestorar.utilitarios.Mascaras;
import dsj.gestorar.visual.buscas.BuscaMaquinas;
import dsj.gestorar.visual.complemento.MaquinaC;

import java.awt.Color;
import java.awt.Component;
import java.awt.Window.Type;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;

public class FormularioMaquina {

	private JFrame frame;
	private JTextField tCodigo;
	private JTextField tProcessador;
	private JTextField tOs;
	private JTextField tNome;
	private JTextField tMemoria;
	private JTextField tImpressora;
	private JTextField tCamera;
	private JTextField tBiometria;
	private JFormattedTextField tDataAtivacao;
	private BuscaMaquinas bm;
	private MaquinaC mc;
	private Maquina maquinaSalva;
	private JTextField tSsdHdd;
	private JTextField tGrafico;
	private JTextField tMae;
	private JTextField tMac;
	private JTextField tProvedor;
	private JTextField tDna;
	private JComboBox<String> cbCriptografia;
	private JComboBox<String> cbModelo;
	private JComboBox<PontoAtendimento> cbPonto;
	private DefaultComboBoxModel<String> modosCriptografia = new DefaultComboBoxModel<>();
	private DefaultComboBoxModel<String> modelos = new DefaultComboBoxModel<>();
	private PontoAtendimento pa;
	private JButton bExcluir;
	private JTextField tPlacaRede;
	private JTextField tSituacao;
	private JTextField tNfe;
	private Usuario usuario;
	private Component cp;
	private Maquina maquina;
	private Object origem;
	private TituloUsuarios permissoes;
	private JButton btnNova;
	private JButton bSalvar;
	
	public void  setSituacaoBExcluir(boolean x) {
		this.bExcluir.setEnabled(x);
	}
	public void setSituacaoBNova(boolean x) {
		this.btnNova.setEnabled(x);
	}
	public void setSituacaoBSalvar(boolean x) {
		this.bSalvar.setEnabled(x);
	}
	
	public PontoAtendimento getPaSelecionado() {
		
		return pa;
		
	}
	
	public void setPaSelecionado(PontoAtendimento pa) {
		
		this.pa = pa;
		
	}

	public Maquina getMaquinaSalva() {
		return maquinaSalva;
	}

	public void setMaquinaSalva(Maquina maquinaSalva) {
		this.maquinaSalva = maquinaSalva;
	}

	public BuscaMaquinas getBm() {
		return bm;
	}

	public void setBm(BuscaMaquinas bm) {
		this.bm = bm;
	}

	public MaquinaC getMc() {
		return mc;
	}

	public void setMc(MaquinaC mc) {
		this.mc = mc;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public FormularioMaquina(Usuario usuario, Maquina maquina, Component componente, Object origem, TituloUsuarios permissoes) {
		
		this.origem = origem;
		this.cp = componente;
		this.maquina = maquina;
		this.usuario = usuario;
		this.permissoes = permissoes;
		initialize();
	}
	
	public FormularioMaquina(Usuario usuario, Component componente, Object origem, TituloUsuarios permissoes) {
		
		this.origem = origem;
		this.cp = componente;
		this.usuario = usuario;
		this.permissoes = permissoes;
		initialize();
	}

	private void initialize() {
		
		frame = new JFrame();
		frame.setType(Type.UTILITY);
		frame.getContentPane().setBackground(new Color(0, 0, 0));
		frame.setBounds(100, 100, 950, 573);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(cp);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		JPanel pPrincipal = new JPanel();
		springLayout.putConstraint(SpringLayout.SOUTH, pPrincipal, 524, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, pPrincipal, 924, SpringLayout.WEST, frame.getContentPane());
		pPrincipal.setBackground(new Color(0, 64, 128));
		springLayout.putConstraint(SpringLayout.NORTH, pPrincipal, 10, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, pPrincipal, 10, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(pPrincipal);
		SpringLayout sl_pPrincipal = new SpringLayout();
		pPrincipal.setLayout(sl_pPrincipal);
		
		JPanel pTitulo = new JPanel();
		sl_pPrincipal.putConstraint(SpringLayout.NORTH, pTitulo, 10, SpringLayout.NORTH, pPrincipal);
		sl_pPrincipal.putConstraint(SpringLayout.EAST, pTitulo, -10, SpringLayout.EAST, pPrincipal);
		pTitulo.setBackground(new Color(0, 64, 128));
		pPrincipal.add(pTitulo);
		
		JPanel pForm = new JPanel();
		sl_pPrincipal.putConstraint(SpringLayout.NORTH, pForm, 91, SpringLayout.NORTH, pPrincipal);
		sl_pPrincipal.putConstraint(SpringLayout.WEST, pTitulo, 0, SpringLayout.WEST, pForm);
		sl_pPrincipal.putConstraint(SpringLayout.SOUTH, pTitulo, -6, SpringLayout.NORTH, pForm);
		sl_pPrincipal.putConstraint(SpringLayout.WEST, pForm, 10, SpringLayout.WEST, pPrincipal);
		pForm.setBackground(new Color(0, 64, 128));
		pPrincipal.add(pForm);
		
		JPanel pBotoes = new JPanel();
		sl_pPrincipal.putConstraint(SpringLayout.SOUTH, pForm, -6, SpringLayout.NORTH, pBotoes);
		sl_pPrincipal.putConstraint(SpringLayout.EAST, pForm, 0, SpringLayout.EAST, pBotoes);
		sl_pPrincipal.putConstraint(SpringLayout.WEST, pBotoes, 10, SpringLayout.WEST, pPrincipal);
		sl_pPrincipal.putConstraint(SpringLayout.EAST, pBotoes, -10, SpringLayout.EAST, pPrincipal);
		sl_pPrincipal.putConstraint(SpringLayout.NORTH, pBotoes, 460, SpringLayout.NORTH, pPrincipal);
		sl_pPrincipal.putConstraint(SpringLayout.SOUTH, pBotoes, -10, SpringLayout.SOUTH, pPrincipal);
		pBotoes.setBackground(new Color(0, 64, 128));
		pForm.setLayout(null);
		
		
		ModelosMaquina[] modelos = ModelosMaquina.values();
		for(ModelosMaquina mm : modelos) {
			
			this.modelos.addElement(mm.toString());
			
		}
		
		
		ModosCriptografiaMaquina[] valoresModos = ModosCriptografiaMaquina.values();
		for(ModosCriptografiaMaquina 	m : valoresModos) {
			
			modosCriptografia.addElement(m.toString());
		}
		
		tCodigo = new JTextField();
		tCodigo.setBounds(833, 340, 50, 20);
		pForm.add(tCodigo);
		tCodigo.setEditable(false);
		tCodigo.setFont(new Font("Arial", Font.BOLD, 12));
		tCodigo.setColumns(15);
		
		JLabel lHostname = new JLabel("Hostname");
		lHostname.setHorizontalAlignment(SwingConstants.CENTER);
		lHostname.setBounds(10, 40, 121, 20);
		pForm.add(lHostname);
		lHostname.setForeground(new Color(255, 255, 255));
		lHostname.setFont(new Font("Arial", Font.BOLD, 12));
		
		tNome = new JTextField();
		tNome.setBounds(140, 40, 220, 20);
		pForm.add(tNome);
		tNome.setFont(new Font("Arial", Font.BOLD, 12));
		tNome.setColumns(10);
		
		JLabel lOs = new JLabel("Sistema Operacional");
		lOs.setHorizontalAlignment(SwingConstants.CENTER);
		lOs.setBounds(10, 70, 121, 20);
		pForm.add(lOs);
		lOs.setForeground(new Color(255, 255, 255));
		lOs.setFont(new Font("Arial", Font.BOLD, 12));
		
		tOs = new JTextField();
		tOs.setBounds(140, 70, 220, 20);
		pForm.add(tOs);
		tOs.setFont(new Font("Arial", Font.BOLD, 12));
		tOs.setColumns(10);
		
		JLabel lProcessador = new JLabel("Processador");
		lProcessador.setHorizontalAlignment(SwingConstants.CENTER);
		lProcessador.setBounds(10, 100, 121, 20);
		pForm.add(lProcessador);
		lProcessador.setForeground(new Color(255, 255, 255));
		lProcessador.setFont(new Font("Arial", Font.BOLD, 12));
		
		tProcessador = new JTextField();
		tProcessador.setBounds(140, 100, 220, 20);
		pForm.add(tProcessador);
		tProcessador.setFont(new Font("Arial", Font.BOLD, 12));
		tProcessador.setColumns(10);
		
		tMemoria = new JTextField();
		tMemoria.setBounds(140, 130, 220, 20);
		pForm.add(tMemoria);
		tMemoria.setFont(new Font("Arial", Font.BOLD, 12));
		tMemoria.setColumns(10);
		
		JLabel lMemoria = new JLabel("Mémoria");
		lMemoria.setHorizontalAlignment(SwingConstants.CENTER);
		lMemoria.setBounds(10, 130, 121, 20);
		pForm.add(lMemoria);
		lMemoria.setForeground(new Color(255, 255, 255));
		lMemoria.setFont(new Font("Arial", Font.BOLD, 12));
		
		tGrafico = new JTextField();
		tGrafico.setBounds(140, 190, 220, 20);
		pForm.add(tGrafico);
		tGrafico.setFont(new Font("Arial", Font.BOLD, 12));
		tGrafico.setColumns(10);
		
		JLabel lGrafico = new JLabel("Gráfico");
		lGrafico.setHorizontalAlignment(SwingConstants.CENTER);
		lGrafico.setBounds(10, 190, 121, 20);
		pForm.add(lGrafico);
		lGrafico.setForeground(Color.WHITE);
		lGrafico.setFont(new Font("Arial", Font.BOLD, 12));
		
		tSsdHdd = new JTextField();
		tSsdHdd.setBounds(140, 220, 220, 20);
		pForm.add(tSsdHdd);
		tSsdHdd.setFont(new Font("Arial", Font.BOLD, 12));
		tSsdHdd.setColumns(10);
		
		JLabel lSddHdd = new JLabel("HDD's | SSD's");
		lSddHdd.setHorizontalAlignment(SwingConstants.CENTER);
		lSddHdd.setBounds(10, 220, 121, 20);
		pForm.add(lSddHdd);
		lSddHdd.setForeground(Color.WHITE);
		lSddHdd.setFont(new Font("Arial", Font.BOLD, 12));
		
		tMae = new JTextField();
		tMae.setBounds(140, 160, 220, 20);
		pForm.add(tMae);
		tMae.setFont(new Font("Arial", Font.BOLD, 12));
		tMae.setColumns(10);
		
		JLabel lMae = new JLabel("Placa Mãe");
		lMae.setHorizontalAlignment(SwingConstants.CENTER);
		lMae.setBounds(10, 160, 121, 20);
		pForm.add(lMae);
		lMae.setForeground(Color.WHITE);
		lMae.setFont(new Font("Arial", Font.BOLD, 12));
		
		JLabel lMac = new JLabel("MAC");
		lMac.setHorizontalAlignment(SwingConstants.CENTER);
		lMac.setBounds(10, 280, 121, 20);
		pForm.add(lMac);
		lMac.setForeground(Color.WHITE);
		lMac.setFont(new Font("Arial", Font.BOLD, 12));
		
		tMac = new JTextField();
		tMac.setBounds(140, 280, 220, 20);
		pForm.add(tMac);
		tMac.setFont(new Font("Arial", Font.BOLD, 12));
		tMac.setColumns(10);
		cbModelo = new JComboBox<String>();
		cbModelo.setBounds(530, 40, 220, 20);
		pForm.add(cbModelo);
		cbModelo.setFont(new Font("Arial", Font.BOLD, 12));
		cbModelo.setModel(this.modelos);
		
		JLabel lModelo = new JLabel("Modelo");
		lModelo.setHorizontalAlignment(SwingConstants.CENTER);
		lModelo.setForeground(Color.WHITE);
		lModelo.setFont(new Font("Arial", Font.BOLD, 12));
		lModelo.setBounds(400, 40, 120, 20);
		pForm.add(lModelo);
		
		JLabel lProvedor = new JLabel("Provedor e Rede");
		lProvedor.setHorizontalAlignment(SwingConstants.CENTER);
		lProvedor.setBounds(10, 310, 121, 20);
		pForm.add(lProvedor);
		lProvedor.setForeground(Color.WHITE);
		lProvedor.setFont(new Font("Arial", Font.BOLD, 12));
		
		tProvedor = new JTextField();
		tProvedor.setBounds(140, 310, 220, 20);
		pForm.add(tProvedor);
		tProvedor.setFont(new Font("Arial", Font.BOLD, 12));
		tProvedor.setColumns(10);
		
		JLabel lImpressora = new JLabel("Impressora");
		lImpressora.setHorizontalAlignment(SwingConstants.CENTER);
		lImpressora.setBounds(400, 160, 120, 20);
		pForm.add(lImpressora);
		lImpressora.setForeground(new Color(255, 255, 255));
		lImpressora.setFont(new Font("Arial", Font.BOLD, 12));
		
		tImpressora = new JTextField();
		tImpressora.setBounds(530, 160, 220, 20);
		pForm.add(tImpressora);
		tImpressora.setFont(new Font("Arial", Font.BOLD, 12));
		tImpressora.setColumns(10);
		
		JLabel lCriptografia = new JLabel("Modo de Criptografia");
		lCriptografia.setHorizontalAlignment(SwingConstants.CENTER);
		lCriptografia.setBounds(402, 70, 118, 20);
		pForm.add(lCriptografia);
		lCriptografia.setForeground(Color.WHITE);
		lCriptografia.setFont(new Font("Arial", Font.BOLD, 12));
		cbCriptografia = new JComboBox<>();
		cbCriptografia.setBounds(530, 70, 220, 20);
		pForm.add(cbCriptografia);
		cbCriptografia.setFont(new Font("Arial", Font.BOLD, 12));
		cbCriptografia.setModel(modosCriptografia);
		
		JLabel lDna = new JLabel("DNA");
		lDna.setHorizontalAlignment(SwingConstants.CENTER);
		lDna.setBounds(20, 340, 111, 20);
		pForm.add(lDna);
		lDna.setForeground(Color.WHITE);
		lDna.setFont(new Font("Arial", Font.BOLD, 12));
		
		tDna = new JTextField();
		tDna.setBounds(140, 340, 610, 20);
		pForm.add(tDna);
		tDna.setFont(new Font("Arial", Font.BOLD, 12));
		tDna.setColumns(10);
		
		JLabel lBiometria = new JLabel("Leitor Biometrico");
		lBiometria.setHorizontalAlignment(SwingConstants.CENTER);
		lBiometria.setBounds(400, 100, 120, 20);
		pForm.add(lBiometria);
		lBiometria.setForeground(new Color(255, 255, 255));
		lBiometria.setFont(new Font("Arial", Font.BOLD, 12));
		
		JLabel lCamera = new JLabel("Camera");
		lCamera.setHorizontalAlignment(SwingConstants.CENTER);
		lCamera.setBounds(400, 130, 120, 20);
		pForm.add(lCamera);
		lCamera.setForeground(new Color(255, 255, 255));
		lCamera.setFont(new Font("Arial", Font.BOLD, 12));
		
		tCamera = new JTextField();
		tCamera.setBounds(530, 130, 220, 20);
		pForm.add(tCamera);
		tCamera.setFont(new Font("Arial", Font.BOLD, 12));
		tCamera.setColumns(10);
		
		tBiometria = new JTextField();
		tBiometria.setBounds(530, 100, 220, 20);
		pForm.add(tBiometria);
		tBiometria.setFont(new Font("Arial", Font.BOLD, 12));
		tBiometria.setColumns(10);
		
		JLabel lblDataAtivao = new JLabel("Data Ativação");
		lblDataAtivao.setHorizontalAlignment(SwingConstants.CENTER);
		lblDataAtivao.setBounds(400, 220, 120, 20);
		pForm.add(lblDataAtivao);
		lblDataAtivao.setForeground(new Color(255, 255, 255));
		lblDataAtivao.setFont(new Font("Arial", Font.BOLD, 12));
		
		tDataAtivacao = new JFormattedTextField(Mascaras.mascaraDatasFormularios());
		tDataAtivacao.setBounds(530, 218, 220, 20);
		tDataAtivacao.setFont(new Font("Arial", Font.BOLD, 16));
		tDataAtivacao.setColumns(10);
		pForm.add(tDataAtivacao);
		
		JLabel lPlacaRede = new JLabel("Placa de Rede");
		lPlacaRede.setHorizontalAlignment(SwingConstants.CENTER);
		lPlacaRede.setForeground(Color.WHITE);
		lPlacaRede.setFont(new Font("Arial", Font.BOLD, 12));
		lPlacaRede.setBounds(10, 250, 121, 20);
		pForm.add(lPlacaRede);
		
		tPlacaRede = new JTextField();
		tPlacaRede.setFont(new Font("Arial", Font.BOLD, 12));
		tPlacaRede.setColumns(10);
		tPlacaRede.setBounds(140, 250, 220, 20);
		pForm.add(tPlacaRede);
		
		JLabel lSituacao = new JLabel("Situação");
		lSituacao.setHorizontalAlignment(SwingConstants.CENTER);
		lSituacao.setForeground(Color.WHITE);
		lSituacao.setFont(new Font("Arial", Font.BOLD, 12));
		lSituacao.setBounds(619, 310, 120, 20);
		pForm.add(lSituacao);
		
		tSituacao = new JTextField();
		tSituacao.setEditable(false);
		tSituacao.setFont(new Font("Arial", Font.BOLD, 12));
		tSituacao.setColumns(10);
		tSituacao.setBounds(749, 309, 135, 20);
		pForm.add(tSituacao);
		
		JLabel lNfe = new JLabel("Nota Fiscal");
		lNfe.setHorizontalAlignment(SwingConstants.CENTER);
		lNfe.setForeground(Color.WHITE);
		lNfe.setFont(new Font("Arial", Font.BOLD, 12));
		lNfe.setBounds(400, 280, 120, 20);
		pForm.add(lNfe);
		
		tNfe = new JTextField();
		tNfe.setFont(new Font("Arial", Font.BOLD, 12));
		tNfe.setColumns(10);
		tNfe.setBounds(530, 280, 353, 20);
		pForm.add(tNfe);
		pTitulo.setLayout(null);
		
		JLabel lTitulo = new JLabel("Máquina");
		lTitulo.setBounds(10, 11, 874, 24);
		lTitulo.setForeground(new Color(255, 255, 255));
		lTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lTitulo.setFont(new Font("Arial", Font.BOLD, 20));
		pTitulo.add(lTitulo);
		
		JButton bProgramas = new JButton("Programas");
		bProgramas.setBounds(493, 44, 180, 20);
		bProgramas.setFont(new Font("Arial", Font.BOLD, 12));
		
		bProgramas.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int maquinaDesativada = 0;
				if(maquina.getSituacao().equals("DESATIVADA"))
					maquinaDesativada = 1;
				
				FormularioProgramas fp = new FormularioProgramas(usuario, permissoes, maquinaDesativada);
				
				Maquina maquina = carregaClasse();
				
				fp.carregarDados(maquina);
				
				
			}
		});
		
		pTitulo.add(bProgramas);
		
		mc = new MaquinaC(bm);

		try(PontoAtendimentoDAO paDAO = new PontoAtendimentoDAO()){
			
			cbPonto = new JComboBox<PontoAtendimento>();
			cbPonto.setBounds(161, 44, 322, 20);
			cbPonto.setToolTipText("Ponto de Atendimento");
			cbPonto.setModel(mc.modeloPontos(paDAO.listaPontos()));
			cbPonto.setRenderer(new DefaultListCellRenderer() {
				

				private static final long serialVersionUID = 1L;

				@Override
			    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			        if (value instanceof PontoAtendimento) {
			        	PontoAtendimento pa = (PontoAtendimento) value;
			            setText(pa.getApelido());
			        }
			        return this;
				}
				
			});
			cbPonto.setFont(new Font("Arial", Font.BOLD, 12));
			pTitulo.add(cbPonto);
			
		}catch(Exception e) {
			
			System.err.println(e);
			
		}
		
		
		
		JButton btnAgentesMaquina = new JButton("Agentes Maquina");
		btnAgentesMaquina.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try(MaquinaDAO mDAO = new MaquinaDAO()){
					
					Maquina m = mDAO.carregaMaquina(Integer.parseInt(tCodigo.getText()));	
					
					AgentesMaquina am = new AgentesMaquina(m.getPa().getCodigo(), m.getCodigo(), permissoes, usuario);
					
					am.abrirTela();
					
				}catch(Exception ex) {
					
					System.err.println(ex);
					
				}
			}
		});
		btnAgentesMaquina.setBounds(683, 44, 201, 20);
		btnAgentesMaquina.setFont(new Font("Arial", Font.BOLD, 12));
		pTitulo.add(btnAgentesMaquina);
		
		JLabel lAGM = new JLabel("Ponto de Atendimento");
		lAGM.setHorizontalAlignment(SwingConstants.CENTER);
		lAGM.setForeground(Color.WHITE);
		lAGM.setFont(new Font("Arial", Font.BOLD, 12));
		lAGM.setBounds(10, 44, 141, 20);
		pTitulo.add(lAGM);
		pPrincipal.add(pBotoes);
		
		bSalvar = new JButton("Salvar");
		bSalvar.setBounds(169, 3, 142, 35);
		bSalvar.setFont(new Font("Arial", Font.BOLD, 14));
		bSalvar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				if(permissoes.getPolitica().podeGravarMaquina()) {
					
					try(MaquinaDAO mDAO = new MaquinaDAO()){
						
						if(mDAO.verificaHostnameMaquina(tNome.getText())) {
							
							JOptionPane.showMessageDialog(FormularioMaquina.this.frame, "Já existe uma máquina ativa com o nome escolhido\nEscolha outro!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						
						if(mDAO.gravarMaquina(carregaClasse(), usuario)) {
							
							try(ProgramasDAO pDAO = new ProgramasDAO()){
								
								Programas p = new Programas(carregaClasse());
								
								tCodigo.setText(String.valueOf(mDAO.getCodigoMaquinaInserida()));
								
								if(pDAO.buscaProgramas(p.getMaquina()).getCodigo() <= 0) {
									
									Maquina m = new Maquina();
									
									if(mDAO.getCodigoMaquinaInserida() > 0) {
										
										m.setCodigo(mDAO.getCodigoMaquinaInserida());
									}
									
									p = new Programas(m);
									
									if(!pDAO.gravaDados(p, usuario)) {

										JOptionPane.showMessageDialog(FormularioMaquina.this.frame, "Erro ao gravar programas ", "Erro!", JOptionPane.ERROR_MESSAGE);
										
									}
									
								}
								
								JOptionPane.showMessageDialog(FormularioMaquina.this.frame, "Maquina "+tNome.getText()+ " gravada com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
								setSituacaoBExcluir(true);
								setSituacaoBNova(true);
							}
							
							
							
							
							
						}else {
							
							JOptionPane.showMessageDialog(FormularioMaquina.this.frame, "Erro ao gravar maquina ", "Erro!", JOptionPane.ERROR_MESSAGE);
					}
					
						
					}catch(Exception ex) {
						
						System.err.println(ex);
						
					}
					
				}else {
					
					JOptionPane.showMessageDialog(frame,
							"Você não tem permissão para isso! ",
							"Acesso negado",
							JOptionPane.WARNING_MESSAGE);
					
				}
				
				
			}});
		
		pBotoes.setLayout(null);
		pBotoes.add(bSalvar);
		
		JButton bVoltar = new JButton("Voltar");
		bVoltar.setBounds(10, 3, 149, 35);
		bVoltar.setFont(new Font("Arial", Font.BOLD, 14));
		bVoltar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(origem instanceof FormularioAtivo) {
					
					fechaTela();
					
				}
				
				if(origem instanceof BuscaMaquinas) {
					
					new BuscaMaquinas(usuario, permissoes).abrirTela();;
					fechaTela();
					
				}
				
				
			}
		});
		pBotoes.add(bVoltar);
		
		bExcluir = new JButton("Excluir");
		bExcluir.setBounds(745, 3, 149, 35);
		bExcluir.setFont(new Font("Arial", Font.BOLD, 14));
		bExcluir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(permissoes.getPolitica().podeExcluirMaquina()) {
					
					if(JOptionPane.showConfirmDialog(FormularioMaquina.this.frame, "Deseja realmente excluir o registro ? ", "Exclusão", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						
						
						try(MaquinaDAO mDAO = new MaquinaDAO()){
							
							if(mDAO.deletaMaquina(carregaClasse(), usuario)) {
								
								JOptionPane.showMessageDialog(FormularioMaquina.this.frame, "Maquina "+tNome.getText()+ " deletada com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
								
								if(origem instanceof FormularioAtivo) {
									
									fechaTela();
									
								}
								
								if(origem instanceof BuscaMaquinas) {
									
									new BuscaMaquinas(usuario, permissoes).abrirTela();;
									fechaTela();
									
								}
								
							}else {
								
								JOptionPane.showMessageDialog(FormularioMaquina.this.frame, "Erro ao deletar maquina ", "Erro!", JOptionPane.ERROR_MESSAGE);
								
							}
						}catch(Exception ex) {
							
							System.err.println(ex);
							
						}
					}
					
				}else {
					
					JOptionPane.showMessageDialog(frame,
							"Você não tem permissão para isso! ",
							"Acesso negado",
							JOptionPane.WARNING_MESSAGE);
					
				}
			}
				
		});
		pBotoes.add(bExcluir);
		
		btnNova = new JButton("Nova Máquina");
		btnNova.setBounds(321, 3, 148, 35);
		btnNova.setFont(new Font("Arial", Font.BOLD, 14));
		btnNova.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(permissoes.getPolitica().podeAdicionarMaquina()) {
					
					limpaCampos();
					setSituacaoBExcluir(false);
					setSituacaoBNova(false);
					
				}else {
					
					JOptionPane.showMessageDialog(frame,
							"Você não tem permissão para isso! ",
							"Acesso negado",
							JOptionPane.WARNING_MESSAGE);
					
				}
				
				
				
			}
		});
		pBotoes.add(btnNova);
		
		carregarDados();
		
	}
	
	public void carregarDados() {
		
		if(maquina != null) {
			
			
			tCodigo.setText(String.valueOf(maquina.getCodigo()));
			tBiometria.setText(maquina.getBiometria());
			tCamera.setText(maquina.getCamera());
			tDataAtivacao.setText(maquina.getDataAtivacao());
			tNome.setText(maquina.getNome());
			tOs.setText(maquina.getOs());
			tMac.setText(maquina.getMac());
			tMae.setText(maquina.getMae());
			tMemoria.setText(maquina.getMemoria());
			tProcessador.setText(maquina.getProcessador());
			tProvedor.setText(maquina.getProvedor());
			tSsdHdd.setText(maquina.getSsdhdd());
			tGrafico.setText(maquina.getGrafico());
			tImpressora.setText(maquina.getImpressora());
			tDna.setText(maquina.getDna());
			cbCriptografia.setSelectedItem(maquina.getModoCriptografia());
			cbModelo.setSelectedItem(maquina.getModelo());
			
			for(int i = 0; i < cbPonto.getItemCount(); i++) {
				
				PontoAtendimento x = new PontoAtendimento();
				
				x = (PontoAtendimento)cbPonto.getItemAt(i);
				
				if(x.getCodigo() == maquina.getPa().getCodigo()) {
					
					setPaSelecionado(x);
				}
				
			}
			
			this.cbPonto.setSelectedItem(getPaSelecionado());
			
			try {
				
				verificaAtivo(Integer.parseInt(tCodigo.getText()));
				
			}catch(Exception e) {
				
				System.err.println("Erro ao verificar maquina: " + e);
			}
			
			tSituacao.setText(maquina.getSituacao());
			if(maquina.getSituacao().equals("DESATIVADA"))
				setSituacaoBSalvar(false);
			
			tPlacaRede.setText(maquina.getPlacaRede());
			tNfe.setText(maquina.getNfe());
			
			if(tSituacao.getText().equals("DESATIVADA")) {
				
				bExcluir.setEnabled(false);
				
			}
			
			if(!this.tSituacao.getText().equals("PENDENTE") || this.tSituacao.getText().isEmpty()) {
				
				
				tNome.setEditable(false);
				
			}
			
		}
		
		
		
	}
	
	public void fechaTela() {
		
		frame.setVisible(false);
		frame.dispose();
		
	}
	
	public void abrirTela() {
		
		frame.setVisible(true);
		
	}
	
	public void limpaCampos() {
		
		this.tCodigo.setText("");
		this.tBiometria.setText("Fultronic FS-80H");
		this.tCamera.setText("Logitech C920 PRO");
		this.tDataAtivacao.setText(dataAtual());
		this.tNome.setText("NATHYELLE");
		this.tOs.setText("WINDOWS 10 PRO 64");
		this.tMac.setText("");
		this.tMae.setText("");
		this.tMemoria.setText("");
		this.tProcessador.setText("");
		this.tProvedor.setText("");
		this.tSsdHdd.setText("");
		this.tGrafico.setText("");
		this.tImpressora.setText("");
		this.tDna.setText("");
		this.cbCriptografia.setSelectedIndex(0);
		this.cbModelo.setSelectedIndex(0);
		this.cbPonto.setSelectedIndex(0);
		this.tSituacao.setText("");
		this.tPlacaRede.setText("");
		this.tNfe.setText("");
		this.tNome.setEditable(true);
		
	}
	
	public Maquina carregaClasse() {
		
		Maquina maquina = new Maquina();
		
		if(tCodigo.getText().isEmpty()) {
			
			maquina.setCodigo(0);
			
		}else {
			
			maquina.setCodigo(Integer.parseInt(tCodigo.getText()));
		}

		maquina.setBiometria(tBiometria.getText());
		maquina.setCamera(tCamera.getText());
		maquina.setDataAtivacao(tDataAtivacao.getText());
		maquina.setGrafico(tGrafico.getText());
		maquina.setImpressora(tImpressora.getText());
		maquina.setMac(tMac.getText());
		maquina.setMae(tMae.getText());
		maquina.setMemoria(tMemoria.getText());
		maquina.setNome(tNome.getText());
		maquina.setOs(tOs.getText());
		maquina.setProcessador(tProcessador.getText());
		maquina.setProvedor(tProvedor.getText());
		maquina.setSsdhdd(tSsdHdd.getText());
		maquina.setModelo((String)cbModelo.getSelectedItem());
		maquina.setDna(tDna.getText());
		maquina.setModoCriptografia((String) cbCriptografia.getSelectedItem());
		maquina.setPa((PontoAtendimento) cbPonto.getSelectedItem());
		if(tSituacao.getText().isEmpty()) {
			maquina.setSituacao("PENDENTE");
		}else {
			maquina.setSituacao(tSituacao.getText());
		}
		maquina.setPlacaRede(tPlacaRede.getText());
		maquina.setNfe(tNfe.getText());
		return maquina;
		
	}
	
	public Maquina carregaClasseCheia(int codigo) {
		
		Maquina maquina = null;
		
		try(MaquinaDAO mDAO = new MaquinaDAO()){
		
			maquina = mDAO.carregaMaquina(codigo);
			
		}catch(Exception e) {
			
			System.err.println(e);
		}
		
		
		if(tCodigo.getText().isEmpty()) {
			
			maquina.setCodigo(0);
			
		}else {
			
			maquina.setCodigo(Integer.parseInt(tCodigo.getText()));
		}

		maquina.setBiometria(tBiometria.getText());
		maquina.setCamera(tCamera.getText());
		maquina.setDataAtivacao(tDataAtivacao.getText());
		maquina.setGrafico(tGrafico.getText());
		maquina.setImpressora(tImpressora.getText());
		maquina.setMac(tMac.getText());
		maquina.setMae(tMae.getText());
		maquina.setMemoria(tMemoria.getText());
		maquina.setNome(tNome.getText());
		maquina.setOs(tOs.getText());
		maquina.setProcessador(tProcessador.getText());
		maquina.setProvedor(tProvedor.getText());
		maquina.setModelo((String)cbModelo.getSelectedItem());
		maquina.setDna(tDna.getText());
		maquina.setModoCriptografia((String) cbCriptografia.getSelectedItem());
		maquina.setPa((PontoAtendimento) cbPonto.getSelectedItem());
		maquina.setPlacaRede(tPlacaRede.getText());
		maquina.setNfe(tNfe.getText());
		
		return maquina;
		
	}
	
	public String dataAtual() {
		
		Date dataCompleta = new Date();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		
		return sdf.format(dataCompleta);
		
	}
	
	public Maquina pegaMaquinaSalva(Maquina maquina) {
		
		return maquina;
	}
	
	public void verificaAtivo(int codigo) throws SQLException {
		
		try(AtivoDAO atDAO = new AtivoDAO()){
			
			if(atDAO.verificaMaquinaAtiva(codigo)) {
				
				this.bExcluir.setEnabled(false);
			
				
			}else {
				
				this.bExcluir.setEnabled(true);
			}
			
		}catch(Exception e) {
			
			System.err.println(e);
			
		}
		
		
		
		
	}
}


