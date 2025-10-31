package com.roblez.inventorysystem.audit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;

@Service
public class AuditService {

    private final AuditEntryRepository repo;
    private final ObjectMapper mapper;
    private final EntityManager em;

    public AuditService(AuditEntryRepository repo, ObjectMapper mapper, EntityManager em) {
        this.repo = repo;
        this.mapper = mapper;
        this.em = em;
    }

    public void record(Object entity, String action) {
        try {
            String entityName = entity.getClass().getSimpleName();
            String entityId = extractId(entity);
            String beforeJson = null;
            String afterJson = mapper.writeValueAsString(entity);

            if ("UPDATE".equals(action) || "DELETE".equals(action)) {
                // intentar cargar estado persistido (antes del patch)
                if (entityId != null) {
                    Object persisted = em.find(entity.getClass(), convertId(entityId, entity));
                    if (persisted != null) {
                        beforeJson = mapper.writeValueAsString(persisted);
                    }
                }
            }
            if ("CREATE".equals(action)) beforeJson = null;

            String diff = computeDiff(beforeJson, afterJson);

            AuditEntry ae = new AuditEntry();
            ae.setEntityName(entityName);
            ae.setEntityId(entityId);
            ae.setAction(action);
            ae.setUsername(currentUser());
            ae.setCreatedAt(Instant.now());
            ae.setBeforeState(beforeJson);
            ae.setAfterState(afterJson);
            ae.setDiff(diff);

            repo.save(ae);
        } catch (Exception e) {
            // no romper la tx por fallo de auditoría: log y continuar
            LoggerFactory.getLogger(AuditService.class).warn("Falló auditoría: {}", e.getMessage());
        }
    }

    private String currentUser() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        return (a != null && a.getName() != null) ? a.getName() : "SYSTEM";
    }

    private String extractId(Object entity) {
        try {
            // asume campo 'id' o getId()
            Field f = ReflectionUtils.findField(entity.getClass(), "id");
            if (f != null) {
                f.setAccessible(true);
                Object val = f.get(entity);
                return val == null ? null : val.toString();
            } else {
                // try getter
                Method m = ReflectionUtils.findMethod(entity.getClass(), "getId");
                if (m != null) {
                    Object val = m.invoke(entity);
                    return val == null ? null : val.toString();
                }
            }
        } catch (Exception ignore) {}
        return null;
    }

    private Object convertId(String idStr, Object entity) {
        // UUID o Long;
        Field f = ReflectionUtils.findField(entity.getClass(), "id");
        if (f != null) {
            Class<?> t = f.getType();
            if (UUID.class.equals(t)) return UUID.fromString(idStr);
            if (Long.class.equals(t) || long.class.equals(t)) return Long.valueOf(idStr);
            if (String.class.equals(t)) return idStr;
        }
        return idStr;
    }

    private String computeDiff(String beforeJson, String afterJson) {
        // opcional: un diff simple; aquí devolvemos null o resumen
        if (beforeJson == null) return null;
        if (Objects.equals(beforeJson, afterJson)) return null;
        return "changed"; // puedes implementar un diff JSON real (Jackson + JsonNode)
    }
}
