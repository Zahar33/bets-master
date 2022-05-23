package com.example.action.get;

import com.example.action.Action;
import com.example.action.ActionException;
import com.example.action.ActionResult;
import com.example.model.Bet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowBetEditPageAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        Bet bet = (Bet) req.getSession(false).getAttribute("bet");
        if (bet != null) {
            bet.calculateFinalCoefficient();
            bet.calculatePossibleGain();
            req.getSession(false).setAttribute("bet", bet);
            return new ActionResult("bet-edit");
        } else {
            req.setAttribute("flash.cancelBet", "Customer cancel");
            return new ActionResult("home", true);
        }
    }
}
