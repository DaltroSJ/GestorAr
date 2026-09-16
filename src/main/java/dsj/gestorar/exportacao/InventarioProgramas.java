package dsj.gestorar.exportacao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.aspose.cells.PdfSaveOptions;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;

import dsj.gestorar.modelo.Programas;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.persistencia.DesativacaoDAO;
import dsj.gestorar.persistencia.ProgramasDAO;

public class InventarioProgramas {

	private Usuario usuario;

	public InventarioProgramas(Usuario usuario) {

		this.usuario = usuario;

	}

	public void criarCabechalho(Workbook pasta, Sheet planilha, int linhaFinal) {

		// CRIACAO CABEÇARIO
		Row linhaCabecalho = planilha.createRow(0);
		linhaCabecalho.setHeightInPoints((short) 50);

		Cell autoridadeRegistroC = linhaCabecalho.createCell(0);
		autoridadeRegistroC.setCellValue("Autoridade\nde\nRegistro");
		Cell autoridadeCertificadoraC = linhaCabecalho.createCell(1);
		autoridadeCertificadoraC.setCellValue("Autoridade\nCertificadora");
		Cell hostnameC = linhaCabecalho.createCell(2);
		hostnameC.setCellValue("Hostname");
		Cell macC = linhaCabecalho.createCell(3);
		macC.setCellValue("Endereço MAC");
		Cell peC = linhaCabecalho.createCell(4);
		peC.setCellValue("Programa de emissão");
		Cell osC = linhaCabecalho.createCell(5);
		osC.setCellValue("Sistema Operacional");
		Cell javaC = linhaCabecalho.createCell(6);
		javaC.setCellValue("Java");
		Cell edgeC = linhaCabecalho.createCell(7);
		edgeC.setCellValue("Edge");
		Cell safeC = linhaCabecalho.createCell(8);
		safeC.setCellValue("Safesign");
		Cell pdfC = linhaCabecalho.createCell(9);
		pdfC.setCellValue("Leitor de PDF");
		Cell lbC = linhaCabecalho.createCell(10);
		lbC.setCellValue("Leitor Biometrico");
		Cell dcC = linhaCabecalho.createCell(11);
		dcC.setCellValue("Driver Câmera");

		CellStyle styleCabecalho = pasta.createCellStyle();
		Font fonte = pasta.createFont();
		fonte.setFontName("Arial");
		fonte.setFontHeightInPoints((short) 14);
		fonte.setBold(true);
		styleCabecalho.setFont(fonte);
		styleCabecalho.setAlignment(HorizontalAlignment.CENTER);
		styleCabecalho.setVerticalAlignment(VerticalAlignment.CENTER);
		styleCabecalho.setWrapText(true);
		styleCabecalho.setBorderTop(BorderStyle.THIN);
		styleCabecalho.setBorderBottom(BorderStyle.THIN);
		styleCabecalho.setBorderLeft(BorderStyle.THIN);
		styleCabecalho.setBorderRight(BorderStyle.THIN);
		styleCabecalho.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		styleCabecalho.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		autoridadeRegistroC.setCellStyle(styleCabecalho);
		autoridadeCertificadoraC.setCellStyle(styleCabecalho);
		autoridadeCertificadoraC.setCellStyle(styleCabecalho);
		hostnameC.setCellStyle(styleCabecalho);
		macC.setCellStyle(styleCabecalho);
		peC.setCellStyle(styleCabecalho);
		osC.setCellStyle(styleCabecalho);
		javaC.setCellStyle(styleCabecalho);
		edgeC.setCellStyle(styleCabecalho);
		safeC.setCellStyle(styleCabecalho);
		pdfC.setCellStyle(styleCabecalho);
		lbC.setCellStyle(styleCabecalho);
		dcC.setCellStyle(styleCabecalho);

		// CRIACAO GESTOR
		Row linhaGestor = planilha.createRow(1);
		Cell celulaGestor = linhaGestor.createCell(0);
		celulaGestor.setCellValue("Responsável Técnico: Daltro Slongo Junior");
		CellRangeAddress regiaoGestor = new CellRangeAddress(1, 2, 0, 11);
		RegionUtil.setBorderTop(BorderStyle.THIN, regiaoGestor, planilha);
		RegionUtil.setBorderBottom(BorderStyle.THIN, regiaoGestor, planilha);
		RegionUtil.setBorderLeft(BorderStyle.THIN, regiaoGestor, planilha);
		RegionUtil.setBorderRight(BorderStyle.THIN, regiaoGestor, planilha);
		// CRIACAO AR
		Row linhaBase = planilha.createRow(3);
		CellStyle styleVertial = pasta.createCellStyle();
		styleVertial.setRotation((short) 90);
		styleVertial.setAlignment(HorizontalAlignment.CENTER);
		styleVertial.setVerticalAlignment(VerticalAlignment.CENTER);
		planilha.addMergedRegion(regiaoGestor);

		Cell verticalAr = linhaBase.createCell(0);
		verticalAr.setCellValue(usuario.getAr().getNome());
		CellRangeAddress regiaoAr = new CellRangeAddress(3, linhaFinal - 1, 0, 0);
		RegionUtil.setBorderTop(BorderStyle.THIN, regiaoAr, planilha);
		RegionUtil.setBorderBottom(BorderStyle.THIN, regiaoAr, planilha);
		RegionUtil.setBorderLeft(BorderStyle.THIN, regiaoAr, planilha);
		RegionUtil.setBorderRight(BorderStyle.THIN, regiaoAr, planilha);
		planilha.addMergedRegion(regiaoAr);
		verticalAr.setCellStyle(styleVertial);
		// CRIACAO AC

		Cell verticalAc = linhaBase.createCell(1);
		verticalAc.setCellValue(usuario.getAr().getVinculacao());
		CellRangeAddress regiaoAc = new CellRangeAddress(3, linhaFinal - 1, 1, 1);
		RegionUtil.setBorderTop(BorderStyle.THIN, regiaoAc, planilha);
		RegionUtil.setBorderBottom(BorderStyle.THIN, regiaoAc, planilha);
		RegionUtil.setBorderLeft(BorderStyle.THIN, regiaoAc, planilha);
		RegionUtil.setBorderRight(BorderStyle.THIN, regiaoAc, planilha);
		planilha.addMergedRegion(regiaoAc);
		verticalAc.setCellStyle(styleVertial);

		Font fonteEsquerdaVertical = pasta.createFont();
		fonteEsquerdaVertical.setFontName("Arial");
		short x = (short) (linhaFinal / 0.8);
		fonteEsquerdaVertical.setFontHeightInPoints(x);
		fonteEsquerdaVertical.setBold(true);

		CellStyle styleFonteEsquerdaVertical = pasta.createCellStyle();
		styleFonteEsquerdaVertical.setFont(fonteEsquerdaVertical);
		styleFonteEsquerdaVertical.setRotation((short) 90);
		styleFonteEsquerdaVertical.setVerticalAlignment(VerticalAlignment.CENTER);
		styleFonteEsquerdaVertical.setAlignment(HorizontalAlignment.CENTER);
		styleFonteEsquerdaVertical.setBorderBottom(BorderStyle.THIN);
		styleFonteEsquerdaVertical.setBorderTop(BorderStyle.THIN);
		styleFonteEsquerdaVertical.setBorderLeft(BorderStyle.THIN);
		styleFonteEsquerdaVertical.setBorderRight(BorderStyle.THIN);
		styleFonteEsquerdaVertical.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
		styleFonteEsquerdaVertical.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		Font fonteGestor = pasta.createFont();
		fonteGestor.setFontName("Arial");
		fonteGestor.setFontHeightInPoints((short) 16);
		fonteGestor.setBold(true);
		CellStyle styleGestor = pasta.createCellStyle();
		styleGestor.setFont(fonteGestor);
		styleGestor.setAlignment(HorizontalAlignment.CENTER);
		styleGestor.setVerticalAlignment(VerticalAlignment.CENTER);
		styleGestor.setBorderBottom(BorderStyle.THIN);
		styleGestor.setBorderTop(BorderStyle.THIN);
		styleGestor.setBorderLeft(BorderStyle.THIN);
		styleGestor.setBorderRight(BorderStyle.THIN);
		styleGestor.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
		styleGestor.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		verticalAr.setCellStyle(styleFonteEsquerdaVertical);
		verticalAc.setCellStyle(styleFonteEsquerdaVertical);
		celulaGestor.setCellStyle(styleGestor);

		planilha.setColumnWidth(0, 5000);
		planilha.setColumnWidth(1, 5000);
		planilha.setColumnWidth(2, 5500);
		planilha.setColumnWidth(3, 5250);
		planilha.setColumnWidth(4, 3200);
		planilha.setColumnWidth(5, 6000);
		planilha.setColumnWidth(6, 3000);
		planilha.setColumnWidth(7, 4800);
		planilha.setColumnWidth(8, 5000);
		planilha.setColumnWidth(9, 8500);
		planilha.setColumnWidth(10, 4000);
		planilha.setColumnWidth(11, 2600);

	}

