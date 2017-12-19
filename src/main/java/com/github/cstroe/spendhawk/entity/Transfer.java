package com.github.cstroe.spendhawk.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id = 0L;

    Account source = null;
    Account target = null;
    Integer amount = 0;
}
