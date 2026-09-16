package dsj.gestorar.visual.buscas;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Window.Type;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JDialog;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import dsj.gestorar.modelo.Ar;
import dsj.gestorar.modelo.Hostname;
import dsj.gestorar.modelo.TituloUsuarios;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.persistencia.HostnameDAO;
import dsj.gestorar.visual.X;
import dsj.gestorar.visual.complemento.HostnameC;

public class BuscaHostnames {

    private JFrame frmHostnames;
    private HostnameC hc;
    private Usuario usuario;
    private JLabel lDisponivel, lLiberado, lRequisitado;
    private JTable tabela;
    private TituloUsuarios permissoes;

    public BuscaHostnames(Usuario usuario, TituloUsuarios permissoes) {
        this.usuario = usuario;
        this.permissoes = permissoes;
        initialize();
    }

    private void initialize() {
    	
        frmHostnames = new JFrame();
        frmHostnames.getContentPane().setBackground(new Color(0, 0, 0));
        frmHostnames.setTitle("Hostnames");
        frmHostnames.setType(Type.UTILITY);
        frmHostnames.setBounds(100, 100, 737, 370);
        frmHostnames.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frmHostnames.getContentPane().setLayout(null);
        frmHostnames.setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setBackground(new Color(0, 64, 128));
        painelPrincipal.setBounds(10, 11, 700, 309);
        frmHostnames.getContentPane().add(painelPrincipal);
        painelPrincipal.setLayout(null);

        JPanel painelBotoes = new JPanel();
        painelBotoes.setBackground(new Color(0, 64, 128));
        painelBotoes.setBounds(10, 252, 680, 46);
        painelPrincipal.add(painelBotoes);
        painelBotoes.setLayout(null);

        JButton botaoAdicionar = new JButton("Novo Hostname");
        botaoAdicionar.setFont(new Font("Arial", Font.BOLD, 12));
        botaoAdicionar.setBounds(557, 0, 123, 46);
        botaoAdicionar.addActionListener(e -> {

            if (permissoes.getPolitica().podeAdicionarHostname()) {

                FrameInterno fi = new FrameInterno(
                        usuario.getAr(),
                        BuscaHostnames.this,
                        TipoFrame.INSERIR,
                        frmHostnames);

                fi.abrirTela();

            } else {
                JOptionPane.showMessageDialog(frmHostnames,
                        "Você não tem permissão para adicionar hostnames",
                        "Acesso negado",
                        JOptionPane.WARNING_MESSAGE);
            }

        });
        painelBotoes.add(botaoAdicionar);

        JButton botaoVoltar = new JButton("Voltar");
        botaoVoltar.addActionListener(e -> {
            X pp = new X(usuario);
            pp.abrirTela();
            fechaTela();
        });
        botaoVoltar.setFont(new Font("Arial", Font.BOLD, 12));
        botaoVoltar.setBounds(0, 0, 123, 46);
        painelBotoes.add(botaoVoltar);

        JScrollPane painelTabela = new JScrollPane();
        painelTabela.setBounds(10, 52, 680, 189);
        painelPrincipal.add(painelTabela);

        JLabel lTitulo = new JLabel("Hostnames");
        lTitulo.setForeground(Color.WHITE);
        lTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lTitulo.setBounds(288, 11, 158, 30);
        painelPrincipal.add(lTitulo);
        
        tabela = new JTable();
        hc = new HostnameC(this);
        tabela.setModel(hc);
        painelTabela.setViewportView(tabela);
        hc.adicionaFuncionalidadeTabela(tabela);
        hc.adicionarPropriedadesTabela(tabela);
        hc.ajustarLarguraColunas(tabela);
        atualizaTabela();
    }

    public void fechaTela() {
        frmHostnames.setVisible(false);
        frmHostnames.dispose();
    }

    public void abrirTela() {
        frmHostnames.setVisible(true);
    }

