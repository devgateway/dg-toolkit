package org.devgateway.toolkit.forms.wicket.page.error;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author Nadejda Mandrescu
 */
@MountPath(value = "/error/access-denied")
public class AccessDeniedPage extends ErrorPage {
    private static final long serialVersionUID = 6845999925231189839L;

    public AccessDeniedPage(final PageParameters parameters) {
        super(parameters);
    }

}
