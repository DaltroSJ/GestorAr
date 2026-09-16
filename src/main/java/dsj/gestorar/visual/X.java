package dsj.gestorar.visual;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

import dsj.gestorar.modelo.*;
import dsj.gestorar.persistencia.ArDAO;
import dsj.gestorar.visual.buscas.*;
import dsj.gestorar.visual.config.Configuracoes;
import dsj.gestorar.visual.exportacao.Exportacoes;
import dsj.gestorar.visual.formularios.*;

public class X {
	
	public static class Icones{
		
		private static final Map<String, ImageIcon> cacheIcones = new HashMap<>();
		
		public static ImageIcon obterImagem(String nome) {

		    return cacheIcones.computeIfAbsent(nome, n -> {

		        URL url = Icones.class.getClassLoader()
		                .getResource(n);

		        if (url == null) {
		            System.err.println("Ícone não encontrado: " + n);
		            return new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
		        }

		        return new ImageIcon(url);
		    });
		} 
	}
	

    private JFrame frame;
    private final Usuario usuario;
    private final TituloUsuarios permissoes;

    private static final Map<String, ImageIcon> cacheDark = new HashMap<>();
    private static final Map<String, ImageIcon> cacheEscaladas = new HashMap<>();
    private Ar ar;

    public X(Usuario usuario, Ar ar) {
    	System.gc();
        this.usuario = usuario;
        this.permissoes = TituloUsuarios.fromCodigo(usuario.getCodigoTitulo());
        this.ar = ar;
        conferirDadosAr(usuario.getAr().getCodigo());
        initialize();
    }
    
    public X(Usuario usuario) {
        this.usuario = usuario;
        this.permissoes = TituloUsuarios.fromCodigo(usuario.getCodigoTitulo());
        conferirDadosAr(usuario.getAr().getCodigo());
        initialize();
    }

    private void initialize() {

        frame = new JFrame();
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        frame.add(painelPrincipal);

        JPanel painel1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 30));
        painelPrincipal.add(painel1, BorderLayout.CENTER);

        adicionarBotoesTela(painel1);
    }

    private void adicionarBotoesTela(JPanel p) {

        p.add(criarItem("Máquinas", "maquinas.png", 
                () -> new BuscaMaquinas(usuario, permissoes).abrirTela()));

        p.add(criarItem("Agentes de Registro", "agentes.png",
                () -> new BuscaAgente(usuario, permissoes).abrirTela()));

        p.add(criarItem("Prontos de Atendimento", "ponto_atendimento.png",
                () -> new BuscaPontoAtendimento(usuario, permissoes).abrirTela()));

        p.add(criarItem("Autoridade de Registro", "ar.png",
                () -> new FormularioAr(usuario, ar, this.frame, this,  permissoes).abrirTela()));
        
        p.add(criarItem("Gestores", "gestores.png",
                () -> new BuscaGestores(usuario, permissoes).abrirTela()));

        p.add(criarItem("Relatórios", "exportacao.png",
                () -> new Exportacoes(usuario).abrirTela()));

        p.add(criarItem("Máquinas Ativas", "ativacao.png",
                () -> new BuscaAtivos(usuario, permissoes).abrirTela()));

        p.add(criarItem("Liberar Hostnames", "hostnames.png",
                () -> new BuscaHostnames(usuario, permissoes).abrirTela()));

        p.add(criarItem("Registros de Desativações", "desativacao.png",
                () -> new BuscaDesativacao(usuario).abrirTela()));

        if(permissoes == TituloUsuarios.ADMINISTRADOR) {
        	
        	p.add(criarItem("Dados dos Usuários", "usuario.png",
                    () -> new BuscaUsuarios(usuario, permissoes).abreTela()));
        	
        }else {
        	
        	p.add(criarItem("Dados do usuário", "usuario.png",
                    () -> new FormularioUsuario(usuario, TipoCadastroUsuario.ALETRACAO, permissoes).abrirTela()));
        	
        }

        p.add(criarItem("Gerenciar Configurações", "configuracoes.png",
                () -> new Configuracoes(usuario).abrirTela()));

        p.add(criarItem("Sair do Sistema", "sair.png", () -> System.exit(0)));
    }

    private JPanel criarItem(String texto, String icone, Runnable destino) {

        JPanel painel = new JPanel();
        painel.setPreferredSize(new Dimension(140, 150));
        painel.setLayout(null);

        ImageIcon icon = obterIconeEscalado(icone, 120, 120);
        JLabel imagem = new JLabel(icon);
        imagem.setBounds(10, 0, 120, 120);

        adicionarHover(imagem, icon, icone, destino);

        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setBounds(0, 125, 140, 20);

        painel.add(imagem);
        painel.add(label);

        return painel;
    }

    private void adicionarHover(JLabel label, ImageIcon original, String nomeIcone, Runnable destino) {

        ImageIcon dark = obterDark(original, nomeIcone);

        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        label.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                label.setIcon(dark);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setIcon(original);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                fechaTela();
                destino.run();
            }
        });
    }

    private ImageIcon obterIconeEscalado(String nome, int w, int h) {

        String chave = nome + "_" + w + "_" + h;

        return cacheEscaladas.computeIfAbsent(chave, k -> {
            ImageIcon icon = Icones.obterImagem(nome);
            Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        });
    }

    private ImageIcon obterDark(ImageIcon icone, String nome) {

        return cacheDark.computeIfAbsent(nome, n -> {

            Image img = icone.getImage();

            BufferedImage darkImg = new BufferedImage(
                    img.getWidth(null),
                    img.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = darkImg.createGraphics();
            g2d.drawImage(img, 0, 0, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f));
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, img.getWidth(null), img.getHeight(null));
            g2d.dispose();

            return new ImageIcon(darkImg);
        });
    }

    public void fechaTela() {
        frame.setVisible(false);
        frame.dispose();
    }

    public void abrirTela() {
        frame.setVisible(true);
    }
    
    public void conferirDadosAr(int codigo) {
    	
		try(ArDAO arDAO = new ArDAO()){
			
			ar = arDAO.buscaAr(codigo);
			
		}catch (Exception e1) {
			
			System.err.println(e1);
		}
    	
    }
    
}
