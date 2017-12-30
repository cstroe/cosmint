package com.github.cstroe.spendhawk.accounting.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Credit extends AbstractEntry {
    private final boolean credit = true;
    private final boolean debit = false;
}
