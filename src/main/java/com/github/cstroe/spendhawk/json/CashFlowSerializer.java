package com.github.cstroe.spendhawk.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.cstroe.spendhawk.entity.CashFlow;

import java.io.IOException;

public class CashFlowSerializer extends StdSerializer<CashFlow> {

    public CashFlowSerializer() {
        super(CashFlow.class);
    }

    @Override
    public void serialize(CashFlow cashFlow, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", cashFlow.getId());
        jsonGenerator.writeNumberField("amount", cashFlow.getAmount());
        if(cashFlow.getTransaction() != null) {
            jsonGenerator.writeNumberField("transaction", cashFlow.getTransaction().getId());
        }
        jsonGenerator.writeEndObject();
    }
}
