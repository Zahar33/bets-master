package com.example.action.get;

import com.example.action.Action;
import com.example.action.ActionException;
import com.example.action.ActionResult;
import com.example.model.Bookmaker;
import com.example.model.CashAccount;
import com.example.model.Customer;
import com.example.services.PersonService;
import com.example.services.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class RefreshPersonBalanceAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        Customer loggedCustomer = (Customer) req.getSession().getAttribute("loggedCustomer");
        Bookmaker bookmaker = (Bookmaker) req.getSession().getAttribute("bookmaker");
        PersonService service = new PersonService();
        CashAccount purse;
        if (bookmaker != null) {
            purse = bookmaker.getPersonsPurse();
            try {
                purse = service.refreshCashAccount(purse);
            } catch (ServiceException e) {
                throw new ActionException("Cannot refresh bookmaker's balance", e);
            }
            bookmaker.setPersonsPurse(purse);
            req.getSession().setAttribute("bookmaker", bookmaker);
        }
        if (loggedCustomer != null) {
            purse = loggedCustomer.getPersonsPurse();
            try {
                purse = service.refreshCashAccount(purse);
            } catch (ServiceException e) {
                throw new ActionException("Cannot refresh customer's balance", e);
            }
            loggedCustomer.setPersonsPurse(purse);
            req.getSession().setAttribute("loggedCustomer", loggedCustomer);
        }
        return new ActionResult(req.getHeader("Referer"), true);
    }
}
