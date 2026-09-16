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
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import dsj.gestorar.modelo.Maquina;
import dsj.gestorar.modelo.Programas;
import dsj.gestorar.modelo.TituloUsuarios;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.persistencia.ProgramasDAO;

import java.awt.Color;
import java.awt.Window.Type;

public class FormularioProgramas {

	private JFrame frame;
	private JTextField tCodigo;
	private JTextField tEmissao;
	private JTextField tNome;
	private int codigoMaquina;
	private JTextField tJava;
	private JTextField tSafesing;
	private JTextField tEdge;
	private JTextField tPdf;
	private JTextField tDriverCamera;
	private JTextField tDriverBiometria;
	private Usuario usuario;
	private TituloUsuarios permissoes;
	private int maquinaDesativada;
	
	

	public int getCodigoMaquina() {
		return codigoMaquina;
	}

	public void setCodigoMaquina(int codigoMaquina) {
		this.codigoMaquina = codigoMaquina;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public FormularioProgramas(Usuario usuario, TituloUsuarios permissoes, int maquinaDesativada) {
		this.permissoes = permissoes;
		this.usuario = usuario;
		this.maquinaDesativada = maquinaDesativada;
		initialize();

	}

	private void initialize() {
		
		frame = new JFrame();
		frame.setType(Type.UTILITY);
		frame.getContentPane().setBackground(new Color(0, 0, 0));
		frame.setBounds(100, 100, 700, 450);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		Font customFont = new Font("Arial", Font.BOLD, 12);

        UIManager.put("OptionPane.messageFont", customFont);
        UIManager.put("OptionPane.buttonFont", customFont);
        UIManager.put("TextField.font", customFont);
		
		JPanel pPrincipal = new JPanel();
		springLayout.putConstraint(SpringLayout.SOUTH, pPrincipal, 401, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, pPrincipal, 674, SpringLayout.WEST, frame.getContentPane());
		pPrincipal.setBackground(new Color(0, 64, 128));
		springLayout.putConstraint(SpringLayout.NORTH, pPrincipal, 10, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, pPrincipal, 10, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(pPrincipal);
		pPrincipal.setLayout(null);
		
		JPanel pTitulo = new JPanel();
		pTitulo.setBounds(10, 10, 644, 38);
		pTitulo.setBackground(new Color(0, 64, 128));
		pPrincipal.add(pTitulo);
		
		JPanel pForm = new JPanel();
		pForm.setBounds(10, 66, 644, 241);
		pForm.setBackground(new Color(0, 64, 128));
		pForm.setForeground(new Color(0, 64, 128));
		pPrincipal.add(pForm);
		
		JPanel pBotoes = new JPanel();
		pBotoes.setBounds(10, 318, 644, 62);
		pBotoes.setBackground(new Color(0, 64, 128));
		pBotoes.setForeground(new Color(0, 64, 128));
		pForm.setLayout(null);
		
		JLabel lblMicrosoftEdge = new JLabel("Microsoft Edge");
		lblMicrosoftEdge.setHorizontalAlignment(SwingConstants.CENTER);
		lblMicrosoftEdge.setBounds(10, 90, 120, 20);
		lblMicrosoftEdge.setForeground(Color.WHITE);
		lblMicrosoftEdge.setFont(new Font("Arial", Font.BOLD, 12));
		pForm.add(lblMicrosoftEdge);
		
		tJava = new JTextField();
		tJava.setBounds(140, 120, 159, 20);
		tJava.setFont(new Font("Arial", Font.BOLD, 12));
		tJava.setColumns(10);
		pForm.add(tJava);
		
		JLabel lblJava = new JLabel("Java");
		lblJava.setHorizontalAlignment(SwingConstants.CENTER);
		lblJava.setBounds(10, 120, 120, 20);
		lblJava.setForeground(Color.WHITE);
		lblJava.setFont(new Font("Arial", Font.BOLD, 12));
		pForm.add(lblJava);
		
		tSafesing = new JTextField();
		tSafesing.setBounds(140, 61, 159, 20);
		tSafesing.setFont(new Font("Arial", Font.BOLD, 12));
		tSafesing.setColumns(10);
		pForm.add(tSafesing);
		
		tEdge = new JTextField();
		tEdge.setBounds(140, 90, 159, 20);
		tEdge.setFont(new Font("Arial", Font.BOLD, 12));
		tEdge.setColumns(6);
		pForm.add(tEdge);
		
		JLabel lblSafesing = new JLabel("Safesing");
		lblSafesing.setHorizontalAlignment(SwingConstants.CENTER);
		lblSafesing.setBounds(10, 61, 120, 20);
		lblSafesing.setForeground(Color.WHITE);
		lblSafesing.setFont(new Font("Arial", Font.BOLD, 12));
		pForm.add(lblSafesing);
		
		tPdf = new JTextField();
		tPdf.setFont(new Font("Arial", Font.BOLD, 12));
		tPdf.setColumns(10);
		tPdf.setBounds(140, 151, 159, 20);
		pForm.add(tPdf);
		
		JLabel lPdf = new JLabel("Adobe Reader");
		lPdf.setHorizontalAlignment(SwingConstants.CENTER);
		lPdf.setForeground(Color.WHITE);
		lPdf.setFont(new Font("Arial", Font.BOLD, 12));
		lPdf.setBounds(10, 150, 120, 20);
		pForm.add(lPdf);
		
		JLabel lblCamera = new JLabel("Driver Camera");
		lblCamera.setHorizontalAlignment(SwingConstants.CENTER);
		lblCamera.setForeground(Color.WHITE);
		lblCamera.setFont(new Font("Arial", Font.BOLD, 12));
		lblCamera.setBounds(320, 30, 120, 20);
		pForm.add(lblCamera);
		
		tDriverCamera = new JTextField();
		tDriverCamera.setFont(new Font("Arial", Font.BOLD, 12));
		tDriverCamera.setColumns(10);
		tDriverCamera.setBounds(450, 30, 120, 20);
		pForm.add(tDriverCamera);
		
		JLabel lblLeitorBiometrico = new JLabel("Driver Scanner");
		lblLeitorBiometrico.setHorizontalAlignment(SwingConstants.CENTER);
		lblLeitorBiometrico.setForeground(Color.WHITE);
		lblLeitorBiometrico.setFont(new Font("Arial", Font.BOLD, 12));
		lblLeitorBiometrico.setBackground(new Color(0, 64, 128));
		lblLeitorBiometrico.setBounds(320, 60, 120, 20);
		pForm.add(lblLeitorBiometrico);
		
		tDriverBiometria = new JTextField();
		tDriverBiometria.setFont(new Font("Arial", Font.BOLD, 12));
		tDriverBiometria.setColumns(10);
		tDriverBiometria.setBounds(450, 60, 120, 20);
		pForm.add(tDriverBiometria);
		
		tCodigo = new JTextField();
		tCodigo.setBounds(585, 210, 49, 20);
		pForm.add(tCodigo);
		tCodigo.setEditable(false);
		tCodigo.setEnabled(false);
		tCodigo.setFont(new Font("Arial", Font.BOLD, 12));
		tCodigo.setColumns(15);
		
		tNome = new JTextField();
		tNome.setBounds(450, 182, 184, 20);
		pForm.add(tNome);
		tNome.setEnabled(false);
		tNome.setEditable(false);
		tNome.setFont(new Font("Arial", Font.BOLD, 12));
		tNome.setColumns(10);
		
		JLabel lSistemaEmissao = new JLabel("Sistema Emissão");
		lSistemaEmissao.setHorizontalAlignment(SwingConstants.CENTER);
		lSistemaEmissao.setBounds(10, 30, 120, 20);
		pForm.add(lSistemaEmissao);
		lSistemaEmissao.setForeground(new Color(255, 255, 255));
		lSistemaEmissao.setFont(new Font("Arial", Font.BOLD, 12));
		
		tEmissao = new JTextField();
		tEmissao.setEditable(false);
		tEmissao.setBounds(140, 30, 160, 20);
		pForm.add(tEmissao);
		tEmissao.setFont(new Font("Arial", Font.BOLD, 12));
		tEmissao.setColumns(10);
		pTitulo.setLayout(null);
		
		JLabel lTitulo = new JLabel("Programas");
		lTitulo.setBounds(270, 11, 109, 19);
		lTitulo.setForeground(new Color(255, 255, 255));
		lTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lTitulo.setFont(new Font("Arial", Font.BOLD, 16));
		pTitulo.add(lTitulo);
		pPrincipal.add(pBotoes);
		
		JButton bSalvar = new JButton("Salvar");
		bSalvar.setBounds(140, 11, 120, 41);
		bSalvar.setFont(new Font("Arial", Font.BOLD, 14));
		bSalvar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(permissoes.getPolitica().podeGravarProgramas()) {
					
					try(ProgramasDAO pDAO = new ProgramasDAO()){
						
						if(pDAO.gravaDados(carregaClasse(), usuario)) {
							
							JOptionPane.showMessageDialog(null, "Programas gravados com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
							
						}else {
							
							JOptionPane.showMessageDialog(null, "Erro ao gravar programas", "Erro!", JOptionPane.ERROR_MESSAGE);
							
						}
						
					}catch(Exception ex) {
						
						System.err.println(ex);
						
					}

				}else {
					
					JOptionPane.showMessageDialog(frame, "Você não tem permissão para alterar programas.", "Acesso negado",
							JOptionPane.WARNING_MESSAGE);
					
				}
				
				
				
			}
		});
		pBotoes.setLayout(null);
		pBotoes.add(bSalvar);
		
		if(maquinaDesativada == 1) {
			bSalvar.setEnabled(false);
		}
		
		JButton bVoltar = new JButton("Voltar");
		bVoltar.setBounds(10, 11, 120, 41);
		bVoltar.setFont(new Font("Arial", Font.BOLD, 14));
		bVoltar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				frame.dispose();
				
			}
		});
		pBotoes.add(bVoltar);
	}
	
	public void carregarDados(Maquina maquina) {
		
		try(ProgramasDAO pDAO = new ProgramasDAO()){
			
			Programas p = pDAO.buscaProgramas(maquina);
			
			this.setCodigoMaquina(maquina.getCodigo());
			this.tCodigo.setText(String.valueOf(p.getCodigo()));
			this.tNome.setText(maquina.getNome());
			this.tDriverBiometria.setText(p.getDriverLeitorBiometrico());
			this.tDriverCamera.setText(p.getDriverCamera());
			this.tEdge.setText(p.getEdge());
			this.tEmissao.setText(p.getEmissao());
			this.tJava.setText(p.getJava());
			this.tPdf.setText(p.getPdf());
			this.tSafesing.setText(p.getSafesing());
			
			abrirTela();
			
		}catch(Exception ex) {
			
			System.err.println(ex);
			
		}

		
		
	}
	
	public Programas carregaClasse() {
		
		Programas p = new Programas();
		
		p.setCodigo(Integer.parseInt(tCodigo.getText()));
		p.setDriverCamera(tDriverCamera.getText());
		p.setDriverLeitorBiometrico(tDriverBiometria.getText());
		p.setEdge(tEdge.getText());
		p.setEmissao(tEmissao.getText());
		p.setJava(tJava.getText());
		p.setMaquina(new Maquina(getCodigoMaquina()));
		p.setPdf(tPdf.getText());
		p.setSafesing(tSafesing.getText());
		
		return p;
		
	}
	
	public void abrirTela() {
		
		getFrame().setVisible(true);
		
	}
	
	public void fechaTela() {
		
		getFrame().dispose();
		
	}
}
