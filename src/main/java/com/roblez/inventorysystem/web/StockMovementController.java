package com.roblez.inventorysystem.web;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.roblez.inventorysystem.dto.StockMovementResponse;
import com.roblez.inventorysystem.service.StockMovementService;

import io.swagger.v3.oas.annotations.Operation;
	

@RestController
@RequestMapping("/api/stock-movements")
public class StockMovementController {
	private final StockMovementService stockService;

	public StockMovementController(StockMovementService stockService) {
		super();
		this.stockService = stockService;
	}
	
    @GetMapping
    public ResponseEntity<List<StockMovementResponse>> getAllMovements() {
        List<StockMovementResponse> list = stockService.getAllMovements();
        return ResponseEntity.ok(list);
    }
	
	@GetMapping("/product/{id}")
	public ResponseEntity<List<StockMovementResponse>> getMovementsByProduct(@PathVariable UUID id) {
		List<StockMovementResponse> list = stockService.getMovementsByProduct(id);
		
		return ResponseEntity.ok(list);
	}
	
    @GetMapping("/product/{id}/range")
    @Operation(summary = "Obtener dentro de un rango de fechas", description = "Obtiene todos los movimiento dentro de un rango de fechas (from, to)")
    public ResponseEntity<List<StockMovementResponse>> getMovementsByDateRange(
            @PathVariable UUID id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
    	
        List<StockMovementResponse> list = stockService.getMovementsByProductAndDate(id, from, to);
        
        return ResponseEntity.ok(list);
    }
    
    @GetMapping("/recent")
    @Operation(summary = "Obtener recientes", description = "Obtiene los últimos 10 movimientos")
    public ResponseEntity<List<StockMovementResponse>> getRecentMovements() {
        List<StockMovementResponse> list = stockService.getRecentMovements();
        return ResponseEntity.ok(list);
    }
    
    @GetMapping("/after-date")
    @Operation(summary = "Obtener después de una fecha", description = "Obtiene todos los movimientos a partir de una fecha")
    public ResponseEntity<List<StockMovementResponse>> getMovementsAfterDate(@RequestParam Instant date ) {
        List<StockMovementResponse> list = stockService.getMovementsAfterDate(date);
        return ResponseEntity.ok(list);
    }
    
    @GetMapping("/before-date")
    @Operation(summary = "Obtener antes de una fecha", description = "Obtiene todos los movimientos antes de una fecha")
    public ResponseEntity<List<StockMovementResponse>> getMovementsBeforeDate(@RequestParam Instant date ) {
        List<StockMovementResponse> list = stockService.getMovementsBeforeDate(date);
        return ResponseEntity.ok(list);
    }
    
    @GetMapping("/gte-quantity")
    public ResponseEntity<List<StockMovementResponse>> getMovementsGreaterThanDelta(@RequestParam Integer delta ) {
        List<StockMovementResponse> list = stockService.getMovementsGreaterThanDelta(delta);
        return ResponseEntity.ok(list);
    }
}