package com.baile.grupodebaile.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class MyBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
                response.sendError(401, authException.getMessage());
                response.addHeader("WWW-Authenticate", "Basic realm=" +getRealmName());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                PrintWriter writer = response.getWriter();
                writer.println("HTTP Status 401 - " + authException.getMessage());
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("Xareu D'Ochobre");
        super.afterPropertiesSet();
    }
}