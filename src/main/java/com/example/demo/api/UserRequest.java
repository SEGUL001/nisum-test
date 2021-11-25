package com.example.demo.api;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class UserRequest {
    @NotEmpty
    private String name;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    @Pattern(regexp="^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).*$",message="Password must contain at lease one uppercase, one lowercase and one number")
    private String password;
    private List<PhoneRequest> phones;
}
