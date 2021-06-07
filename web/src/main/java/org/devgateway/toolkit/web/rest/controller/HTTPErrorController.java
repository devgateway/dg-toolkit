package org.devgateway.toolkit.web.rest.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Nadejda Mandrescu
 */
@RestController
public class HTTPErrorController implements ErrorController {

    @RequestMapping("/error")
    public void handleError(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                response.sendRedirect("/error/not-found");
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                response.sendRedirect("/error/access-denied");
            }
        }
    }
}
