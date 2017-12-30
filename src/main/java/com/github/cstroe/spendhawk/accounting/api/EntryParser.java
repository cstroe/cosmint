package com.github.cstroe.spendhawk.accounting.api;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public interface EntryParser {
    List<Entry> parse(Reader reader) throws IllegalArgumentException, IOException;
}
