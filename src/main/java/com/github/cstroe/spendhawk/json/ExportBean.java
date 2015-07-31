package com.github.cstroe.spendhawk.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.cstroe.spendhawk.entity.User;

import java.io.StringWriter;

public class ExportBean {
    public String doExportJson(User u) throws Exception {
        ObjectMapper om = new ObjectMapper();
        om.configure(SerializationFeature.INDENT_OUTPUT, true);

        StringWriter sw = new StringWriter();
        sw.write("");
        SimpleModule testModule = new SimpleModule("MyModule", new Version(1,0,0,null));
        testModule.addSerializer(new UserSerializer(1));
        testModule.addSerializer(new AccountSerializer());
        testModule.addSerializer(new CashFlowSerializer());
        om.registerModule(testModule);
        om.writeValue(sw, u);
        return sw.toString();
    }
}
