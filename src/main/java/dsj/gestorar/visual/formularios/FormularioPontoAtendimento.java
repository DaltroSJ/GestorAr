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

import dsj.gestorar.modelo.PontoAtendimento;
import dsj.gestorar.modelo.TituloUsuarios;
import dsj.gestorar.modelo.Ufs;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.persistencia.PontoAtendimentoDAO;
import dsj.gestorar.visual.buscas.BuscaPontoAtendimento;

import java.awt.Color;
import java.awt.Component;
import java.awt.Window.Type;
import javax.swing.JComboBox;

public class FormularioPontoAtendimento {

	private JFrame frame;
	private JTextField tCodigo;
	private JTextField tApelido;
	private JTextField tCidade;
	private JComboBox<String> cbUf;
	private DefaultComboBoxModel<String> ufs = new DefaultComboBoxModel<>();
	private Usuario usuario;
	private PontoAtendimento pontoAtendimento;
	private Component cp;
	private Object origem;
	private TituloUsuarios permissoes;
	
	public JFrame getFrame() {
		return frame;
	}




	public void setFrame(JFrame frame) {
		this.frame = frame;
	}



	public FormularioPontoAtendimento(Usuario usuario, Component componente, TituloUsuarios permissoes) {
		this.permissoes = permissoes;
		this.cp = componente;
		this.usuario = usuario;
		initialize();
		
	}
	
