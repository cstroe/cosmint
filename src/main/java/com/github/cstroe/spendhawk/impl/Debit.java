package com.github.cstroe.spendhawk.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Debit extends AbstractEntry {
    private final boolean credit = false;
    private final boolean debit = true;
}
