package dsj.gestorar.visual.complemento;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import dsj.gestorar.modelo.Agente;
import dsj.gestorar.modelo.Maquina;
import dsj.gestorar.modelo.PontoAtendimento;
import dsj.gestorar.persistencia.AgentesMaquinaDAO;
import dsj.gestorar.visual.buscas.BuscaMaquinas;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MaquinaC extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    private final String[] colunas = {"Código", "Hostname", "Situação", "Ponto", "Agentes"};
    private Object[][] dados = new Object[0][colunas.length];
    private final BuscaMaquinas bm;
    private JTable tabelaAtivos;

    public MaquinaC(BuscaMaquinas bm) {
        this.bm = bm;
    }

    public void setTabelaAtivos(JTable tabelaAtivos) {
        this.tabelaAtivos = tabelaAtivos;
    }

    public void adicionarFuncaoTabela(JTable tabela) {
        tabela.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (KeyEvent.VK_ENTER == e.getKeyCode()) {
                    abrirFormulario();
                }
            }
        });

        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    abrirFormulario();
                }
            }
        });
    }

    private void abrirFormulario() {
    	
        int linha = tabelaAtivos.getSelectedRow();
        
        if (linha >= 0 && linha < dados.length) {
        	
            Maquina maquina = new Maquina(
                    (Integer) dados[linha][0],
                    (String) dados[linha][1],
                    (String) dados[linha][2],
                    (String) dados[linha][3],
                    (String) dados[linha][4]
            );
            bm.abreFormularioGestores(maquina, bm);
            
        }
    }

    public void adicionarPropriedadesTabela(JTable tabela) {
    	
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setFont(new Font("Arial", Font.BOLD, 12));
        tabela.setSelectionBackground(Color.ORANGE);
        tabela.setGridColor(Color.BLUE);
        tabela.requestFocusInWindow();
        
    }

    public JTable tabelaAtivos(List<Maquina> lista) {
    	
        atualizarTabela(lista);
        ajustarLarguraColunas(tabelaAtivos);
        adicionarFuncaoTabela(tabelaAtivos);
        adicionarPropriedadesTabela(tabelaAtivos);
        return tabelaAtivos;
        
    }

    public void atualizarTabela(List<Maquina> maquinas) {
    	
        if (maquinas == null) 
        	maquinas = new ArrayList<Maquina>();

        dados = new Object[maquinas.size()][colunas.length];

        for (int i = 0; i < maquinas.size(); i++) {
        	
            Maquina m = maquinas.get(i);
            try (AgentesMaquinaDAO dao = new AgentesMaquinaDAO()) {
            	
                List<Agente> agentes = dao.listaAgentesMaquina(m.getCodigo(), m.getPa().getCodigo());
                String nomesAgentes;
                
                if(agentes.isEmpty() || agentes == null) {
                	
                	nomesAgentes = "Sem agente vinculado";
                	
                }else {
                	
                	nomesAgentes = agentes.stream()
                            .map(Agente::getNome)
                            .collect(Collectors.joining(", "));
                	
                }
                
                
               
                dados[i][0] = m.getCodigo();
                dados[i][1] = m.getNome();
                dados[i][2] = m.getSituacao();
                dados[i][3] = m.getPa().getApelido();
                dados[i][4] = nomesAgentes;
                
            } catch (Exception e) {
            	
                System.err.println(e);
                
            }
        }

        fireTableDataChanged();
    }

    private void ajustarLarguraColunas(JTable tabela) {
    	
        TableColumnModel model = tabela.getColumnModel();
        
        int[] larguras = {50, 130, 75, 225, 225};
        
        for (int i = 0; i < larguras.length; i++) {
        	
            TableColumn coluna = model.getColumn(i);
            coluna.setMinWidth(larguras[i]);
            coluna.setPreferredWidth(larguras[i]);
            coluna.setMaxWidth(larguras[i]);
            
        }
    }

    public DefaultComboBoxModel<PontoAtendimento> modeloPontos(List<PontoAtendimento> pontos) {
    	
        DefaultComboBoxModel<PontoAtendimento> modelo = new DefaultComboBoxModel<>();
        pontos.forEach(modelo::addElement);
        return modelo;
        
    }

    @Override
    public int getRowCount() {
        return dados.length;
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return dados[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int columnIndex) {
        return colunas[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}