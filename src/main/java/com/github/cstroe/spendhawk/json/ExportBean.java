package com.github.cstroe.spendhawk.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.cstroe.spendhawk.entity.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;

@Service
public class ExportBean {

    public ObjectMapper getObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.configure(SerializationFeature.INDENT_OUTPUT, true);
        SimpleModule testModule = new SimpleModule("MyModule", new Version(1,0,0,null, null, null));
        testModule.addSerializer(new UserSerializer(1));
        testModule.addSerializer(new AccountSerializer());
        testModule.addSerializer(new CashFlowSerializer());
        om.registerModule(testModule);
        return om;
    }

    public String doExportJson(User u) throws Exception {
        ObjectMapper om = getObjectMapper();
        StringWriter sw = new StringWriter();
        om.writeValue(sw, u);
        return sw.toString();
    }

    public void doExportJson(User u, OutputStream os) throws IOException {
        ObjectMapper om = getObjectMapper();
        om.writeValue(os, u);
    }
}
