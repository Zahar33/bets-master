package com.example.web.listener;

import com.example.connection.ConnectionPool;
import com.example.connection.ConnectionPoolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class WebAppListener implements ServletContextListener {
    private static final Logger LOG = LoggerFactory.getLogger(WebAppListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        ConnectionPool pool = new ConnectionPool();
        LOG.info("Create new singleton instance of connection pool");
        ConnectionPool.InstanceHolder.setInstance(pool);
        servletContext.setAttribute("pool", pool);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        ConnectionPool pool = (ConnectionPool) servletContext.getAttribute("pool");
        try {
            pool.close();
        } catch (ConnectionPoolException e) {
            LOG.error("Cannot close all connection in pool", e);
        }

    }
}
