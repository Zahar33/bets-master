package com.example.action.post;

import com.example.action.Action;
import com.example.action.ActionException;
import com.example.action.ActionResult;
import com.example.model.Bet;
import com.example.model.Customer;
import com.example.services.BetService;
import com.example.services.PersonService;
import com.example.services.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SaveNewBetAction implements Action {

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        BetService betService = new BetService();
        PersonService personService = new PersonService();
        Bet bet = (Bet) req.getSession(false).getAttribute("bet");
        Customer loggedCustomer = (Customer) req.getSession(false).getAttribute("loggedCustomer");
        if (bet.getConditions().isEmpty()) {
            req.setAttribute("emptyError", "true");
            return new ActionResult("bet-edit");
        }
        try {
            betService.completeBetsCreation(bet);
            personService.replaceBatsValueToBookmaker(loggedCustomer, bet.getValue());
        } catch (ServiceException e) {
            throw new ActionException("Cannot complete bets creation", e);
        }
        req.getSession().removeAttribute("bet");
        req.setAttribute("flash.create_bet_successfully", "true");
        return new ActionResult("bets/active", true);
    }
}
