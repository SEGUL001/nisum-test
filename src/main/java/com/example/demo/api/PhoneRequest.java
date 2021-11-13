package com.example.demo.api;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PhoneRequest {
    @NotEmpty
    private String number;
    @NotEmpty
    private String cityCode;
    @NotEmpty
    private String countryCode;
}
