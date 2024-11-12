package com.infomedia.abacox.control.entity;

import com.infomedia.abacox.control.entity.superclass.ActivableEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "module_endpoints")
public class ModuleEndpoint extends ActivableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "method", nullable = false, length = 10)
    private String method;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "secured", nullable = false)
    private boolean secured;

    @ManyToOne(optional = false)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ModuleEndpoint that = (ModuleEndpoint) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
