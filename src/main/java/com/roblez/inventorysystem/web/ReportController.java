package com.roblez.inventorysystem.web;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.roblez.inventorysystem.exception.ResourceNotFoundException;
import com.roblez.inventorysystem.report.AsciiTableRenderer;
import com.roblez.inventorysystem.report.GenericReportRow;
import com.roblez.inventorysystem.report.PdfReportRenderer;
import com.roblez.inventorysystem.report.ReportSchema;
import com.roblez.inventorysystem.service.ProductReportProvider;
import com.roblez.inventorysystem.service.PurchaseOrderReportProvider;
import com.roblez.inventorysystem.service.StockMovementReportProvider;
import com.roblez.inventorysystem.service.SupplierReportProvider;
import com.roblez.inventorysystem.service.UserReportProvider;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
	
	private final StockMovementReportProvider stockProvider;
	private final UserReportProvider userProvider;
	private final ProductReportProvider productProvider;
	private final PurchaseOrderReportProvider purchaseOrderProvider;
	private final SupplierReportProvider supplierProvider;
    private final PdfReportRenderer pdfRenderer;
	 
	public ReportController (
			StockMovementReportProvider stockProvider, 
			UserReportProvider userProvider, 
			ProductReportProvider productProvider, 
			PurchaseOrderReportProvider purchaseOrderProvider,
			SupplierReportProvider supplierProvider,
			PdfReportRenderer pdfRenderer

			) {
		this.stockProvider = stockProvider;
		this.userProvider = userProvider;
		this.productProvider = productProvider;
		this.purchaseOrderProvider = purchaseOrderProvider;
		this.supplierProvider = supplierProvider;
		this.pdfRenderer = pdfRenderer;
	}
	
	@GetMapping("/cli-stock-report")
	public ResponseEntity<Void> generateCLIReport(@RequestParam(required=false) UUID productId, @RequestParam(required=false) Instant from, @RequestParam(required=false) Instant to) {
		
		ReportSchema schema = stockProvider.schema();
		List<GenericReportRow> rows = stockProvider.fetch(productId, from, to, true);
		
	    if (rows.isEmpty()) {
	        throw new ResourceNotFoundException("No se encontraron movimientos para los filtros indicados");
	    }
		
		String table = new AsciiTableRenderer().renderTerminal(schema, rows);
		System.out.println(table);
		
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/pdf-stock-report")
	public ResponseEntity<byte[]> generatePdfStockReport(
	        @RequestParam(required=false) UUID productId,
	        @RequestParam(required = false) Instant from,
	        @RequestParam(required = false) Instant to) throws Exception {

	    return generatePdfReport(
	        "Reporte de Movimientos de Stock",
	        stockProvider::schema,
	        () -> stockProvider.fetch(productId, from, to, true),
	        "No se encontraron movimientos de stock"
	    );
	}

	@GetMapping("/pdf-user-report")
	public ResponseEntity<byte[]> generatePdfUserReport() throws Exception {
	    return generatePdfReport(
	        "Reporte de Usuarios registrados",
	        userProvider::schema,
	        userProvider::fetch,
	        "No se encontraron usuarios registrados"
	    );
	}

	@GetMapping("/pdf-product-report")
	public ResponseEntity<byte[]> generatePdfProductReport() throws Exception {
	    return generatePdfReport(
	        "Reporte de Productos registrados",
	        productProvider::schema,
	        productProvider::fetch,
	        "No se encontraron productos registrados"
	    );
	}
	
	@GetMapping("/pdf-purchase-order-report")
	public ResponseEntity<byte[]> generatePdfPurchaseOrderReport() throws Exception {
	    return generatePdfReport(
	        "Reporte de Ordenes registradas",
	        purchaseOrderProvider::schema,
	        purchaseOrderProvider::fetch,
	        "No se encontraron ordenes registradas"
	    );
	}
	
	@GetMapping("/pdf-supplier-report")
	public ResponseEntity<byte[]> generatePdfSupplierReport() throws Exception {
	    return generatePdfReport(
	        "Reporte de Proveedores registrados",
	        supplierProvider::schema,
	        supplierProvider::fetch,
	        "No se encontraron proveedores registrados"
	    );
	}
	
	private ResponseEntity<byte[]> generatePdfReport(
	        String title,
	        Supplier<ReportSchema> schemaSupplier,
	        Supplier<List<GenericReportRow>> rowsSupplier,
	        String emptyMessage) throws Exception {

	    ReportSchema schema = schemaSupplier.get();
	    List<GenericReportRow> rows = rowsSupplier.get();

	    if (rows.isEmpty()) {
	        throw new ResourceNotFoundException(emptyMessage);
	    }

	    byte[] pdfBytes = pdfRenderer.renderPdf(title, schema, rows);

	    return ResponseEntity.ok()
	            .header("Content-Disposition", "attachment; filename=\"" + title + ".pdf\"")
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(pdfBytes);
	}
}
