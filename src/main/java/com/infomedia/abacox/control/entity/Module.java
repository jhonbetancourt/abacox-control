package com.infomedia.abacox.control.entity;

import com.infomedia.abacox.control.constants.ModuleType;
import com.infomedia.abacox.control.entity.superclass.ActivableEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "modules")
public class Module extends ActivableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "url", nullable = false, unique = true)
    private String url;

    @Column(name = "name", nullable = false, length = 50, unique = true)
    private String name;

    @Column(name = "prefix", nullable = false, length = 20, unique = true)
    private String prefix;

    @Column(name = "description", length = 100, nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "module_type", nullable = false, length = 20)
    private ModuleType type;

    @Column(name = "version", nullable = false, length = 20)
    private String version;

    @ToString.Exclude
    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<ModuleEndpoint> endpoints = new LinkedHashSet<>();


}
