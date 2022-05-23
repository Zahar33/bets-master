package com.example.action.post;

import com.example.action.Action;
import com.example.action.ActionException;
import com.example.action.ActionResult;
import com.example.model.Match;
import com.example.services.MatchService;
import com.example.services.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SaveNewMatchAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        Match match = (Match) req.getSession(false).getAttribute("match");
        MatchService service = new MatchService();
        if (match.getConditionList().isEmpty()) {
            req.setAttribute("emptyError", "true");
            return new ActionResult("new-match-edit");
        }
        try {
            service.completeMatchsCreation(match);
        } catch (ServiceException e) {
            throw new ActionException("Cannot add conditions to match", e);
        }
        req.getSession(false).removeAttribute("match");
        req.getSession(false).setAttribute("flash.successAddMatch", true);
        return new ActionResult("matches/edit/active", true);
    }
}
