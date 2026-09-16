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
import java.text.ParseException;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.text.MaskFormatter;

import dsj.gestorar.modelo.Agente;
import dsj.gestorar.modelo.PontoAtendimento;
import dsj.gestorar.modelo.SituacaoesAgente;
import dsj.gestorar.modelo.TituloUsuarios;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.persistencia.AgenteDAO;
import dsj.gestorar.persistencia.PontoAtendimentoDAO;
import dsj.gestorar.utilitarios.Mascaras;
import dsj.gestorar.visual.buscas.BuscaAgente;

import java.awt.Color;
import java.awt.Component;
import java.awt.Window.Type;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;

public class FormularioAgente {

	private JFrame frame;
	private JTextField tCodigo;
	private JTextField tNome;
	private JTextField tCpf;
	private JTextField tDataH;
	private JComboBox<PontoAtendimento> cbPontos;
	private JComboBox<String> cbSituacao;
	private DefaultComboBoxModel<String> situacaoes = new DefaultComboBoxModel<>();
	private PontoAtendimento pontoSelecionado;
	private Usuario usuario;
	private Agente agente;
	private TituloUsuarios permissoes;
	private BuscaAgente bc;
	private JTextField tEmail;

	public PontoAtendimento getPontoSelecionado() {
		return pontoSelecionado;
	}


	public void setPontoSelecionado(PontoAtendimento pontoSelecionado) {
		this.pontoSelecionado = pontoSelecionado;
	}


	public FormularioAgente(Usuario usuario, Agente agente, TituloUsuarios permissoes, BuscaAgente bc) {
		this.agente = agente;
		this.usuario = usuario;
		this.permissoes = permissoes;
		this.bc = bc;
		initialize();
		
	}

