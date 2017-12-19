package com.github.cstroe.spendhawk.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Validated
@Data
public class AddAccountForm {
    @NotNull
    @Min(value = 1, message = "User ID is invalid.")
    private Long userId = -1L;

    @NotNull
    @Size(min = 1, message = "Account name must be at least 1 charater long.")
    @Pattern(regexp = "[a-zA-Z][a-zA-Z0-9 ]*[a-zA-Z0-9]?",
            message = "Account name must contain only alphanumeric charaters, and spaces, and start with a letter.")
    private String accountName = "";
}
