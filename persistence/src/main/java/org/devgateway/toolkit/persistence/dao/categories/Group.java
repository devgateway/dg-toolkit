/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.persistence.dao.categories;

import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.TestFormChild;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author mpostelnicu
 *
 */
@Entity
@Audited
public class Group extends Category {
    private static final long serialVersionUID = 8451785172092014455L;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private Set<Person> persons = new HashSet<>();

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToMany(mappedBy = "groups")
    private List<TestFormChild> testFormChildren = new ArrayList<>();

    public Group() {
        super();
    }

    public Group(final String label) {
        super();
        this.label = label;
    }

    @Override
    public String toString() {
        return getLabel();
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(final String label) {
        this.label = label;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(final Set<Person> persons) {
        this.persons = persons;
    }

    public List<TestFormChild> getTestFormChildren() {
        return testFormChildren;
    }

    public void setTestFormChildren(List<TestFormChild> testFormChildren) {
        this.testFormChildren = testFormChildren;
    }
}