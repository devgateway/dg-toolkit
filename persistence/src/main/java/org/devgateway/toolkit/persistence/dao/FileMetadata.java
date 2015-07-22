package org.devgateway.toolkit.persistence.dao;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

/**
 * @author idobre
 * @since 11/13/14
 *
 * Entity used to store the metadata of uploaded files
 */

@Entity
@Audited
public class FileMetadata extends AbstractAuditableEntity implements Serializable  {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private FileContent content;

    private String name;

    private String contentType;

    private long size;

    private boolean isUserSupportDocument = false;

    @Override
    public String toString() {
        return name;
    }


    public FileContent getContent() {
        return content;
    }

    public void setContent(FileContent content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isUserSupportDocument() {
        return isUserSupportDocument;
    }

    public void setUserSupportDocument(boolean isUserSupportDocument) {
        this.isUserSupportDocument = isUserSupportDocument;
    }


	/* (non-Javadoc)
	 * @see org.devgateway.ccrs.persistence.dao.AbstractAuditableEntity#getParent()
	 */
	@Override
	public AbstractAuditableEntity getParent() {
		return null;
	}
}
