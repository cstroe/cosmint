package com.github.cstroe.spendhawk.bean;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;
import static org.apache.commons.lang3.StringEscapeUtils.escapeXml10;

/**
 * A bean to clean up various things.
 */
@Service
public class JanitorBean {
    public boolean isBlank(String s) {
        return StringUtils.isBlank(s);
    }

    public String sanitize(String s) {
        return escapeXml10(escapeHtml4(s));
    }
}
