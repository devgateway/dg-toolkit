package org.devgateway.toolkit.forms.wicket.page.error;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.page.Homepage;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author Nadejda Mandrescu
 */
@MountPath(value = "/error-page")
public class ErrorPage extends BasePage {
    private static final long serialVersionUID = 3350781701642135173L;

    public ErrorPage(final PageParameters parameters) {
        super(parameters);

        add(new Label("error", new StringResourceModel("error")));

        AbstractLink link = getLink();
        link.add(new Label("linkMessage", new StringResourceModel("linkMessage")));
        add(link);
    }

    protected AbstractLink getLink() {
        return new Link<String>("link") {
            private static final long serialVersionUID = -4332229952335036068L;

            @Override
            public void onClick() {
                setResponsePage(getResponsePage());
            }
        };
    }

    protected Class<? extends IRequestablePage> getResponsePage() {
        return Homepage.class;
    }
}
