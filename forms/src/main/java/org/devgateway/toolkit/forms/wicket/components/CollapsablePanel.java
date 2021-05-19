package org.devgateway.toolkit.forms.wicket.components;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * This collapsable panel uses a random id to allow multiple panel instances in the same page.
 * @author Nadejda Mandrescu
 */
public abstract class CollapsablePanel extends Panel {
    private static final long serialVersionUID = 4490805819246597450L;

    private IModel<String> headerLabelModel;

    public CollapsablePanel(final String id) {
        this(id, new StringResourceModel("headerLabel"));
    }

    public CollapsablePanel(final String id, final IModel<String> headerLabelModel) {
        super(id);
        this.headerLabelModel = headerLabelModel;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        String collapseId = getId() + RandomStringUtils.randomAlphanumeric(10);

        WebMarkupContainer header = new WebMarkupContainer("header");
        header.add(new AttributeModifier("href", "#" + collapseId));
        header.add(new AttributeModifier("aria-controls", collapseId));
        add(header);

        header.add(new Label("headerLabel", headerLabelModel));

        WebMarkupContainer body = new WebMarkupContainer("body");
        body.add(new AttributeModifier("id", collapseId));
        add(body);

        onInitializeBody(body);
    }

    protected abstract void onInitializeBody(WebMarkupContainer body);
}
