package com.github.cstroe.spendhawk.bean;

import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

@Service
public class FormatService {
    private static final DecimalFormat FORMAT = new DecimalFormat("#,##0.00");

    public String format(Money money) {
        return FORMAT.format(money.getNumber().doubleValueExact());
    }

    public String format(double value) {
        return FORMAT.format(value);
    }
}
