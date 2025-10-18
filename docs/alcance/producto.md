# Alcance del Módulo: Gestión de Inventario

## 1. Resumen
Gestión de artículos del inventario: permite crear, editar, listar productos y controlar el stock disponible.

## 2. Actores / Usuarios
- **Administrador:** crea y edita artículos.  
- **Almacenero:** actualiza el stock.  
- **Sistema externo / ERP:** consulta el inventario mediante API.

## 3. Casos de Uso Principales
- Crear artículo (nombre, SKU, precio, stock inicial).  
- Actualizar stock (entradas y salidas).  
- Consultar inventario por SKU o categoría.  
- Bloquear operaciones si el stock es negativo.  

## 4. Fuera de Alcance
- Facturación y ventas.  
- Sincronización multi-sede (se agregará en versiones futuras).

## 5. Restricciones / Dependencias
- Base de datos relacional (PostgreSQL).  
- Integración con módulo de usuarios para permisos.  
- SKU alfanumérico único obligatorio.

## 6. Requisitos No Funcionales
- Consistencia transaccional en operaciones de stock.  
- Tiempo de respuesta < 500 ms.  
- Control de acceso por roles (admin/almacenero).  

## 7. Criterios de Aceptación (Gherkin)

### Crear artículo OK
```gherkin
Feature: Crear artículo
  Scenario: Admin crea artículo válido
    Given datos válidos (nombre, sku, stock>=0)
    When POST /api/products
    Then 201 Created y body contiene id y sku
```

### SKU duplicado
```gherkin
Scenario: SKU duplicado
  Given existe artículo con sku "ABC"
  When POST /api/products con sku "ABC"
  Then 409 Conflict y mensaje "Ya existe el SKU"
```

### Stock negativo bloqueado
```gherkin
Scenario: Retiro que dejaría stock negativo
  Given item con stock 2
  When POST /api/products/{id}/stock con -5
  Then 400 Bad Request y error "Stock insuficiente"
```

## 8. Definición de Done
- Endpoints definidos en OpenAPI.  
- Todos los criterios de aceptación pasan.  
- Tests unitarios e integración básicos.  
- Migraciones BD completadas.  
- Documentación actualizada en README y Swagger.

## 9. Métricas de Éxito
- Tiempo de creación de artículo < 2 s.  
- 0% discrepancias de stock en pruebas.  
- Feedback positivo del usuario piloto.

## 10. Checklist Final
- [x] Resumen y actores documentados  
- [x] Casos de uso priorizados  
- [x] Criterios de aceptación listos  
- [x] Dependencias identificadas  
- [-] Definición de Done aprobada  