	public boolean exportaPlanilhaInventarioAtivosNovo(String diretorio, String dataInicio, String dataFinal) {

		List<Programas> programas = buscaProgramas(dataInicio, dataFinal);
		int linhaIncial = 3;
		int linhaFinal = programas.size() + 3;

		try (
				ByteArrayOutputStream bufferInventario = new ByteArrayOutputStream();
				ByteArrayOutputStream bufferAssinatura = new ByteArrayOutputStream();) {
			// CRIACAO DA PLANILHA
			try (Workbook pasta = new XSSFWorkbook();) {

				Sheet planilha = pasta.createSheet();

				Font fontoResto = pasta.createFont();
				fontoResto.setFontName("Arial");
				fontoResto.setFontHeightInPoints((short) 12);
				fontoResto.setBold(false);
				CellStyle styleResto = pasta.createCellStyle();
				styleResto.setFont(fontoResto);
				styleResto.setAlignment(HorizontalAlignment.LEFT);
				styleResto.setVerticalAlignment(VerticalAlignment.CENTER);
				styleResto.setBorderBottom(BorderStyle.MEDIUM);
				styleResto.setBorderTop(BorderStyle.MEDIUM);
				styleResto.setBorderLeft(BorderStyle.MEDIUM);
				styleResto.setBorderRight(BorderStyle.MEDIUM);

				criarCabechalho(pasta, planilha, linhaFinal);

				for (Programas p : programas) {

					criaLinha(linhaIncial, planilha, p, styleResto);

					linhaIncial++;

				}

				pasta.write(bufferInventario);
			}

			com.aspose.cells.Workbook aspInventario = new com.aspose.cells.Workbook(
					new ByteArrayInputStream(bufferInventario.toByteArray()));

			com.aspose.cells.Workbook aspAssinatura = new com.aspose.cells.Workbook(
					new ByteArrayInputStream(bufferAssinatura.toByteArray()));
			PdfSaveOptions opcoes = new PdfSaveOptions();
			opcoes.setOnePagePerSheet(true);

			ByteArrayOutputStream pdfInventario = new ByteArrayOutputStream();

			aspInventario.save(pdfInventario, opcoes);
			aspInventario.dispose();
			aspAssinatura.dispose();

			try (PdfDocument destino = new PdfDocument(
					new PdfWriter(diretorio + "Inventario de Programas " + dataInicio + " " + dataFinal + ".pdf"));
					PdfDocument docInventario = new PdfDocument(
							new PdfReader(new ByteArrayInputStream(pdfInventario.toByteArray())));) {

				PdfMerger merger = new PdfMerger(destino);

				merger.merge(docInventario, 1, docInventario.getNumberOfPages());
				merger.close();
			}
			
			return true;

		} catch (Exception e) {
			System.err.println("Erro aqui: " + e);
			return false;
		}

	}

