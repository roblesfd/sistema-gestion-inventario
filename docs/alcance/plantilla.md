# Alcance del Módulo: Gestión de Inventario

## 1. Resumen


## 2. Actores / Usuarios
- **Administrador:** crea y edita artículos.  

## 3. Casos de Uso Principales
- Crear artículo (nombre, SKU, precio, stock inicial).  


## 4. Fuera de Alcance
- Facturación y ventas.  

## 5. Restricciones / Dependencias
- Base de datos relacional (PostgreSQL).  


## 6. Requisitos No Funcionales
- Consistencia transaccional en operaciones de stock.  

## 7. Criterios de Aceptación (Gherkin)

### Crear artículo OK
```gherkin
Feature: Crear artículo
  Scenario: Admin crea artículo válido
    Given datos válidos (nombre, sku, stock>=0)
    When POST /api/items
    Then 201 Created y body contiene id y sku
```

## 8. Definición de Done
- Endpoints definidos en OpenAPI.  

## 9. Métricas de Éxito
- Tiempo de creación de artículo < 2 s.  

## 10. Checklist Final
- [ ] Resumen y actores documentados  
