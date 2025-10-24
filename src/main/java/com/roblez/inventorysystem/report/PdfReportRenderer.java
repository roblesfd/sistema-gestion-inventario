package com.roblez.inventorysystem.report;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Component
public class PdfReportRenderer {
	private final TemplateEngine templateEngine;
	
    public PdfReportRenderer(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
    
    public byte[] renderPdf(String title, ReportSchema schema, List<GenericReportRow> rows) throws Exception {
        
        Context context = new Context();
        context.setVariable("title", title);
        context.setVariable("columns", schema.getColumns());
        context.setVariable("rows", rows);
        
        // Generar HTML usando Thymeleaf
        String htmlContent = templateEngine.process("report-template", context);
        
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
        	ITextRenderer renderer = new ITextRenderer();
        	renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);      

			return outputStream.toByteArray();
        }
    } 
}
