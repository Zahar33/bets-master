package com.example.action.post;

import com.example.action.Action;
import com.example.action.ActionException;
import com.example.action.ActionResult;
import com.example.model.Match;
import com.example.services.MatchService;
import com.example.services.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CancelMatchCreationAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        Match match = (Match) req.getSession(false).getAttribute("match");
        MatchService service = new MatchService();

        try {
            service.cancelMatchCreation(match);
        } catch (ServiceException e) {
            throw new ActionException("Cannot cancel match creation", e);
        }
        req.getSession().removeAttribute("match");
        req.setAttribute("flash.cancelMatch", "Bookmaker cancel");
        return new ActionResult("matches/edit/active", true);
    }
}
