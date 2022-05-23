package com.example.action.get;

import com.example.action.Action;
import com.example.action.ActionException;
import com.example.action.ActionResult;
import com.example.model.Match;
import com.example.services.MatchService;
import com.example.services.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowSumUpMatchsResultPageAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        MatchService service = new MatchService();
        String id = req.getParameter("id");
        Match match;
        if (id == null) {
            return null;
        }
        try {
            match = service.getMatchById(id);
        } catch (ServiceException e) {
            throw new ActionException("Cannot get match by id", e);
        }
        if (match.getId() == 0) {
            return null;
        }

        req.setAttribute("match", match);
        return new ActionResult("sum-up-result");
    }
}
