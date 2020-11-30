package org.devgateway.toolkit.persistence.dao;

import org.devgateway.toolkit.persistence.dao.categories.Group;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 2019-03-22
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Audited
@Table(indexes = {@Index(columnList = "test_form_id")})
public class TestFormChild extends AbstractAuditableEntity {
    @ManyToOne
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private TestForm testForm;

    private String header;

    private Integer value;

    @ManyToOne
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Group group;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToMany
    private List<Group> groups = new ArrayList<>();

    public TestForm getTestForm() {
        return testForm;
    }

    public void setTestForm(final TestForm testForm) {
        this.testForm = testForm;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(final String header) {
        this.header = header;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(final Integer value) {
        this.value = value;
    }

    @Override
    public AbstractAuditableEntity getParent() {
        return testForm;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
