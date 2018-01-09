package com.github.cstroe.spendhawk.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class AddUserForm {
    @Size(min = 1, message = "Username must be at least 1 character long.")
    @Pattern(regexp = "[a-zA-Z][a-zA-Z0-9]*", message = "Username must contain only alphanumeric charaters and start with a letter.")
    private String username;
}
