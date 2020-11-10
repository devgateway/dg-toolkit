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
package org.devgateway.toolkit.forms.wicket.page;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.persistence.dao.AdminSettings;
import org.devgateway.toolkit.persistence.service.AdminSettingsService;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author idobre
 * @since 12/1/14
 */

public class Header extends Panel {
    private static final long serialVersionUID = 1L;

    private static final Duration ONE_MIN = Duration.ofMinutes(1);
    private static final long ALERT_UPDATE_INTERVAL_SECONDS = 15;

    @SpringBean
    private AdminSettingsService adminSettingsService;

    private LocalDateTime rebootSince;

    public Header(final String markupId) {
        this(markupId, null);
    }

    public Header(final String markupId, final PageParameters parameters) {
        super(markupId);

        addRebootAlert();
    }

    private void addRebootAlert() {
        AdminSettings as = adminSettingsService.get();
        if (as == null) {
            add(new WebMarkupContainer("rebootAlert"));
            return;
        }
        rebootSince = as.isRebootServer() ? as.getRebootAlertSince() : null;

        IModel<String> rebootDurationModel = new IModel<String>() {
            private static final long serialVersionUID = -8601598474017148336L;

            @Override
            public String getObject() {
                if (rebootSince == null) {
                    return "";
                } else {
                    Duration remaining = AdminSettings.REBOOT_ALERT_DURATION
                            .minus(Duration.between(rebootSince, LocalDateTime.now()))
                            // round up
                            .plusSeconds(30);
                    if (remaining.minus(ONE_MIN).isNegative()) {
                        remaining = ONE_MIN;
                    }
                    return DurationFormatUtils.formatDuration(remaining.toMillis(), "m");
                }
            }
        };

        Label rebootAlert = new Label("rebootAlert", new StringResourceModel("rebootAlert", rebootDurationModel)) {
            private static final long serialVersionUID = -3562806753180165059L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisible(rebootSince != null);
            }
        };
        rebootAlert.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
        add(rebootAlert);
        MetaDataRoleAuthorizationStrategy.authorize(rebootAlert, Component.RENDER, SecurityConstants.Roles.ROLE_USER);

        add(new AbstractAjaxTimerBehavior(org.apache.wicket.util.time.Duration.seconds(ALERT_UPDATE_INTERVAL_SECONDS)) {
            private static final long serialVersionUID = -1168209018766325709L;

            @Override
            protected void onTimer(final AjaxRequestTarget target) {
                AdminSettings as = adminSettingsService.get();
                rebootSince = as.isRebootServer() ? as.getRebootAlertSince() : null;
                target.add(rebootAlert);
            }
        });
    }
}
