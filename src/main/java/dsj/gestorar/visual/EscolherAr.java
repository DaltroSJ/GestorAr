package dsj.gestorar.visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dsj.gestorar.modelo.Ar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.JOptionPane;

public class EscolherAr extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private Ar arSelecionado = null;
    private final List<JPanel> paineis = new ArrayList<>();
    private final Map<JPanel, Ar> mapaAr = new HashMap<>();
    private final JPanel container;

    public EscolherAr(List<Ar> listaDeArs) {

        container = new JPanel(null);
        container.setBackground(new Color(0, 64, 128));
        int y = 5;

        for (Ar ar : listaDeArs) {
            JPanel painel = criaPainelAr(ar, y);
            container.add(painel);
            paineis.add(painel);
            mapaAr.put(painel, ar);
            y += 50;
        }

        container.setPreferredSize(new Dimension(270, y));

        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        setTitle("Escolha a Autoridade de Registro");
        setType(Type.UTILITY);
        setBounds(100, 100, 310, 321);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(0, 0, 0));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JScrollPane painelScroll = new JScrollPane();
        painelScroll.setBounds(10, 11, 272, 206);
        painelScroll.setViewportView(container);
        contentPanel.add(painelScroll);

        JPanel panel = new JPanel();
        panel.setBounds(10, 228, 272, 43);
        contentPanel.add(panel);
        panel.setLayout(null);

        JButton botaoSelecionar = new JButton("Entrar");
        botaoSelecionar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botaoSelecionar.setBounds(0, 0, 275, 43);
        panel.add(botaoSelecionar);

        botaoSelecionar.addActionListener(e -> {
            if (arSelecionado == null) {
                JOptionPane.showMessageDialog(EscolherAr.this,
                		"Selecione uma autoridade de registro.",
                		"Aviso",
                		JOptionPane.WARNING_MESSAGE);
                return;
            }
            dispose();
        });
    }

    private JPanel criaPainelAr(Ar ar, int y) {
        JPanel painel = new JPanel(null);
        painel.setBounds(5, y, 260, 75);
        painel.setBackground(Color.LIGHT_GRAY);

        JLabel lNome = new JLabel(ar.getNome());
        lNome.setFont(new Font("Segoe UI",Font.BOLD,16));
        lNome.setBounds(10, 10, 230, 25);
        painel.add(lNome);

        painel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        painel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (JPanel p : paineis) {
                    p.setBackground(Color.LIGHT_GRAY);
                }

                painel.setBackground(Color.GRAY);
                arSelecionado = mapaAr.get(painel);
            }
        });

        return painel;
    }

    public Ar getArSelecionado() {
        return arSelecionado;
    }
} 