	public void criaLinha(int linha, Sheet planilha, Programas programas, CellStyle styleResto) {

		int celula = 2;

		Row linhaSelecionada = planilha.getRow(linha);
		if (linhaSelecionada == null)
			linhaSelecionada = planilha.createRow(linha);
		if (linhaSelecionada == null)
			System.out.println("linha null");

		while (celula <= 11) {

			Cell celulaSelecionada = linhaSelecionada.createCell(celula);

			celulaSelecionada.setCellStyle(styleResto);

			switch (celula) {

			case 2:
				celulaSelecionada.setCellValue((programas.getMaquina().getNome()));
				break;

			case 3:
				celulaSelecionada.setCellValue((programas.getMaquina().getMac()));
				break;

			case 4:
				celulaSelecionada.setCellValue((programas.getEmissao()));
				break;

			case 5:
				celulaSelecionada.setCellValue((programas.getMaquina().getOs()));
				break;

			case 6:
				celulaSelecionada.setCellValue((programas.getJava()));
				break;

			case 7:
				celulaSelecionada.setCellValue((programas.getEdge()));
				break;

			case 8:
				celulaSelecionada.setCellValue((programas.getSafesing()));
				break;

			case 9:
				celulaSelecionada.setCellValue((programas.getPdf()));
				break;

			case 10:
				celulaSelecionada.setCellValue((programas.getDriverLeitorBiometrico()));
				break;

			case 11:
				celulaSelecionada.setCellValue((programas.getDriverCamera()));
				break;

			default:
				break;
			}

			celula++;
		}

	}

	public List<Programas> buscaProgramas(String dataInicial, String dataFinal) {

		List<Programas> ativosRelatorio = new ArrayList<Programas>();
		List<Programas> prog = new ArrayList<>();

		try {

			try (ProgramasDAO pDAO = new ProgramasDAO()) {

				for (Programas p : pDAO.listaCompleta(dataFinal)) {

					ativosRelatorio.add(p);
				}

			}

			try (DesativacaoDAO dDAO = new DesativacaoDAO()) {

				prog = dDAO.listaAtivosInventarioProgramas(dataInicial, dataFinal);

				if (prog != null) {

					for (Programas p : prog) {

						ativosRelatorio.add(p);
					}

				}
			}

		} catch (Exception e) {

			System.err.println(e);

		}

		return ativosRelatorio;

	}

	public String dataAtual() {

		Date dataCompleta = new Date();

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

		return sdf.format(dataCompleta);

	}

}
