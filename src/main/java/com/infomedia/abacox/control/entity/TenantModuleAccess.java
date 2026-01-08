package com.infomedia.abacox.control.entity;

import com.infomedia.abacox.control.entity.superclass.ActivableEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "tenant_module_access", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"tenant_id", "module_prefix"})
})
public class TenantModuleAccess extends ActivableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "module_prefix", nullable = false)
    private String modulePrefix;
}