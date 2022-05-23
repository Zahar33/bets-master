package com.example.action.get;

import com.example.action.Action;
import com.example.action.ActionException;
import com.example.action.ActionResult;
import com.example.model.Bet;
import com.example.model.Customer;
import com.example.model.PaginatedList;
import com.example.services.BetService;
import com.example.services.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowCustomersActiveBetsPageAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(ShowCustomersActiveBetsPageAction.class);

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        Customer loggedCustomer = (Customer) req.getSession(false).getAttribute("loggedCustomer");
        BetService service = new BetService();
        PaginatedList<Bet> activeBets;
        String pageNumberParam = req.getParameter("pageNumber");

        int pageSize = 5;
        int pageNumber;
        if (pageNumberParam == null) {
            LOG.debug("Do not get page number parameter. Set page number 1");
            pageNumber = 1;
        } else {
            pageNumber = Integer.parseInt(pageNumberParam);
        }
        try {
            activeBets = service.getAllActiveCustomersBets(loggedCustomer, pageNumber, pageSize);
            for (Bet bet : activeBets) {
                LOG.debug("Active bet contain - {}", bet);
            }
        } catch (ServiceException e) {
            throw new ActionException("Cannot get all active and inactive customer's bets", e);
        }

        req.setAttribute("activeBets", activeBets);
        return new ActionResult("active-bets");
    }
}
