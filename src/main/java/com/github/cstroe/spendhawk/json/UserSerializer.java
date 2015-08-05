package com.github.cstroe.spendhawk.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.CashFlow;
import com.github.cstroe.spendhawk.entity.Transaction;
import com.github.cstroe.spendhawk.entity.User;

import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;


public class UserSerializer extends StdSerializer<User> {

    private final int version;

    public UserSerializer(int version) {
        super(User.class);
        this.version = version;
    }

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("version", version);
        jsonGenerator.writeNumberField("id", user.getId());
        jsonGenerator.writeStringField("name", user.getName());
        jsonGenerator.writeObjectField("accounts", user.getAccounts());

        @SuppressWarnings("Convert2Diamond") // doesn't compile if converted to diamond
        final SortedSet<Transaction> transactionSet =
            new TreeSet<Transaction>((o1, o2) -> o1.getId().compareTo(o2.getId()));

        for (Account account : user.getAccounts()) {
            for (CashFlow cashFlow : account.getCashFlows()) {
                if(cashFlow.getTransaction() != null) {
                    transactionSet.add(cashFlow.getTransaction());
                }
            }
        }

        if(!transactionSet.isEmpty()) {
            jsonGenerator.writeArrayFieldStart("transactions");
            for (Transaction t : transactionSet) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeNumberField("id", t.getId());
                jsonGenerator.writeStringField("notes", t.getNotes());
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndArray();
        }
        jsonGenerator.writeEndObject();
    }
}
