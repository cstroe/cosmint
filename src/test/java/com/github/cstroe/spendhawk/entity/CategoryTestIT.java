package com.github.cstroe.spendhawk.entity;

import com.github.cstroe.spendhawk.util.BaseIT;
import com.github.cstroe.spendhawk.util.Exceptions;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void testChildrenArePersisted() {
        startTransaction();
        User currentUser = User.findById(3l).orElseThrow(Exceptions::userNotFound);

        Category p1 = Category.findById(currentUser, 10l)
            .orElseThrow(Exceptions::categoryNotFound);

        assertThat(p1.getChildren(), is(not(nullValue())));

        Set<Category> children = p1.getChildren();
        assertThat(children.size(), is(2));

        Category c1 = Category.findById(currentUser, 11l)
            .orElseThrow(Exceptions::categoryNotFound);

        Category c2 = Category.findById(currentUser, 12l)
            .orElseThrow(Exceptions::categoryNotFound);

        assertTrue(children.contains(c1));
        assertTrue(children.contains(c2));

        commitTransaction();
    }
}