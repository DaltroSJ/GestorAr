package dsj.gestorar.visual.complemento;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import dsj.gestorar.modelo.Agente;
import dsj.gestorar.modelo.PontoAtendimento;
import dsj.gestorar.visual.buscas.BuscaAgente;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AgenteC extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private final String[] colunas = {"Código", "Nome Agente", "CPF", "Ponto"};
    private Object[][] dados = new Object[0][colunas.length];
    private final BuscaAgente ba;
    private JTable tabelaAtivos;

    public AgenteC(BuscaAgente ba) {
        this.ba = ba;
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
            Agente agente = new Agente(
                    (Integer) dados[linha][0],
                    (String) dados[linha][1],
                    (String) dados[linha][2],
                    new PontoAtendimento((String) dados[linha][3])
            );
            ba.abreFormularioAgente(agente);
        }
    }

    public void adicionarPropriedadesTabela(JTable tabela) {
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setFont(new Font("Arial", Font.BOLD, 12));
        tabela.setSelectionBackground(Color.ORANGE);
        tabela.setGridColor(Color.BLUE);
        tabela.requestFocusInWindow();
    }

    public JTable tabelaAtivos(List<Agente> lista) {
        atualizarTabela(lista);
        tabelaAtivos = new JTable(this);
        ajustarLarguraColunas(tabelaAtivos);
        adicionarFuncaoTabela(tabelaAtivos);
        adicionarPropriedadesTabela(tabelaAtivos);
        return tabelaAtivos;
    }

    public void atualizarTabela(List<Agente> agentes) {
        if (agentes == null) throw new IllegalArgumentException("Lista de agentes não pode ser nula");

        dados = new Object[agentes.size()][colunas.length];
        for (int i = 0; i < agentes.size(); i++) {
            Agente a = agentes.get(i);
            dados[i][0] = a.getCodigo();
            dados[i][1] = a.getNome();
            dados[i][2] = a.getCpf();
            dados[i][3] = a.getPontoAtendimento().getApelido();
        }
        fireTableDataChanged();
    }

    private void ajustarLarguraColunas(JTable tabela) {
        TableColumnModel model = tabela.getColumnModel();
        int[] larguras = {50, 300, 80, 300};
        for (int i = 0; i < larguras.length; i++) {
            TableColumn coluna = model.getColumn(i);
            coluna.setMinWidth(larguras[i]);
            coluna.setPreferredWidth(larguras[i]);
            coluna.setMaxWidth(larguras[i]);
        }
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