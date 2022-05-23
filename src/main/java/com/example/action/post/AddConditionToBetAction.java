package com.example.action.post;

import com.example.action.ActionException;
import com.example.action.ActionResult;
import com.example.model.Bet;
import com.example.model.Condition;
import com.example.action.Action;
import com.example.services.MatchService;
import com.example.services.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddConditionToBetAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(AddConditionToBetAction.class);

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        MatchService service = new MatchService();
        Bet bet = (Bet) req.getSession(false).getAttribute("bet");
        String id = req.getParameter("conditionId");
        LOG.debug("Condition's id - {}", id);
        Condition condition;
        try {
            condition = service.getConditionById(id);
            LOG.debug("Get condition - {} by id", condition);
        } catch (ServiceException e) {
            throw new ActionException("Cannot get condition by id", e);
        }
        boolean result = true;
        LOG.debug("Take all bet's condition and compare it with just added");
        if (!bet.getConditions().isEmpty()) {
            for (Condition betCondition : bet.getConditions()) {
                if (betCondition.getId() == condition.getId()) {
                    LOG.debug("{} equals - {}", condition, betCondition);
                    result = false;
                }

            }
        }
        if (result) {
            bet.addCondition(condition);
            LOG.debug("Have no one same condition. Add condition to bet - {}", bet);
        } else {
            req.setAttribute("flash.equalsError", "true");
        }
        req.getSession(false).setAttribute("bet", bet);
        return new ActionResult("bet/edit", true);
    }
}
