package com.roblez.inventorysystem.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.roblez.inventorysystem.config.PurchaseOrderItemMapper;
import com.roblez.inventorysystem.config.PurchaseOrderMapper;
import com.roblez.inventorysystem.domain.OrderStatus;
import com.roblez.inventorysystem.domain.Product;
import com.roblez.inventorysystem.domain.PurchaseOrder;
import com.roblez.inventorysystem.domain.PurchaseOrderItem;
import com.roblez.inventorysystem.domain.Supplier;
import com.roblez.inventorysystem.domain.User;
import com.roblez.inventorysystem.dto.PurchaseOrderItemRequest;
import com.roblez.inventorysystem.dto.PurchaseOrderItemResponse;
import com.roblez.inventorysystem.dto.PurchaseOrderItemUpdateRequest;
import com.roblez.inventorysystem.dto.PurchaseOrderRequest;
import com.roblez.inventorysystem.dto.PurchaseOrderResponse;
import com.roblez.inventorysystem.dto.PurchaseOrderUpdateRequest;
import com.roblez.inventorysystem.exception.ResourceNotFoundException;
import com.roblez.inventorysystem.repository.ProductRepository;
import com.roblez.inventorysystem.repository.PurchaseOrderItemRepository;
import com.roblez.inventorysystem.repository.PurchaseOrderRepository;
import com.roblez.inventorysystem.repository.SupplierRepository;
import com.roblez.inventorysystem.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PurchaseOrderService {

	private final PurchaseOrderRepository purchaseOrderRepo;
	private final PurchaseOrderItemRepository purchaseOrderItemRepo;
	private final SupplierRepository supplierRepo;
	private final UserRepository userRepo;
	private final ProductRepository productRepo;
	private final PurchaseOrderMapper mapper;
	private final PurchaseOrderItemMapper itemMapper;
	
	
	public PurchaseOrderService(
			PurchaseOrderRepository purchaseOrderRepo, 
			PurchaseOrderItemRepository purchaseOrderItemRepo, 
			SupplierRepository supplierRepo, 
			UserRepository userRepo, 
			ProductRepository productRepo, 
			PurchaseOrderMapper mapper,
			PurchaseOrderItemMapper itemMapper
			) {
		this.purchaseOrderRepo = purchaseOrderRepo;
		this.purchaseOrderItemRepo = purchaseOrderItemRepo;
		this.supplierRepo = supplierRepo;
		this.userRepo = userRepo;
		this.productRepo = productRepo;
		this.mapper = mapper;
		this.itemMapper = itemMapper;
	}
	
	public PurchaseOrderResponse createPurchaseOrder(PurchaseOrderRequest request) {
	    // 1) cargar supplier y user
		Supplier supplier = supplierRepo.findById(request.supplierId())
				.orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado: " + request.supplierId()));
		
		User user = userRepo.findById(request.generatedById())				
				.orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado: " + request.supplierId()));
		
	    // 2) recolectar productIds
	    Set<UUID> productIds = request.items().stream()
	        .map(PurchaseOrderItemRequest::productId)
	        .collect(Collectors.toSet());
	    

	    // 3) Cargar productos en batch y mapear por id
	    List<Product> products = productRepo.findByIdIn(productIds);
	    Map<UUID, Product> productById = products.stream()
	        .collect(Collectors.toMap(Product::getId, Function.identity()));
	    
	    // 4) Crear la orden (sin persistir aún)
	    PurchaseOrder order = new PurchaseOrder();
	    order.setStatus(request.status());
	    order.setSupplier(supplier);
	    order.setGeneratedBy(user);
	    order.setExpectedDeliveryDate(request.expectedDeliveryDate());
	    
	    // 5) Construir items y vincularlos a la misma instancia de order
	    Set<PurchaseOrderItem> items = request.items().stream()
	            .map(itemReq -> {
	                Product product = productById.get(itemReq.productId());
	                if (product == null) {
	                    throw new ResourceNotFoundException("Producto no encontrado: " + itemReq.productId());
	                }

	                PurchaseOrderItem item = new PurchaseOrderItem();
	                item.setProduct(product);
	                item.setQuantity(itemReq.quantity());
	                item.setUnitPrice(itemReq.unitPrice());
	                item.setPurchaseOrder(order); // vínculo bidireccional en memoria
	                return item;
	            })
	            .collect(Collectors.toSet());

	        order.setItems(items); // requiere cascade = ALL en PurchaseOrder para persistir items automáticamente

	        // 6) recalcular total en servidor
	        double computedTotal = items.stream()
	            .mapToDouble(PurchaseOrderItem::getSubtotal)
	            .sum();
	        order.setTotal(computedTotal);
	        
	        // 7) persistir UNA vez y devolver DTO
	        PurchaseOrder saved = purchaseOrderRepo.save(order);
	        return mapper.toDto(saved);
	}
	
	public List<PurchaseOrderResponse> getAllPurchaseOrders() {
		return purchaseOrderRepo.findAll().stream()
				.map(mapper::toDto)
				.collect(Collectors.toList());
	}
	
	public PurchaseOrderResponse getPurchaseOrderById(UUID id) {
		PurchaseOrder order = purchaseOrderRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Orden de compra no encontrada " + id));
		return mapper.toDto(order);
	}
	
	public  List<PurchaseOrderResponse> getPurchaseOrdersBySupplierId(UUID id) {
		 List<PurchaseOrderResponse> orders = purchaseOrderRepo.findBySupplierId(id).stream()
					.map(mapper::toDto)
					.collect(Collectors.toList());
		return orders;
	}
	
	public  List<PurchaseOrderResponse> getPurchaseOrdersByStatus(OrderStatus status) {
		 List<PurchaseOrderResponse> orders = purchaseOrderRepo.findByStatus(status).stream()
					.map(mapper::toDto)
					.collect(Collectors.toList());
		return orders;
	}
	
	public  List<PurchaseOrderResponse> getPurchaseOrdersByGeneratedBy(UUID id) {
		 List<PurchaseOrderResponse> orders = purchaseOrderRepo.findByGeneratedById(id).stream()
					.map(mapper::toDto)
					.collect(Collectors.toList());
		return orders;
	}
	
	public  List<PurchaseOrderResponse> getPurchaseOrdersByCreatedAtBetween(Instant start, Instant end) {
		 List<PurchaseOrderResponse> orders = purchaseOrderRepo.findByCreatedAtBetween(start, end).stream()
					.map(mapper::toDto)
					.collect(Collectors.toList());
		return orders;
	}
	
	public  List<PurchaseOrderResponse> getPurchaseOrdersByStatusIn(List<OrderStatus> statuses) {
		 List<PurchaseOrderResponse> orders = purchaseOrderRepo.findByStatusIn(statuses).stream()
					.map(mapper::toDto)
					.collect(Collectors.toList());
		return orders;
	}
	
	public long countBySupplierIdAndStatus(UUID supplierId, List<OrderStatus> statuses) {
		return purchaseOrderRepo.countBySupplierIdAndStatusIn(supplierId, statuses);
	}
	
	public List<PurchaseOrderResponse> getPurchaseOrdersByProductId(UUID productId) {
		return purchaseOrderRepo.findByProductId(productId).stream()
				.map(mapper::toDto)
				.collect(Collectors.toList());
	}
	
	public PurchaseOrderResponse updatePurchaseOrder(UUID id, PurchaseOrderUpdateRequest request) {
		
		PurchaseOrder order = purchaseOrderRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Orden de compra no encontrada"));
		
		Supplier supplier = supplierRepo.findById(request.supplier())
							.orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " + request.supplier()));
		
		User user = userRepo.findById(request.generatedBy())
				.orElseThrow(() -> new ResourceNotFoundException("Usuario generador no encontrado " + request.generatedBy()));
		
		syncItems(order, request.items());
		
		order.setStatus(request.status());
		order.setSupplier(supplier);
		order.setGeneratedBy(user);
		order.setExpectedDeliveryDate(request.expectedDeliveryDate());
		
		return mapper.toDto(purchaseOrderRepo.save(order));
	}
	
	public void deletePurchaseOrder(UUID id) {
		PurchaseOrder order = purchaseOrderRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Orden de compra no encontrada"));
		
		purchaseOrderRepo.deleteById(order.getId());
	}
	
	private void syncItems(PurchaseOrder order, Set<PurchaseOrderItemUpdateRequest> itemRequests) {
	    if (itemRequests == null) {
	        itemRequests = Collections.emptySet();
	    }

	    // 1) ids y productIds a procesar (filtrar nulls)
	    Set<UUID> incomingIds = itemRequests.stream()
	        .map(PurchaseOrderItemUpdateRequest::id)
	        .filter(Objects::nonNull)
	        .collect(Collectors.toSet());

	    Set<UUID> productIds = itemRequests.stream()
	        .map(PurchaseOrderItemUpdateRequest::productId)
	        .filter(Objects::nonNull)
	        .collect(Collectors.toSet());

	    // 2) cargar productos y items existentes (solo los que pertenecen a esta orden)
	    Map<UUID, Product> productsById = productRepo.findByIdIn(productIds).stream()
	        .collect(Collectors.toMap(Product::getId, Function.identity()));

	    Map<UUID, PurchaseOrderItem> existingById = purchaseOrderItemRepo.findAllById(incomingIds).stream()
	        .filter(i -> i.getPurchaseOrder() != null && i.getPurchaseOrder().getId().equals(order.getId()))
	        .collect(Collectors.toMap(PurchaseOrderItem::getId, Function.identity()));

	    Set<UUID> processedIds = new HashSet<>();

	    // 3) procesar requests
	    for (PurchaseOrderItemUpdateRequest req : itemRequests) {
	        PurchaseOrderItem item;
	        if (req.id() != null && existingById.containsKey(req.id())) {
	            // actualizar entidad existente usando el mapper
	            item = existingById.get(req.id());
	            PurchaseOrderItemMapper.INSTANCE.updateFromRequest(req, item);
	            processedIds.add(req.id());
	        } else {
	            // nuevo item: crear entidad usando el mapper y añadir con helper
	            item = PurchaseOrderItemMapper.INSTANCE.toEntity(req);
	            order.addItem(item);
	        }

	        // asignar producto
	        Product product = productsById.get(req.productId());
	        if (product == null) {
	            throw new ResourceNotFoundException("Producto no encontrado: " + req.productId());
	        }
	        item.setProduct(product);
	    }

	    // 4) eliminar orphans usando helper
	    List<PurchaseOrderItem> snapshot = new ArrayList<>(order.getItems());
	    for (PurchaseOrderItem mi : snapshot) {
	        UUID miId = mi.getId();
	        if (miId != null && !processedIds.contains(miId)) {
	            order.removeItem(mi);
	            // si no tienes orphanRemoval = true, también puedes borrar explícitamente:
	            // purchaseOrderItemRepo.delete(mi);
	        }
	    }

	    // 5) recalcular total de la orden
	    double total = order.getItems().stream()
	        .mapToDouble(i -> {
	            Integer q = i.getQuantity() == null ? 0 : i.getQuantity();
	            Double up = i.getUnitPrice() == null ? 0.0 : i.getUnitPrice();
	            return q * up;
	        })
	        .sum();
	    order.setTotal(total);
	}

	public List<PurchaseOrderItemResponse> getPurchaseOrderItemsByOrderId(UUID purchaseOrderId) {
		return purchaseOrderItemRepo.findByPurchaseOrderId(purchaseOrderId).stream()
				.map(itemMapper::toDto)
				.collect(Collectors.toList());
	}
}
