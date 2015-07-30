package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.entity.User;

public interface ExportBean {
    String doExportJson(User u) throws Exception;
}
