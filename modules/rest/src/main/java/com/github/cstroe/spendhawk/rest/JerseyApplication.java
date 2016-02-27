package com.github.cstroe.spendhawk.rest;

import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.ext.ContextResolver;
import java.util.HashMap;
import java.util.Map;

@ApplicationPath("/spendhawk-rest")
public class JerseyApplication extends ResourceConfig {
    public JerseyApplication() {
        packages("com.github.cstroe.spendhawk.rest");
        register(createMoxyJsonResolver());
    }

    public static ContextResolver<MoxyJsonConfig> createMoxyJsonResolver() {
        final MoxyJsonConfig moxyJsonConfig = new MoxyJsonConfig();
        Map<String, String> namespacePrefixMapper = new HashMap<>(1);
        namespacePrefixMapper.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
        moxyJsonConfig.setNamespacePrefixMapper(namespacePrefixMapper).setNamespaceSeparator(':');
        return moxyJsonConfig.resolver();
    }
}
