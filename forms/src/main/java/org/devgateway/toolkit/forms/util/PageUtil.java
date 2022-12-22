package org.devgateway.toolkit.forms.util;

import org.apache.wicket.request.cycle.RequestCycle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Nadejda Mandrescu
 */
public final class PageUtil {
    private PageUtil() {
    }

    public static HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest();
    }

    public static HttpServletResponse getHttpServletResponse() {
        return (HttpServletResponse) RequestCycle.get().getResponse().getContainerResponse();
    }

}
