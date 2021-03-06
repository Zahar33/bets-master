package com.example.action.post;

import com.example.action.Action;
import com.example.action.ActionException;
import com.example.action.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ChangeLocaleAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(ChangeLocaleAction.class);

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        String language = req.getParameter("locale");
        if (language.equals("ru") || language.equals("en")) {
            Cookie locale = new Cookie("locale", language);
            LOG.debug("Create cookie with language for locale - {}", language);
            resp.addCookie(locale);
        }
        return new ActionResult(req.getHeader("Referer"), true);
    }
}
