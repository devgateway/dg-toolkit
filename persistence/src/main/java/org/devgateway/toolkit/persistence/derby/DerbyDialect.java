package org.devgateway.toolkit.persistence.derby;

import org.hibernate.dialect.DerbyTenSevenDialect;

import static java.sql.Types.VARBINARY;

/**
 * Setup non-default derby configurations.
 *
 * @author Nadejda Mandrescu
 */
public class DerbyDialect extends DerbyTenSevenDialect {

    public DerbyDialect() {
        super();
        registerColumnType(VARBINARY, "BLOB($l)");
    }
}
