package org.devgateway.toolkit.web.rest.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.devgateway.toolkit.web.WebConstants.FORMS_BASE_PATH;

/**
 * @author Nadejda Mandrescu
 */
@RestController
public class HTTPErrorController implements ErrorController {
    @Override
    public String getErrorPath() {
        return null;
    }

    @RequestMapping("/error")
    public void handleError(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            String contextPath = request.getContextPath();
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                response.sendRedirect(contextPath + FORMS_BASE_PATH + "/error/not-found");
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                response.sendRedirect(contextPath + FORMS_BASE_PATH + "/error/access-denied");
            }
        }
    }
}
