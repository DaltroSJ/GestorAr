package dsj.gestorar.exportacao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.aspose.cells.PdfSaveOptions;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;

import dsj.gestorar.modelo.Agente;
import dsj.gestorar.modelo.Usuario;
import dsj.gestorar.persistencia.AgenteDAO;
import dsj.gestorar.utilitarios.Mascaras;

public class InventarioAgentes {

	public InventarioAgentes(Usuario usuario) {
	}

	public void exportarDadosExcel(String diretorio) {

		List<Agente> agentes;

		try (AgenteDAO aDAO = new AgenteDAO()) {
			agentes = aDAO.listaAgentes();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao obter lista de agentes");
			return;
		}

		try (InputStream modelo = getClass().getResourceAsStream("/modeloPlanilhaAgentes.xlsx");
				XSSFWorkbook wb = (XSSFWorkbook) WorkbookFactory.create(modelo)) {

			XSSFSheet sheet = wb.getSheetAt(0);

			int linhaAtual = 6;
			int numeroAgente = 1;

			for (Agente agente : agentes) {
				preencerDados(agente, sheet, dataAtual(), linhaAtual, numeroAgente);
				linhaAtual++;
				numeroAgente++;
			}

			ByteArrayOutputStream bufferPlanilha = new ByteArrayOutputStream();
			wb.write(bufferPlanilha);

			com.aspose.cells.Workbook aspPlanilha = new com.aspose.cells.Workbook(
					new ByteArrayInputStream(bufferPlanilha.toByteArray()));

			com.aspose.cells.PageSetup ps = aspPlanilha.getWorksheets().get(0).getPageSetup();
			ps.setFitToPagesWide(1);
			ps.setFitToPagesTall(0);

			ByteArrayOutputStream pdfPlanilha = new ByteArrayOutputStream();
			aspPlanilha.save(pdfPlanilha, new PdfSaveOptions());

			try (PdfDocument destino = new PdfDocument(new PdfWriter(diretorio));
					PdfDocument pdf1 = new PdfDocument(
							new PdfReader(new ByteArrayInputStream(pdfPlanilha.toByteArray())));) {

				PdfMerger merger = new PdfMerger(destino);

				merger.merge(pdf1, 1, pdf1.getNumberOfPages());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void preencerDados(Agente agente, XSSFSheet planilha, String dataInventario, int linhaAtual, int numero) {

		XSSFWorkbook workbook = planilha.getWorkbook();

		XSSFFont fontAtivos = workbook.createFont();
		fontAtivos.setFontName("Arial");
		fontAtivos.setFontHeightInPoints((short) 12);
		fontAtivos.setColor(new XSSFColor(java.awt.Color.WHITE, null));
		fontAtivos.setBold(true);

		CellStyle styleAtivos = workbook.createCellStyle();
		styleAtivos.setFont(fontAtivos);
		styleAtivos.setAlignment(HorizontalAlignment.CENTER);
		styleAtivos.setBorderBottom(BorderStyle.THIN);
		styleAtivos.setBorderTop(BorderStyle.THIN);
		styleAtivos.setBorderLeft(BorderStyle.THIN);
		styleAtivos.setBorderRight(BorderStyle.THIN);

		XSSFFont fontDesativados = workbook.createFont();
		fontDesativados.setFontName("Arial");
		fontDesativados.setFontHeightInPoints((short) 12);
		fontDesativados.setColor(new XSSFColor(java.awt.Color.BLACK, null));
		fontDesativados.setBold(true);

		CellStyle styleDesativados = workbook.createCellStyle();
		styleDesativados.setFont(fontDesativados);
		styleDesativados.setAlignment(HorizontalAlignment.CENTER);
		styleDesativados.setBorderBottom(BorderStyle.THIN);
		styleDesativados.setBorderTop(BorderStyle.THIN);
		styleDesativados.setBorderLeft(BorderStyle.THIN);
		styleDesativados.setBorderRight(BorderStyle.THIN);

		XSSFFont fontTreinamento = workbook.createFont();
		fontTreinamento.setFontName("Arial");
		fontTreinamento.setFontHeightInPoints((short) 12);
		fontTreinamento.setColor(new XSSFColor(java.awt.Color.RED, null));
		fontTreinamento.setBold(true);

		CellStyle styleTrienmaneto = workbook.createCellStyle();
		styleTrienmaneto.setFont(fontTreinamento);
		styleTrienmaneto.setAlignment(HorizontalAlignment.CENTER);
		styleTrienmaneto.setBorderBottom(BorderStyle.THIN);
		styleTrienmaneto.setBorderTop(BorderStyle.THIN);
		styleTrienmaneto.setBorderLeft(BorderStyle.THIN);
		styleTrienmaneto.setBorderRight(BorderStyle.THIN);

		byte[] corCelulaAtivos = new byte[] { (byte) 75, (byte) 172, (byte) 198 };
		XSSFColor azul = new XSSFColor(corCelulaAtivos, null);
		styleAtivos.setFillForegroundColor(azul);
		styleAtivos.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		byte[] corCelulaDesativado = new byte[] { (byte) 255, (byte) 255, (byte) 255 };
		XSSFColor branco = new XSSFColor(corCelulaDesativado, null);
		styleDesativados.setFillForegroundColor(branco);
		styleDesativados.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		byte[] corCelulaTreinemanto = new byte[] { (byte) 255, (byte) 230, (byte) 230 };
		XSSFColor azulT = new XSSFColor(corCelulaTreinemanto, null);
		styleTrienmaneto.setFillForegroundColor(azulT);
		styleTrienmaneto.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		Row linha = planilha.getRow(linhaAtual);
		if (linha == null)
			linha = planilha.createRow(linhaAtual);

		linha.setHeightInPoints(20);

		Cell celulaNumero = linha.createCell(1);
		Cell celulaNome = linha.createCell(2);
		Cell celulaCpf = linha.createCell(3);
		Cell celulaEmail = linha.createCell(4);
		Cell celulaSituacao = linha.createCell(5);
		Cell celulaDataAtivacao = linha.createCell(6);
		
		celulaNumero.setCellValue(numero);
		celulaNome.setCellValue(agente.getNome());
		celulaCpf.setCellValue(Mascaras.mascaraCpf(agente.getCpf()));
		celulaEmail.setCellValue(agente.getEmail());
		celulaSituacao.setCellValue(agente.getSituacao());
		celulaDataAtivacao.setCellValue(agente.getDataHabilitacao());
		CellStyle estilo;

		switch (agente.getSituacao()) {
		    case "ATIVO": estilo = styleAtivos; break;
		    case "DESATIVADO": estilo = styleDesativados; break;
		    case "TREINAMENTO": estilo = styleTrienmaneto; break;
		    default: estilo = styleAtivos; break;
		}

		celulaNumero.setCellStyle(estilo);
		celulaNome.setCellStyle(estilo);
		celulaCpf.setCellStyle(estilo);
		celulaEmail.setCellStyle(estilo);
		celulaSituacao.setCellStyle(estilo);
		celulaDataAtivacao.setCellStyle(estilo);
		
	}

	public String preencherDadosAssinatura(String data) throws EncryptedDocumentException, IOException {

		InputStream modelo = getClass().getResourceAsStream("/modeloAssinaturaAgentes.xlsx");

		String caminhoTempAssinatura = "C:\\valid_nathyelle\\temp\\assinaturaTemp.xlsx";

		Workbook planilhaX = WorkbookFactory.create(modelo);
		XSSFSheet existingSheet = (XSSFSheet) planilhaX.getSheetAt(0);

		XSSFWorkbook planilha = existingSheet.getWorkbook();

		XSSFFont fonte = planilha.createFont();
		fonte.setFontName("Arial");
		fonte.setFontHeightInPoints((short) 12);
		fonte.setBold(true);

		CellStyle styleRestante = planilha.createCellStyle();
		styleRestante.setFont(fonte);

		int i;
		int j;

		CellReference dataRef = new CellReference("A5:C5".split(":")[0]);
		i = dataRef.getRow();
		j = dataRef.getCol();
		Row linha = existingSheet.getRow(i);
		Cell celulaData = linha.getCell(j);
		celulaData.setCellValue("Brasília, " + data);
		celulaData.setCellStyle(styleRestante);

		try (FileOutputStream fos = new FileOutputStream(caminhoTempAssinatura)) {
			planilhaX.write(fos);
		}

		File tempDir = new File(System.getProperty("java.io.tmpdir"));
		File tempFile = File.createTempFile("pdfTempAssinatura", ".pdf", tempDir);
		String tempPdfFilePath = tempFile.getAbsolutePath();

		converterExcelPdfAspose(caminhoTempAssinatura, tempPdfFilePath);

		return tempPdfFilePath;

	}

	public void converterExcelPdfAspose(String arquivoexcel, String saidaPdf) {

		try {

			com.aspose.cells.Workbook pastaTrabalho = new com.aspose.cells.Workbook(arquivoexcel);

			PdfSaveOptions opcoesImpressao = new PdfSaveOptions();

			opcoesImpressao.setOnePagePerSheet(true);

			pastaTrabalho.save(saidaPdf, opcoesImpressao);

		} catch (Exception e) {

			System.err.println("Erro ao transformar arquivo em pdf " + e);

		}
	}

	public String dataAtual() {

		Date dataCompleta = new Date();

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

		return sdf.format(dataCompleta);

	}

}
