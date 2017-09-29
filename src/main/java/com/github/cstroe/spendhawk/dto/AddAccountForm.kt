package com.github.cstroe.spendhawk.dto

import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Validated
class AddAccountForm {
    @NotNull
    @Min(value = 1, message = "User ID is invalid.")
    var userId: Int = -1

    @NotNull
    @Size(min = 1, message = "Account name must be at least 1 charater long.")
    @Pattern(regexp = "[a-zA-Z][a-zA-Z0-9 ]*[a-zA-Z0-9]?", message = "Account name must contain only alphanumeric charaters, and spaces, and start with a letter.")
    val accountName: String = ""
}