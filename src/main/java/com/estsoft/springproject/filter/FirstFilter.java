package com.estsoft.springproject.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
public class FirstFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("FirstFilter.init()");
    }

    @Override
    public void destroy() {
        System.out.println("FirstFilter.destroy()");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("FirstFilter.doFilter() servletRequest");

        // requestURI
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        System.out.println("request.getRequestURI() = " + request.getRequestURI());

        chain.doFilter(servletRequest, servletResponse);

        System.out.println("FirstFilet.doFilter() servletResponse");


    }
}