	private void initialize() {
		
		frame = new JFrame();
		frame.setType(Type.UTILITY);
		frame.getContentPane().setBackground(new Color(0, 0, 0));
		frame.setBounds(100, 100, 750, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		SpringLayout sl_pPrincipal = new SpringLayout();
		pPrincipal.setLayout(sl_pPrincipal);
		
		JPanel pTitulo = new JPanel();
		pTitulo.setBackground(new Color(0, 64, 128));
		sl_pPrincipal.putConstraint(SpringLayout.NORTH, pTitulo, 10, SpringLayout.NORTH, pPrincipal);
		sl_pPrincipal.putConstraint(SpringLayout.WEST, pTitulo, 10, SpringLayout.WEST, pPrincipal);
		sl_pPrincipal.putConstraint(SpringLayout.SOUTH, pTitulo, 48, SpringLayout.NORTH, pPrincipal);
		pPrincipal.add(pTitulo);
		SpringLayout sl_pTitulo = new SpringLayout();
		pTitulo.setLayout(sl_pTitulo);
		
		JPanel pBotoes = new JPanel();
		sl_pPrincipal.putConstraint(SpringLayout.WEST, pBotoes, 10, SpringLayout.WEST, pPrincipal);
		sl_pPrincipal.putConstraint(SpringLayout.EAST, pBotoes, -10, SpringLayout.EAST, pPrincipal);
		sl_pPrincipal.putConstraint(SpringLayout.EAST, pTitulo, 0, SpringLayout.EAST, pBotoes);
		sl_pPrincipal.putConstraint(SpringLayout.NORTH, pBotoes, 310, SpringLayout.NORTH, pPrincipal);
		sl_pPrincipal.putConstraint(SpringLayout.SOUTH, pBotoes, -10, SpringLayout.SOUTH, pPrincipal);
		pBotoes.setBackground(new Color(0, 64, 128));
		
		JLabel lTitulo = new JLabel("Agente de Resgistro");
		sl_pTitulo.putConstraint(SpringLayout.WEST, lTitulo, 142, SpringLayout.WEST, pTitulo);
		sl_pTitulo.putConstraint(SpringLayout.SOUTH, lTitulo, -9, SpringLayout.SOUTH, pTitulo);
		sl_pTitulo.putConstraint(SpringLayout.EAST, lTitulo, -118, SpringLayout.EAST, pTitulo);
		lTitulo.setForeground(new Color(255, 255, 255));
		lTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lTitulo.setFont(new Font("Arial", Font.BOLD, 16));
		pTitulo.add(lTitulo);
		pPrincipal.add(pBotoes);
		
		JButton bSalvar = new JButton("Salvar");
		bSalvar.setBounds(170, 11, 150, 50);
		bSalvar.setFont(new Font("Arial", Font.BOLD, 14));
		bSalvar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				if(permissoes.getPolitica().podeAdicionarAgente()) {
					
					int codigo = 0;
					
					if(!tCodigo.getText().isEmpty()) {
						
						codigo = Integer.parseInt(tCodigo.getText());
						
					}
					
					String cpf = tCpf.getText().replaceAll("[.-]", "");
					
					Agente a = new Agente(codigo,
							tNome.getText(),
							tDataH.getText(),
							cpf,
							(String)cbSituacao.getSelectedItem(),
							tEmail.getText(),
							(PontoAtendimento)cbPontos.getSelectedItem());
					
					try(AgenteDAO aDAO = new AgenteDAO()){
						
						
						if(aDAO.gravaAgente(a, usuario)) {
							
							JOptionPane.showMessageDialog(frame,
									"Agente " + tNome.getText() + " gravado com sucesso.",
									"Sucesso!",
									JOptionPane.INFORMATION_MESSAGE);
							
							abreBuscaAgentes();
							
						}else {
							
							JOptionPane.showMessageDialog(frame,
									"Problema ao salvar o ponto, campos inválidos",
									"Erro!",
									JOptionPane.ERROR_MESSAGE);
							
						}
						
						
					}catch(Exception ex) {
						
						System.err.println(ex);
						
					}
					
				}else {
					
					JOptionPane.showMessageDialog(FormularioAgente.this.frame, "Usuário não tem acesso para esta ação!", "", JOptionPane.INFORMATION_MESSAGE);
					
				}
			
		}});
		pBotoes.setLayout(null);
		pBotoes.add(bSalvar);
		
		JButton bVoltar = new JButton("Voltar");
		bVoltar.setBounds(10, 11, 150, 50);
		bVoltar.setFont(new Font("Arial", Font.BOLD, 14));
		bVoltar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				fechaTela();
				abreBuscaAgentes();
			}
		});
		pBotoes.add(bVoltar);
		
		JPanel pDados = new JPanel();
		sl_pPrincipal.putConstraint(SpringLayout.NORTH, pDados, 6, SpringLayout.SOUTH, pTitulo);
		sl_pPrincipal.putConstraint(SpringLayout.WEST, pDados, 10, SpringLayout.WEST, pPrincipal);
		sl_pPrincipal.putConstraint(SpringLayout.SOUTH, pDados, -6, SpringLayout.NORTH, pBotoes);
		sl_pPrincipal.putConstraint(SpringLayout.EAST, pDados, -10, SpringLayout.EAST, pPrincipal);
		pDados.setBackground(new Color(0, 64, 128));
		pDados.setForeground(new Color(255, 255, 255));
		pPrincipal.add(pDados);
		pDados.setLayout(null);
		
		JLabel lNome = new JLabel("Nome");
		lNome.setHorizontalAlignment(SwingConstants.CENTER);
		lNome.setBounds(10, 10, 120, 20);
		lNome.setForeground(Color.WHITE);
		lNome.setFont(new Font("Arial", Font.BOLD, 12));
		pDados.add(lNome);
		
		JLabel lSituacao = new JLabel("Situacao");
		lSituacao.setHorizontalAlignment(SwingConstants.CENTER);
		lSituacao.setBounds(10, 100, 120, 20);
		lSituacao.setForeground(Color.WHITE);
		lSituacao.setFont(new Font("Arial", Font.BOLD, 12));
		pDados.add(lSituacao);
		
		tCodigo = new JTextField();
		tCodigo.setBounds(623, 219, 61, 20);
		tCodigo.setFont(new Font("Arial", Font.BOLD, 12));
		tCodigo.setEditable(false);
		pDados.add(tCodigo);
		tCodigo.setColumns(10);
		
		tNome = new JTextField();
		tNome.setBounds(160, 10, 350, 20);
		tNome.setFont(new Font("Arial", Font.BOLD, 12));
		tNome.setColumns(10);
		pDados.add(tNome);

		try(PontoAtendimentoDAO pDAO = new PontoAtendimentoDAO()){
			
			cbPontos = new JComboBox<PontoAtendimento>();
			cbPontos.setBounds(160, 70, 350, 20);
			cbPontos.setModel(modeloPontos(pDAO.listaPontos()));
			cbPontos.setRenderer(new DefaultListCellRenderer() {
				

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
			cbPontos.setFont(new Font("Arial", Font.BOLD, 12));
			pDados.add(cbPontos);
			
		}catch(Exception e) {
			
			System.err.println(e);
			
		}
		
		
		
		JLabel lCidade = new JLabel("CPF");
		lCidade.setHorizontalAlignment(SwingConstants.CENTER);
		lCidade.setBounds(10, 40, 120, 20);
		lCidade.setForeground(Color.WHITE);
		lCidade.setFont(new Font("Arial", Font.BOLD, 12));
		pDados.add(lCidade);
		
		MaskFormatter mascaraCfp = null; 
		
		try {
			mascaraCfp = new MaskFormatter("###.###.###-##");
		} catch (ParseException e1) {
			
			System.err.println("Erro ao transformar mascara: " + e1);
			
			e1.printStackTrace();
		}
		tCpf = new JFormattedTextField(mascaraCfp);
		tCpf.setBounds(160, 40, 100, 20);
		tCpf.setFont(new Font("Arial", Font.BOLD, 12));
		tCpf.setColumns(10);
		pDados.add(tCpf);
		
		JLabel lPonto = new JLabel("Ponto Atendimento");
		lPonto.setHorizontalAlignment(SwingConstants.CENTER);
		lPonto.setBounds(10, 70, 120, 20);
		lPonto.setForeground(Color.WHITE);
		lPonto.setFont(new Font("Arial", Font.BOLD, 12));
		pDados.add(lPonto);
		
		SituacaoesAgente[] situacoesAgente = SituacaoesAgente.values();
	
		for(SituacaoesAgente sa : situacoesAgente) {
			
			this.situacaoes.addElement(sa.toString());
			
		}
		
		
		cbSituacao = new JComboBox<String>();
		cbSituacao.setBounds(160, 100, 100, 22);
		cbSituacao.setModel(situacaoes);
		cbSituacao.setFont(new Font("Arial", Font.BOLD, 12));
		pDados.add(cbSituacao);
		
		JLabel lDataHabilitacao = new JLabel("Data Habilitação");
		lDataHabilitacao.setHorizontalAlignment(SwingConstants.CENTER);
		lDataHabilitacao.setBounds(10, 130, 120, 20);
		lDataHabilitacao.setForeground(Color.WHITE);
		lDataHabilitacao.setFont(new Font("Arial", Font.BOLD, 12));
		pDados.add(lDataHabilitacao);
		
		tDataH = new JFormattedTextField(Mascaras.mascaraDatasFormularios());
		tDataH.setBounds(160, 133, 100, 20);
		tDataH.setEditable(true);
		tDataH.setFont(new Font("Arial", Font.BOLD, 12));
		tDataH.setColumns(10);
		pDados.add(tDataH);
		
		JLabel lEmail = new JLabel("E-Mail");
		lEmail.setHorizontalAlignment(SwingConstants.CENTER);
		lEmail.setForeground(Color.WHITE);
		lEmail.setFont(new Font("Arial", Font.BOLD, 12));
		lEmail.setBounds(10, 166, 120, 20);
		pDados.add(lEmail);
		
		tEmail = new JTextField();
		tEmail.setFont(new Font("Arial", Font.BOLD, 12));
		tEmail.setColumns(10);
		tEmail.setBounds(160, 166, 350, 20);
		pDados.add(tEmail);

		carregarDados();
	}
	
	public void carregarDados() {
		
		if(agente != null) {
			
			tCodigo.setText(String.valueOf(agente.getCodigo()));
			tNome.setText(agente.getNome());
			tCpf.setText(agente.getCpf());
			tDataH.setText(agente.getDataHabilitacao());
			cbSituacao.setSelectedItem(agente.getSituacao());
			tEmail.setText(agente.getEmail());
			
			try(PontoAtendimentoDAO pDAO = new PontoAtendimentoDAO()){
				
				PontoAtendimento pa = pDAO.buscaPonto(agente.getPontoAtendimento().getCodigo());
				
				for(int i = 0; i < cbPontos.getItemCount(); i++) {
					
					PontoAtendimento x = new PontoAtendimento();
					
					x = (PontoAtendimento)cbPontos.getItemAt(i);
					
					if(x.getCodigo() == pa.getCodigo()) {
						
						setPontoSelecionado(x);
					}
				}
				
				this.cbPontos.setSelectedItem(getPontoSelecionado());
				this.tDataH.setText(agente.getDataHabilitacao());
			
				this.tDataH.setEditable(true);
			
				
			}catch(Exception e) {
				
				System.err.println(e);
				
			}
			
		}
		
		
		
		
		
	}
	
	public DefaultComboBoxModel<PontoAtendimento> modeloPontos(List<PontoAtendimento> pontos) {
        DefaultComboBoxModel<PontoAtendimento> modelo = new DefaultComboBoxModel<>();
        pontos.forEach(modelo::addElement);
        return modelo;
    }
	
	public void limpaDados() {
		
		this.tCodigo.setText("");
		this.tNome.setText("");
		this.tCpf.setText("");
		this.tDataH.setText("");
		this.cbPontos.setSelectedIndex(-1);
		this.cbSituacao.setSelectedIndex(-1);
		this.tDataH.setText("");
		
	}
	
	public void abreBuscaAgentes() {
		
		bc.abrirTela();
		
		frame.dispose();
		
	}
	
	public void abrirTela() {
		
		frame.setVisible(true);
		
	}
	
	public void fechaTela() {
		
		frame.dispose();
		
	}
}
