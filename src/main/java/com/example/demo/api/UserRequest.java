package com.example.demo.api;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class UserRequest {
    @NotEmpty
    private String name;
    @NotEmpty
    @Email
    private String email;
    private String password;
    private List<PhoneRequest> phones;
}
