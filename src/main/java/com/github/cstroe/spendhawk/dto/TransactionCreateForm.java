package com.github.cstroe.spendhawk.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * A form to create a transaction with one initial entry.
 */
@Validated
@Data
public class TransactionCreateForm {
    @NotNull
    private Long accountId;

    @NotNull
    private Long entryId;
}
