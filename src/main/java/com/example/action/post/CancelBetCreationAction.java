package com.example.action.post;

import com.example.action.Action;
import com.example.action.ActionException;
import com.example.action.ActionResult;
import com.example.model.Bet;
import com.example.services.BetService;
import com.example.services.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CancelBetCreationAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        Bet bet = (Bet) req.getSession(false).getAttribute("bet");
        BetService service = new BetService();
        try {
            service.cancelBetCreation(bet);
        } catch (ServiceException e) {
            throw new ActionException("Cannot cancel bet creation", e);
        }
        req.getSession().removeAttribute("bet");
        req.setAttribute("flash.cancelBet", "Customer cancel");
        return new ActionResult("home", true);
    }
}
