package com.github.cstroe.spendhawk.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.cstroe.spendhawk.entity.Account;

import java.io.IOException;

public class AccountSerializer extends JsonSerializer<Account> {
    @Override
    public void serialize(Account account, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", account.getId());
        jsonGenerator.writeStringField("name", account.getName());
        if(account.getParent() == null) {
            jsonGenerator.writeNullField("parent");
        } else {
            jsonGenerator.writeNumberField("parent", account.getParent().getId());
        }
        jsonGenerator.writeObjectField("cashflows", account.getCashFlows());
        jsonGenerator.writeEndObject();
    }
}
