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

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuDivider;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.HtmlTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarComponents;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarDropDownButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.utilities.BackgroundColorBehavior;
import de.agilecoders.wicket.core.markup.html.themes.bootstrap.BootstrapCssReference;
import de.agilecoders.wicket.core.util.CssClassNames;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5CssReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.MetaDataHeaderItem;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.resource.JQueryResourceReference;
import org.apache.wicket.util.string.StringValue;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.security.SecurityUtil;
import org.devgateway.toolkit.forms.wicket.page.lists.ListGroupPage;
import org.devgateway.toolkit.forms.wicket.page.lists.ListTestFormPage;
import org.devgateway.toolkit.forms.wicket.page.lists.ListUserPage;
import org.devgateway.toolkit.forms.wicket.page.user.EditUserPage;
import org.devgateway.toolkit.forms.wicket.page.user.LogoutPage;
import org.devgateway.toolkit.forms.wicket.styles.BaseStyles;
import org.devgateway.toolkit.persistence.dao.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Base wicket-bootstrap {@link org.apache.wicket.Page}
 *
 * @author miha
 */
public abstract class BasePage extends GenericWebPage<Void> {
    @Serial
    private static final long serialVersionUID = -4179591658828697452L;

    protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);

    private TransparentWebMarkupContainer mainContainer;

    private Header mainHeader;

    private Footer mainFooter;

    protected Label pageTitle;

    private Navbar navbar;

    protected NotificationPanel feedbackPanel;

    /**
     * Determines if this page has a fluid container for the content or not.
     */
    public Boolean fluidContainer() {
        return false;
    }

    public static class UIRedirectPage extends RedirectPage {
        @Serial
        private static final long serialVersionUID = -750983217518258464L;

        public UIRedirectPage() {
            super(WebApplication.get().getServletContext().getContextPath() + "/ui/index.html");
        }
    }

    /**
     * Selects/changes the default language in the current session. If the
     * {@link WebConstants#LANGUAGE_PARAM} is found in the
     * {@link PageParameters} then its contents is set as language in the
     * session object.
     */
    protected void selectLanguage() {
        StringValue lang = this.getPageParameters().get(WebConstants.LANGUAGE_PARAM);
        if (!lang.isEmpty()) {
            WebSession.get().setLocale(new Locale(lang.toString()));
        }
    }

    /**
     * Construct.
     *
     * @param parameters current page parameters
     */
    public BasePage(final PageParameters parameters) {
        super(parameters);

        selectLanguage();

        add(new HtmlTag("html"));

        // Add javascript files.
        add(new HeaderResponseContainer("scripts-container", "scripts-bucket"));

        feedbackPanel = createFeedbackPanel();
        add(feedbackPanel);

        mainContainer = new TransparentWebMarkupContainer("mainContainer");
        add(mainContainer);

        // Set the bootstrap container class.
        // @see https://getbootstrap.com/css/#grid
        if (fluidContainer()) {
            mainContainer.add(new CssClassNameAppender(CssClassNames.Grid.containerFluid));
        } else {
            mainContainer.add(new CssClassNameAppender(CssClassNames.Grid.container));
        }

        mainHeader = new Header("mainHeader", this.getPageParameters());
        add(mainHeader);

        navbar = newNavbar("navbar");
        mainHeader.add(navbar);

        // Add information about navbar position on mainHeader element.
        if (navbar.getPosition().equals(Navbar.Position.DEFAULT)) {
            mainHeader.add(new CssClassNameAppender("with-navbar-default"));
        } else {
            mainHeader.add(new CssClassNameAppender("with-" + navbar.getPosition().cssClassName()));
        }

        mainFooter = new Footer("mainFooter");
        add(mainFooter);

        pageTitle = new Label("pageTitle", new ResourceModel("page.title"));
        add(pageTitle);

    }

    protected NotificationPanel createFeedbackPanel() {
        final NotificationPanel notificationPanel = new NotificationPanel("feedback");
        notificationPanel.setOutputMarkupId(true);
        return notificationPanel;
    }

    public NavbarDropDownButton newLanguageMenu() {
        final NavbarDropDownButton languageDropDown =
                new NavbarDropDownButton(new StringResourceModel("navbar.lang", this, null)) {

                    @Serial
                    private static final long serialVersionUID = 319842753824102674L;

                    @Override
                    protected List<AbstractLink> newSubMenuButtons(final String buttonMarkupId) {
                        final List<AbstractLink> list = new ArrayList<>();

                        for (final Locale l : WebConstants.AVAILABLE_LOCALES) {
                            final PageParameters params = new PageParameters(BasePage.this.getPageParameters());
                            params.set(WebConstants.LANGUAGE_PARAM, l.getLanguage());
                            list.add(new MenuBookmarkablePageLink<Page>(BasePage.this.getPageClass(), params,
                                    Model.of(l.getDisplayName())));
                        }

                        return list;
                    }
                };
        languageDropDown.setIconType(FontAwesome5IconType.flag_s);
        return languageDropDown;
    }

    protected MetaDataHeaderItem getFavicon() {
        PackageResourceReference faviconRef =
                new PackageResourceReference(BaseStyles.class, "assets/img/icons/toolkit-favicon.svg");
        MetaDataHeaderItem icon = MetaDataHeaderItem.forLinkTag("icon",
                urlFor(faviconRef, null).toString());
        icon.addTagAttribute("type", "image/svg+xml");
        return icon;

    }

    protected NavbarButton<LogoutPage> newLogoutMenu() {
        // logout menu
        final NavbarButton<LogoutPage> logoutMenu =
                new NavbarButton<LogoutPage>(LogoutPage.class, new StringResourceModel("navbar.logout", this, null));
        logoutMenu.setIconType(FontAwesome5IconType.sign_out_alt_s);
        MetaDataRoleAuthorizationStrategy.authorize(logoutMenu, Component.RENDER, SecurityConstants.Roles.ROLE_USER);

        return logoutMenu;
    }

    protected NavbarButton<EditUserPage> newAccountMenu() {
        final PageParameters pageParametersForAccountPage = new PageParameters();
        final Person person = SecurityUtil.getCurrentAuthenticatedPerson();
        // account menu
        Model<String> account = null;
        if (person != null) {
            account = Model.of(person.getFirstName());
            pageParametersForAccountPage.add(WebConstants.PARAM_ID, person.getId());
        }

        final NavbarButton<EditUserPage> accountMenu =
                new NavbarButton<>(EditUserPage.class, pageParametersForAccountPage, account);
        accountMenu.setIconType(FontAwesome5IconType.user_s);
        MetaDataRoleAuthorizationStrategy.authorize(accountMenu, Component.RENDER, SecurityConstants.Roles.ROLE_USER);
        return accountMenu;
    }

    protected NavbarButton<Homepage> newHomeMenu() {
        // home
        NavbarButton<Homepage> homeMenu = new NavbarButton<>(Homepage.class,
                new StringResourceModel("navbar.home", this, null));
        homeMenu.setIconType(FontAwesome5IconType.home_s);
        MetaDataRoleAuthorizationStrategy.authorize(homeMenu, Component.RENDER, SecurityConstants.Roles.ROLE_USER);
        return homeMenu;
    }

    protected NavbarDropDownButton newAdminMenu() {

        // admin menu
        NavbarDropDownButton adminMenu = new NavbarDropDownButton(new StringResourceModel("navbar.admin", this, null)) {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            protected List<AbstractLink> newSubMenuButtons(final String arg0) {
                final List<AbstractLink> list = new ArrayList<>();
                list.add(new MenuBookmarkablePageLink<ListTestFormPage>(ListUserPage.class, null,
                        new StringResourceModel("navbar.users", this, null))
                        .setIconType(FontAwesome5IconType.users_s));

                list.add(new MenuBookmarkablePageLink<ListGroupPage>(ListGroupPage.class, null,
                        new StringResourceModel("navbar.groups", this, null)).setIconType(FontAwesome5IconType.tags_s));

                list.add(new MenuBookmarkablePageLink<ListTestFormPage>(ListTestFormPage.class, null,
                        new StringResourceModel("navbar.testcomponents", this, null))
                        .setIconType(FontAwesome5IconType.android));

                list.add(new MenuDivider());

                final BootstrapBookmarkablePageLink swagger = new MenuBookmarkablePageLink<Void>(SwaggerPage.class,
                        new StringResourceModel("navbar.swagger", BasePage.this, null))
                        .setIconType(FontAwesome5IconType.code_s);
                MetaDataRoleAuthorizationStrategy.authorize(swagger, Component.RENDER,
                        SecurityConstants.Roles.ROLE_ADMIN);
                list.add(swagger);

                final BootstrapBookmarkablePageLink javamelody = new MenuBookmarkablePageLink<Void>(
                        JavamelodyPage.class, new StringResourceModel("navbar.javamelody",
                        BasePage.this, null)).setIconType(FontAwesome5IconType.eye_s);
                MetaDataRoleAuthorizationStrategy.authorize(javamelody, Component.RENDER,
                        SecurityConstants.Roles.ROLE_ADMIN);
                list.add(javamelody);

                list.add(new MenuBookmarkablePageLink<SpringEndpointsPage>(SpringEndpointsPage.class, null,
                        new StringResourceModel("navbar.springendpoints", this, null))
                        .setIconType(FontAwesome5IconType.anchor_s));

                final MenuBookmarkablePageLink<UIRedirectPage> uiBrowserLink =
                        new MenuBookmarkablePageLink<UIRedirectPage>(
                                UIRedirectPage.class, null, new StringResourceModel("navbar.ui", this, null)) {
                            @Serial
                            private static final long serialVersionUID = 1L;

                            @Override
                            protected void onComponentTag(final ComponentTag tag) {
                                super.onComponentTag(tag);
                                tag.put("target", "_blank");
                            }
                        };
                uiBrowserLink.setIconType(FontAwesome5IconType.rocket_s).setEnabled(true);
                list.add(uiBrowserLink);

                list.add(new MenuDivider());

                list.add(new MenuBookmarkablePageLink<Void>(EditAdminSettingsPage.class,
                        new StringResourceModel("navbar.adminSettings", BasePage.this, null))
                        .setIconType(FontAwesome5IconType.briefcase_s));

                return list;
            }
        };

        adminMenu.setIconType(FontAwesome5IconType.cog_s);
        MetaDataRoleAuthorizationStrategy.authorize(adminMenu, Component.RENDER, SecurityConstants.Roles.ROLE_USER);

        return adminMenu;
    }

    /**
     * creates a new {@link Navbar} instance
     *
     * @param markupId The components markup id.
     * @return a new {@link Navbar} instance
     */
    protected Navbar newNavbar(final String markupId) {

        Navbar navbar = new Navbar(markupId);

        /**
         * Make sure to update the BaseStyles when the navbar position changes.
         *
         * @see org.devgateway.toolkit.forms.wicket.styles.BaseStyles
         */
        navbar.setPosition(Navbar.Position.TOP);
        navbar.setBrandImage(new PackageResourceReference(BaseStyles.class, "assets/img/toolkit-logo-0048.png"),
                new StringResourceModel("brandImageAltText", this, null));
        navbar.setInverted(true);
        navbar.setBackgroundColor(BackgroundColorBehavior.Color.Dark);

        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.RIGHT, newHomeMenu(), newAdminMenu(),
                newAccountMenu(), newLogoutMenu()));

        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT, newLanguageMenu()));

        return navbar;
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);

        //favicon
        response.render(getFavicon());

        // Load Styles.
        response.render(CssHeaderItem.forReference(BootstrapCssReference.instance()));
        response.render(CssHeaderItem.forReference(FontAwesome5CssReference.instance()));
        response.render(CssHeaderItem.forReference(BaseStyles.INSTANCE));

        // Load Scripts.
        response.render(JavaScriptHeaderItem.forReference(JQueryResourceReference.getV3()));

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(BaseStyles.class,
                "assets/js/fileupload.js")));
    }
}
