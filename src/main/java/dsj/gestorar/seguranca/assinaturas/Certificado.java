package dsj.gestorar.seguranca.assinaturas;

import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.IExternalDigest;
import com.itextpdf.signatures.IExternalSignature;
import com.itextpdf.signatures.PdfSignatureAppearance;
import com.itextpdf.signatures.PdfSigner;
import com.itextpdf.signatures.PrivateKeySignature;


public class Certificado {

	
	public void assinaDocumento(String arquivoEntrada, String arquivoSaida) throws Exception{
		
            Security.addProvider(new BouncyCastleProvider());
     
            KeyStore ks = KeyStore.getInstance("Windows-MY");
            ks.load(null, null);
            
            Enumeration<String> aliases = ks.aliases();
            DefaultListModel<String> listModel = new DefaultListModel<>();

            while (aliases.hasMoreElements()) {
            	
                String alias = aliases.nextElement();
                X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
                listModel.addElement(alias + " - " + cert.getSerialNumber());
                
            }

            if (listModel.isEmpty()) {
            	
                throw new Exception("Nenhum certificado encontrado.");
                
            }

            String selectedAlias = (String) JOptionPane.showInputDialog(
                    null,
                    "Selecione um certificado",
                    "Certificados",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    listModel.toArray(),
                    listModel.getElementAt(0)
            );

            if (selectedAlias == null) {
            	
                throw new Exception("Nenhum certificado selecionado.");
                
            }

            String alias = selectedAlias.split(" - ")[0];
            
            String pins = JOptionPane.showInputDialog("Digite seu PIN");

            if (pins == null || pins.isEmpty()) {
            	
                throw new Exception("PIN não fornecido.");
                
            }

            char[] pin = pins.toCharArray();

            if (!ks.isKeyEntry(alias)) {
            	
                throw new Exception("Nenhuma chave privada encontrada para o alias: " + alias);
                
            }

            PrivateKey pk = (PrivateKey) ks.getKey(alias, pin);

            if (pk == null) {
            	
                throw new Exception("Chave privada não encontrada ou PIN incorreto.");
                
            }
            
            PdfReader leitor = new PdfReader(arquivoEntrada);
            PdfWriter escritor = new PdfWriter(new FileOutputStream(arquivoSaida));
            PdfDocument documento = new PdfDocument(leitor, escritor);
            
            System.out.println(documento.getNumberOfPages());
            
            PdfSigner signer = new PdfSigner(documento.getReader(), documento.getWriter(), false);
            
            IExternalSignature pks = new PrivateKeySignature(pk,
            		"SHA-256",
            		BouncyCastleProvider.PROVIDER_NAME);
            
            IExternalDigest digest = new BouncyCastleDigest();
			
            
            // Configuração da aparência da assinatura
            PdfSignatureAppearance appearance = signer.getSignatureAppearance()
                    .setReason("Motivo da assinatura")
                    .setLocation("Local da assinatura")
                    .setReuseAppearance(false);

            // Posição da assinatura na página
            Rectangle rect = new Rectangle(36, 648, 200, 100);
            appearance.setPageRect(rect).setPageNumber(1);
            
            // Adicionando campo de assinatura no formulário
            PdfAcroForm acroForm = PdfAcroForm.getAcroForm(signer.getDocument(), true);
            PdfFormField signatureField = PdfFormField.createSignature(signer.getDocument());
            acroForm.addField(signatureField);

            // Finalizando a assinatura
            signer.signDetached(digest, pks, ks.getCertificateChain(alias), null, null, null, 0, PdfSigner.CryptoStandard.CMS);

            documento.close();
            
            signer.getDocument().close();
            
        
	}
	
}
