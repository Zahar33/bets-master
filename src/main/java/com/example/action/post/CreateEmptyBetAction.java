package com.example.action.post;

import com.example.action.Action;
import com.example.action.ActionException;
import com.example.action.ActionResult;
import com.example.model.Bet;
import com.example.model.CashAccount;
import com.example.model.Customer;
import com.example.services.BetService;
import com.example.services.ServiceException;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

public class CreateEmptyBetAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        BetService service = new BetService();
        Properties properties = new Properties();
        String parameter = req.getParameter("value");
        Customer loggedCustomer = (Customer) req.getSession(false).getAttribute("loggedCustomer");

        try {
            properties.load(CreateEmptyBetAction.class.getClassLoader().getResourceAsStream("validation.properties"));
        } catch (IOException e) {
            throw new ActionException("Cannot load properties for validation money", e);
        }

        if (parameter.matches(properties.getProperty("number.regex"))) {
            Money value = Money.of(CurrencyUnit.of(CashAccount.CURRENCY), Double.parseDouble(parameter));
            if (loggedCustomer.getPersonsPurse().balanceAvailabilityFor(value)) {
                Bet bet = new Bet();
                bet.setCustomer(loggedCustomer);
                bet.setValue(value);
                bet.calculateFinalCoefficient();
                bet.calculatePossibleGain();
                try {
                    service.registerCustomersBet(bet, loggedCustomer);
                } catch (ServiceException e) {
                    throw new ActionException("cannot register bet", e);
                }
                req.getSession(false).setAttribute("bet", bet);
                return new ActionResult("bet/edit", true);
            } else {
                req.setAttribute("valueError", "notAvailable");
                return new ActionResult("create-bet");
            }
        } else {
            req.setAttribute("valueError", "incorrect");
            return new ActionResult("create-bet");
        }
    }
}
