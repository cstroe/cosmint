package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.mocks.BareAccountsMock;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.InputStream;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class ExportBean_v1Test {

    @Test
    public void do_bare_export() throws Exception {
        final InputStream bareExportStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("export/bare.json");
        final String bareExport = IOUtils.toString(bareExportStream);

        BareAccountsMock mock = new BareAccountsMock();
        ExportBean v1 = new ExportBean_v1();
        final String s = v1.doExportJson(mock.getMockUser());

        assertNotNull("doExportJson should not return null.", s);
        assertEquals(s, bareExport);
    }
}