package com.example.main.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class SpringRequestHelper {

    private static final Logger logger = LoggerFactory.getLogger(SpringRequestHelper.class);

    public Map<String, String> getTokenInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();


        CsrfToken csrf = (CsrfToken)request.getAttribute(CsrfToken.class
                .getName());
        logger.info("csrf: {}", csrf.getToken());
        map.put("_csrf.token", csrf.getToken());
        map.put("_csrf.header", csrf.getHeaderName());
        map.put("_csrf.parameterName", csrf.getParameterName());

        return map;
    }
}