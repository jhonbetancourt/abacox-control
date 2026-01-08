package com.infomedia.abacox.control.dto.tenantaccess;

import com.infomedia.abacox.control.dto.superclass.ActivableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantModuleAccessDto extends ActivableDto {
    private UUID id;
    private String tenantId;
    private String modulePrefix;
}