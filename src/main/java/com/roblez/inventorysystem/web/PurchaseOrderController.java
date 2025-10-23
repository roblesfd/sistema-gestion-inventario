package com.roblez.inventorysystem.web;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.roblez.inventorysystem.domain.OrderStatus;
import com.roblez.inventorysystem.dto.PurchaseOrderItemResponse;
import com.roblez.inventorysystem.dto.PurchaseOrderRequest;
import com.roblez.inventorysystem.dto.PurchaseOrderResponse;
import com.roblez.inventorysystem.dto.PurchaseOrderUpdateRequest;
import com.roblez.inventorysystem.service.PurchaseOrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderController {
	
	private final PurchaseOrderService purchaseOrderService;
	
	public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
		this.purchaseOrderService = purchaseOrderService;
	}
	
	// Crea una orden de compra
	@PostMapping
	public ResponseEntity<PurchaseOrderResponse> createPurchaseOrder(@Valid @RequestBody PurchaseOrderRequest request) {
	    PurchaseOrderResponse order = purchaseOrderService.createPurchaseOrder(request);
	    
	    return ResponseEntity.status(HttpStatus.CREATED).body(order);
	}
	
	@GetMapping
	public ResponseEntity<List<PurchaseOrderResponse>> getAllPurchaseOrders() {
		List<PurchaseOrderResponse> list = purchaseOrderService.getAllPurchaseOrders();
	    
	    return ResponseEntity.ok().body(list);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PurchaseOrderResponse> getPurchaseOrderById(@PathVariable UUID id) {
		PurchaseOrderResponse order = purchaseOrderService.getPurchaseOrderById(id);
	    
	    return ResponseEntity.ok().body(order);
	}
	
	// Obtener órdenes por proveedor
    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<PurchaseOrderResponse>> getBySupplier(@PathVariable UUID supplierId) {
        return ResponseEntity.ok(purchaseOrderService.getPurchaseOrdersBySupplierId(supplierId));
    }
    
    // Obtener órdenes por estado
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PurchaseOrderResponse>> getByStatus(@PathVariable OrderStatus status) {
        return ResponseEntity.ok(purchaseOrderService.getPurchaseOrdersByStatus(status));
    }
    
    // Obtener órdenes por rango de fechas
    @GetMapping("/date-range")
    public ResponseEntity<List<PurchaseOrderResponse>> getByDateRange(
            @RequestParam Instant start,
            @RequestParam Instant end) {
        return ResponseEntity.ok(purchaseOrderService.getPurchaseOrdersByCreatedAtBetween(start, end));
    }
    
    // Obtener órdenes por varios estados
    @GetMapping("/statuses")
    public ResponseEntity<List<PurchaseOrderResponse>> getByStatuses(@RequestParam List<OrderStatus> statuses) {
        return ResponseEntity.ok(purchaseOrderService.getPurchaseOrdersByStatusIn(statuses));
    }
    
    // Obtener órdenes por usuario generador
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PurchaseOrderResponse>> getByGeneratedBy(@PathVariable UUID userId) {
        return ResponseEntity.ok(purchaseOrderService.getPurchaseOrdersByGeneratedBy(userId));
    }
    
    // Contar órdenes por proveedor
    @GetMapping("/supplier/{supplierId}/count")
    public ResponseEntity<Long> countActiveBySupplier(
            @PathVariable UUID supplierId,
            @RequestParam List<OrderStatus> statuses) {
        return ResponseEntity.ok(purchaseOrderService.countBySupplierIdAndStatus(supplierId, statuses));
    }
    
    // Buscar órdenes por producto
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<PurchaseOrderResponse>> getByProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(purchaseOrderService.getPurchaseOrdersByProductId(productId));
    }
    
    // Obtener items de orden
    @GetMapping("/items/{purchaseOrderId}")
    public ResponseEntity<List<PurchaseOrderItemResponse>> getPurchaseOrderItems(@PathVariable UUID purchaseOrderId) {
        return ResponseEntity.ok(purchaseOrderService.getPurchaseOrderItemsByOrderId(purchaseOrderId));
    }

	@PatchMapping("/{id}")
	public ResponseEntity<PurchaseOrderResponse> updatePurchaseOrder(@PathVariable UUID id, @Valid @RequestBody PurchaseOrderUpdateRequest updateRequest) {
	    PurchaseOrderResponse updated = purchaseOrderService.updatePurchaseOrder(id, updateRequest);
	    
	    return ResponseEntity.ok().body(updated);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePurchaseOrder(@PathVariable UUID id) {
			    purchaseOrderService.deletePurchaseOrder(id);
	    return ResponseEntity.noContent().build();
	}
}
