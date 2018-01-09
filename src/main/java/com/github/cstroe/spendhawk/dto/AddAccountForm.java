package com.github.cstroe.spendhawk.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.File;

@Validated
@Data
public class AddAccountForm {
    public static final String BLANK_ACCOUNT = "blank";
    public static final String CSV_IMPORT = "csv";

    @NotNull
    @Min(value = 1, message = "User ID is invalid.")
    private Long userId = -1L;

    @NotNull(message = "Account name must be entered.")
    @Size(min = 1, message = "Account name must be at least 1 character long.")
    @Pattern(regexp = "[a-zA-Z][a-zA-Z0-9 ]*[a-zA-Z0-9]?",
            message = "Account name must contain only alphanumeric characters, and spaces, and start with a letter.")
    private String accountName = "";

    @NotNull(message = "Please select an import option.")
    @Pattern(regexp = "(blank|csv)")
    private String accountType;

    private MultipartFile csvFile;
}
