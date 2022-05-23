package com.example.action.post;

import com.example.action.Action;
import com.example.action.ActionException;
import com.example.action.ActionResult;
import com.example.model.Condition;
import com.example.model.Match;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class DeleteConditionFromMatchAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        Match match = (Match) req.getSession(false).getAttribute("match");
        String current_id = req.getParameter("id");
        Condition deletedCondition = null;
        for (Condition condition : match.getConditionList()) {
            if (condition.getId() == Integer.parseInt(current_id)) {
                deletedCondition = condition;
            }
        }
        match.getConditionList().remove(deletedCondition);
        req.getSession(false).setAttribute("match", match);
        return new ActionResult("match/new/edit", true);
    }
}
