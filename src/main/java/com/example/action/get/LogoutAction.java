package com.example.action.get;

import com.example.action.Action;
import com.example.action.ActionResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutAction implements Action {

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) {
        String role = req.getParameter("role");
        req.getSession().removeAttribute("bookmaker");
        req.getSession().removeAttribute("loggedCustomer");

        if (role != null) {
            if (role.equals("customer")) {
                req.setAttribute("flash.authorizationError", "customer");
            } else {
                req.setAttribute("flash.authorizationError", "bookmaker");
            }
        }
        return new ActionResult("welcome", true);
    }
}
