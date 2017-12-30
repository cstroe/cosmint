package com.github.cstroe.spendhawk.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.cstroe.spendhawk.dao.AccountDao;
import com.github.cstroe.spendhawk.dao.TransactionDao;
import com.github.cstroe.spendhawk.dao.UserDao;

import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;


public class UserSerializer extends StdSerializer<UserDao> {

    private final int version;

    public UserSerializer(int version) {
        super(UserDao.class);
        this.version = version;
    }

    @Override
    public void serialize(UserDao user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("version", version);
        jsonGenerator.writeNumberField("id", user.getId());
        jsonGenerator.writeStringField("name", user.getName());
        jsonGenerator.writeObjectField("accounts", user.getAccounts());

        @SuppressWarnings("Convert2Diamond") // doesn't compile if converted to diamond
        final SortedSet<TransactionDao> transactionSet =
            new TreeSet<TransactionDao>((o1, o2) -> o1.getId().compareTo(o2.getId()));

        for (AccountDao account : user.getAccounts()) {
//            for (CashFlow cashFlow : account.getCashFlows()) {
//                if(cashFlow.getTransaction() != null) {
//                    transactionSet.add(cashFlow.getTransaction());
//                }
//            }
        }

        if(!transactionSet.isEmpty()) {
            jsonGenerator.writeArrayFieldStart("transactions");
            for (TransactionDao t : transactionSet) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeNumberField("id", t.getId());
//                jsonGenerator.writeStringField("notes", t.getNotes());
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndArray();
        }
        jsonGenerator.writeEndObject();
    }
}
