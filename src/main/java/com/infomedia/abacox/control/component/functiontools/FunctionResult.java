package com.infomedia.abacox.control.component.functiontools;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FunctionResult {
    private boolean success;
    private String exception;
    private String message;
    private Object result;
}
