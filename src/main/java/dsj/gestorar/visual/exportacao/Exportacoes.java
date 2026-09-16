package dsj.gestorar.visual.exportacao;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.SwingConstants;

import dsj.gestorar.exportacao.InventarioAgentes;
import dsj.gestorar.exportacao.InventarioMaquinas;
import dsj.gestorar.exportacao.InventarioProgramas;
import dsj.gestorar.modelo.Meses;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.utilitarios.Mascaras;
import dsj.gestorar.utilitarios.Verificacoes;
import dsj.gestorar.visual.X;

import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JFormattedTextField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Window.Type;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Exportacoes {

	private JFrame frmExportao;
	private JTextField tDiretorio;
	private File diretoriaInicial = new File(Verificacoes.caminhoPastaPrincipal);
	private JButton bGerar;
	private JButton bBuscar;
	private JFormattedTextField tDataInicial, tDataFinal;
	private JCheckBox chekIProgramas;
	private JCheckBox chekMes;
	private JCheckBox chekIAtivos;
	private int valorBarraProgresso1;
	private int valorBarraProgresso2;
	private Usuario usuario;
	
	public void setValorBarraProgresso1(int valorBarraProgresso1) {
		
		this.valorBarraProgresso1 = valorBarraProgresso1;
		
	}
	
	public int getValorBarraProgresso1() {
		
		return valorBarraProgresso1;
		
	}
	public void setValorBarraProgresso2(int valorBarraProgresso2) {
		
		this.valorBarraProgresso2 = valorBarraProgresso2;
		
	}
	
	public int getValorBarraProgresso2() {
		
		return valorBarraProgresso2;
		
	}
	

	public Exportacoes(Usuario usuario) {
		
		this.usuario = usuario;
		initialize();
	}

	private void initialize() {
		frmExportao = new JFrame();
		frmExportao.setTitle("Exportação");
		frmExportao.setType(Type.UTILITY);
		frmExportao.setResizable(false);
		frmExportao.setFont(new Font("Arial", Font.BOLD, 12));
		frmExportao.getContentPane().setBackground(new Color(0, 0, 0));
		frmExportao.setBounds(100, 100, 650, 350);
		frmExportao.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmExportao.getContentPane().setLayout(null);
		frmExportao.setLocationRelativeTo(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 64, 128));
		panel.setBounds(10, 11, 614, 289);
		frmExportao.getContentPane().add(panel);
		panel.setLayout(null);
		
		chekMes = new JCheckBox("Relatorio Mensal");
		chekMes.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				
				if(chekMes.isSelected()) {
					
					acaoMarcaInventarioMes();
					
				}else {
					
					acaoDesmarcaInventarioMes();
					
				}
				
			}
		});
		chekMes.setBackground(new Color(0, 64, 128));
		chekMes.setForeground(new Color(255, 255, 255));
		chekMes.setFont(new Font("Arial", Font.BOLD, 12));
		chekMes.setHorizontalAlignment(SwingConstants.LEFT);
		chekMes.setBounds(10, 110, 140, 23);
		panel.add(chekMes);
		
		chekIAtivos = new JCheckBox("Inventario de Ativos");
		chekIAtivos.setToolTipText("Relatorio com todos os dados da maquina de acordo com a data selecionada.");
		chekIAtivos.setHorizontalAlignment(SwingConstants.LEFT);
		chekIAtivos.setForeground(Color.WHITE);
		chekIAtivos.setFont(new Font("Arial", Font.BOLD, 12));
		chekIAtivos.setBackground(new Color(0, 64, 128));
		chekIAtivos.setBounds(10, 140, 140, 23);
		panel.add(chekIAtivos);
		
		chekIProgramas = new JCheckBox("Inventario de Programas");
		chekIProgramas.setToolTipText("Relatorio de todas as versões dos programas instalados nas maquinas ativas na data selecionada.");
		chekIProgramas.setHorizontalAlignment(SwingConstants.LEFT);
		chekIProgramas.setForeground(Color.WHITE);
		chekIProgramas.setFont(new Font("Arial", Font.BOLD, 12));
		chekIProgramas.setBackground(new Color(0, 64, 128));
		chekIProgramas.setBounds(10, 170, 171, 23);
		panel.add(chekIProgramas);
		
		tDataInicial = new JFormattedTextField(Mascaras.mascaraDatasFormularios());
		tDataInicial.setFont(new Font("Arial", Font.BOLD, 12));
		tDataInicial.setBounds(300, 111, 85, 20);
		panel.add(tDataInicial);
		
		tDataFinal = new JFormattedTextField(Mascaras.mascaraDatasFormularios());
		tDataFinal.setFont(new Font("Arial", Font.BOLD, 12));
		tDataFinal.setBounds(475, 111, 85, 20);
		panel.add(tDataFinal);
		
		JLabel lParametros = new JLabel("Parametros");
		lParametros.setHorizontalAlignment(SwingConstants.CENTER);
		lParametros.setForeground(new Color(255, 255, 255));
		lParametros.setFont(new Font("Arial", Font.BOLD, 12));
		lParametros.setBounds(41, 76, 140, 20);
		panel.add(lParametros);
		
		JSeparator separaBaixo = new JSeparator();
		separaBaixo.setBounds(10, 203, 594, 2);
		panel.add(separaBaixo);
		
		JSeparator separaCima = new JSeparator();
		separaCima.setBounds(10, 98, 200, 5);
		panel.add(separaCima);
		
		JLabel lDataInicial = new JLabel("Data Inicial");
		lDataInicial.setHorizontalAlignment(SwingConstants.CENTER);
		lDataInicial.setForeground(Color.WHITE);
		lDataInicial.setFont(new Font("Arial", Font.BOLD, 12));
		lDataInicial.setBounds(220, 110, 70, 20);
		panel.add(lDataInicial);
		
		JLabel lblDataFinal = new JLabel("Data Final");
		lblDataFinal.setHorizontalAlignment(SwingConstants.CENTER);
		lblDataFinal.setForeground(Color.WHITE);
		lblDataFinal.setFont(new Font("Arial", Font.BOLD, 12));
		lblDataFinal.setBounds(395, 111, 70, 20);
		panel.add(lblDataFinal);
		
		JLabel lblLocalDeGravao = new JLabel("Local de gravação");
		lblLocalDeGravao.setHorizontalAlignment(SwingConstants.CENTER);
		lblLocalDeGravao.setForeground(Color.WHITE);
		lblLocalDeGravao.setFont(new Font("Arial", Font.BOLD, 12));
		lblLocalDeGravao.setBounds(222, 141, 338, 20);
		panel.add(lblLocalDeGravao);
		
		tDiretorio = new JTextField();
		tDiretorio.setFont(new Font("Arial", Font.BOLD, 12));
		tDiretorio.setBounds(220, 171, 301, 20);
		panel.add(tDiretorio);
		tDiretorio.setColumns(10);
		
		bBuscar = new JButton("...");
		bBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser buscador = new JFileChooser(diretoriaInicial);
				
				buscador.setFont(new Font("Arial", Font.BOLD, 12));
				
				buscador.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				int retorno = buscador.showOpenDialog(null);
				
				if(retorno == JFileChooser.APPROVE_OPTION) {
					
					String diretorioSelecionado = buscador.getSelectedFile().getAbsolutePath() + File.separator;
					
					tDiretorio.setText(diretorioSelecionado);
					
				}
			}
				
				
		});
		bBuscar.setFont(new Font("Arial", Font.BOLD, 14));
		bBuscar.setBounds(531, 169, 29, 23);
		panel.add(bBuscar);
		
		bGerar = new JButton("Gerar Relatorio");
		bGerar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(chekIAtivos.isSelected() && chekIProgramas.isSelected()) {
					
					if(!verificaDatas()) {
						
						return;
					}else {
						
						if(!verificaCaminhoDiretorio()) {
							return;
						}
						
					}
					
					exportaMaquinas(tDiretorio.getText(), tDataInicial.getText(), tDataFinal.getText());
					exportaProgramas(tDiretorio.getText(), tDataInicial.getText(), tDataFinal.getText());
					System.gc();
					
				}else if(chekIAtivos.isSelected() && !chekIProgramas.isSelected()){
					
					if(!verificaDatas()) {
						
						return;
					}else {
						
						if(!verificaCaminhoDiretorio()) {
							return;
						}
						
					}
					
					exportaMaquinas(tDiretorio.getText(), tDataInicial.getText(), tDataFinal.getText());
					System.gc();
					
				}else if(!chekIAtivos.isSelected() && chekIProgramas.isSelected()){
					
					if(!verificaDatas()) {
						
						return;
					}else {
						
						if(!verificaCaminhoDiretorio()) {
							return;
						}
						
					}
					
					exportaProgramas(tDiretorio.getText(), tDataInicial.getText(), tDataFinal.getText());
					System.gc();
					
				}else {
					
					JOptionPane.showMessageDialog(null, "Escolha uma das opções de exportação!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
					
					return;
				}
				
				JOptionPane.showMessageDialog(null, "Exportação obteve sucesso! caminho: " + tDiretorio.getText(), "Aviso", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		bGerar.setFont(new Font("Arial", Font.BOLD, 14));
		bGerar.setBounds(300, 239, 151, 39);
		panel.add(bGerar);
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				abreTelaPrincipal();
				
			}
		});
		btnVoltar.setFont(new Font("Arial", Font.BOLD, 14));
		btnVoltar.setBounds(10, 239, 151, 39);
		panel.add(btnVoltar);
		
		JLabel lPrincipal= new JLabel("Relatórios");
		lPrincipal.setBounds(10, 11, 182, 32);
		panel.add(lPrincipal);
		lPrincipal.setForeground(new Color(255, 255, 255));
		lPrincipal.setHorizontalAlignment(SwingConstants.CENTER);
		lPrincipal.setFont(new Font("Arial", Font.BOLD, 24));
		
		JSeparator separaCima_1 = new JSeparator();
		separaCima_1.setBounds(220, 98, 384, 2);
		panel.add(separaCima_1);
		
		JButton btnAgentes = new JButton("Relatório de Agentes");
		btnAgentes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser buscador = new JFileChooser(diretoriaInicial);
				
				buscador.setFont(new Font("Arial", Font.BOLD, 12));
				
				buscador.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				int retorno = buscador.showOpenDialog(null);
				
				if(retorno == JFileChooser.APPROVE_OPTION) {
					
					String diretorioSelecionado = buscador.getSelectedFile().getAbsolutePath() + File.separator;
					
					String x = diretorioSelecionado+"InventarioAgentes.pdf";
					
					new InventarioAgentes(usuario).exportarDadosExcel(x);
					
					JOptionPane.showMessageDialog(frmExportao, "Relatorio gerado com sucesso em: "+x, "Confirmação", JOptionPane.INFORMATION_MESSAGE);
				}
				
				
				
			}
		});
		btnAgentes.setFont(new Font("Arial", Font.BOLD, 14));
		btnAgentes.setBounds(404, 64, 200, 23);
		panel.add(btnAgentes);
		setarMesPassado(tDataInicial, tDataFinal);
		setarAreaDeTrabalho(tDiretorio);
		
	}
	
	public void setarAreaDeTrabalho(JTextField campo) {
	    String home = System.getProperty("user.home");
	    campo.setText(home + File.separator);
	}
	
	private void acaoMarcaInventarioMes() {
		
		this.chekIProgramas.setSelected(true);
		this.chekIAtivos.setSelected(true);
		this.chekIAtivos.setEnabled(false);
		this.chekIProgramas.setEnabled(false);
	}
	
	private void acaoDesmarcaInventarioMes() {
		
		this.chekIProgramas.setSelected(false);
		this.chekIAtivos.setSelected(false);
		this.chekIAtivos.setEnabled(true);
		this.chekIProgramas.setEnabled(true);
	}
	
	public String mesAtual() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		
		Date dataMes = new Date();
		
		String data = sdf.format(dataMes);
		
		switch(data) {
		
		case "01": return Meses.Janeiro.name();
		case "02": return Meses.Fevereiro.name(); 
		case "03": return Meses.Março.name(); 
		case "04": return Meses.Abril.name(); 
		case "05": return Meses.Maio.name(); 
		case "06": return Meses.Junho.name(); 
		case "07": return Meses.Julho.name(); 
		case "08": return Meses.Agosto.name(); 
		case "09": return Meses.Setembro.name(); 
		case "10": return Meses.Outubro.name(); 
		case "11": return Meses.Novembro.name();
		case "12": return Meses.Dezembro.name();
		
		default: return "SEM_MES";
		
		}
		
	}
	
	public void setarMesPassado(JFormattedTextField inicio, JFormattedTextField fim) {

	    YearMonth mesPassado = YearMonth.now().minusMonths(1);

	    LocalDate dataInicio = mesPassado.atDay(1);
	    LocalDate dataFim = mesPassado.atEndOfMonth();

	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	    inicio.setText(dataInicio.format(formatter));
	    fim.setText(dataFim.format(formatter));
	}
	
	public void fechaTela() {
		
		frmExportao.dispose();
		
	}
	
	public void abrirTela() {
		
		this.frmExportao.setVisible(true);
		
	}
	
	public void abreTelaPrincipal() {
		
X pp = new X(usuario);
		
		pp.abrirTela();
		
		fechaTela();
		
	}
	
	
	public void exportaProgramas(String diretorio, String data1, String data2) {
			
		InventarioProgramas ip = new InventarioProgramas(usuario);
		
		ip.exportaPlanilhaInventarioAtivosNovo(diretorio, data1, data2);
		
		
	}
	
	public void exportaMaquinas(String diretorio, String data1, String data2) {
		
		InventarioMaquinas im = new InventarioMaquinas(usuario);
		
		im.exportar(diretorio, data1, data2);	
		
	}
	
	public boolean verificaDatas() {
		
		if(tDataInicial.getText().isEmpty() || tDataInicial.getText().equals("  .  .    ")) {
			
			JOptionPane.showMessageDialog(null, "Digite a data inicial","Aviso",JOptionPane.INFORMATION_MESSAGE);
			
			return false;
			
		}else if(tDataFinal.getText().isEmpty() || tDataFinal.getText().equals("  .  .    ")) {
			
			JOptionPane.showMessageDialog(null, "Digite a data final","Aviso",JOptionPane.INFORMATION_MESSAGE);
			
			return false;
			
		}else {
			return true;
		}
		
	}
	
	public boolean verificaCaminhoDiretorio() {
		
		if(tDiretorio.getText().isEmpty() || tDiretorio.getText().equals("")) {
			
			JOptionPane.showMessageDialog(null, "Escolha o caminho de gravação do arquivo","Aviso",JOptionPane.INFORMATION_MESSAGE);
			
			return false;
			
		}else {
			
			return true;
			
		}
		
	}
}
