package com.github.cstroe.spendhawk.entity;

import com.github.cstroe.spendhawk.util.BaseIT;
import com.github.cstroe.spendhawk.util.Exceptions;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

@RunWith(Arquillian.class)
public class CategoryTestIT extends BaseIT {

    @Test
    public void testParentIsPersisted() {
        startTransaction();
        User currentUser = User.findById(3l).orElseThrow(Exceptions::userNotFound);

        Category c1 = Category.findById(currentUser, 11l)
            .orElseThrow(Exceptions::categoryNotFound);

        assertThat(c1.getParent(), is(not(nullValue())));

        commitTransaction();
    }
}