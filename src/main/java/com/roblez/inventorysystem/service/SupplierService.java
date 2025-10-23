package com.roblez.inventorysystem.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.roblez.inventorysystem.config.SupplierMapper;
import com.roblez.inventorysystem.domain.Supplier;
import com.roblez.inventorysystem.dto.SupplierRequest;
import com.roblez.inventorysystem.dto.SupplierResponse;
import com.roblez.inventorysystem.exception.ResourceNotFoundException;
import com.roblez.inventorysystem.repository.SupplierRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SupplierService {
	private final SupplierRepository supplierRepo;
	private final SupplierMapper mapper;
	
	public SupplierService(SupplierRepository supplierRepo, SupplierMapper mapper) {
		this.supplierRepo = supplierRepo;
		this.mapper = mapper;
	}
	
	public SupplierResponse createSupplier(SupplierRequest request) {
		Supplier supplier = mapper.toEntity(request);
		return mapper.toDto(supplierRepo.save(supplier));
	}
	
	public List<SupplierResponse> getAllSuppliers() {
		return supplierRepo.findAll().stream()
				.map(mapper::toDto)
				.collect(Collectors.toList());
	}
	
	public SupplierResponse getSupplierById(UUID id) {
		Supplier supplier = supplierRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No existe el proveedor con ID: " + id)); 
		return mapper.toDto(supplier);
	}
	
	
	public SupplierResponse updateSupplier(UUID id, SupplierRequest request) {
		
		if(supplierRepo.existsById(id) == false) {
			throw new ResourceNotFoundException("No existe el proveedor con ID: " + id); 
		}
		
		Supplier supplier = mapper.toEntity(request);
		supplier.setId(id);
		return mapper.toDto(supplierRepo.save(supplier));
	}
	
	
	public void deleteSupplier(UUID id) {
		Supplier supplier = supplierRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No se encontró el proveedor con ID: " + id)); 
		supplierRepo.deleteById(supplier.getId());
	}
}
