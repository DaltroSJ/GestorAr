package dsj.gestorar.visual.config;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dsj.gestorar.modelo.TiposAmbiente;
import dsj.gestorar.modelo.TiposBanco;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.visual.X;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Configuracoes extends JFrame {

	private static final long serialVersionUID = 1L;
    private final JComboBox<String> cAmbiente;
    private final JComboBox<String> cTipoBanco;
    private Usuario usuario;
    

	public Configuracoes(Usuario usuario) {
		this.usuario = usuario;
		setType(Type.UTILITY);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 300);
        JPanel contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLocationRelativeTo(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel painelPrincipal = new JPanel();
		painelPrincipal.setBounds(10, 11, 364, 239);
		painelPrincipal.setBackground(new Color(0, 64, 124));
		contentPane.add(painelPrincipal);
		painelPrincipal.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Configurações");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 20));
		lblNewLabel.setBounds(10, 11, 344, 29);
		painelPrincipal.add(lblNewLabel);
		
		JLabel lblTipoAmbiente = new JLabel("Tipo ambiente");
		lblTipoAmbiente.setEnabled(false);
		lblTipoAmbiente.setHorizontalAlignment(SwingConstants.CENTER);
		lblTipoAmbiente.setForeground(Color.WHITE);
		lblTipoAmbiente.setFont(new Font("Arial", Font.BOLD, 12));
		lblTipoAmbiente.setBounds(10, 51, 120, 20);
		painelPrincipal.add(lblTipoAmbiente);
		
		TiposAmbiente[] tipos = TiposAmbiente.values();

        DefaultComboBoxModel<String> tiposAmbiente = new DefaultComboBoxModel<>();
        for(TiposAmbiente ta : tipos) {
			
			tiposAmbiente.addElement(ta.toString());
			
		}
		
		cAmbiente = new JComboBox<String>();
		cAmbiente.setEnabled(false);
		cAmbiente.setFont(new Font("Arial", Font.BOLD, 12));
		cAmbiente.setBounds(140, 51, 210, 20);
		cAmbiente.setModel(tiposAmbiente);
		painelPrincipal.add(cAmbiente);
		
		JButton bVoltar = new JButton("Voltar");
		bVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				fechaTela();
				abrePaginaInicial();
			}
		});
		bVoltar.setFont(new Font("Arial", Font.BOLD, 12));
		bVoltar.setBounds(10, 190, 120, 40);
		painelPrincipal.add(bVoltar);
		
		JButton bSalvar = new JButton("Salvar");
		bSalvar.setEnabled(false);
		bSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//alteraConfiguracoes("ambiente", Objects.requireNonNull(cAmbiente.getSelectedItem()).toString());
				//alteraConfiguracoes("banco", Objects.requireNonNull(cTipoBanco.getSelectedItem().toString()));
				
				JOptionPane.showMessageDialog(null, "O sisteam vai ser fechado", "Aviso",  JOptionPane.INFORMATION_MESSAGE);
				
				dispose();
				
				fechaSistema();
				
			}
		});
		bSalvar.setFont(new Font("Arial", Font.BOLD, 12));
		bSalvar.setBounds(234, 190, 120, 40);
		painelPrincipal.add(bSalvar);
		
		JLabel lblTipoBancoDe = new JLabel("Tipo banco de dados");
		lblTipoBancoDe.setEnabled(false);
		lblTipoBancoDe.setHorizontalAlignment(SwingConstants.CENTER);
		lblTipoBancoDe.setForeground(Color.WHITE);
		lblTipoBancoDe.setFont(new Font("Arial", Font.BOLD, 12));
		lblTipoBancoDe.setBounds(10, 82, 120, 20);
		lblTipoBancoDe.setVisible(false);
		painelPrincipal.add(lblTipoBancoDe);
		
		cTipoBanco = new JComboBox<String>();
		cTipoBanco.setEnabled(false);
		cTipoBanco.setBounds(140, 82, 210, 22);
		cTipoBanco.setVisible(false);
		
		TiposBanco[] tiposBancos = TiposBanco.values();

        DefaultComboBoxModel<String> tiposBancos1 = new DefaultComboBoxModel<>();
        for(TiposBanco tb : tiposBancos) {
			
        	tiposBancos1.addElement(tb.toString());
			
		}
        cTipoBanco.setModel(tiposBancos1);
		
		painelPrincipal.add(cTipoBanco);
		
		carregaConfiguracoes();
		
		dsj.gestorar.utilitarios.Configuracoes.carregarPropriedades();
		
		if(dsj.gestorar.utilitarios.Configuracoes.tipoAmbiente.equals(TiposAmbiente.HOMOLOGAÇÃO.toString())) {
			
			lblTipoBancoDe.setVisible(true);
			cTipoBanco.setVisible(true);
		}
		
		
	}
	
	public void fechaTela() {
		
		dispose();
		
	}
	
	public void abrirTela() {
		
		setVisible(true);
		
	}
	
	public void carregaConfiguracoes() {
		
		this.cAmbiente.setSelectedItem(dsj.gestorar.utilitarios.Configuracoes.tipoAmbiente);
		this.cTipoBanco.setSelectedItem(dsj.gestorar.utilitarios.Configuracoes.tipoBanco);
		
	}
	
	public void alteraConfiguracoes(String key, String valor) {
		
		
		dsj.gestorar.utilitarios.Configuracoes.atualizarPropriedade(key, valor);
		
		
	}
	
	public void abrePaginaInicial() {
		
		X pp = new X(usuario);
		
		pp.abrirTela();
		
		fechaTela();
		
	}
	
	public void fechaSistema() {
		
		System.exit(0);
		
	}
}
