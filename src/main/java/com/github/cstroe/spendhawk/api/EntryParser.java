package com.github.cstroe.spendhawk.api;

import com.github.cstroe.spendhawk.api.Entry;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public interface EntryParser {
    List<Entry> parse(Reader reader) throws IllegalArgumentException, IOException;
}
