package com.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Builder
@Data
public class DemoModel {

    private String stringAttribute;
    private int intAttribute;
    private double doubleAttribute;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HttpStatus httpStatus;
}
