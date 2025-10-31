package com.roblez.inventorysystem.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.roblez.inventorysystem.domain.AlertEvent;

@Repository
public interface AlertEventRepository extends JpaRepository<AlertEvent, UUID>{}
