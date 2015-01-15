package com.github.cstroe.spendhawk.testutil.web;

import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filter;

public class AggregateFilter implements Filter<ArchivePath> {

    final Filter[] filters;

    public AggregateFilter(Filter... filters) {
        this.filters = filters;
    }

    @Override
    public boolean include(ArchivePath object) {
        boolean include = true;
        for(Filter f : filters) {
            include = include && f.include(object);
        }
        return include;
    }
}