	public FormularioPontoAtendimento(Usuario usuario, PontoAtendimento pa, Component componente, Object origem, TituloUsuarios permissoes) {
		this.permissoes = permissoes;
		this.origem = origem;
		this.cp = componente;
		this.pontoAtendimento = pa;
		this.usuario = usuario;
		initialize();
		
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
		
		JPanel pBotoes = new JPanel();
		sl_pPrincipal.putConstraint(SpringLayout.NORTH, pBotoes, 221, SpringLayout.SOUTH, pTitulo);
		sl_pPrincipal.putConstraint(SpringLayout.WEST, pBotoes, 0, SpringLayout.WEST, pTitulo);
		sl_pPrincipal.putConstraint(SpringLayout.SOUTH, pBotoes, -10, SpringLayout.SOUTH, pPrincipal);
		sl_pPrincipal.putConstraint(SpringLayout.EAST, pBotoes, 0, SpringLayout.EAST, pTitulo);
		pBotoes.setBackground(new Color(0, 64, 128));
		
		JLabel lTitulo = new JLabel("Ponto Atendimento");
		sl_pTitulo.putConstraint(SpringLayout.WEST, lTitulo, 100, SpringLayout.WEST, pTitulo);
		sl_pTitulo.putConstraint(SpringLayout.SOUTH, lTitulo, -9, SpringLayout.SOUTH, pTitulo);
		sl_pTitulo.putConstraint(SpringLayout.EAST, lTitulo, -115, SpringLayout.EAST, pTitulo);
		lTitulo.setForeground(new Color(255, 255, 255));
		lTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lTitulo.setFont(new Font("Arial", Font.BOLD, 16));
		pTitulo.add(lTitulo);
		pPrincipal.add(pBotoes);
		SpringLayout sl_pBotoes = new SpringLayout();
		pBotoes.setLayout(sl_pBotoes);
		
		JButton bSalvar = new JButton("Salvar");
		bSalvar.setFont(new Font("Arial", Font.BOLD, 14));
		sl_pBotoes.putConstraint(SpringLayout.WEST, bSalvar, 10, SpringLayout.WEST, pBotoes);
		sl_pBotoes.putConstraint(SpringLayout.SOUTH, bSalvar, -10, SpringLayout.SOUTH, pBotoes);
		bSalvar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try(PontoAtendimentoDAO paDAO = new PontoAtendimentoDAO()){
					
					int codigo = 0;
					
					if(!tCodigo.getText().isEmpty()) {
						
						codigo = Integer.parseInt(tCodigo.getText());
						
					}
					
					PontoAtendimento pa = new PontoAtendimento(codigo, tApelido.getText(), tCidade.getText(),
							(String)cbUf.getSelectedItem());
					
					if(paDAO.gravaPonto(pa, usuario)) {
						
						JOptionPane.showMessageDialog(null,
								"Ponto " + tApelido.getText() + " gravado com sucesso.",
								"Sucesso!",
								JOptionPane.INFORMATION_MESSAGE);
						
						abreBuscaPontosAtendimento();
						
					}else {
						
						JOptionPane.showMessageDialog(null,
								"Problema ao salvar o ponto, campos inválidos",
								"Erro!",
								JOptionPane.ERROR_MESSAGE);
						
					}
					
				}catch(Exception ex) {
					
					System.out.println(ex);
					
				}
				
			}
		});
		pBotoes.add(bSalvar);
		
		JButton bVoltar = new JButton("Voltar");
		sl_pBotoes.putConstraint(SpringLayout.NORTH, bVoltar, 0, SpringLayout.NORTH, bSalvar);
		sl_pBotoes.putConstraint(SpringLayout.WEST, bVoltar, 43, SpringLayout.EAST, bSalvar);
		bVoltar.setFont(new Font("Arial", Font.BOLD, 14));
		bVoltar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(origem instanceof FormularioAtivo) {
					
					fechaTela();
					
				}
				
				if(origem instanceof BuscaPontoAtendimento) {
					
					fechaTela();
					abreBuscaPontosAtendimento();
					
				}
			}
		});
		pBotoes.add(bVoltar);
		
		JPanel pDados = new JPanel();
		sl_pPrincipal.putConstraint(SpringLayout.NORTH, pDados, 7, SpringLayout.SOUTH, pTitulo);
		sl_pPrincipal.putConstraint(SpringLayout.SOUTH, pDados, -5, SpringLayout.NORTH, pBotoes);
		pDados.setBackground(new Color(0, 64, 128));
		pDados.setForeground(new Color(255, 255, 255));
		sl_pPrincipal.putConstraint(SpringLayout.WEST, pDados, 0, SpringLayout.WEST, pTitulo);
		sl_pPrincipal.putConstraint(SpringLayout.EAST, pDados, 0, SpringLayout.EAST, pTitulo);
		pPrincipal.add(pDados);
		SpringLayout sl_pDados = new SpringLayout();
		pDados.setLayout(sl_pDados);
		
		JLabel lCodigo = new JLabel("Código");
		lCodigo.setForeground(new Color(255, 255, 255));
		lCodigo.setFont(new Font("Arial", Font.BOLD, 12));
		sl_pDados.putConstraint(SpringLayout.NORTH, lCodigo, 10, SpringLayout.NORTH, pDados);
		sl_pDados.putConstraint(SpringLayout.WEST, lCodigo, 10, SpringLayout.WEST, pDados);
		pDados.add(lCodigo);
		
		JLabel lApelido = new JLabel("Apelido");
		sl_pDados.putConstraint(SpringLayout.NORTH, lApelido, 20, SpringLayout.SOUTH, lCodigo);
		sl_pDados.putConstraint(SpringLayout.WEST, lApelido, 0, SpringLayout.WEST, lCodigo);
		lApelido.setForeground(Color.WHITE);
		lApelido.setFont(new Font("Arial", Font.BOLD, 12));
		pDados.add(lApelido);
		
		JLabel lCidade = new JLabel("Cidade");
		sl_pDados.putConstraint(SpringLayout.NORTH, lCidade, 85, SpringLayout.NORTH, pDados);
		sl_pDados.putConstraint(SpringLayout.WEST, lCidade, 0, SpringLayout.WEST, lCodigo);
		lCidade.setForeground(Color.WHITE);
		lCidade.setFont(new Font("Arial", Font.BOLD, 12));
		pDados.add(lCidade);
		
		tCodigo = new JTextField();
		sl_pDados.putConstraint(SpringLayout.NORTH, tCodigo, 7, SpringLayout.NORTH, pDados);
		sl_pDados.putConstraint(SpringLayout.WEST, tCodigo, 34, SpringLayout.EAST, lCodigo);
		tCodigo.setFont(new Font("Arial", Font.BOLD, 12));
		tCodigo.setEditable(false);
		pDados.add(tCodigo);
		tCodigo.setColumns(10);
		
		tApelido = new JTextField();
		sl_pDados.putConstraint(SpringLayout.NORTH, tApelido, -3, SpringLayout.NORTH, lApelido);
		sl_pDados.putConstraint(SpringLayout.EAST, tApelido, 273, SpringLayout.EAST, lApelido);
		tApelido.setFont(new Font("Arial", Font.BOLD, 12));
		tApelido.setColumns(10);
		pDados.add(tApelido);
		
		tCidade = new JTextField();
		sl_pDados.putConstraint(SpringLayout.WEST, tCidade, 34, SpringLayout.EAST, lCidade);
		sl_pDados.putConstraint(SpringLayout.WEST, tApelido, 0, SpringLayout.WEST, tCidade);
		sl_pDados.putConstraint(SpringLayout.NORTH, tCidade, -3, SpringLayout.NORTH, lCidade);
		tCidade.setFont(new Font("Arial", Font.BOLD, 12));
		tCidade.setColumns(10);
		pDados.add(tCidade);
		
		JLabel lUf = new JLabel("UF");
		sl_pDados.putConstraint(SpringLayout.NORTH, lUf, 30, SpringLayout.SOUTH, lCidade);
		sl_pDados.putConstraint(SpringLayout.WEST, lUf, 0, SpringLayout.WEST, lCodigo);
		lUf.setForeground(Color.WHITE);
		lUf.setFont(new Font("Arial", Font.BOLD, 12));
		pDados.add(lUf);
		
		Ufs[] valoresUfs = Ufs.values();
		for(Ufs uf : valoresUfs) {
			
			ufs.addElement(uf.toString());
		}
		
		cbUf = new JComboBox<String>();
		sl_pDados.putConstraint(SpringLayout.EAST, tCodigo, 0, SpringLayout.EAST, cbUf);
		sl_pDados.putConstraint(SpringLayout.EAST, cbUf, 113, SpringLayout.EAST, lUf);
		cbUf.setFont(new Font("Arial", Font.BOLD, 12));
		cbUf.setModel(ufs);
		sl_pDados.putConstraint(SpringLayout.NORTH, cbUf, 23, SpringLayout.SOUTH, tCidade);
		sl_pDados.putConstraint(SpringLayout.WEST, cbUf, 59, SpringLayout.EAST, lUf);
		pDados.add(cbUf);
		
		carregarDados();
		
	}
	
	public void carregarDados() {
		
			
		if(pontoAtendimento != null) {
			
			this.tCodigo.setText(String.valueOf(pontoAtendimento.getCodigo()));
			this.tApelido.setText(pontoAtendimento.getApelido());
			this.tCidade.setText(pontoAtendimento.getCidade());
			this.cbUf.setSelectedItem(pontoAtendimento.getUf());
			
		}
		
	}
	
	public void limpaDados() {
		
		this.tCodigo.setText("");
		this.tApelido.setText("");
		this.tCidade.setText("");
		this.cbUf.setSelectedItem(0);
		
	}
	
	public void abreBuscaPontosAtendimento() {
		
		BuscaPontoAtendimento bpa = new BuscaPontoAtendimento(usuario, permissoes);
		
		bpa.getFrame().setVisible(true);
		
		frame.dispose();
		
	}
	
	public void abrirTela() {
		
		frame.setVisible(true);
		
	}
	
	public void fechaTela() {
		
		frame.dispose();
		
	}
}
