package com.github.cstroe.spendhawk.bean;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.cstroe.spendhawk.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.StringWriter;

public class ExportBean_v1 implements ExportBean {

    @Override
    public String doExportJson(User u) throws Exception {
        ObjectMapper om = new ObjectMapper();
        om.configure(SerializationFeature.INDENT_OUTPUT, true);

        StringWriter sw = new StringWriter();
        om.writeValue(sw, u);
        return sw.toString();
    }
}
