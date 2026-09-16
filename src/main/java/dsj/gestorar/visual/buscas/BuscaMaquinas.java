package dsj.gestorar.visual.buscas;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingWorker;

import dsj.gestorar.modelo.Maquina;
import dsj.gestorar.modelo.TituloUsuarios;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.persistencia.MaquinaDAO;
import dsj.gestorar.visual.X;
import dsj.gestorar.visual.complemento.MaquinaC;
import dsj.gestorar.visual.formularios.FormularioMaquina;

import javax.swing.JPanel;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.awt.Color;
import java.awt.Window.Type;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

public class BuscaMaquinas {

	private JFrame frame;
	private JScrollPane spTabela;
	private MaquinaC mc;
	private JButton bNovaMaquina;
	private JButton bVoltar;
	private JTable tabela;
	private JLabel lAtivas;
	private JLabel lDesativadas;
	private JLabel lPendentes;
	private JLabel lTodas;
	private Usuario usuario;
	private JLabel lMaquinas;
	private TituloUsuarios permissoes;
	
	public BuscaMaquinas(Usuario usuario, TituloUsuarios permissoes) {
		
		this.usuario = usuario;
		this.permissoes = permissoes;
		initialize();
		
	}
	
	
	private void initialize() {

		frame = new JFrame();
		frame.setType(Type.UTILITY);
		frame.getContentPane().setBackground(new Color(0, 0, 0));
		frame.setBounds(100, 100, 756, 479);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JPanel pPrincipal = new JPanel();
		pPrincipal.setBounds(10, 10, 727, 424);
		pPrincipal.setBackground(new Color(0, 64, 128));
		frame.getContentPane().add(pPrincipal);
		
		pPrincipal.setLayout(null);
		
		JPanel pBusca = new JPanel();
		pBusca.setBounds(10, 10, 705, 40);
		pBusca.setBackground(new Color(0, 64, 128));
		pPrincipal.add(pBusca);
		pBusca.setLayout(null);
		
		lMaquinas = new JLabel("Maquinas");
		lMaquinas.setForeground(new Color(255, 255, 255));
		lMaquinas.setFont(new Font("Arial", Font.BOLD, 24));
		lMaquinas.setBounds(279, 11, 123, 29);
		pBusca.add(lMaquinas);
		
		ButtonGroup group = new ButtonGroup();
        String[] estados = {"Todas", "ATIVA", "DESATIVADA", "PENDENTE"};
        int[] posicoes = {6, 122, 238, 378};
        JLabel[] labels = new JLabel[4];

        for (int i = 0; i < estados.length; i++) {
        	
            String estado = estados[i];
            int posX = posicoes[i];

            JRadioButton rb = new JRadioButton();
            rb.setBounds(posX, 7, 20, 20);
            rb.setSelected(i == 0);
            rb.addActionListener(e -> carregarSituacaoAsync(estado));
            pBusca.add(rb);
            group.add(rb);

            JLabel label = new JLabel(estado + ": 0");
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Arial", Font.BOLD, 12));
            label.setBounds(posX + 26, 7, 100, 20);
            pBusca.add(label);
            labels[i] = label;
        }

        lTodas = labels[0];
        lAtivas = labels[1];
        lDesativadas = labels[2];
        lPendentes = labels[3];
        
        mc = new MaquinaC(this);
		tabela = new JTable(mc);
        
		mc.setTabelaAtivos(tabela);
		mc.adicionarFuncaoTabela(tabela);
		mc.adicionarPropriedadesTabela(tabela);

		spTabela = new JScrollPane(tabela);
		spTabela.setBounds(10, 61, 705, 290);
		spTabela.setPreferredSize(tabela.getPreferredSize());
		pPrincipal.add(spTabela);
		
		bNovaMaquina = new JButton("Nova Maquina");
		bNovaMaquina.setBounds(472, 362, 243, 40);
		bNovaMaquina.setFont(new Font("Arial", Font.BOLD, 12));
		bNovaMaquina.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				if(permissoes.getPolitica().podeAdicionarMaquina()) {
					
					fechaTela();
					FormularioMaquina fm = new FormularioMaquina(usuario, null, BuscaMaquinas.this, permissoes);
					fm.abrirTela();
					fm.limpaCampos();
					fm.setSituacaoBExcluir(false);
					fm.setSituacaoBNova(false);
					
				}else {
					
					JOptionPane.showMessageDialog(frame,
							"Você não tem permissão para isso! ",
							"Acesso negado",
							JOptionPane.WARNING_MESSAGE);
					
				}
				
				
				
			}
		});
		pPrincipal.add(bNovaMaquina);
		
		bVoltar = new JButton("Voltar");
		bVoltar.setBounds(10, 362, 120, 40);
		bVoltar.setFont(new Font("Arial", Font.BOLD, 12));
		bVoltar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				fechaTela();
				
				X pp = new X(usuario);
				
				pp.abrirTela();
				
			}
		});
		pPrincipal.add(bVoltar);
		
		atualizarTabelaPorSituacao("Todas");
		carregaDadosContagens();
	}
	
	public void abrirTela() {
		
		frame.setVisible(true);
		
	}
	
	public void fechaTela() {
		
		frame.dispose();
		
	}
	
	private void carregarSituacaoAsync(String situacao) {

	    new SwingWorker<List<Maquina>, Void>() {

	        @Override
	        protected List<Maquina> doInBackground() {
	            try (MaquinaDAO mDAO = new MaquinaDAO()) {
	            	
	                return mDAO.listaMaquinas(situacao);
	                
	            } catch (SQLException e) {
	            	
					e.printStackTrace();
					
				} catch (Exception e) {
					
					e.printStackTrace();
					
				}
				return null;
	        }

	        @Override
	        protected void done() {
	        	
	            try {
	            	
	                List<Maquina> maquinas = get();
	                mc.atualizarTabela(maquinas);
	                
	            } catch (Exception e) {
	            	
	                JOptionPane.showMessageDialog(frame, 
	                    "Erro ao carregar máquinas: " + e.getMessage());
	            }
	        }

	    }.execute();
	}
	
	private void atualizarTabelaPorSituacao(String situacao) {

        try (MaquinaDAO mDAO = new MaquinaDAO()) {
        	
            mc.atualizarTabela(mDAO.listaMaquinas(situacao));
            
        } catch (Exception e) {
        	
            JOptionPane.showMessageDialog(frame, "Erro ao carregar máquinas: " + e.getMessage());
            
        }
    }
	
    public void carregaDadosContagens() {
    	
        String[] situacoes = {"ATIVA", "DESATIVADA", "PENDENTE"};
        int[] totais = new int[3];
        int totalGeral = 0;

        for (int i = 0; i < situacoes.length; i++) {
        	
            try (MaquinaDAO mDAO = new MaquinaDAO()) {
            	
                totais[i] = mDAO.contaMaquinasSituacao(situacoes[i]);
                totalGeral += totais[i];
                
            } catch (Exception e) {
            	
                System.err.println(e);
                
            }
        }

        lAtivas.setText("Ativas: " + totais[0]);
        lDesativadas.setText("Desativadas: " + totais[1]);
        lPendentes.setText("Pendentes: " + totais[2]);
        lTodas.setText("Todas: " + totalGeral);
    }

    public void abreFormularioGestores(Maquina maquinaTabela, BuscaMaquinas bm) {
    	
        try (MaquinaDAO mDAO = new MaquinaDAO()) {
        	
            Maquina maquina = mDAO.carregaMaquina(maquinaTabela.getCodigo());
            
            FormularioMaquina fm = new FormularioMaquina(usuario, maquina, null, BuscaMaquinas.this, permissoes);
            
            fm.setBm(bm);
            fm.abrirTela();
            fechaTela();
            
        } catch (Exception ex) {
        	
            JOptionPane.showMessageDialog(frame, "Erro ao abrir formulário: " + ex.getMessage());
            
        }
    }
				
		
	
}
