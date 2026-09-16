package dsj.gestorar.exportacao;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import com.aspose.cells.PdfSaveOptions;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;

import dsj.gestorar.modelo.Agente;
import dsj.gestorar.modelo.AgentesMaquina;
import dsj.gestorar.modelo.Ativo;
import dsj.gestorar.modelo.Desativacao;
import dsj.gestorar.modelo.Programas;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.persistencia.AgenteDAO;
import dsj.gestorar.persistencia.AgentesMaquinaDAO;
import dsj.gestorar.persistencia.AtivoDAO;
import dsj.gestorar.persistencia.DesativacaoDAO;
import dsj.gestorar.persistencia.ProgramasDAO;
import dsj.gestorar.utilitarios.Verificacoes;

public class InventarioMaquinas {

	public InventarioMaquinas(Usuario usuario) {

	}

	public boolean exportar(String diretorio, String dataInicial, String dataFinal) {
		File arquivoTempExcel = null;
		File arquivoTempAssinatura = null;
		File pdfTempInventario = null;

		try (AgentesMaquinaDAO aDAO = new AgentesMaquinaDAO();
				ProgramasDAO pDAO = new ProgramasDAO()) {

			try (SXSSFWorkbook pasta = new SXSSFWorkbook(100);) {
				
				Map<String, CellStyle> mapaEstilos = estilos(pasta);

				List<Ativo> ativos = buscaAtivos(dataInicial, dataFinal);
				System.out.println("ativos: " + ativos.size());
				List<AgentesMaquina> agentes = aDAO.listaGeral();
				System.out.println("agentes: " + agentes.size());
				Map<Integer, Programas> mapaProg = pDAO.listaGeral().stream()
						.collect(Collectors.toMap(p -> p.getMaquina().getCodigo(), p -> p, (a, b) -> a));
				System.out.println("mp: " + mapaProg.size());
				for (int x = 0; x < ativos.size(); x++) {
					Sheet planilha = pasta.createSheet();
					criarPlanilha(pasta,
							planilha,
							x + 1,
							ativos.size(),
							ativos.get(x),
							dataInicial,
							agentesMaquina(agentes, ativos.get(x).getMaquina().getCodigo()),
							mapaProg.getOrDefault(ativos.get(x).getMaquina().getCodigo(),
							new Programas()),
							mapaEstilos);
				}
				mapaEstilos.clear();
				mapaProg.clear();
				agentes.clear();

				arquivoTempExcel = new File(Verificacoes.caminhoPastaTemp + File.separator + "inventario.xlsx");
				try (FileOutputStream fos = new FileOutputStream(arquivoTempExcel)) {
					pasta.write(fos);
				}

			}
			pdfTempInventario = new File(Verificacoes.caminhoPastaTemp + File.separator + "inv.pdf");

			PdfSaveOptions opcoes = new PdfSaveOptions();
			opcoes.setOnePagePerSheet(true);

			com.aspose.cells.Workbook wb1 = new com.aspose.cells.Workbook(arquivoTempExcel.getAbsolutePath());
			wb1.save(pdfTempInventario.getAbsolutePath(), opcoes);
			wb1.dispose();

			String nomeFinal = diretorio + "Inventario de Ativos " + dataInicial + " " + dataFinal + ".pdf";
			try (PdfDocument destino = new PdfDocument(new PdfWriter(nomeFinal));
					PdfDocument docInv = new PdfDocument(new PdfReader(pdfTempInventario.getAbsolutePath()));) {

				PdfMerger merger = new PdfMerger(destino);
				merger.merge(docInv, 1, docInv.getNumberOfPages());
				merger.close();
			}
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {

			if (arquivoTempExcel != null)
				arquivoTempExcel.delete();
			if (arquivoTempAssinatura != null)
				arquivoTempAssinatura.delete();
			if (pdfTempInventario != null)
				pdfTempInventario.delete();
		}
	}

	public List<Agente> agentesMaquina(List<AgentesMaquina> am, int codigoMaquina) throws Exception {

		List<Agente> agentes = new ArrayList<>();
		for (AgentesMaquina amx : am) {

			if (amx.getMaquina().getCodigo() == codigoMaquina) {

				try (AgenteDAO aDAO = new AgenteDAO()) {

					agentes.add(aDAO.buscaAgente(amx.getAgente().getCodigo()));

				}

			}

		}

		return agentes;
	}

	public void criarPlanilha(Workbook pasta, Sheet planilha, int numeroPagina, int numeroPaginas, Ativo ativo,
			String dataInicial, List<Agente> agentesML, Programas programas, Map<String, CellStyle> estilos) {
		System.out.println("pagina: " + numeroPagina);
		List<CellRangeAddress> listaRegioes = new ArrayList<>();
		Row linha1 = planilha.createRow(0);

		Cell celulaTRevisao = linha1.createCell(2);
		celulaTRevisao.setCellValue("Revisão");
		celulaTRevisao.setCellStyle(estilos.get("tituloComBold"));
		CellRangeAddress regiaoRevisao = new CellRangeAddress(0, 0, 2, 2);
		listaRegioes.add(regiaoRevisao);

		Cell celulaTCodigo = linha1.createCell(3);
		celulaTCodigo.setCellValue("Código");
		celulaTCodigo.setCellStyle(estilos.get("tituloComBold"));
		CellRangeAddress regiaoCodigo = new CellRangeAddress(0, 0, 3, 3);
		listaRegioes.add(regiaoCodigo);

		Row linha2 = planilha.createRow(1);
		Cell codigoV = linha2.createCell(3);
		codigoV.setCellValue("VCD-AR-07.012a");
		codigoV.setCellStyle(estilos.get("tituloSemBold"));
		CellRangeAddress regiaoCodigo1 = new CellRangeAddress(1, 1, 3, 3);
		listaRegioes.add(regiaoCodigo1);

		Cell revisaoV = linha2.createCell(2);
		revisaoV.setCellValue("1.1");
		revisaoV.setCellStyle(estilos.get("tituloSemBold"));
		CellRangeAddress revisaoV11 = new CellRangeAddress(1, 1, 2, 2);
		listaRegioes.add(revisaoV11);

		Row linha3 = planilha.createRow(2);

		Cell celulaTPagina = linha3.createCell(2);
		celulaTPagina.setCellValue("Página");
		celulaTPagina.setCellStyle(estilos.get("tituloComBold"));
		CellRangeAddress celulaTPagina1 = new CellRangeAddress(2, 2, 2, 2);
		listaRegioes.add(celulaTPagina1);

		Cell celulaTData = linha3.createCell(3);
		celulaTData.setCellValue("Data");
		celulaTData.setCellStyle(estilos.get("tituloComBold"));
		CellRangeAddress revisaoV1 = new CellRangeAddress(1, 1, 2, 2);
		listaRegioes.add(revisaoV1);
		CellRangeAddress dara1 = new CellRangeAddress(2, 2, 3, 3);
		listaRegioes.add(dara1);

		Row linha4 = planilha.createRow(3);
		Cell numeroPaginaV = linha4.createCell(2);
		numeroPaginaV.setCellValue(numeroPagina + " - " + numeroPaginas);
		numeroPaginaV.setCellStyle(estilos.get("estiloNumeroPaginas"));
		CellRangeAddress numeroPaginaV1 = new CellRangeAddress(3, 3, 2, 2);
		listaRegioes.add(numeroPaginaV1);

		Cell data = linha4.createCell(3);
		data.setCellValue(dataAtual());
		data.setCellStyle(estilos.get("tituloSemBold"));
		CellRangeAddress dara12 = new CellRangeAddress(3, 3, 3, 3);
		listaRegioes.add(dara12);

		Cell celulaInventario = linha3.createCell(1);
		CellRangeAddress regiaoIA = new CellRangeAddress(2, 3, 1, 1);
		celulaInventario.setCellValue("Inventário de Ativos");
		planilha.addMergedRegion(regiaoIA);
		listaRegioes.add(regiaoIA);
		celulaInventario.setCellStyle(estilos.get("estiloTituloTitulo"));

		CellRangeAddress r1 = new CellRangeAddress(4, 4, 0, 3);
		planilha.createRow(4).createCell(0).setCellStyle(estilos.get("estiloQuebra"));
		planilha.addMergedRegion(r1);
		listaRegioes.add(r1);

		Row linhaRelacaoAtivos = planilha.createRow(5);
		Cell relacaoAtivos = linhaRelacaoAtivos.createCell(0);
		relacaoAtivos.setCellValue("Relação de Ativos");
		relacaoAtivos.setCellStyle(estilos.get("estiloTituloAzul"));
		planilha.createRow(6);
		CellRangeAddress r2 = new CellRangeAddress(5, 6, 0, 3);
		planilha.addMergedRegion(r2);
		listaRegioes.add(r2);

		Row caracteristicas = planilha.createRow(7);
		CellRangeAddress r3 = new CellRangeAddress(7, 7, 0, 3);
		listaRegioes.add(r3);
		planilha.addMergedRegion(r3);
		Cell caracterisV = caracteristicas.createCell(0);
		caracterisV.setCellValue("Caracteriscas");
		caracterisV.setCellStyle(estilos.get("tituloComBold"));

		Row linhaNomeAr = planilha.createRow(8);
		CellRangeAddress r4 = new CellRangeAddress(8, 8, 0, 1);
		CellRangeAddress r5 = new CellRangeAddress(8, 8, 2, 3);
		listaRegioes.add(r4);
		listaRegioes.add(r5);

		Cell nomeArT = linhaNomeAr.createCell(0);
		nomeArT.setCellValue("Nome da AR");
		Cell nomeArV = linhaNomeAr.createCell(2);
		nomeArV.setCellValue(ativo.getAr().getNome());

		planilha.addMergedRegion(r4);
		planilha.addMergedRegion(r5);
		nomeArV.setCellStyle(estilos.get("estiloRestante"));
		nomeArT.setCellStyle(estilos.get("estiloRestante"));

		Row linhaVinculacao = planilha.createRow(9);
		CellRangeAddress r6 = new CellRangeAddress(9, 9, 0, 1);
		CellRangeAddress r7 = new CellRangeAddress(9, 9, 2, 3);
		listaRegioes.add(r6);
		listaRegioes.add(r7);

		Cell vinculacaoT = linhaVinculacao.createCell(0);
		vinculacaoT.setCellValue("Vinculação");
		Cell vinculacaoV = linhaVinculacao.createCell(2);
		vinculacaoV.setCellValue(ativo.getAr().getVinculacao());
		planilha.addMergedRegion(r6);
		planilha.addMergedRegion(r7);
		vinculacaoT.setCellStyle(estilos.get("estiloRestante"));
		vinculacaoV.setCellStyle(estilos.get("estiloRestante"));

		Row linhaEndereco = planilha.createRow(10);
		CellRangeAddress r8 = new CellRangeAddress(10, 10, 0, 1);
		CellRangeAddress r9 = new CellRangeAddress(10, 10, 2, 3);
		listaRegioes.add(r8);
		listaRegioes.add(r9);

		Cell enderecoT = linhaEndereco.createCell(0);
		enderecoT.setCellValue("Endereco da AR");
		Cell enderecoV = linhaEndereco.createCell(2);
		enderecoV.setCellValue(ativo.getAr().getEndereco());
		planilha.addMergedRegion(r8);
		planilha.addMergedRegion(r9);
		enderecoT.setCellStyle(estilos.get("estiloRestante"));
		enderecoV.setCellStyle(estilos.get("estiloRestante"));

		Row linhaData = planilha.createRow(11);
		CellRangeAddress r10 = new CellRangeAddress(11, 11, 0, 1);
		CellRangeAddress r11 = new CellRangeAddress(11, 11, 2, 3);
		listaRegioes.add(r10);
		listaRegioes.add(r11);

		Cell dataT = linhaData.createCell(0);
		dataT.setCellValue("Data Inventário");
		Cell dataVCell = linhaData.createCell(2);
		dataVCell.setCellValue(dataInicial);
		planilha.addMergedRegion(r10);
		planilha.addMergedRegion(r11);
		dataT.setCellStyle(estilos.get("estiloRestante"));
		dataVCell.setCellStyle(estilos.get("estiloRestante"));

		Row linhaHostname = planilha.createRow(12);
		CellRangeAddress r12 = new CellRangeAddress(12, 12, 0, 1);
		CellRangeAddress r13 = new CellRangeAddress(12, 12, 2, 3);
		listaRegioes.add(r12);
		listaRegioes.add(r13);

		Cell hostT = linhaHostname.createCell(0);
		hostT.setCellValue("Hostname");
		Cell hostV = linhaHostname.createCell(2);
		hostV.setCellValue(ativo.getMaquina().getNome());
		planilha.addMergedRegion(r12);
		planilha.addMergedRegion(r13);
		hostT.setCellStyle(estilos.get("estiloRestante"));
		hostV.setCellStyle(estilos.get("estiloRestante"));

		Row linhaGestor = planilha.createRow(13);
		CellRangeAddress r14 = new CellRangeAddress(13, 13, 0, 1);
		CellRangeAddress r15 = new CellRangeAddress(13, 13, 2, 3);
		listaRegioes.add(r14);
		listaRegioes.add(r15);

		Cell gestorT = linhaGestor.createCell(0);
		gestorT.setCellValue("Gestor");
		Cell gestorV = linhaGestor.createCell(2);
		gestorV.setCellValue(ativo.getGestor().getNome());
		planilha.addMergedRegion(r14);
		planilha.addMergedRegion(r15);
		gestorT.setCellStyle(estilos.get("estiloRestante"));
		gestorV.setCellStyle(estilos.get("estiloRestante"));

		Row linhaTelefone = planilha.createRow(14);
		CellRangeAddress r16 = new CellRangeAddress(14, 14, 0, 1);
		CellRangeAddress r17 = new CellRangeAddress(14, 14, 2, 3);
		listaRegioes.add(r16);
		listaRegioes.add(r17);

		Cell telefoneT = linhaTelefone.createCell(0);
		telefoneT.setCellValue("Telefone");
		Cell telefoneV = linhaTelefone.createCell(2);
		telefoneV.setCellValue(ativo.getAr().getTelefone());
		planilha.addMergedRegion(r16);
		planilha.addMergedRegion(r17);
		telefoneT.setCellStyle(estilos.get("estiloRestante"));
		telefoneV.setCellStyle(estilos.get("estiloRestante"));

		Row linhaEmail = planilha.createRow(15);
		CellRangeAddress r18 = new CellRangeAddress(15, 15, 0, 1);
		CellRangeAddress r19 = new CellRangeAddress(15, 15, 2, 3);
		listaRegioes.add(r18);
		listaRegioes.add(r19);

		Cell emailT = linhaEmail.createCell(0);
		emailT.setCellValue("Email");
		Cell emailV = linhaEmail.createCell(2);
		emailV.setCellValue(ativo.getGestor().getEmail());
		planilha.addMergedRegion(r18);
		planilha.addMergedRegion(r19);
		emailT.setCellStyle(estilos.get("estiloRestante"));
		emailV.setCellStyle(estilos.get("estiloRestante"));

		CellRangeAddress r20 = new CellRangeAddress(16, 16, 0, 3);
		planilha.addMergedRegion(r20);
		planilha.createRow(16).createCell(0).setCellStyle(estilos.get("estiloQuebra"));
		;
		listaRegioes.add(r20);

		Row linha18 = planilha.createRow(17);
		Cell ativoP = linha18.createCell(0);
		ativoP.setCellValue("Ativo de Processamento");
		CellRangeAddress r21 = new CellRangeAddress(17, 17, 0, 3);
		listaRegioes.add(r21);
		planilha.addMergedRegion(r21);
		ativoP.setCellStyle(estilos.get("estiloTituloAzul"));

		CellRangeAddress r2x1 = new CellRangeAddress(18, 18, 0, 3);
		planilha.addMergedRegion(r2x1);
		planilha.createRow(18).createCell(0).setCellStyle(estilos.get("estiloQuebra"));
		;
		listaRegioes.add(r2x1);

		Row linha19 = planilha.createRow(19);
		Cell linha19V = linha19.createCell(0);
		linha19V.setCellValue("Estação de trabalho");
		CellRangeAddress r22 = new CellRangeAddress(19, 19, 0, 3);
		listaRegioes.add(r22);
		planilha.addMergedRegion(r22);
		linha19V.setCellStyle(estilos.get("estiloTituloAzul"));

		Row linha21 = planilha.createRow(20);
		Cell linha21T = linha21.createCell(0);
		linha21T.setCellValue("Processador");
		CellRangeAddress r23 = new CellRangeAddress(20, 20, 0, 1);
		CellRangeAddress r24 = new CellRangeAddress(20, 20, 2, 3);
		listaRegioes.add(r23);
		listaRegioes.add(r24);

		Cell linha21V = linha21.createCell(2);
		linha21V.setCellValue(ativo.getMaquina().getProcessador());
		planilha.addMergedRegion(r23);
		planilha.addMergedRegion(r24);
		linha21T.setCellStyle(estilos.get("estiloRestante"));
		linha21V.setCellStyle(estilos.get("estiloRestante"));

		Row linha22 = planilha.createRow(21);
		Cell linha22T = linha22.createCell(0);
		linha22T.setCellValue("Memoria");
		CellRangeAddress r25 = new CellRangeAddress(21, 21, 0, 1);
		CellRangeAddress r26 = new CellRangeAddress(21, 21, 2, 3);
		listaRegioes.add(r25);
		listaRegioes.add(r26);

		Cell linha22V = linha22.createCell(2);
		linha22V.setCellValue(ativo.getMaquina().getMemoria());
		planilha.addMergedRegion(r25);
		planilha.addMergedRegion(r26);
		linha22T.setCellStyle(estilos.get("estiloRestante"));
		linha22V.setCellStyle(estilos.get("estiloRestante"));

		Row linha23 = planilha.createRow(22);
		Cell linha23T = linha23.createCell(0);
		linha23T.setCellValue("Placa mãe");
		CellRangeAddress r27 = new CellRangeAddress(22, 22, 0, 1);
		CellRangeAddress r28 = new CellRangeAddress(22, 22, 2, 3);
		listaRegioes.add(r27);
		listaRegioes.add(r28);

		Cell linha23V = linha23.createCell(2);
		linha23V.setCellValue(ativo.getMaquina().getMae());
		planilha.addMergedRegion(r27);
		planilha.addMergedRegion(r28);
		linha23T.setCellStyle(estilos.get("estiloRestante"));
		linha23V.setCellStyle(estilos.get("estiloRestante"));

		Row linha24 = planilha.createRow(23);
		Cell linha24T = linha24.createCell(0);
		linha24T.setCellValue("Placa Gráfica");
		CellRangeAddress r29 = new CellRangeAddress(23, 23, 0, 1);
		CellRangeAddress r30 = new CellRangeAddress(23, 23, 2, 3);
		listaRegioes.add(r29);
		listaRegioes.add(r30);

		Cell linha24V = linha24.createCell(2);
		linha24V.setCellValue(ativo.getMaquina().getGrafico());

		Row linha25 = planilha.createRow(24);
		Cell linha25T = linha25.createCell(0);
		linha25T.setCellValue("Modelo");
		CellRangeAddress r31 = new CellRangeAddress(24, 24, 0, 1);
		CellRangeAddress r32 = new CellRangeAddress(24, 24, 2, 3);
		listaRegioes.add(r31);
		listaRegioes.add(r32);

		Cell linha25V = linha25.createCell(2);
		linha25V.setCellValue(ativo.getMaquina().getModelo());

		Row linha26 = planilha.createRow(25);
		Cell linha26T = linha26.createCell(0);
		linha26T.setCellValue("SSD's e HD's");
		CellRangeAddress r33 = new CellRangeAddress(25, 25, 0, 1);
		CellRangeAddress r34 = new CellRangeAddress(25, 25, 2, 3);
		listaRegioes.add(r33);
		listaRegioes.add(r34);

		Cell linha26V = linha26.createCell(2);
		linha26V.setCellValue(ativo.getMaquina().getSsdhdd());

		Row linha27 = planilha.createRow(26);
		Cell linha27T = linha27.createCell(0);
		linha27T.setCellValue("Criptografia");
		CellRangeAddress r35 = new CellRangeAddress(26, 26, 0, 1);
		CellRangeAddress r36 = new CellRangeAddress(26, 26, 2, 3);
		listaRegioes.add(r35);
		listaRegioes.add(r36);

		Cell linha27V = linha27.createCell(2);
		linha27V.setCellValue(ativo.getMaquina().getModoCriptografia());

		Row linha28 = planilha.createRow(27);
		Cell linha28T = linha28.createCell(0);
		linha28T.setCellValue("Camera");
		CellRangeAddress r37 = new CellRangeAddress(27, 27, 0, 1);
		CellRangeAddress r38 = new CellRangeAddress(27, 27, 2, 3);
		listaRegioes.add(r37);
		listaRegioes.add(r38);

		Cell linha28V = linha28.createCell(2);
		linha28V.setCellValue(ativo.getMaquina().getCamera());

		Row linha29 = planilha.createRow(28);
		Cell linha29T = linha29.createCell(0);
		linha29T.setCellValue("Biometria");
		CellRangeAddress r39 = new CellRangeAddress(28, 28, 0, 1);
		CellRangeAddress r40 = new CellRangeAddress(28, 28, 2, 3);
		listaRegioes.add(r39);
		listaRegioes.add(r40);

		Cell linha29V = linha29.createCell(2);
		linha29V.setCellValue(ativo.getMaquina().getBiometria());

		Row linha30 = planilha.createRow(29);
		Cell linha30T = linha30.createCell(0);
		linha30T.setCellValue("Impressora");
		CellRangeAddress r41 = new CellRangeAddress(29, 29, 0, 1);
		CellRangeAddress r42 = new CellRangeAddress(29, 29, 2, 3);
		listaRegioes.add(r41);
		listaRegioes.add(r42);

		Cell linha30V = linha30.createCell(2);
		linha30V.setCellValue(ativo.getMaquina().getImpressora());

		Row linha31 = planilha.createRow(30);
		Cell linha31T = linha31.createCell(0);
		linha31T.setCellValue("Placa de Rede");
		CellRangeAddress r43 = new CellRangeAddress(30, 30, 0, 1);
		CellRangeAddress r44 = new CellRangeAddress(30, 30, 2, 3);
		listaRegioes.add(r43);
		listaRegioes.add(r44);

		Cell linha31V = linha31.createCell(2);
		linha31V.setCellValue(ativo.getMaquina().getPlacaRede());

		Row linha32 = planilha.createRow(31);
		Cell linha32T = linha32.createCell(0);
		linha32T.setCellValue("Mac Address");
		CellRangeAddress r45 = new CellRangeAddress(31, 31, 0, 1);
		CellRangeAddress r46 = new CellRangeAddress(31, 31, 2, 3);
		listaRegioes.add(r45);
		listaRegioes.add(r46);

		Cell linha32V = linha32.createCell(2);
		linha32V.setCellValue(ativo.getMaquina().getMac());

		Row linha33 = planilha.createRow(32);
		Cell linha33T = linha33.createCell(0);
		linha33T.setCellValue("Provedor de Rede");
		CellRangeAddress r47 = new CellRangeAddress(32, 32, 0, 1);
		CellRangeAddress r48 = new CellRangeAddress(32, 32, 2, 3);
		listaRegioes.add(r47);
		listaRegioes.add(r48);

		Cell linha33V = linha33.createCell(2);
		linha33V.setCellValue(ativo.getMaquina().getProvedor());

		Row linha34 = planilha.createRow(33);
		Cell linha34T = linha34.createCell(0);
		linha34T.setCellValue("Safesign");
		CellRangeAddress r49 = new CellRangeAddress(33, 33, 0, 1);
		CellRangeAddress r50 = new CellRangeAddress(33, 33, 2, 3);
		listaRegioes.add(r49);
		listaRegioes.add(r50);

		Cell linha34V = linha34.createCell(2);
		linha34V.setCellValue(programas.getSafesing());

		Row linha35 = planilha.createRow(34);
		Cell linha35T = linha35.createCell(0);
		linha35T.setCellValue("Edge");
		CellRangeAddress r51 = new CellRangeAddress(34, 34, 0, 1);
		CellRangeAddress r52 = new CellRangeAddress(34, 34, 2, 3);
		listaRegioes.add(r51);
		listaRegioes.add(r52);

		Cell linha35V = linha35.createCell(2);
		linha35V.setCellValue(programas.getEdge());

		Row linha36 = planilha.createRow(35);
		Cell linha36T = linha36.createCell(0);
		linha36T.setCellValue("Java");
		CellRangeAddress r53 = new CellRangeAddress(35, 35, 0, 1);
		CellRangeAddress r54 = new CellRangeAddress(35, 35, 2, 3);
		listaRegioes.add(r53);
		listaRegioes.add(r54);

		Cell linha36V = linha36.createCell(2);
		linha36V.setCellValue(programas.getJava());

		Row linha37 = planilha.createRow(36);
		Cell paT = linha37.createCell(0);
		paT.setCellValue("Ponto de Atendimento");
		CellRangeAddress r55 = new CellRangeAddress(36, 36, 0, 3);
		listaRegioes.add(r55);
		planilha.addMergedRegion(r55);
		paT.setCellStyle(estilos.get("estiloTituloAzul"));

		Row linha38 = planilha.createRow(37);
		Cell linha38T = linha38.createCell(0);
		linha38T.setCellValue("Nome");
		CellRangeAddress r56 = new CellRangeAddress(37, 37, 0, 1);
		CellRangeAddress r57 = new CellRangeAddress(37, 37, 2, 3);
		listaRegioes.add(r56);
		listaRegioes.add(r57);

		Cell linha38V = linha38.createCell(2);
		linha38V.setCellValue(ativo.getMaquina().getPa().getApelido());

		Row linha39 = planilha.createRow(38);
		Cell linha39T = linha39.createCell(0);
		linha39T.setCellValue("Localidade");
		CellRangeAddress r58 = new CellRangeAddress(38, 38, 0, 1);
		CellRangeAddress r59 = new CellRangeAddress(38, 38, 2, 3);
		listaRegioes.add(r58);
		listaRegioes.add(r59);

		Cell linha39V = linha39.createCell(2);
		linha39V.setCellValue(ativo.getMaquina().getPa().getCidade() + " - " + ativo.getMaquina().getPa().getUf());

		Row linha40 = planilha.createRow(39);
		Cell agentes = linha40.createCell(0);
		agentes.setCellValue("Agentes");
		CellRangeAddress r60 = new CellRangeAddress(39, 39, 0, 3);
		listaRegioes.add(r60);
		planilha.addMergedRegion(r60);
		agentes.setCellStyle(estilos.get("estiloTituloAzul"));

		if (agentesML != null) {

			int linhaInicial = 40;

			for (int i = 0; i <= agentesML.size() - 1; i++) {
				int y = linhaInicial + i;
				Row linhax = planilha.createRow(y);
				CellRangeAddress xx = new CellRangeAddress(y, y, 0, 3);
				Cell texto = linhax.createCell(0);
				texto.setCellValue(agentesML.get(i).getNome() + " CPF: " + agentesML.get(i).getCpf());
				planilha.addMergedRegion(xx);
				texto.setCellStyle(estilos.get("estiloRestante"));
				listaRegioes.add(xx);
			}

		}

		planilha.addMergedRegion(r29);
		planilha.addMergedRegion(r30);
		linha24T.setCellStyle(estilos.get("estiloRestante"));
		linha24V.setCellStyle(estilos.get("estiloRestante"));

		planilha.addMergedRegion(r31);
		planilha.addMergedRegion(r32);
		linha25T.setCellStyle(estilos.get("estiloRestante"));
		linha25V.setCellStyle(estilos.get("estiloRestante"));

		planilha.addMergedRegion(r33);
		planilha.addMergedRegion(r34);
		linha26T.setCellStyle(estilos.get("estiloRestante"));
		linha26V.setCellStyle(estilos.get("estiloRestante"));

		planilha.addMergedRegion(r35);
		planilha.addMergedRegion(r36);
		linha27T.setCellStyle(estilos.get("estiloRestante"));
		linha27V.setCellStyle(estilos.get("estiloRestante"));

		planilha.addMergedRegion(r37);
		planilha.addMergedRegion(r38);
		linha28T.setCellStyle(estilos.get("estiloRestante"));
		linha28V.setCellStyle(estilos.get("estiloRestante"));

		planilha.addMergedRegion(r39);
		planilha.addMergedRegion(r40);
		linha29T.setCellStyle(estilos.get("estiloRestante"));
		linha29V.setCellStyle(estilos.get("estiloRestante"));

		planilha.addMergedRegion(r41);
		planilha.addMergedRegion(r42);
		linha30T.setCellStyle(estilos.get("estiloRestante"));
		linha30V.setCellStyle(estilos.get("estiloRestante"));

		linha31T.setCellStyle(estilos.get("estiloRestante"));
		linha31V.setCellStyle(estilos.get("estiloRestante"));

		planilha.addMergedRegion(r43);
		planilha.addMergedRegion(r44);
		linha32T.setCellStyle(estilos.get("estiloRestante"));
		linha32V.setCellStyle(estilos.get("estiloRestante"));

		planilha.addMergedRegion(r45);
		planilha.addMergedRegion(r46);
		linha33T.setCellStyle(estilos.get("estiloRestante"));
		linha33V.setCellStyle(estilos.get("estiloRestante"));

		planilha.addMergedRegion(r47);
		planilha.addMergedRegion(r48);
		linha34T.setCellStyle(estilos.get("estiloRestante"));
		linha34V.setCellStyle(estilos.get("estiloRestante"));

		planilha.addMergedRegion(r49);
		planilha.addMergedRegion(r50);
		linha35T.setCellStyle(estilos.get("estiloRestante"));
		linha35V.setCellStyle(estilos.get("estiloRestante"));

		planilha.addMergedRegion(r51);
		planilha.addMergedRegion(r52);
		linha36T.setCellStyle(estilos.get("estiloRestante"));
		linha36V.setCellStyle(estilos.get("estiloRestante"));

		planilha.addMergedRegion(r53);
		planilha.addMergedRegion(r54);
		linha38T.setCellStyle(estilos.get("estiloRestante"));
		linha38V.setCellStyle(estilos.get("estiloRestante"));

		planilha.addMergedRegion(r56);
		planilha.addMergedRegion(r57);
		planilha.addMergedRegion(r58);
		planilha.addMergedRegion(r59);
		linha39T.setCellStyle(estilos.get("estiloRestante"));
		linha39V.setCellStyle(estilos.get("estiloRestante"));

		planilha.setColumnWidth(0, 3000);
		planilha.setColumnWidth(1, 7000);
		planilha.setColumnWidth(2, 5500);
		planilha.setColumnWidth(3, 15000);
		adicionaBordaRegioes(planilha, listaRegioes);

	}

	public ArrayList<Ativo> buscaAtivos(String dataInicial, String dataFinal) {

		ArrayList<Ativo> ativosRelatorio = new ArrayList<Ativo>();

		try {

			try (AtivoDAO aDAO = new AtivoDAO()) {

				for (Ativo a : aDAO.listaAtivosFiltradoDataAtivacao(dataFinal)) {

					ativosRelatorio.add(a);
				}

			}

			try (DesativacaoDAO dDAO = new DesativacaoDAO()) {

				for (Desativacao d : dDAO.listaAtivosData(dataInicial, dataFinal)) {

					ativosRelatorio.add(
							new Ativo(0, d.getGestor(), d.getMaquina(), d.getAr(), d.getMaquina().getDataAtivacao()));
				}

			}

		} catch (Exception e) {

			System.err.println("Erro ao buscar os ativos");

		}

		return ativosRelatorio;

	}

	public String dataAtual() {

		Date dataCompleta = new Date();

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

		return sdf.format(dataCompleta);

	}

	public Map<String, CellStyle> estilos(Workbook workbook) {
		Map<String, CellStyle> estilos = new HashMap<>();

		Font fontBold = workbook.createFont();
		fontBold.setFontName("Arial");
		fontBold.setFontHeightInPoints((short) 12);
		fontBold.setBold(true);

		Font fontNormal = workbook.createFont();
		fontNormal.setFontName("Arial");
		fontNormal.setFontHeightInPoints((short) 12);
		fontNormal.setBold(false);

		CellStyle styleQuebra = workbook.createCellStyle();
		styleQuebra.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		styleQuebra.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		estilos.put("estiloQuebra", styleQuebra);

		CellStyle styleTitulo = workbook.createCellStyle();
		styleTitulo.setFont(fontBold); // USA NEGRITO
		styleTitulo.setAlignment(HorizontalAlignment.CENTER);
		styleTitulo.setVerticalAlignment(VerticalAlignment.CENTER);
		setBordas(styleTitulo);
		styleTitulo.setWrapText(true);
		styleTitulo.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
		styleTitulo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		estilos.put("estiloTituloAzul", styleTitulo);

		CellStyle styleTitulo2 = workbook.createCellStyle();
		styleTitulo2.setFont(fontBold);
		styleTitulo2.setAlignment(HorizontalAlignment.CENTER);
		styleTitulo2.setVerticalAlignment(VerticalAlignment.CENTER);
		setBordas(styleTitulo2);
		styleTitulo2.setWrapText(true);
		estilos.put("estiloTituloTitulo", styleTitulo2);

		CellStyle stylePaginas = workbook.createCellStyle();
		stylePaginas.setFont(fontBold); // USA NEGRITO
		stylePaginas.setAlignment(HorizontalAlignment.CENTER);
		stylePaginas.setVerticalAlignment(VerticalAlignment.CENTER);
		stylePaginas.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		stylePaginas.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		estilos.put("estiloNumeroPaginas", stylePaginas);

		CellStyle styleRestante = workbook.createCellStyle();
		styleRestante.setFont(fontNormal); // USA NORMAL
		styleRestante.setAlignment(HorizontalAlignment.LEFT); // ALINHAMENTO À ESQUERDA
		estilos.put("estiloRestante", styleRestante);

		CellStyle styleTituloSemBold = workbook.createCellStyle();
		styleTituloSemBold.setFont(fontNormal);
		styleTituloSemBold.setAlignment(HorizontalAlignment.CENTER);
		styleTituloSemBold.setVerticalAlignment(VerticalAlignment.CENTER);
		setBordas(styleTituloSemBold);
		styleTituloSemBold.setWrapText(true);
		estilos.put("tituloSemBold", styleTituloSemBold);

		CellStyle styleTituloComBold = workbook.createCellStyle();
		styleTituloComBold.setFont(fontBold);
		styleTituloComBold.setAlignment(HorizontalAlignment.CENTER);
		styleTituloComBold.setVerticalAlignment(VerticalAlignment.CENTER);
		setBordas(styleTituloComBold);
		styleTituloComBold.setWrapText(true);
		styleTituloComBold.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		styleTituloComBold.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		estilos.put("tituloComBold", styleTituloComBold);

		return estilos;
	}

	private void setBordas(CellStyle style) {
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
	}

	public void adicionaBordaRegioes(Sheet planilha, List<CellRangeAddress> regioes) {

		for (CellRangeAddress x : regioes) {

			RegionUtil.setBorderTop(BorderStyle.THIN, x, planilha);
			RegionUtil.setBorderBottom(BorderStyle.THIN, x, planilha);
			RegionUtil.setBorderLeft(BorderStyle.THIN, x, planilha);
			RegionUtil.setBorderRight(BorderStyle.THIN, x, planilha);

		}

	}

	

}
