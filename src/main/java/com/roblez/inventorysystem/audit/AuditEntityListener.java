package com.roblez.inventorysystem.audit;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

public class AuditEntityListener {

    @PrePersist
    public void prePersist(Object entity) {
        AuditService svc = SpringContext.getBean(AuditService.class);
        svc.record(entity, "CREATE");
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        AuditService svc = SpringContext.getBean(AuditService.class);
        svc.record(entity, "UPDATE");
    }

    @PreRemove
    public void preRemove(Object entity) {
        AuditService svc = SpringContext.getBean(AuditService.class);
        svc.record(entity, "DELETE");
    }
}