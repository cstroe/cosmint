package com.github.cstroe.spendhawk.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id = 0L;

    Account source = null;
    Account target = null;
    Integer amount = 0;
}
