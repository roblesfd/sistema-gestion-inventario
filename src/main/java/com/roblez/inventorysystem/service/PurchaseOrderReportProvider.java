package com.roblez.inventorysystem.service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.roblez.inventorysystem.domain.PurchaseOrder;
import com.roblez.inventorysystem.exception.ResourceNotFoundException;
import com.roblez.inventorysystem.report.ColumnDescriptor;
import com.roblez.inventorysystem.report.GenericReportRow;
import com.roblez.inventorysystem.report.ReportSchema;
import com.roblez.inventorysystem.repository.PurchaseOrderRepository;

@Service
public class PurchaseOrderReportProvider {
	private final PurchaseOrderRepository repo;
	private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
	
	public PurchaseOrderReportProvider(PurchaseOrderRepository repo) {
		this.repo = repo;
	}
	
	public ReportSchema schema() {
		return new ReportSchema(List.of(
				new ColumnDescriptor("id", "ID", Objects::toString),
				new ColumnDescriptor("createdAt", "Creado el", v -> DTF.format((java.time.Instant)v)),
				new ColumnDescriptor("status", "Estado", Objects::toString),
				new ColumnDescriptor("supplier", "Proveedor", Objects::toString),
				new ColumnDescriptor("generatedBy", "Generado por", Objects::toString),
				new ColumnDescriptor("expectedDeliveryDate", "Fecha de entrega estimada", Objects::toString),
				new ColumnDescriptor("total", "Total", Objects::toString)




		));
	}
	

	public List<GenericReportRow> fetch() {
		List<PurchaseOrder> orders = repo.findAll();
		
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron ordenes de compra");
        }
        
        List<GenericReportRow> rows = orders.stream().map(p -> {
            GenericReportRow r = new GenericReportRow();
            r.put("id", p.getId());
			r.put("createdAt", p.getCreatedAt());
			r.put("status", p.getStatus());
			r.put("supplier", p.getSupplier().getFullName());
			r.put("generatedBy", p.getGeneratedBy().getUsername());
			r.put("expectedDeliveryDate", p.getExpectedDeliveryDate());
			r.put("total", p.getTotal());
            
            return r;
        }).collect(Collectors.toList());
        
        return rows;
	}	
}
