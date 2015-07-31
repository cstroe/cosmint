package com.github.cstroe.spendhawk.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.cstroe.spendhawk.entity.Account;

import java.io.IOException;

public class AccountSerializer extends StdSerializer<Account> {

    public AccountSerializer() {
        super(Account.class);
    }

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
