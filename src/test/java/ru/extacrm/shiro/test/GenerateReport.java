/**
 *
 */
package ru.extacrm.shiro.test;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import org.apache.poi.xwpf.converter.core.XWPFConverterException;
import org.joda.time.LocalDate;
import ru.extas.model.Contact;
import ru.extas.model.Insurance;
import ru.extas.model.PersonInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

/**
 * @author Valery Orlov
 */
public class GenerateReport {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            // 1) Load Docx file by filling Velocity template engine and cache
            // it to the registry
            InputStream in = GenerateReport.class.getResourceAsStream("/reports/insurance/PropertyInsuranceTemplate.docx");
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Freemarker);

            // 2) Create context Java model
            IContext context = report.createContext();
            Insurance insurance = createInsurance();
            context.put("ins", insurance);

            report.process(context, new FileOutputStream(new File("PropertyInsurance.docx")));
            // // 3) Generate report by merging Java model with the Docx
            // final ByteArrayOutputStream outDoc = new ByteArrayOutputStream();
            // report.process(context, outDoc);
            //
            // // Конвертируем в PDF
            // // 1) Load DOCX into XWPFDocument
            // XWPFDocument document = new XWPFDocument(new
            // ByteArrayInputStream(outDoc.toByteArray()));
            //
            // // 2) Prepare Pdf options
            // PdfOptions options = PdfOptions.create();
            // options.fontEncoding("cp1251");
            // // options.fontProvider(new ExtaFontProvider());
            //
            // // 3) Convert XWPFDocument to Pdf
            // final OutputStream outPdf = new FileOutputStream(new
            // File("PropertyInsurance.pdf"));
            // PdfConverter.getInstance().convert(document, outPdf, options);

        } catch (XWPFConverterException | IOException | XDocReportException e) {
            e.printStackTrace();
        }

    }

    /**
     * @return
     */
    private static Insurance createInsurance() {
        Insurance ins = new Insurance();
        Contact client = new Contact();
        client.setName("Снегирев Владимир Иванович");
        client.getPersonInfo().setBirthday(new LocalDate(1979, 5, 25));
        client.setCellPhone("+7 925 300 20 40");
        client.getPersonInfo().setSex(PersonInfo.Sex.MALE);
        ins.setRegNum("СП-2013/00046");
        ins.setDate(new LocalDate(2013, 5, 28));
        ins.setMotorType("Снегоболотоход");
        ins.setMotorBrand("POLARIS");
        ins.setMotorModel("Sportsman 550  Touring EFI EPS");
        ins.setRiskSum(new BigDecimal(434714));
        ins.setPremium(new BigDecimal(19563));
        ins.setClient(client);
        return ins;
    }
}
