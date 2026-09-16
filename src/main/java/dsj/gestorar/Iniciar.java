package dsj.gestorar;

import java.awt.EventQueue;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import com.formdev.flatlaf.FlatLightLaf;

import dsj.gestorar.modelo.Ar;
import dsj.gestorar.modelo.TipoCadastroUsuario;
import dsj.gestorar.persistencia.ArDAO;
import dsj.gestorar.persistencia.GerenciarConexoes;
import dsj.gestorar.persistencia.HibernateUtil;
import dsj.gestorar.utilitarios.Verificacoes;
import dsj.gestorar.visual.EscolherAr;
import dsj.gestorar.visual.Login;
import dsj.gestorar.visual.formularios.FormularioAr;
import dsj.gestorar.visual.formularios.FormularioUsuario;

public class Iniciar {

	public static void main(String[] args) throws SQLException, UnsupportedLookAndFeelException {
		HibernateUtil.getSessionFactory();
		GerenciarConexoes.getConexao();
		UIManager.setLookAndFeel(new FlatLightLaf());
		Verificacoes.verificacoesInicais();
		EventQueue.invokeLater(() -> {

			try {
				if (!Verificacoes.verificaSeHaCadastroAR(null)) {
					new FormularioAr().abrirTela();
					return;

				}

				Ar ar = null;

				try (ArDAO arDAO = new ArDAO()) {

					EscolherAr escolherAr = new EscolherAr(arDAO.listaArs());
					escolherAr.setModal(true);
					escolherAr.setLocationRelativeTo(null);

					while (ar == null) {

						escolherAr.setVisible(true);
						ar = escolherAr.getArSelecionado();

					}

				}
				if (!Verificacoes.verificaSeHaUsuarioCadastrado(ar.getCodigo())) {

					JOptionPane.showMessageDialog(null,
							"Ainda não há nenhum usuário cadastrado nesta AR\nPor favor cadastre a seguir", "Aviso",
							JOptionPane.WARNING_MESSAGE);
					new FormularioUsuario(ar, TipoCadastroUsuario.SEM_USUARIO, null).abrirTela();
				} else {

					new Login(ar).abreTela();
				}

			} catch (Exception e) {

				System.err.println(e);
				JOptionPane.showMessageDialog(null, "Erro fatal na inicialização do sistema.", "Erro",
						JOptionPane.ERROR_MESSAGE);
			}
		});

	}

}
