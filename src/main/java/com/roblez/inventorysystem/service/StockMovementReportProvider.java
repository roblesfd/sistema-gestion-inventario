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
import com.roblez.inventorysystem.exception.ResourceNotFoundException;
import com.roblez.inventorysystem.report.ColumnDescriptor;
import com.roblez.inventorysystem.report.GenericReportRow;
import com.roblez.inventorysystem.report.ReportSchema;
import com.roblez.inventorysystem.repository.StockMovementRepository;

@Service
public class StockMovementReportProvider {
	private final StockMovementRepository repo;
	private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
	
	public StockMovementReportProvider(StockMovementRepository repo) {
		this.repo = repo;
	}
	
	// Schema púplico para que los renderers lo usen
	public ReportSchema schema() {
		return new ReportSchema(List.of(
				new ColumnDescriptor("happenedAt", "Fecha", v -> DTF.format((Instant)v)),
				new ColumnDescriptor("sku", "SKU", Objects::toString),
				new ColumnDescriptor("productName", "Product", Objects::toString),
				new ColumnDescriptor("delta", "Delta", Objects::toString),
				new ColumnDescriptor("reason", "Motivo", Objects::toString),
				new ColumnDescriptor("balance", "Balance", Objects::toString)
				
		));
	}
	
    /**
     * Construye GenericReportRow para un producto y rango. Si productId == null devuelve todos (o top10).
     */
	public List<GenericReportRow> fetch(UUID productId, Instant from, Instant to, boolean includeRunningBalance) {
		List<StockMovement> movements;
		
        if (productId != null && from != null && to != null) {
            movements = repo.findByProductIdAndHappenedAtBetween(productId, from, to);
        } else if (productId != null) {
            movements = repo.findByProductId(productId);
        } else {
            movements = repo.findTop10ByOrderByHappenedAtDesc();
        }
        
        if (movements.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron movimientos para los filtros indicados");
        }
        
        // ordenar ascendente para balance
        movements.sort(Comparator.comparing(StockMovement::getHappenedAt));
        
        List<GenericReportRow> rows = movements.stream().map(m -> {
            GenericReportRow r = new GenericReportRow();
            Product p = m.getProduct(); // si es LAZY, considera fetch/join en repo
            r.put("happenedAt", m.getHappenedAt());
            r.put("sku", p != null ? p.getSku() : "");
            r.put("productName", p != null ? p.getName() : "");
            r.put("delta", m.getDelta());
            r.put("reason", m.getReason());
            r.put("balance", null);
            return r;
        }).collect(Collectors.toList());
        
        if (includeRunningBalance) {
            int bal = 0;
            for (GenericReportRow r : rows) {
                Integer d = (Integer) r.get("delta");
                bal += (d == null ? 0 : d);
                r.put("balance", bal);
            }
        }

        return rows;
	}	
}
