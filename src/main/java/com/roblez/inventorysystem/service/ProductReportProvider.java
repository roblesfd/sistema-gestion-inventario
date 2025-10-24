package com.roblez.inventorysystem.service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.roblez.inventorysystem.domain.Product;
import com.roblez.inventorysystem.exception.ResourceNotFoundException;
import com.roblez.inventorysystem.report.ColumnDescriptor;
import com.roblez.inventorysystem.report.GenericReportRow;
import com.roblez.inventorysystem.report.ReportSchema;
import com.roblez.inventorysystem.repository.ProductRepository;

@Service
public class ProductReportProvider {
	private final ProductRepository repo;
	private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
	
	public ProductReportProvider(ProductRepository repo) {
		this.repo = repo;
	}
	
	public ReportSchema schema() {
		return new ReportSchema(List.of(
				new ColumnDescriptor("id", "ID", Objects::toString),
				new ColumnDescriptor("name", "Nombre", Objects::toString),
				new ColumnDescriptor("sku", "SKU", Objects::toString),
				new ColumnDescriptor("price", "Precio", Objects::toString),
				new ColumnDescriptor("description", "Descripción", Objects::toString),
				new ColumnDescriptor("stock", "Stock", Objects::toString),
				new ColumnDescriptor("active", "Activo", v -> ((Boolean)v) ? "Sí" : "No"),
				new ColumnDescriptor("createdAt", "Creado el", v -> DTF.format((java.time.Instant)v)),
				new ColumnDescriptor("updatedAt", "Actualizado el", v -> DTF.format((java.time.Instant)v))
		));
	}
	

	public List<GenericReportRow> fetch() {
		List<Product> products = repo.findAll();
		
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron productos");
        }
        
        List<GenericReportRow> rows = products.stream().map(p -> {
            GenericReportRow r = new GenericReportRow();
            r.put("id", p.getId());
            r.put("name", p.getName());
            r.put("sku", p.getSku());
            r.put("price", p.getPrice());
            r.put("description", p.getDescription());
            r.put("stock", p.getStock());
            r.put("active", p.getActive());
            r.put("createdAt", p.getCreatedAt());
            r.put("updatedAt", p.getUpdatedAt());
            
            return r;
        }).collect(Collectors.toList());
        
        return rows;
	}	
}
