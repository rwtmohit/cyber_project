package com.example.cyber.services;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service
public class PdfService {

    public byte[] generatePdf(Map<String, Object> data) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Security Analysis Report"));
            document.add(new Paragraph(" "));

            Table table = new Table(2);

            table.addCell("URL");
            table.addCell(data.get("url").toString());

            table.addCell("URL Score");
            table.addCell(data.get("urlPatternScore").toString());

            table.addCell("SSL Score");
            table.addCell(data.get("sslScore").toString());

            table.addCell("Header Score");
            table.addCell(data.get("headerScore").toString());

            table.addCell("Total Score");
            table.addCell(data.get("totalScore").toString());

            table.addCell("Risk Level");
            table.addCell(data.get("risk_level").toString());

            table.addCell("DNS Report");
            table.addCell(data.get("dnsReport").toString());

            document.add(table);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }
}
