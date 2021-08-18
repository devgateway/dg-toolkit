package org.devgateway.toolkit.persistence.dao;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;

/**
 * These is a set of possible views to be used with com.fasterxml.jackson.annotation.JsonView,
 * that allows multiple serializations options for the same class based on the view.
 *
 * If you need to change a view, make sure it doesn't impact existing usage or rather add a new one.
 *
 * @author Nadejda Mandrescu
 */
public class JsonViews {

    /** These type of fields should be provided to the public */
    public static class Public {
    }

    /** These type of fields should be provided for internal purposes only */
    public static class Internal extends Public {
    }

    /** These is the auditable view of an object data */
    public static class Auditable {
    }
}
