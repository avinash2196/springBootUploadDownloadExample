package com.demo.model;

import lombok.Builder;
import lombok.Data;

import java.io.ByteArrayInputStream;

@Builder
@Data
public class OutputModel {
    Boolean isValid;
    ByteArrayInputStream byteArrayInputStream;
}