    public void atualizaTabela() {

        try (HostnameDAO hDAO = new HostnameDAO()) {
            hc.atualizarTabela(hDAO.buscaHostnames());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void abreFrameInterno(Hostname hostname, BuscaHostnames bh) {
        new FrameInterno(hostname, bh, TipoFrame.ALTERAR, frmHostnames).abrirTela();
    }

    public class FrameInterno {

        private JDialog frame;
        private Hostname hostname;
        private BuscaHostnames bh;
        private Ar ar;
        private Component pai;

        public FrameInterno(Ar ar, BuscaHostnames bh, TipoFrame tipo, Component pai) {
            this.pai = pai;
            this.ar = ar;
            this.bh = bh;

            if (tipo.equals(TipoFrame.INSERIR)) {
                inicializaFrameInserir();
            } else if (tipo.equals(TipoFrame.ALTERAR)) {
                inicializaFrameAlterar();
            }
        }

        public FrameInterno(Hostname hostname, BuscaHostnames bh, TipoFrame tipo, Component pai) {
            this.pai = pai;
            this.hostname = hostname;
            this.bh = bh;

            if (tipo.equals(TipoFrame.INSERIR)) {
                inicializaFrameInserir();
            } else if (tipo.equals(TipoFrame.ALTERAR)) {
                inicializaFrameAlterar();
            }
        }

        private void inicializaFrameAlterar() {
            frame = new JDialog((JFrame) pai, true);
            frame.setResizable(false);
            frame.setSize(250, 250);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(pai);
            frame.setType(Type.UTILITY);
            frame.setBackground(Color.BLACK);

            JPanel painelPrincipal = new JPanel(null);

            JLabel lInfDisponivel = new JLabel("Disponível:");
            lInfDisponivel.setFont(new Font("Consolas", Font.BOLD, 12));
            lInfDisponivel.setBounds(10, 10, 100, 25);
            painelPrincipal.add(lInfDisponivel);

            lDisponivel = new JLabel(hostname.isDisponivel() ? "SIM" : "NÃO");
            lDisponivel.setForeground(hostname.isDisponivel() ? Color.GREEN : Color.RED);
            lDisponivel.setFont(new Font("Consolas", Font.BOLD, 12));
            lDisponivel.setBounds(120, 10, 100, 25);
            painelPrincipal.add(lDisponivel);

            JLabel lInfRequisitado = new JLabel("Requisitado:");
            lInfRequisitado.setFont(new Font("Consolas", Font.BOLD, 12));
            lInfRequisitado.setBounds(10, 40, 100, 25);
            painelPrincipal.add(lInfRequisitado);

            lRequisitado = new JLabel(hostname.isRequisitado() ? "SIM" : "NÃO");
            lRequisitado.setFont(new Font("Consolas", Font.BOLD, 12));
            lRequisitado.setForeground(hostname.isRequisitado() ? Color.GREEN : Color.RED);
            lRequisitado.setBounds(120, 40, 100, 25);
            painelPrincipal.add(lRequisitado);

            JLabel lInfLiberado = new JLabel("Liberado:");
            lInfLiberado.setFont(new Font("Consolas", Font.BOLD, 12));
            lInfLiberado.setBounds(10, 70, 100, 25);
            painelPrincipal.add(lInfLiberado);

            lLiberado = new JLabel(hostname.isLiberado() ? "SIM" : "NÃO");
            lLiberado.setFont(new Font("Consolas", Font.BOLD, 12));
            lLiberado.setForeground(hostname.isLiberado() ? Color.GREEN : Color.RED);
            lLiberado.setBounds(120, 70, 100, 25);
            painelPrincipal.add(lLiberado);

            JButton botaoResete = new JButton("Resetar hostname");
            botaoResete.setFont(new Font("Consolas", Font.BOLD, 12));
            botaoResete.setBounds(10, 120, 200, 30);
            botaoResete.addActionListener(e -> {

                if (permissoes.getPolitica().podeManipularHostnames()) {

                    try (HostnameDAO hDAO = new HostnameDAO()) {
                        if (hDAO.resetarHostname(hostname, usuario)) {
                            JOptionPane.showMessageDialog(frame, "Hostname foi resetado!",
                                    "Resetado", JOptionPane.INFORMATION_MESSAGE);
                            bh.atualizaTabela();
                            fechaTela();
                        } else {
                            JOptionPane.showMessageDialog(frame,
                                    "Erro ao resetar hostname!", "Erro!",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } else {
                    JOptionPane.showMessageDialog(frmHostnames,
                            "Você não tem permissão para manipular hostnames",
                            "Acesso negado",
                            JOptionPane.WARNING_MESSAGE);
                }

            });
            painelPrincipal.add(botaoResete);

            JButton botaoFechar = new JButton("Fechar");
            botaoFechar.setFont(new Font("Consolas", Font.BOLD, 12));
            botaoFechar.setBounds(10, 160, 200, 30);
            botaoFechar.addActionListener(e -> fechaTela());
            painelPrincipal.add(botaoFechar);

            frame.getContentPane().add(painelPrincipal);
        }

        private void inicializaFrameInserir() {
            frame = new JDialog((JFrame) pai, true);
            frame.setResizable(false);
            frame.setSize(250, 200);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(pai);
            frame.setType(Type.UTILITY);
            frame.setBackground(Color.BLACK);

            JPanel painelPrincipal = new JPanel(null);

            JTextField nomeNovoHostname = new JTextField();
            nomeNovoHostname.setBounds(10, 10, 200, 25);
            nomeNovoHostname.setFont(new Font("Consolas", Font.BOLD, 12));
            painelPrincipal.add(nomeNovoHostname);

            JButton botaoAdicionar = new JButton("Adicionar Hostname");
            botaoAdicionar.setBounds(10, 50, 200, 30);
            botaoAdicionar.setFont(new Font("Consolas", Font.BOLD, 12));
            botaoAdicionar.addActionListener(e -> {
                Hostname h = new Hostname(
                        0,
                        nomeNovoHostname.getText(),
                        false,
                        true,
                        true,
                        ar,
                        ""
                );

                try (HostnameDAO hDAO = new HostnameDAO()) {
                    if (hDAO.inserir(h, usuario)) {
                        JOptionPane.showMessageDialog(frame,
                                "Dados gravados com sucesso", "Informativo!",
                                JOptionPane.INFORMATION_MESSAGE);
                        bh.atualizaTabela();
                        fechaTela();
                    } else {
                        JOptionPane.showMessageDialog(frame,
                                "Erro ao gravar os dados", "Aviso!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            painelPrincipal.add(botaoAdicionar);

            JButton botaoCancelar = new JButton("Cancelar");
            botaoCancelar.setBounds(10, 90, 200, 30);
            botaoCancelar.setFont(new Font("Consolas", Font.BOLD, 12));
            botaoCancelar.addActionListener(e -> {
                bh.atualizaTabela();
                fechaTela();
            });
            painelPrincipal.add(botaoCancelar);

            frame.getContentPane().add(painelPrincipal);
        }

        public void fechaTela() {
            frame.setVisible(false);
            frame.dispose();
        }

        public void abrirTela() {
            frame.setVisible(true);
        }

    }

    enum TipoFrame {
        ALTERAR(1, "Alterar Hostname"),
        INSERIR(2, "Inserir Hostname");

        public int codigo;
        public String tipo;

        TipoFrame(int codigo, String tipo) {
            this.codigo = codigo;
            this.tipo = tipo;
        }
    }
}
