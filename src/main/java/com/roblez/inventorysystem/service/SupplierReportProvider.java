package com.roblez.inventorysystem.service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.roblez.inventorysystem.domain.Product;
import com.roblez.inventorysystem.domain.StockMovement;
import com.roblez.inventorysystem.domain.Supplier;
import com.roblez.inventorysystem.exception.ResourceNotFoundException;
import com.roblez.inventorysystem.report.ColumnDescriptor;
import com.roblez.inventorysystem.report.GenericReportRow;
import com.roblez.inventorysystem.report.ReportSchema;
import com.roblez.inventorysystem.repository.StockMovementRepository;
import com.roblez.inventorysystem.repository.SupplierRepository;

@Service
public class SupplierReportProvider {
	private final SupplierRepository repo;
	private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
	
	public SupplierReportProvider(SupplierRepository repo) {
		this.repo = repo;
	}
	
	// Schema púplico para que los renderers lo usen
	public ReportSchema schema() {
		return new ReportSchema(List.of(
				new ColumnDescriptor("id", "ID", Objects::toString),
				new ColumnDescriptor("fullName", "Nombre", Objects::toString),
				new ColumnDescriptor("contactEmail", "Correo electrónico", Objects::toString),
				new ColumnDescriptor("phoneNumer", "Teléfono", Objects::toString)
		));
	}
	
    /**
     * Construye GenericReportRow para un producto y rango. Si productId == null devuelve todos (o top10).
     */
	public List<GenericReportRow> fetch( ) {
		List<Supplier> suppliers = repo.findAll();
		
        List<GenericReportRow> rows = suppliers.stream().map(s -> {
            GenericReportRow r = new GenericReportRow();
            r.put("id", s.getId());
            			r.put("fullName", s.getFullName());
            			r.put("contactEmail", s.getContactEmail());
        				r.put("phoneNumer", s.getPhoneNumber());
			return r;
        }).collect(Collectors.toList());

        return rows;
	}	
}